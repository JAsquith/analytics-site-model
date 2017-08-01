package tests.admin.data.basedata;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.basedata.*;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

/**
 * Add Javadoc comments here!
 */

public class ImportEAPTest extends SISRATest {


    @Test
    public void importEAPDataTest() {
        try {
            this.login();

            this.gotoPage1();       // Open the EAP List page

            EAPDetail eapPage = this.page1Actions();    // Drill into a named EAP

            ImportEAPData importPage = this.page2Actions(eapPage);  // Click the Import EAP button

            if(!this.page3Actions(importPage)){     // Imports the named file (should land on the Confirm Import page)
                return;                             // Expected validation messages
            }

            if(!this.page4Actions()){
                return;                             // Expected validation messages
            }

            this.successCriteria_ValidationPassed();    // Per-test Asserts in here


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            if (currentPage == 4) {
                ConfirmEAPImport page = new ConfirmEAPImport(driver);
                page.clickCancel();
            }
            throw other;

        }
    }

    private void gotoPage1(){
        new AnalyticsPage(driver).waitForLoadingWrapper();
        DataHome dataPage = new DataHome(driver, true);
        DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
        modeAndCohort.selectMode("EAP");

        String cohort = getTestParam("eap-cohort");
        if (cohort.length() > 2){
            cohort = cohort.substring(cohort.length()-2);
        }

        DataSideMenu sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        if (!sideMenu.isTabSelected("KS2 / EAP")){
            sideMenu.clickTab("KS2 / EAP");
        }
        sideMenu.clickMenuOption("EAP");

        currentPage++;
    }

    public EAPDetail page1Actions(){
        // Find the specified EAP
        EAPDetail nextPage = new EAPList(driver, false).openEAP(getTestParam("eap-name"));
        currentPage++;
        return nextPage;
    }

    public ImportEAPData page2Actions(EAPDetail page) {
        // Open the Import EAP Data page
        ImportEAPData nextPage = page.clickImportEAP();
        currentPage++;
        return nextPage;
    }

    public boolean page3Actions(ImportEAPData page){

        String resDir = System.getProperty("user.dir") + File.separator + "test-resources";
        String filePath = resDir + File.separator + getTestParam("file-path");

        page.setImportFile(filePath);
        page.clickImport();

        assertThat("Server error - check Admin Panel > Errors page",
                page.getErrorMessage(),
                isEmptyOrNullString());

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            page.clickCancel();
            return false;
        }

        currentPage++;
        return true;

    }

    private boolean page4Actions(){
        ConfirmEAPImport page = new ConfirmEAPImport(driver);
        page.clickConfirm();

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            page.clickCancel();
            return false;
        }

        currentPage++;
        return true;
    }

    protected void successCriteria_ValidationPassed() {
        EAPDetail detailPage = new EAPDetail(driver);

        assertThat("Success banner displayed",
                detailPage.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                detailPage.getSuccessBannerText(),
                is("EAP successfully imported"));

    }

    @BeforeTest
    public void setup(ITestContext testContext) {
        // The initialise method of the SISRATest superclass:
        //      - Copies the test parameters from the context to the Map<String, String> field testParams
        //      - Creates a new AnalyticsDriver (starting a browser session)
        try {
            this.initialise(testContext);
        } catch (MalformedURLException e) {
            return;
        }
    }

    @AfterTest
    public void tearDown() {
        try {
            if (driver.getSessionId() != null) {
                AnalyticsPage page = new AnalyticsPage(driver);
                page.waitForLoadingWrapper();
                if (page.getMenuOptions().size() > 0) {
                    page.clickMenuHome();
                    page.waitForLoadingWrapper();
                    page.clickMenuLogout();
                    page.waitForLoadingWrapper();
                }
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
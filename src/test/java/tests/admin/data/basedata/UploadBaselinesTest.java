package tests.admin.data.basedata;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.GradesFileUpload;
import pages.data.GradesFileUploadConfirm;
import pages.data.basedata.UploadsForBaselinesList;
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

/**
 * Add Javadoc comments here!
 */

public class UploadBaselinesTest extends SISRATest {

    private String fileTitle = null;

    @Test
    public void uploadBaselinesFile() {
        try {
            this.login();

            this.gotoPage1();

            this.page1Actions();            // No validation messages check

            if(!this.page2Actions()){
                return;                     // Expected validation messages
            }

            if(!this.page3Actions()){
                return;                     // Expected validation messages
            }

            this.successCriteria_ValidationPassed();


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();

            if (currentPage == 3){
                // Don't leave an unconfirmed upload
                new GradesFileUploadConfirm(driver).clickCancel();
            }
            throw other;

        }
    }

    private void gotoPage1(){
        new AnalyticsPage(driver).waitForLoadingWrapper(30);
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
        sideMenu.clickMenuOption("Uploads");

        currentPage++;
    }

    public void page1Actions(){
        UploadsForBaselinesList testPage = new UploadsForBaselinesList(driver);
        testPage.uploadGradesFile();
        currentPage++;
    }

    public boolean page2Actions() {
        GradesFileUpload page = new GradesFileUpload(driver);

        String resDir = System.getProperty("user.dir") + File.separator + "test-resources";
        String filePath = resDir + File.separator + getTestParam("file-path");

        page.setFilePath(filePath);

        fileTitle = getTestParam("file-title");
        page.setFileTitle(fileTitle);
        page.clickUpload();

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

    public boolean page3Actions(){
        // Click confirm upload
        GradesFileUploadConfirm page = new GradesFileUploadConfirm(driver);
        page.continueUpload();

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
        UploadsForBaselinesList listPage = new UploadsForBaselinesList(driver);

        assertThat("Success banner displayed",
                listPage.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                listPage.getSuccessBannerText(),
                is("File successfully uploaded"));

        assertThat("File listed on uploads page",
                listPage.isListed(fileTitle),
                is(true));
    }

    @BeforeTest
    public void setup(ITestContext testContext) {
        // The initialise method of the SISRATest superclass:
        //      - Copies the test parameters from the context to the Map<String, String> field testParams
        //      - Creates a new AnalyticsDriver
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
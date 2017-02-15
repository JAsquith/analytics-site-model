package admin;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.DataHome;
import analytics.pages.data.GradesFileUpload;
import analytics.pages.data.GradesFileUploadConfirm;
import analytics.pages.data.basedata.UploadsForBaselinesList;
import analytics.pages.data.components.DataAdminSelect;
import analytics.pages.data.components.DataSideMenu;
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

public class BlankTest extends SISRATest {


    @Test
    public void testMethod() {
        try {
            this.login();                   // In SISRATest

            this.gotoPage1();               // Gets us to the Start Page

            this.page1Actions();            // No check for validation messages

            if(!this.page2Actions()){
                return;                     // Expected validation messages
            }

            if(!this.page3Actions()){
                return;                     // Expected validation messages
            }

            this.successCriteria_ValidationPassed();    // Per-test Asserts in here


        } catch (AssertionError assertFail) {
            System.out.println(testNGCaseName + "(" + currentPage + ")" +
                    ": AssertionError Exception (" + assertFail.getMessage() + ")");
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            System.out.println(testNGCaseName + "(" + currentPage + ")" +
                    ": non-AssertionError Exception (" + other.getMessage() + ")");
            this.saveScreenshots();
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

    private void page1Actions(){
        UploadsForBaselinesList testPage = new UploadsForBaselinesList(driver);
        testPage.uploadGradesFile();
        currentPage++;
    }

    public boolean page2Actions() {
        GradesFileUpload page = new GradesFileUpload(driver);

        String resDir = System.getProperty("user.dir") + File.separator + "test-resources";
        String filePath = resDir + File.separator + getTestParam("file-path");

        page.setFilePath(filePath);
        page.setFileTitle(getTestParam("file-title"));
        page.clickUpload();

        if (this.checkForExpectedValidationFail(page)){
            if (valExpectedPage == currentPage) {
                // Validation failed as expected, clean up and return false so the test can end cleanly
                page.clickCancel();
                return false;
            } else {
                // Page was not expected to fail validation at this point!
                throw new IllegalStateException("Page validation failed unexpectedly");
            }
        }

        currentPage++;
        return true;
    }

    public boolean page3Actions(){
        // Click confirm upload
        GradesFileUploadConfirm page = new GradesFileUploadConfirm(driver);
        page.continueUpload();

        if (this.checkForExpectedValidationFail(page)){
            if (valExpectedPage == currentPage) {
                // Validation failed as expected, clean up and return false so the test can end cleanly
                page.clickCancel();
                return false;
            } else {
                // Page was not expected to fail validation at this point!
                throw new IllegalStateException("Page validation failed unexpectedly");
            }
        }

        currentPage++;
        return true;

    }

    protected void successCriteria_ValidationPassed() {
        // Todo
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
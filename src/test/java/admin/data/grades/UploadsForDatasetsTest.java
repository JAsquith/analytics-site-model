package admin.data.grades;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.DataHome;
import analytics.pages.data.GradesFileUpload;
import analytics.pages.data.GradesFileUploadConfirm;
import analytics.pages.data.components.DataAdminSelect;
import analytics.pages.data.components.DataSideMenu;
import analytics.pages.data.grades.UploadsForDatasets;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Add Javadoc comments here!
 */

public class UploadsForDatasetsTest extends SISRATest {


    @Test
    public void uploadDatasetGrades() {
        try {
            this.login();                   // See SISRATest super class

            this.gotoPage1();               // Gets us to the Uploads For [Dataset name] Page

            GradesFileUpload page = this.page1Actions();    // Clicks the Upload button

            if(!this.page2Actions(page)){       // Sets the upload page fields and submits the page
                return;                         // Expected validation messages
            }

            if(!this.page3Actions()){
                return;
            }

            // Todo - handle Confirm Qualification Names page

            this.successCriteria_ValidationPassed();    // Per-test Asserts in here


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            if(currentPage==3){
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
        if (!sideMenu.isTabSelected("GRADES")){
            sideMenu.clickTab("GRADES");
        }
        sideMenu.clickMenuOption("Uploads");
        sideMenu.clickSubMenuOption(getTestParam("dataset-name"));

        currentPage++;
    }

    public GradesFileUpload page1Actions(){
        UploadsForDatasets testPage = new UploadsForDatasets(driver);
        GradesFileUpload nextPage = testPage.
                selectEAPTerm(getTestParam("eap-term")).
                clickUploadGradeFile();
        currentPage++;
        return nextPage;
    }

    public boolean page2Actions(GradesFileUpload page) {

        String resDir = System.getProperty("user.dir") + File.separator + "test-resources";
        String filePath = resDir + File.separator + getTestParam("file-path");

        page.setFilePath(filePath);
        page.setFileTitle(getTestParam("file-title"));
        page.setResultsDate(getTestParam("results-date"));
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
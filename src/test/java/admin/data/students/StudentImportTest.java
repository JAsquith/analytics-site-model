package admin.data.students;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.DataHome;
import analytics.pages.data.components.DataAdminSelect;
import analytics.pages.data.components.DataSideMenu;
import analytics.pages.data.students.StudentImportComplete;
import analytics.pages.data.students.StudentsImportFile;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Add Javadoc comments here!
 */

public class StudentImportTest extends SISRATest {

    @Test
    public void uploadStudentData() {
        try {
            this.login();

            this.gotoPage1();

            if(!this.page1Actions()){
                return;                     // Expected validation failure
            }

            if(!this.page2Actions()){
                return;                     // Expected validation failure
            }

            this.successCriteria_ValidationPassed();

        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            if (currentPage == 2) {
                new StudentImportComplete(driver).clickCancelImport();
            }
            throw other;
        }
    }

    private void gotoPage1(){
        try {
            new AnalyticsPage(driver).waitForLoadingWrapper(30);
            DataHome dataPage = new DataHome(driver, true);
            DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
            modeAndCohort.selectMode("EAP");

            String cohort = getTestParam("eap-cohort");
            if (cohort.length() > 2) {
                cohort = cohort.substring(cohort.length() - 2);
            }

            DataSideMenu sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
            if (sideMenu.isTabSelected("STUDENTS")==false){
                sideMenu.clickTab("STUDENTS");
            }
            sideMenu.clickMenuOption("Import");
            currentPage++;

        } catch (Exception other) {
            // The default behaviour for any type of Exception is to throw it up the call stack so
            // the TestNG invoker knows the test has failed, but we may also need to do some
            // tidying up before that to avoid leaving the school in an unpredictable state
            System.out.println(testNGCaseName + ": non-AssertionError Exception (" + other.getMessage() + ")");
            throw other;
        }
    }

    public boolean page1Actions(){
        StudentsImportFile page = new StudentsImportFile(driver);

        page.selectKeyStage(getTestParam("keystage"));
        page.setUploadFile(getTestParam("data-file"));

        page.clickImport();

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            return false;
        }

        currentPage++;
        return true;
    }

    public boolean page2Actions() {
        StudentImportComplete page = new StudentImportComplete(driver);

        String updateClasses = getTestParam("update-classes");
        String updateFilters = getTestParam("update-filters");
        boolean updateStatuses = getTestParamAsBool("update-statuses");

        try {
            if (updateClasses.length() > 0) {page.setUpdateClassesRadio(updateClasses);}
            if (updateFilters.length() > 0) {page.setUpdateFiltersRadio(updateFilters);}
            if (updateStatuses) {page.setUpdateStatuses(updateFilters);}

        } catch (Exception e) {
            page.clickCancelImport();
        }

        page.clickCompleteImport();

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            page.clickCancelImport();
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
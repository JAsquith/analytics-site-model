package admin.data.grades;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.DataHome;
import analytics.pages.data.components.DataAdminSelect;
import analytics.pages.data.components.DataSideMenu;
import analytics.pages.data.grades.UploadsForAssessments;
import analytics.pages.data.grades.components.CreateCollectionModal;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Add Javadoc comments here!
 */

public class CreateAssessmentCollectionTest extends SISRATest {

    private String collectionDate = null;

    @Test
    public void createCollection() {
        try {
            this.login();                   // See SISRATest super class

            this.gotoPage1();               // Gets us to the Uploads For Assessments Page

            CreateCollectionModal modal = this.page1Actions();   // Opens the Create Collection Modal

            if(!this.modalActions(modal)){      // Set the modal fields and clicks create
                return;                         // Found expected validation messages
            }

            this.successCriteria_ValidationPassed();    // Check the Collection has been created

        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            if (currentPage==2){
                new CreateCollectionModal(driver).clickCancel();
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

    private CreateCollectionModal page1Actions(){
        UploadsForAssessments page = new UploadsForAssessments(driver);
        CreateCollectionModal modal = page.clickCreateAssessmentCollection();
        currentPage++;
        return modal;
    }

    private boolean modalActions(CreateCollectionModal modal){
        modal.setName(getTestParam("coll-name"));
        modal.setDate(getTestParam("coll-date"));
        collectionDate = getTestParam("coll-term");
        modal.selectTerm(collectionDate);
        modal.setDescription(getTestParam("coll-description"));
        modal.clickCreate();


        if (!this.checkForExpectedValidationFail(modal)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            modal.clickCancel();
            return false;
        }

        currentPage++;
        return true;
    }

    protected void successCriteria_ValidationPassed() {
        UploadsForAssessments listPage = new UploadsForAssessments((driver));
        assertThat("An Assessment Collection of the correct date is listed on the Uploads page",
                listPage.collectionExists(collectionDate),
                is(true));
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
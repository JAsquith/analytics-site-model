package tests.admin.data.grades;

import tests.SISRATest;
import pages.AbstractAnalyticsObject;
import pages.AnalyticsPage;
import pages.data.basedata.PublishBaseData;
import pages.data.grades.PublishGrades;
import pages.data.grades.components.PublishGradesModal;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

/**
 * Add Javadoc comments here!
 */

public class PublishGradesTest extends SISRATest {


    @Test
    public void publishGrades() {
        try {
            this.login();                   // In SISRATest

            PublishGrades page = this.gotoPage1();               // Gets us to the Publish Grades page

            PublishGradesModal modal = this.page1Actions(page);            // No check for validation messages

            AbstractAnalyticsObject finishPage = this.page2Actions(modal);
            //this.successCriteria_ValidationPassed(page);    // Per-test Asserts in here


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            if (currentPage==2){
                PublishGradesModal modal = new PublishGradesModal(driver);
                modal.clickCancel();
            }
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            if (currentPage==2){
                PublishGradesModal modal = new PublishGradesModal(driver);
                modal.clickCancel();
            }
            throw other;
        }
    }

    private PublishGrades gotoPage1(){
        String cohort = getTestParam("eap-cohort");
        if (cohort.length() > 2){
            cohort = cohort.substring(cohort.length()-2);
        }
        PublishGrades page = new PublishGrades(driver).load(cohort,true);

        currentPage++;
        return page;
    }

    public PublishGradesModal page1Actions(PublishGrades page){
        String dataset = getTestParam("dataset-name");
        int assessmentYear = getTestParamAsInt("assessment-year");
        int assessmentTerm = getTestParamAsInt("assessment-term");
        int publishSlot = getTestParamAsInt("publish-slot");
        PublishGradesModal modal;
        if(dataset.equals("")){
            modal = page.clickPublishFor(assessmentYear, assessmentTerm, publishSlot);
        } else {
            modal = page.clickPublishFor(dataset);
        }
        currentPage++;
        return modal;
    }

    private AbstractAnalyticsObject page2Actions(PublishGradesModal modal){
        String assessmentColl = getTestParam("assessment-collection");
        if(!assessmentColl.equals("")){
            modal.selectCollection(assessmentColl);
        }
        modal.selectKS4RulesType(getTestParam("rules-type"));
        modal.selectLogicYear(getTestParam("rules-year"));
        modal.selectA8Estimates(getTestParam("a8-estimates"));
        modal.setReportNote(getTestParam("report-note"));
        modal.selectReportStatus(getTestParam("report-status"));
        AbstractAnalyticsObject page;
        if (getTestParam("publish-type").toLowerCase().equals("local")) {
            page = modal.clickLocal();
        } else {
            page = modal.clickPublish();
        }
        currentPage++;
        return page;
    }
    protected void successCriteria_ValidationPassed(PublishBaseData page) {
        assertThat("Publish Info shows \"Last Published:\"",
                page.getLastPublishedInfo(),
                startsWith("Last Published:"));
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
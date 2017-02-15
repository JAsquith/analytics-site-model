package admin.data.students;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.students.PublishStudents;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.net.MalformedURLException;

/**
 * Add Javadoc comments here!
 */

public class PublishStudentsTest extends SISRATest {


    @Test
    public void publishStudentData() {
        try {
            this.login();                   // In SISRATest

            PublishStudents page = this.gotoPage1();               // Gets us to the Publish Students Page

            page = this.page1Actions(page);   // No check for validation messages

            this.successCriteria_ValidationPassed(page);    // Per-test Asserts in here


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            throw other;

        }
    }

    private PublishStudents gotoPage1(){
        String cohort = getTestParam("eap-cohort");
        if (cohort.length() > 2){
            cohort = cohort.substring(cohort.length()-2);
        }

        PublishStudents page = new PublishStudents(driver).load(cohort, true);
        currentPage++;
        return page;
    }

    public PublishStudents page1Actions(PublishStudents page){
        int publishType = 0;
        if(getTestParam("publish-type").toLowerCase().equals("local")){
            publishType = 1;
        }
        page.clickPublish(publishType);
        return page;
    }

    protected void successCriteria_ValidationPassed(PublishStudents page) {
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
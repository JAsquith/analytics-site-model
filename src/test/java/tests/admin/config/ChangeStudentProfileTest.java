package tests.admin.config;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.config.StudentDataProfile;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.net.MalformedURLException;

/**
 * Add Javadoc comments here!
 */

public class ChangeStudentProfileTest extends SISRATest {


    @Test
    public void editProfileColumns() {
        try {
            this.login();                   // See SISRATest implementation

            // Gets us to the Student Data Profile Page
            StudentDataProfile page = new StudentDataProfile(driver, true);
            currentPage++;

            // Clicks the Create Filter button
            page.clickEditColumns();

            // Set the filters per the test params
            String[] filterNames = getTestParamAsStringArray("filter-names");
            String[] filterIndexes = getTestParamAsStringArray("filter-nums");
            int arrayIndex = 0;
            for(String filterName : filterNames){
                page.selectFilter(Integer.valueOf(filterIndexes[arrayIndex]), filterName);
                arrayIndex++;
            }

            page.clickSave();

            if (!this.checkForExpectedValidationFail(page)) {
                // Page was not expected to fail validation at this point!
                throw new IllegalStateException("Page validation failed unexpectedly");
            }
            if (valExpectedPage == currentPage) {
                // Validation failed as expected, clean up and return so the test can end cleanly
                page.clickCancel();
                return;
            }

            this.successCriteria_ValidationPassed(page);            // Check the filter has been created


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            throw other;

        }
    }

    private void successCriteria_ValidationPassed(StudentDataProfile page) {

        assertThat("Success Banner Displayed",
                page.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                page.getSuccessBannerText(),
                is("Student data defaults successfully updated"));
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
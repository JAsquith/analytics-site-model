package admin.config;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.config.FilterManagement;
import analytics.pages.config.components.CreateFilterModal;
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

public class CreateFilterTest extends SISRATest {


    @Test
    public void createStudentFilter() {
        try {
            this.login();                   // See SISRATest implementation

            FilterManagement page = this.gotoPage1();           // Gets us to the Filter Management Page

            CreateFilterModal modal = this.page1Actions(page);  // Clicks the Create Filter button

            if(!this.modalActions(modal)){                      // Sets fields and submits
                return;                                         // Found expected validation messages
            }

            this.successCriteria_ValidationPassed();            // Check the filter has been created


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;
        } catch (Exception other){
            this.saveScreenshots();
            throw other;

        }
    }

    private FilterManagement gotoPage1(){
        FilterManagement page = new FilterManagement(driver, true);
        currentPage++;
        return page;
    }

    private CreateFilterModal page1Actions(FilterManagement page){
        CreateFilterModal modal = page.clickCreateFilter();
        currentPage++;
        return modal;
    }

    public boolean modalActions(CreateFilterModal modal){
        // Set fields and click Create
        modal.setFilterName(getTestParam("filter-name")).
                setFilterDefault(getTestParam("default-value")).
                clickSave();

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
        FilterManagement page = new FilterManagement(driver, false);

        assertThat("Success banner displayed",
                driver.getCurrentUrl().endsWith("&notiType=0"),
                is(true));

        assertThat("Success banner text",
                page.getSuccessBannerText(),
                is("Filter successfully created"));
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
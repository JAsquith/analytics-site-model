package tests.admin.users;

import tests.SISRATest;
import pages.AnalyticsPage;
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

public class FirstLogin extends SISRATest {


    @Test
    public void loginForFirstTime() {
        try{
            // Login
            this.login();


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            throw other;

        }
    }

    @BeforeTest
    public void setup(ITestContext testContext) throws MalformedURLException {
        // The initialise method of the SISRATest superclass:
        //      - Copies the test parameters from the context to the Map<String, String> field testParams
        //      - Creates a new AnalyticsDriver
        try {
            this.initialise(testContext);
        } catch (MalformedURLException e) {
            throw e;
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
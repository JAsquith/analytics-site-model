package tests.admin.users;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.users.CreateUser;
import pages.users.UserList;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * Add Javadoc comments here!
 */

public class UsersTests extends SISRATest {


    @Test
    public void createNewUserTest() {
        try{
            // Login
            this.login();

            // Open User List and click Create button
            currentPage++;
            UserList userListPage = new UserList(driver, true);

            currentPage++;
            CreateUser newUserPage = userListPage.createNewUser(false);

            // Set field values
            newUserPage.setFields(
                    getTestParam("firstname"),
                    getTestParam("lastname"),
                    getTestParam("email-address"),
                    getTestParam("auth-group"));

            newUserPage.clickCreateNewUser();

            if(checkForExpectedValidationFail(newUserPage)){
                if (valExpectedPage == currentPage) {
                    // Validation failed as expected, return so the test can end cleanly
                    return;
                } else {
                    // Page was not expected to fail validation at this point!
                    throw new IllegalStateException("Page validation failed unexpectedly");
                }
            }

            this.successCriteria_CreateNewUser();

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

    protected void successCriteria_CreateNewUser(){
        UserList userListPage = new UserList(driver, false);

        assertThat(
                "The Users List page is open",
                driver.getCurrentUrl(),
                startsWith(getCurrentDomain() + userListPage.PAGE_PATH));

        String uniqueEmail = getTestParam("email-address");
        assertThat(
                "Email Address '" + uniqueEmail + "' is listed on the User List page",
                userListPage.isUserActive(uniqueEmail),
                is(true));
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
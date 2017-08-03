package tests.admin;

import io.qameta.allure.*;
import org.junit.experimental.categories.Category;
import tests.BaseTest;
import pages.account.LoginPage;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 *
 */
@Epic("Account Security")
@Feature("Login Page")
public class LoginValidationTests extends BaseTest {

    LoginPage testPage;

    @BeforeTest(groups = { "no-details", "no-password", "no-username" })
    public void setup(ITestContext testContext) throws MalformedURLException {

        initialise(testContext);

        testPage = new LoginPage(driver, true);
    }

    @AfterTest(groups = { "no-details", "no-password", "no-username" })
    public void tearDown(){
        driver.quit();
    }

    @Story("Trying to login with no username or password should fail")
    @Severity(SeverityLevel.BLOCKER)
    @Parameters({ "no-details-val-msg-count"})
    @Test(groups = { "no-details"})
    public void noDetailsFails(String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");
        String user = "";
        String pass = "";
        int loginResult = login(testPage, user, pass);
        assertThat("Attempted login should fail ", loginResult, is(1));

        List<WebElement> actualValMsgs = testPage.getValidationMessages();
        assertThat("Number of validation messages should be ", actualValMsgs.size(), is(expectedValMsgsCount));

        checkValidationMessages(actualValMsgs, expectedValMsgs);
    }

    @Story("Trying to login with no username or password should produce 2 validation messages")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({ "no-details-val-msg-count"})
    @Test(groups = { "no-details"})
    public void noDetailsValMsgCount(String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");
        String user = "";
        String pass = "";
        int loginResult = login(testPage, user, pass);
        List<WebElement> actualValMsgs = testPage.getValidationMessages();
        assertThat("Number of validation messages should be ", actualValMsgs.size(), is(expectedValMsgsCount));
    }

    @Story("Trying to login with no username or password should produce the correct validation messages")
    @Severity(SeverityLevel.MINOR)
    @Parameters({ "no-details-val-msg-count"})
    @Test(groups = { "no-details"})
    public void noDetailsValMsgs(String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");
        String user = "";
        String pass = "";
        int loginResult = login(testPage, user, pass);
        List<WebElement> actualValMsgs = testPage.getValidationMessages();
        checkValidationMessages(actualValMsgs, expectedValMsgs);
    }

    @Story("Trying to login with no password should fail")
    @Severity(SeverityLevel.BLOCKER)
    @Parameters({ "username", "no-password-val-msg-count" })
    @Test(groups = { "no-password"})
    public void noPasswordFails(String username, String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");

        String pass = "";
        int loginResult = login(testPage, username, pass);
        assertThat("Attempted login should fail ", loginResult, is(1));
    }

    @Story("Trying to login with no password should give 2 validation messages")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({ "username", "no-password-val-msg-count" })
    @Test(groups = { "no-password"})
    public void noPasswordValMsgsCount(String username, String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");

        String pass = "";
        int loginResult = login(testPage, username, pass);
        List<WebElement> actualValMsgs = testPage.getValidationMessages();
        assertThat("Number of validation messages should be ", actualValMsgs.size(), is(expectedValMsgsCount));
    }

    @Story("Trying to login with no password should give the correct validation message text")
    @Severity(SeverityLevel.MINOR)
    @Parameters({ "username", "no-password-val-msg-count" })
    @Test(groups = { "no-password"})
    public void noPasswordValMsgs(String username, String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");

        String pass = "";
        int loginResult = login(testPage, username, pass);
        List<WebElement> actualValMsgs = testPage.getValidationMessages();
        checkValidationMessages(actualValMsgs, expectedValMsgs);
    }

    @Story("Trying to login with no username should fail")
    @Parameters({ "password", "no-username-val-msg-count" })
    @Test(groups = { "no-username"})
    public void noUsernameTest(String password, String expected_val_msg_count){
        int expectedValMsgsCount = Integer.valueOf(expected_val_msg_count);
        String[] expectedValMsgs = utils.getTestSettingAsArray("val-msgs");

        int loginResult = testPage.loginWith("none", password);
        assertThat("Login Result ", loginResult, is(1));

        List<WebElement> actualValMsgs = testPage.getValidationMessages();
        assertThat("Validation message count ", actualValMsgs.size(), is(expectedValMsgsCount));

        checkValidationMessages(actualValMsgs, expectedValMsgs);
    }

    @Step("Submit Login Details")
    public int login(LoginPage page, String user, String pass){
        return page.loginWith(user,pass,false);
    }

    public void checkValidationMessages(List<WebElement> actualValMsgs, String[] expectedValMsgs){
        for (int i = 0; i < actualValMsgs.size(); i++){
            String expectedValMsg = "[does not exist!]";
            if (i < expectedValMsgs.length){
                expectedValMsg = expectedValMsgs[i];
            }
            String actualValMsg = actualValMsgs.get(i).getText();
            assertThat("Text of validation message "+(i+1), actualValMsg, is(expectedValMsg));
        }
    }

}

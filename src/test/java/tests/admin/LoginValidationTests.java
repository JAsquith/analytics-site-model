package tests.admin;

import io.qameta.allure.*;
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
    @Test(groups = { "no-details"})
    public void noDetailsFails(){
        int loginResult = testPage.loginWith("","");
        assertThat("Attempted login should fail ", loginResult, is(1));
    }

    @Story("Trying to login with no username or password gives the right number of validation messages")
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = { "no-details"})
    public void noDetailsValMsgCount(){
        testPage.loginWith("", "");
        assertThat("Number of validation messages should be ",
                testPage.getValidationMessages().size(),
                is(utils.getTestSetting("no-details-val-msg-count",-1)));
    }

    @Story("Trying to login with no username or password gives the correct validation messages")
    @Severity(SeverityLevel.MINOR)
    @Test(groups = { "no-details"})
    public void noDetailsValMsgs(){
        testPage.loginWith("", "");
        checkValidationMessages(testPage.getValidationMessages(), utils.getTestSettingAsArray("val-msgs"));
    }

    @Story("Trying to login with no password should fail")
    @Severity(SeverityLevel.BLOCKER)
    @Parameters({ "username" })
    @Test(groups = { "no-password"})
    public void noPasswordFails(String username){
        int loginResult = testPage.loginWith(username, "");
        assertThat("Attempted login should fail ", loginResult, is(1));
    }

    @Story("Trying to login with no password gives the right number of validation messages")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({ "username" })
    @Test(groups = { "no-password"})
    public void noPasswordValMsgsCount(String username){
        testPage.loginWith(username, "");
        assertThat("Number of validation messages should be ",
                testPage.getValidationMessages().size(),
                is(utils.getTestSetting("no-password-val-msg-count", -1)));
    }

    @Story("Trying to login with no password gives the correct validation messages")
    @Severity(SeverityLevel.MINOR)
    @Parameters({ "username" })
    @Test(groups = { "no-password"})
    public void noPasswordValMsgs(String username){
        login(testPage, username, "");
        checkValidationMessages(testPage.getValidationMessages(), utils.getTestSettingAsArray("val-msgs"));
    }

    @Story("Trying to login with no username should fail")
    @Parameters({ "password" })
    @Test(groups = { "no-username"})
    public void noUsernameFails(String password){
        int loginResult = testPage.loginWith("", password);
        assertThat("Login Result ", loginResult, is(1));
    }

    @Story("Trying to login with no username gives the right number of validation messages")
    @Parameters({ "password" })
    @Test(groups = { "no-username"})
    public void noUsernameValMsgCount(String password){
       testPage.loginWith("", password);
        assertThat("Validation message count ",
                testPage.getValidationMessages().size(),
                is(utils.getTestSetting("no-username-val-msg-count", -1)));
    }

    @Story("Trying to login with no username gives the correct validation message text")
    @Severity(SeverityLevel.MINOR)
    @Parameters({ "password" })
    @Test(groups = { "no-username"})
    public void noUsernameValMsgs(String password){
        login(testPage, "", password);
        checkValidationMessages(testPage.getValidationMessages(), utils.getTestSettingAsArray("val-msgs"));
    }

    @Step("Submit Login Details")
    public int login(LoginPage page, String user, String pass){
        return page.loginWith(user,pass,false);
    }

    @Step( "Check displayed validation messages against test settings" )
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

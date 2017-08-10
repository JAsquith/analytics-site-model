package tests.account;

import io.qameta.allure.*;
import org.testng.annotations.*;
import tests.BaseTest;
import pages.account.LoginPage;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;

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
    String loginUrl;

    @BeforeTest
    public void setup(ITestContext testContext) throws MalformedURLException {

        initialise(testContext);
        testPage = new LoginPage(driver, true);
        loginUrl = testProtocol+"://"+testDomain+".sisraanalytics.co.uk" + testPage.PAGE_PATH;

    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }

    @Story("Trying to login with no username or password should fail")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void noDetailsFails(){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        int loginResult = login("","");
        assertThat("Attempted login should fail ", loginResult, is(1));
    }

    @Story("Trying to login with no username or password gives the right number of validation messages")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void noDetailsValMsgCount(){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login("", "");
        assertThat("Number of validation messages should be ",
                testPage.getValidationMessages().size(),
                is(utils.getTestSetting("no-details-val-msg-count",-1)));
    }

    @Story("Trying to login with no username or password gives the correct validation messages")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void noDetailsValMsgs(){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login("", "");
        checkValidationMessages(testPage.getValidationMessages(), utils.getTestSettingAsArray("no-details-val-msgs"));
    }

    @Story("Trying to login with no password should fail")
    @Severity(SeverityLevel.BLOCKER)
    @Parameters({ "username" })
    @Test
    public void noPasswordFails(String username){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        int loginResult = login(username, "");
        assertThat("Attempted login should fail ", loginResult, is(1));
    }

    @Story("Trying to login with no password gives the right number of validation messages")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({ "username" })
    @Test
    public void noPasswordValMsgsCount(String username){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login(username, "");
        assertThat("Number of validation messages should be ",
                testPage.getValidationMessages().size(),
                is(utils.getTestSetting("no-password-val-msg-count", -1)));
    }

    @Story("Trying to login with no password gives the correct validation messages")
    @Severity(SeverityLevel.MINOR)
    @Parameters({ "username" })
    @Test
    public void noPasswordValMsgs(String username){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login(username, "");
        checkValidationMessages(testPage.getValidationMessages(), utils.getTestSettingAsArray("no-password-val-msgs"));
    }

    @Story("Trying to login with no username should fail")
    @Parameters({ "password" })
    @Test
    public void noUsernameFails(String password){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        int loginResult = login("", password);
        assertThat("Login Result ", loginResult, is(1));
    }

    @Story("Trying to login with no username gives the right number of validation messages")
    @Parameters({ "password" })
    @Test
    public void noUsernameValMsgCount(String password){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login("", password);
        assertThat("Validation message count ",
                testPage.getValidationMessages().size(),
                is(utils.getTestSetting("no-username-val-msg-count", -1)));
    }

    @Story("Trying to login with no username gives the correct validation message text")
    @Severity(SeverityLevel.MINOR)
    @Parameters({ "password" })
    @Test
    public void noUsernameValMsgs(String password){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login("", password);
        checkValidationMessages(testPage.getValidationMessages(), utils.getTestSettingAsArray("no-username-val-msgs"));
    }

    @Step("Submit Login Details")
    public int login(String user, String pass){
        return testPage.loginWith(user,pass,false);
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

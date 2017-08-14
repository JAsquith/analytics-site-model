package tests.account;

import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.account.LoginPage;
import tests.BaseTest;

import java.net.MalformedURLException;
import java.util.List;

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
        loginUrl = applicationUrl + testPage.PAGE_PATH;
    }

/*
    @AfterTest
    public void tearDown(){
        driver.quit();
    }
*/

    @Story("Trying to login with no username or password should fail")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void noDetailsFails(){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        int loginResult = login("","");
        assertWithScreenshot("Attempted login should fail ", loginResult, is(1));
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
        assertWithScreenshot("Number of validation messages should be ",
                testPage.getValidationMessages().size(),
                is(getIntegerParam("no-details-val-msg-count",-1)));
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
        checkValidationMessages(testPage.getValidationMessages(), getArrayParam("no-details-val-msgs"));
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
        assertWithScreenshot("Attempted login should fail ", loginResult, is(1));
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
        assertWithScreenshot("Number of validation messages should be ",
                testPage.getValidationMessages().size(),
                is(getIntegerParam("no-password-val-msg-count", -1)));
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
        checkValidationMessages(testPage.getValidationMessages(), getArrayParam("no-password-val-msgs"));
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
        assertWithScreenshot("Login Result ", loginResult, is(1));
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
        assertWithScreenshot("Validation message count ",
                testPage.getValidationMessages().size(),
                is(getIntegerParam("no-username-val-msg-count", -1)));
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
        checkValidationMessages(testPage.getValidationMessages(), getArrayParam("no-username-val-msgs"));
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
            assertWithScreenshot("Text of validation message "+(i+1), actualValMsg, is(expectedValMsg));
        }
    }

}

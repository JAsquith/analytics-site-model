package tests.account;

import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.account.LoginPage;
import tests.BaseTest;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

/**
 *
 */
@Epic("Account Security")
@Feature("Login Page")
public class LoginValidationTests extends BaseTest {

    LoginPage testPage;
    String loginUrl;

    @BeforeTest
    public void setup(ITestContext testContext) {
        String initResult = super.initialise(testContext);
        if (!initResult.equals("")){
            fail(initResult);
        }
        testPage = new LoginPage(driver, true);
        loginUrl = applicationUrl + testPage.PAGE_PATH;
    }

    @Test( description = "No Username & Password: Login Fails" )
    @Severity(SeverityLevel.BLOCKER)
    public void noDetailsFails(){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        int loginResult = login("","");
        assertWithScreenshot("Attempted login should fail ", loginResult, is(1));
    }

    @Test( description = "No Username & Password: Validation Count" )
    @Severity(SeverityLevel.MINOR)
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

    @Test( description = "No Username & Password: Validation Messages" )
    @Severity(SeverityLevel.MINOR)
    public void noDetailsValMsgs(){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login("", "");
        checkValidationMessages(testPage.getValidationMessages(), getArrayParam("no-details-val-msgs"));
    }

    @Test( description = "No Password: Login Fails" )
    @Parameters({ "username" })
    @Severity(SeverityLevel.BLOCKER)
    public void noPasswordFails(String username){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        int loginResult = login(username, "");
        assertWithScreenshot("Attempted login should fail ", loginResult, is(1));
    }

    @Test( description = "No Password: Validation Count")
    @Parameters({ "username" })
    @Severity(SeverityLevel.MINOR)
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

    @Test( description = "No Password: Validation Messages" )
    @Parameters({ "username" })
    @Severity(SeverityLevel.MINOR)
    public void noPasswordValMsgs(String username){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        login(username, "");
        checkValidationMessages(testPage.getValidationMessages(), getArrayParam("no-password-val-msgs"));
    }

    @Test( description = "No Username: Login Fails" )
    @Parameters({ "password" })
    @Severity(SeverityLevel.BLOCKER)
    public void noUsernameFails(String password){
        if(!driver.getCurrentUrl().startsWith(loginUrl)){
            testPage = new LoginPage(driver, true);
        }
        testPage = new LoginPage(driver, true);
        int loginResult = login("", password);
        assertWithScreenshot("Login Result ", loginResult, is(1));
    }

    @Test( description = "No Username: Validation Count")
    @Parameters({ "password" })
    @Severity(SeverityLevel.MINOR)
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

    @Test( description = "No Username: Validation Messages" )
    @Parameters({ "password" })
    @Severity(SeverityLevel.MINOR)
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

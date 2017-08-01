package tests.admin;

import tests.SISRATest;
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
public class LoginValidationTests extends SISRATest {

    LoginPage testPage;

    @BeforeTest
    public void setup(ITestContext testContext) throws MalformedURLException {

        initialise(testContext);

        try {driver.manage().window().maximize();}
        catch (Exception e) {System.out.println(e.getMessage());}

        testPage = new LoginPage(driver, true);
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }

    @Parameters({ "username", "password", "val-msg-count", "val-msg-1", "val-msg-2" })
    @Test
    public void noDetailsTest(String username, String password, String val_msg_count,
                              String val_msg_1, String val_msg_2){
        int loginResult = testPage.loginWith(username, password);
        assertThat("Checked login form failed validation", loginResult, is(1));
        //assertThat("Checked login form failed validation", testPage.validationFailed(), is(true));
        List<WebElement> validationMessages = testPage.getValidationMessages();
        assertThat("Checked number of validation messages", validationMessages.size(), is(Integer.valueOf(val_msg_count)));
        String validationMessage = validationMessages.get(0).getText();
        assertThat("Checked validation message text", validationMessage, is(val_msg_1));
        validationMessage = validationMessages.get(1).getText();
        assertThat("Checked validation message text", validationMessage, is(val_msg_2));
    }


    @Parameters({ "username", "val-msg-count", "val-msg-1" })
    @Test
    public void noPasswordTest(String username, String val_msg_count, String val_msg_1){
        int loginResult = testPage.loginWith(username, "");
        assertThat("Checked login form failed validation", loginResult, is(1));
        //assertThat("Checked login form failed validation", testPage.validationFailed(), is(true));
        List<WebElement> validationMessages = testPage.getValidationMessages();
        assertThat("Checked number of validation messages", validationMessages.size(), is(Integer.valueOf(val_msg_count)));
        String validationMessage = validationMessages.get(0).getText();
        assertThat("Checked validation message text", validationMessage, is(val_msg_1));
    }

    @Parameters({ "password", "val-msg-count", "val-msg-1" })
    @Test
    public void noUsernameTest(String password, String val_msg_count, String val_msg_1){
        int loginResult = testPage.loginWith("", password);
        assertThat("Checked login form failed validation", loginResult, is(1));
        //assertThat("Checked login form failed validation", testPage.validationFailed(), is(true));
        List<WebElement> validationMessages = testPage.getValidationMessages();
        assertThat("Checked number of validation messages", validationMessages.size(), is(Integer.valueOf(val_msg_count)));
        String validationMessage = validationMessages.get(0).getText();
        assertThat("Checked validation message text", validationMessage, is(val_msg_1));
    }

    protected void successCriteria_ValidationPassed() {
        // Todo
    }
}

package pages.account;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Represents the contents and interactive elements on the Login Page, and
 * provides methods to perform common action sequences
 */
public class LoginPage {

    private RemoteWebDriver driver;
    public final String PAGE_PATH = "/Account/Login";

    public final By ACC_USERNAME = By.id("LogIn_UserName");
    public final By ACC_PASSWORD = By.id("LogIn_Password");
    public final By ACC_LOG_IN_BUTTON = By.cssSelector("form#fm_LogIn>div>div>button.button.green.noIcon");
    public final By ACC_FORGOT_LOGIN = By.linkText("Forgotten your login details?");

    public final By VAL_MESSAGE_LIST = By.cssSelector("div.validation-summary-errors>ul");
    public final By VAL_MESSAGES = By.cssSelector("div.validation-summary-errors>ul>li");

    public final By LOGOUT_MESSAGE = By.cssSelector("div.logOutMessage");

    public final By OTHER_SESSION_END_AND_LOGIN = By.cssSelector("form#fm_LogIn>div>button.button.green.noIcon");
    public final By OTHER_SESSION_CANCEL_LOGIN = By.cssSelector("form#fm_LogIn>div>a.button.cancel.noIcon");

    public final By LOST_PASSWORD_EMAIL = By.id("Email");
    public final By LOST_PASSWORD_CANCEL = By.cssSelector("#fm_lostPassword a.contains('Cancel')");
    public final By LOST_PASSWORD_OK = By.cssSelector("button:contains('OK')");

    public final By FIRST_LOGIN = By.cssSelector("div.firstLogin");
    public final By FIRST_LOGIN_PASSWORD_CHANGE = By.cssSelector("div.firstLogin>a:nth-of-type(1)");
    public final By FIRST_LOGIN_PASSWORD_KEEP = By.cssSelector("div.firstLogin>a:nth-of-type(2)");
    public final By FIRST_LOGIN_PASSWORD_NEW = By.id("NewPassword");
    public final By FIRST_LOGIN_PASSWORD_NEW_CONFIRM = By.id("NewPassword2");
    public final By FIRST_LOGIN_PASSWORD_SUBMIT = By.cssSelector("#fm_changePassword>button.button.green.noIcon");
    public final By FIRST_LOGIN_PASSWORD_CANCEL = By.cssSelector("#fm_changePassword>a.button.cancel.noIcon");

    public final By COMMERCIAL_SITE_LINK = By.cssSelector(".sisraLink>a");

    private final By MAIN_MENU_BUTTONS = By.cssSelector("#header>#headerUpper>#menuMain li");

    private WebDriverWait wait;
    public final int LOGIN_TIMEOUT = 30;

    /**
     * Navigates to the Account Login page url;
     * Opening this url should close any current user session in the driver's browser
     * @param aDriver   Any AnalyticsDriver object
     * @param loadByUrl
     */
    public LoginPage(RemoteWebDriver aDriver, boolean loadByUrl){
        driver = aDriver;
        wait = new WebDriverWait(driver, LOGIN_TIMEOUT);
        if (loadByUrl) {
            driver.get(getCurrentDomain() + PAGE_PATH);
        }
        wait.until(ExpectedConditions.elementToBeClickable(ACC_LOG_IN_BUTTON));
    }

    /**
     * Gets the domain part of the current URL up to the end of the TLD.  This normally results in
     * either "https://www.sisraanalytics.co.uk" or "http://dev.sisraanalytics.co.uk"
     * @return A String representing the site domain
     */
    public String getCurrentDomain(){
        String location = driver.getCurrentUrl();
        int domainEnd = location.indexOf("sisraanalytics.co.uk") + 20;
        return location.substring(0, domainEnd);
    }

    /**
     * Calls {@link #loginWith(String, String, boolean)} passing {@code false} as the third parameter
     * @param username  the text to be entered in the Username field
     * @param password  the text to be entered in the Password field
     * @return  {@code true} if the login succeeded;
     *          {@code false} if validation fails or the user is logged in elsewhere
     */
    public int loginWith(String username, String password){
        return loginWith(username, password, false);
    }

    /**
     * Enters the given account details and submits the login form.
     * The int returned by this method indicates the result of the login attempt. If none of the anticipated
     * results is detected within {@link #LOGIN_TIMEOUT} seconds, a {@link TimeoutException} is thrown
     * @param username  the text to be entered in the Username field
     * @param password  the text to be entered in the Password field
     * @param endOtherSession   {@code true} to force the login when the user is already logged in elsewhere;
     *                          {@code false} to cancel the login attempt if the user is already logged in;
     * @return  an int indicating the result of the login form submission.
     *          0 = success
     *          1 = validation fail (still on login screen)
     *          2 = user already logged in elsewhere (only valid if the {@code endOtherSession} param is {@code false}
     *          3 = first login for this user (use changePassword, or keepPassword methods to complete login)
     */
    public int loginWith(String username, String password, boolean endOtherSession){
        // Enter the given username and password and submit the form
        WebElement usernameField = getUsernameField();
        WebElement passwordField = getPasswordField();
        usernameField.clear();
        if (!username.equals("")) {
            usernameField.sendKeys(username);
        }
        passwordField.clear();
        if (!password.equals("")) {
            passwordField.sendKeys(password);
        }

        driver.findElement(ACC_LOG_IN_BUTTON).click();

        int result;
        try {
            result = waitForLoginResult(false, true);
            if (result == 2 && endOtherSession){
                try {
                    getOtherSessEndAndLoginButton().click();
                    result = waitForLoginResult(true, true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (result == 3){
                driver.findElement(FIRST_LOGIN_PASSWORD_KEEP).click();
                result = waitForLoginResult(false, false);
            }
        } catch (TimeoutException e) {
            throw new TimeoutException("Timed out waiting for login result", e);
        }
        return result;
    }

    /**
     * Clicks the Change Password button on the First Login form, waits for the Change Password form,
     * sets the new & confirmed password fields and submits the Change Password form.
     * @param password
     * @param passwordConfirm
     */
    public void changePassword(String password, String passwordConfirm){
        driver.findElement(FIRST_LOGIN_PASSWORD_CHANGE).click();
        wait.until(ExpectedConditions.elementToBeClickable(FIRST_LOGIN_PASSWORD_NEW));
        WebElement field = driver.findElement(FIRST_LOGIN_PASSWORD_NEW);
        field.clear();
        field.sendKeys(password);

        field = driver.findElement(FIRST_LOGIN_PASSWORD_NEW_CONFIRM);
        field.clear();
        field.sendKeys(passwordConfirm);

        field.submit();
    }

    /**
     * Clicks the Keep Password button on the First Login form
     */
    public void keepPassword(){
        driver.findElement(FIRST_LOGIN_PASSWORD_KEEP).click();
    }

    /**
     * Simple accessor for the Username input element
     * @return  A {@link WebElement} object representing the username input element
     */
    public WebElement getUsernameField(){
        return driver.findElement(ACC_USERNAME);
    }

    /**
     * Simple accessor for the Password input element
     * @return  A {@link WebElement} object representing the password input element
     */
    public WebElement getPasswordField() {
        return driver.findElement(ACC_PASSWORD);
    }

    /**
     * Simple accessor for the Log In button element
     * @return  A {link WebElement} object representing the Log In button element
     */
    public WebElement getLogInButton(){
        return driver.findElement(ACC_LOG_IN_BUTTON);
    }

    /**
     * Simple accessor for the Forgotten Login Details link (&lt;a&gt;) element
     * @return  A {@link WebElement} object representing the Forgotten Login Details link (&lt;a&gt;) element
     */
    public WebElement getForgottenDetailsLink(){
        return driver.findElement(ACC_FORGOT_LOGIN);
    }

    /**
     * Simple accessor for the validation messages list element
     * @return  A {@link WebElement} object representing the &lt;ul&gt; element showing any validaation messages
     */
    public WebElement getValidationMsgList(){
        return driver.findElement(VAL_MESSAGE_LIST);
    }

    /**
     * Accessor for the validation message list item elements
     * @return  A {@link List} object with entries for each &lt;li&gt; element in the validation messages &lt;ul&gt;
     */
    public List<WebElement> getValidationMessages(){
        return driver.findElements(VAL_MESSAGES);
    }

    /**
     * Simple accessor for the log out message div element.
     * Should contain one of three strings indicating:
     *  - Manual log out
     *  - Session timeout due to inactivity
     *  - Same user logged in on another session
     * @return  A {@link WebElement} object representing the &lt;div&gt; element containing the log out message
     */
    public WebElement getLogoutMessage(){
        return driver.findElement(LOGOUT_MESSAGE);
    }

    /**
     * Simple accessor for the Registered Email Address input element on the Lost Password form
     * @return  A {@link WebElement} object representing the &lt;input&gt; element
     */
    public WebElement getLostPawdEmailField(){
        return driver.findElement(LOST_PASSWORD_EMAIL);
    }

    /**
     * Simple accessor for the Cancel button (a link element) on the Lost Password form
     * @return  A {@link WebElement} object representing the &lt;a&gt; element
     */
    public WebElement getLostPwdCancelButton(){
        return driver.findElement(LOST_PASSWORD_CANCEL);
    }

    /**
     * Simple accessor for the OK button on the Lost Password form
     * @return  A {@link WebElement} object representing the &lt;button&gt; element
     */
    public WebElement getLostPwdOKButton(){
        return driver.findElement(LOST_PASSWORD_OK);
    }

    /**
     * Simple accessor for the End Other Sesssion And Log In button on the Already Logged In form
     * @return  A {@link WebElement} object representing the &lt;button&gt; element
     */
    public WebElement getOtherSessEndAndLoginButton(){
        return driver.findElement(OTHER_SESSION_END_AND_LOGIN);
    }

    /**
     * Simple accessor for the Cancel button (a link element) on the Already Logged In form
     * @return  A {@link WebElement} object representing the &lt;a&gt; element
     */
    public WebElement getOtherSessCancelLoginButton() {
        return driver.findElement(OTHER_SESSION_CANCEL_LOGIN);
    }

    /**
     * Tests for the presence of the Analytics Main Menu as a proxy for a successful login attempt
     * @return  {@code true} if the Analytics Main Menu is found on the current page; otherwise {@code false}
     */
    public boolean loginSucceeded(){
        return driver.findElements(MAIN_MENU_BUTTONS).size() > 0;
    }

    /**
     * Tests how many validation messages are currently displayed on screen
     * @return  {@code true} if there is at least one &lt;li&gt; item in the validation messages list;
     *          {@code false} if the list is not present or contains no items
     */
    public boolean validationFailed(){
        return getValidationMessages().size() > 0;
    }

    /**
     * Tests for the presence of the Cancel button on the Already Logged In form
     * @return  {@code true} if the Cancel button is found on the current page; otherwise {@code false}
     */
    public boolean otherActiveSession(){
        return driver.findElements(OTHER_SESSION_CANCEL_LOGIN).size() > 0;
    }

    /**
     * Tests for the presence of the div element displayed when a user logs in for the first time.
     * @return  {@code true} if the First Login div is found on the current page; otherwise {@code false}
     */
    public boolean firstLogin(){
        return driver.findElements(FIRST_LOGIN).size() > 0;
    }

    /**
     * Checks the outcome of a login attempt.
     * @return  0 = success (the browser is displaying an Analytics page with Main Menu buttons)
     *          1 = validation fail (still on login screen)
     *          2 = user already logged in elsewhere (only valid if the {@code endOtherSession} param is {@code false}
     *          3 = first login for this user (use changePassword, or keepPassword methods to complete login)
     */
    public int loginResult(boolean ignoreOtherSession) {
        return loginResult(ignoreOtherSession, false);
    }

    /**
     * Checks the outcome of a login attempt.
     * @return  0 = success (the browser is displaying an Analytics page with Main Menu buttons)
     *          1 = validation fail (still on login screen)
     *          2 = user already logged in elsewhere (only valid if the {@code endOtherSession} param is {@code false}
     *          3 = first login for this user (use changePassword, or keepPassword methods to complete login)
     */
    public int loginResult(boolean ignoreOtherSession, boolean checkForFirst){
        if(loginSucceeded()){
            return 0;
        }
        if(validationFailed()){
            return 1;
        }
        if(otherActiveSession()){
            if (ignoreOtherSession){
                return 0;
            }
            return 2;
        }
        if(checkForFirst && firstLogin()){
            return 3;
        }
        throw new NoSuchElementException("");
    }

    public int waitForLoginResult(boolean ignoreOtherSession, boolean checkForFirst) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            int loginResult = wait.until((Function<WebDriver, Integer>)
                    driver -> loginResult(ignoreOtherSession,checkForFirst));
            return loginResult;

        } catch (TimeoutException e) {
            throw new TimeoutException("Timed out waiting for login result", e);
        }
    }

}

package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * Provides Selenium locators (as static fields) used to locate common
 * <a href='https://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/WebElement.html'>
 * WebElement</a>s found on the Login pages of
 * <a href='https://www.sisraanalytics.co.uk/'>SISRA Analytics</a>, and convenience methods for
 * common interactions with these elements.
 * <br><br>
 *   Almost all the fields have additional pre-conditions when used with driver.findElement or similar.
 *   The supported page(s)/application state(s) for each locator are described within the javadoc for
 *   each Field. Use on other pages, or when the application is in another state, will result in either:<ul>
 *       <li>An incorrect/unexpected element being identified; or</li>
 *       <li>No element matching the locator (risking a org.openqa.selenium.NotFoundException
 *       or org.openqa.selenium.TimeoutException being thrown.)</li>
 *   </ul>
 *
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-18
 */
public class Login extends Base {

    // Elements on the initial login page

    /** Identifies the 'Username:' field (&lt;input&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed
     */
    public static final By ACC_USERNAME = By.id("LogIn_UserName");
    /** Identifies the 'Password:' field (&lt;input&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed
     */
    public static final By ACC_PASSWORD = By.id("LogIn_Password");
    /** Identifies the 'Log In' button (&lt;button&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed
     */
    public static final By ACC_LOG_IN_BUTTON =
            By.cssSelector("form#fm_LogIn>div>div>button.button.green.noIcon");
    /** Identifies the 'Forgotten your login details' link (&lt;a&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed
     */
    public static final By ACC_FORGOT_LOGIN = By.linkText("Forgotten your login details?");
    /** Identifies the Validation Messages list (&lt;ul&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed or
     * incorrect user credentials have not been submitted. <br>
     * N.B. Submission of a 'Blocked' username will not trigger validation messages.
     * Use {@link #LOGOUT_MESSAGE} instead.
     */
    public static final By VAL_MESSAGE_LIST = By.cssSelector("div.validation-summary-errors>ul");
    /** Identifies the Validation Messages list items (&lt;li&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed or
     * incorrect user credentials have not been submitted. <br>
     * N.B. Submission of a 'Blocked' username will not trigger validation messages.
     * Use {@link #LOGOUT_MESSAGE} instead.
     */
    public static final By VAL_MESSAGES = By.cssSelector("div.validation-summary-errors>ul>li");
    /** Identifies the 'Logout Message' (&lt;div&gt; element).
     * <br><br>
     * Not supported when: the {code /Account/Login} page is not displayed or if one of:
     * <ul>
     *     <li>The Logout button was clicked</li>
     *     <li>The user's session timed out</li>
     *     <li>AA 'Blocked' username was submitted</li>
     * </ul>
     * has not occurred. <br>
     */
    public static final By LOGOUT_MESSAGE = By.cssSelector("div.logOutMessage");
    // Elements shown if the user logging in already has another session
    /** Identifies the 'End Other Session And Login' button (&lt;button&gt; element).
     * <br><br>
     * Not supported when: the 'existing user session' page is not displayed
     */
    public static final By OTHER_SESSION_END_SESSION_AND_LOGIN =
            By.cssSelector("form#fm_LogIn>div>button.button.green.noIcon");
    /** Identifies the 'Cancel' button (&lt;a&gt; element).
     * <br><br>
     * Not supported when: the 'existing user session' page is not displayed
     */
    public static final By OTHER_SESSION_CANCEL_LOGIN =
            By.cssSelector("form#fm_LogIn>div>a.button.cancel.noIcon");
    // Elements on the Lost Password page
    /** Identifies the 'Email:' field (&lt;input&gt; element).
     * <br><br>
     * Not supported when: the 'forgotten login details' page is not displayed
     */
    public static final By LOST_PASSWORD_EMAIL = By.id("Email");
    /** Identifies the 'Cancel' button (&lt;a&gt; element).
     * <br><br>
     * Not supported when: the 'forgotten login details' page is not displayed
     */
    public static final By LOST_PASSWORD_CANCEL = By.cssSelector("#fm_lostPassword a.contains('Cancel')");
    /** Identifies the 'OK' button (&lt;button&gt; element).
     * <br><br>
     * Not supported when: the 'forgotten login details' page is not displayed
     */
    public static final By LOST_PASSWORD_OK = By.cssSelector("button:contains('OK')");
    // Elements shown on first login with new account details
    /** Identifies the 'Change Password' button (&lt;a&gt; element).
     * <br><br>
     * Not supported when: the 'First login' page is not displayed
     */
    public static final By FIRST_LOGIN_PASSWORD_CHANGE = By.cssSelector("div.firstLogin>a:nth-of-type(1)");
    /** Identifies the 'Keep Password' button (&lt;a&gt; element).
     * <br><br>
     * Not supported when: the 'First login' page is not displayed
     */
    public static final By FIRST_LOGIN_PASSWORD_KEEP = By.cssSelector("div.firstLogin>a:nth-of-type(2)");
    /** Identifies the 'New Password' field (&lt;input&gt; element).
     * <br><br>
     * Not supported when: the 'First login &gt; Change Password' page is not displayed
     */
    public static final By FIRST_LOGIN_PASSWORD_NEW = By.id("NewPassword");
    /** Identifies the 'Confirm Password' field (&lt;input&gt; element).
     * <br><br>
     * Not supported when: the 'First login &gt; Change Password' page is not displayed
     */
    public static final By FIRST_LOGIN_PASSWORD_NEW_CONFIRM = By.id("NewPassword2");
    /** Identifies the 'Save' button (&lt;button&gt; element).
     * <br><br>
     * Not supported when: the 'First login &gt; Change Password' page is not displayed
     */
    public static final By FIRST_LOGIN_PASSWORD_SUBMIT =
            By.cssSelector("#fm_changePassword>button.button.green.noIcon");
    /** Identifies the 'Cancel' button (&lt;button&gt; element).
     * <br><br>
     * Not supported when: the 'First login &gt; Change Password' page is not displayed
     */
    public static final By FIRST_LOGIN_PASSWORD_CANCEL =
            By.cssSelector("#fm_changePassword>a.button.cancel.noIcon");

    // Constructors
    /**
     * Creates a new Login instance with the given AnayticsDriver and navigates to the
     * home page of the test environment.
     * @param aDriver   An AnalyticsDriver
     */
    public Login(AnalyticsDriver aDriver){
        this(aDriver, "TEST");
    }
    /**
     * Creates a new Login instance with the given AnayticsDriver and navigates to the
     * homepage of the given environment ("DEV", "TEST", or "LIVE").
     * @param aDriver   An AnalyticsDriver.
     * @param domain    One of: "DEV", "TEST", or "LIVE".
     */
    public Login(AnalyticsDriver aDriver, String domain){
        super(aDriver);
        switch (domain){
            case "DEV":visit(DEV_HOME);break;
            case "TEST":visit(TEST_HOME);break;
            case "LIVE":visit(LIVE_HOME);break;
            default: throw new IllegalArgumentException("Unrecognized domain (" + domain +")");
        }
        waitForClickabilityOf(ACC_LOG_IN_BUTTON);;
    }
    // Methods for actions on the initial login page
    /**
     * Simply types the given username and password into the relevant fields of the
     * login page and clicks the Log In button. <br><br>
     * Does not wait before returning control to the caller.<br>Use this method for
     * tests of the login page validation/security when the user details passed in
     * are/may not be valid, or if the login is expected to succeed, but you want to deal
     * with the subsequent page ('Home', 'Existing Session', or 'First Login') yourself.
     *
     * @param user      A String to type in as the username
     * @param password  A String to type in ass the password
     */
    public void with(String user, String password){
        this.overType(user,ACC_USERNAME);
        this.overType(password, ACC_PASSWORD);
        this.submit(ACC_LOG_IN_BUTTON);
    }
    /**
     * Calls {@link #with(String, String)} using the passed in strings user and password,
     * checks for the 'Existing Session' page and (if it is found),
     * uses the passed in boolean to decide which button to click.
     *
     * @param user              The username to try
     * @param password          The password to try
     * @param forceNewSession   Whether to click {@link #OTHER_SESSION_END_SESSION_AND_LOGIN}
     *                          (when true) or {@link #OTHER_SESSION_CANCEL_LOGIN} (when false)
     *                          if the 'Existing Session' page is displayed
     * @return  true if the login completed fully, false if the username & password failed, or
     *          if an existing session was found and forceNewSession == false.
     */
    public boolean with(String user, String password, boolean forceNewSession){
        // Enter & submit the login details
        this.with(user, password);
        // Check the initial login form is no longer displayed
        try {
            waitForInvisibilityOf(ACC_LOG_IN_BUTTON, DEFAULT_SHORT_TIMEOUT);
        } catch (TimeoutException e){
            return false;
        }

        // Check for the 'Existing Session' page, and act based on forceNewSession
        List<WebElement> endSessionBtns = findAll(OTHER_SESSION_END_SESSION_AND_LOGIN);
        if (endSessionBtns.size()>0){
            if (forceNewSession)
                endSessionBtns.get(0).click();
            else {
                System.out.println("There is currently another active session for " + user + "!");
                find(OTHER_SESSION_CANCEL_LOGIN).click();
                return false;
            }
        }
        return true;
    }
    /**
     * Calls {@link #with(String, String)} with the user and password parameters,
     * checks for the 'First Login' page and if it is found && firstLogin_NewPassword
     * is not the empty String resets the user's password.
     *
     * @param user                      The username to try
     * @param password                  The password to try
     * @param newPassword    What to change the user's password to if this is the
     *                                  first login for this user. If null or the empty String
     *                                  is passed, the existing password is kept.
     * @return  false if the username & password failed or firstLogin_NewPassword was
     *          rejected, otherwise true
     */
    public boolean with(String user, String password, String newPassword){
        // Enter & submit the login details
        this.with(user, password);
        // Check the initial login form is no longer displayed
        try {
            waitForInvisibilityOf(ACC_LOG_IN_BUTTON, DEFAULT_SHORT_TIMEOUT);
        } catch (TimeoutException e){
            return false;
        }

        // This checks for the 'First Login - change password' page, and acts based on
        //      firstLogin_NewPassword
        List<WebElement> firstLoginBtns = findAll(FIRST_LOGIN_PASSWORD_CHANGE);
        if (firstLoginBtns.size()>0){
            if (newPassword == null)
                newPassword = "";

            if (newPassword.equals(""))
                click(FIRST_LOGIN_PASSWORD_KEEP);

            else {
                firstLoginBtns.get(0).click();
                WebDriverWait driverWait = new WebDriverWait(driver, 10);
                driverWait.until(elementToBeClickable(FIRST_LOGIN_PASSWORD_NEW))
                        .sendKeys(newPassword);
                type(newPassword, FIRST_LOGIN_PASSWORD_NEW_CONFIRM);
                click(FIRST_LOGIN_PASSWORD_SUBMIT);
                // TODO: Check for rejected password and return false
            }
        }
        return true;
    }
    /**
     * Clicks the 'Forgotten your login details' link and submits the given email address. <br>
     * Does not wait before returning control to the caller.
     *
     * @param email The email address to which the reset account link should be sent.
     */
    public void accountRecovery(String email){
        click(ACC_FORGOT_LOGIN);
        WebDriverWait driverWait = new WebDriverWait(driver, 10);
        driverWait.until(presenceOfElementLocated(LOST_PASSWORD_EMAIL)).sendKeys(email);
        click(LOST_PASSWORD_OK);
    }

    // Methods to for after main actions
    public boolean waitForLoginSuccess(int timeout){

        boolean isLoggedIn = isDisplayed(By.cssSelector(".navImg"));
        long deadline = System.currentTimeMillis()+(timeout * 1000);

        while (!isLoggedIn && System.currentTimeMillis() < deadline){
            try {
                Thread.sleep(100);
                isLoggedIn = isDisplayed(By.id("mainMenu"));
            } catch (InterruptedException e) {
                // do something?
            }
        }
        if (!isLoggedIn)
            return false;

        return true;
    }
    /**
     * Checks if the validation message list is displayed.
     * @return true/false
     */
    public boolean validationMessagesDisplayed(){return isDisplayed(VAL_MESSAGE_LIST);}
    /**
     * Waits for the Validation Messages List to be visible.
     * @param timeout   maximum number of seconds to wait
     * @return  The &lt;ul&gt; element containing any Validation messages
     * @throws  TimeoutException if the validation messages list is not shown in
     *          the timeout period
     */
    public WebElement waitForValidationMessages(int timeout)
            throws TimeoutException {
        WebDriverWait driverWait = new WebDriverWait(driver, timeout);
        return driverWait.until(visibilityOfElementLocated(Login.VAL_MESSAGE_LIST));
    }
    /**
     * Returns the Validation message &lt;li&gt; elements as a List.
     * @return  the Validation message &lt;li&gt; elements as a List.
     */
    public List<WebElement> getValidationMessages(){return findAll(VAL_MESSAGES);}
    public WebElement waitForLogoutMessage(int timeout){
        WebDriverWait driverWait = new WebDriverWait(driver, timeout);
        return driverWait.until(visibilityOfElementLocated(LOGOUT_MESSAGE));
    }
}

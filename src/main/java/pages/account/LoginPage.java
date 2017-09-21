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
    public final By ACC_LOG_IN_BUTTON = By.cssSelector("form#fm_LogIn button.green");

    public final By VAL_MESSAGES = By.cssSelector("div.validation-summary-errors>ul>li");

    public final By OTHER_SESSION_END_AND_LOGIN = By.cssSelector("form#fm_LogIn button.green");
    public final By OTHER_SESSION_CANCEL_LOGIN = By.cssSelector("form#fm_LogIn a.cancel");

    public final By FIRST_LOGIN = By.cssSelector("div.firstLogin");
    public final By FIRST_LOGIN_PASSWORD_KEEP = By.cssSelector("div.firstLogin>a:nth-of-type(2)");
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
        // Todo: add a "No Dev Access" result to the waitForLoginResult method and deal with it appropriately here
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
     * Simple accessor for the Username input element
     * @return  A {@link WebElement} object representing the username input element
     */
    private WebElement getUsernameField(){
        return driver.findElement(ACC_USERNAME);
    }

    /**
     * Simple accessor for the Password input element
     * @return  A {@link WebElement} object representing the password input element
     */
    private WebElement getPasswordField() {
        return driver.findElement(ACC_PASSWORD);
    }

    /**
     * Accessor for the validation message list item elements
     * @return  A {@link List} object with entries for each &lt;li&gt; element in the validation messages &lt;ul&gt;
     */
    public List<WebElement> getValidationMessages(){
        return driver.findElements(VAL_MESSAGES);
    }

    /**
     * Simple accessor for the End Other Sesssion And Log In button on the Already Logged In form
     * @return  A {@link WebElement} object representing the &lt;button&gt; element
     */
    private WebElement getOtherSessEndAndLoginButton(){
        return driver.findElement(OTHER_SESSION_END_AND_LOGIN);
    }

    /**
     * Tests for the presence of the Analytics Main Menu as a proxy for a successful login attempt
     * @return  {@code true} if the Analytics Main Menu is found on the current page; otherwise {@code false}
     */
    private boolean loginSucceeded(){
        return driver.findElements(MAIN_MENU_BUTTONS).size() > 0;
    }

    /**
     * Tests how many validation messages are currently displayed on screen
     * @return  {@code true} if there is at least one &lt;li&gt; item in the validation messages list;
     *          {@code false} if the list is not present or contains no items
     */
    private boolean validationFailed(){
        return getValidationMessages().size() > 0;
    }

    /**
     * Tests for the presence of the Cancel button on the Already Logged In form
     * @return  {@code true} if the Cancel button is found on the current page; otherwise {@code false}
     */
    private boolean otherActiveSession(){
        return driver.findElements(OTHER_SESSION_CANCEL_LOGIN).size() > 0;
    }

    /**
     * Tests for the presence of the div element displayed when a user logs in for the first time.
     * @return  {@code true} if the First Login div is found on the current page; otherwise {@code false}
     */
    private boolean firstLogin(){
        return driver.findElements(FIRST_LOGIN).size() > 0;
    }

    /**
     * Checks the outcome of a login attempt.
     * @return  0 = success (the browser is displaying an Analytics page with Main Menu buttons)
     *          1 = validation fail (still on login screen)
     *          2 = user already logged in elsewhere (only valid if the {@code endOtherSession} param is {@code false}
     *          3 = first login for this user (use changePassword, or keepPassword methods to complete login)
     */
    private int loginResult(boolean ignoreOtherSession, boolean checkForFirst){
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

    private int waitForLoginResult(boolean ignoreOtherSession, boolean checkForFirst) {
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

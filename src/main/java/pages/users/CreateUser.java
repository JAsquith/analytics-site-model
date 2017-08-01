package pages.users;

import pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Provides access to the contents and interactive elements of the Create New User page
 */
public class CreateUser extends AnalyticsPage {

    private static final String PAGE_PATH = "/Users/CreateUser";
    private static final By FIRST_NAME_FIELD = By.id("UserCreate_SchUser_FirstName");
    private static final By LAST_NAME_FIELD = By.id("UserCreate_SchUser_LastName");
    private static final By EMAIL_ADDRESS_FIELD = By.id("UserCreate_SchUser_Email");
    private static final By AUTH_GROUP_DDL = By.id("UserCreate_AuthGrp_ID");
    private static final By CREATE_NEW_USER_BUTTON = By.cssSelector(".submitButtons>.green");
    private static final By CANCEL_BUTTON = By.cssSelector(".submitButtons>.cancel");

    /**
     * Simple constructor
     * @param aDriver       The browser should be on the New User page, or loadByUrl should be true
     * @param loadByUrl     If {@code true} the WebDriver.get(url) method is used to navigate to the new user page
     */
    public CreateUser(RemoteWebDriver aDriver, boolean loadByUrl) {
        super(aDriver);
        if (loadByUrl) {
            driver.get(getCurrentDomain() + PAGE_PATH);
        } else {
            waitForLoadingWrapper(LONG_WAIT);
        }
    }

    /**
     * Overwrite the value in the First Name field
     * @param firstName     New value for the field
     * @return This page object (to allow method daisy-chaining)
     */
    public CreateUser setFirstName(String firstName){
        WebElement field = driver.findElement(FIRST_NAME_FIELD);
        field.clear();
        field.sendKeys(firstName);
        return this;
    }

    /**
     * Overwrite the value in the Last Name field
     * @param lastName  New value for the field
     * @return This page object (to allow method daisy-chaining)
     */
    public CreateUser setLastName(String lastName){
        WebElement field = driver.findElement(LAST_NAME_FIELD);
        field.clear();
        field.sendKeys(lastName);
        return this;
    }

    /**
     * Overwrite the value in the Email Address field
     * @param emailAddress  New value for the field
     * @return This page object (to allow method daisy-chaining)
     */
    public CreateUser setEmailAddress(String emailAddress){
        WebElement field = driver.findElement(EMAIL_ADDRESS_FIELD);
        field.clear();
        field.sendKeys(emailAddress);
        return this;
    }

    /**
     * Choose a new option from the Authority Group drop down list
     * @param authGroup     Visible text of the option to choose
     * @return This page object (to allow method daisy-chaining)
     */
    public CreateUser selectAuthorityGroup(String authGroup){
        new Select(driver.findElement(AUTH_GROUP_DDL)).selectByVisibleText(authGroup);
        return this;
    }

    /**
     * Convenience method which sets all field values through a single method call
     * @param firstName     The value in the First Name field will be replaced with this string
     * @param lastName      The value in the Last Name field will be replaced with this string
     * @param emailAddress  The value in the Email Address field will be replaced with this string
     * @param authGroup     The option with this visible text will be selected in the Authority Group drop down list
     * @return This page object (to allow method daisy-chaining)
     */
    public CreateUser setFields(String firstName, String lastName, String emailAddress, String authGroup){
        return this.setFirstName(firstName)
                .setLastName(lastName)
                .setEmailAddress(emailAddress)
                .selectAuthorityGroup(authGroup);
    }

    /**
     * Clicks the Create New User button which completes the process of creating the user
     * @return a {@link UserList} page object
     */
    public void clickCreateNewUser(){
        driver.findElement(CREATE_NEW_USER_BUTTON).click();
    }

}

package analytics.pages.users;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Extends the AnalyticsPage class to model components and elements on the Users &gt; User List page
 */
public class UserList extends AnalyticsPage {

    public final String PAGE_PATH = "/Users";
    public final By NEW_USER_BTN = By.linkText("Create User");
    public final By ACTIVE_EMAIL_ADDRESS_CELLS = By.cssSelector(".ftGreen td:nth-of-type(3)");
    public final By BLOCKED_EMAIL_ADDRESS_CELLS = By.cssSelector(".ftRed td:nth-of-type(3)");

    /**
     * Simple constructor
     * @param aDriver   The browser should be showing the User list page, or loadByUrl should be true
     * @param loadByUrl If true, the WebDriver.get(url) method will be used to load the page
     */
    public UserList (AnalyticsDriver aDriver, boolean loadByUrl) {
        super(aDriver);
        if (loadByUrl) {
            driver.get(driver.getDomainRoot() + PAGE_PATH);
        }
        waitForLoadingWrapper(LONG_WAIT);
    }

    /**
     * Navigates to the Create New User page
     * @param byUrl     {@code false} causes the Create New User button on this page to be clicked;
     *                  {@code true} causes the New User page to be loaded by its url
     * @return  A CreateUser page object representing the New User page
     */
    public CreateUser createNewUser(boolean byUrl){
        if (!byUrl){
            driver.findElement(NEW_USER_BTN).click();
            waitForLoadingWrapper();
        }
        return new CreateUser(driver, byUrl);
    }

    /**
     * Used to check whether a user with the given email address is listed in the Active Users table
     * @param emailAddress  The email address to look for
     * @return  {@code true} if the emailAddress param matches a listed user; {@code false} otherwise
     */
    public boolean isUserActive(String emailAddress){
        // ToDo: Change signature to return a UserRow component
        List<WebElement> tableCells = driver.findElements(ACTIVE_EMAIL_ADDRESS_CELLS);
        for (WebElement tableCell : tableCells) {
            if (tableCell.getText().trim().equals(emailAddress)) {
                return true;
            }
        }
        return false;
    }
}

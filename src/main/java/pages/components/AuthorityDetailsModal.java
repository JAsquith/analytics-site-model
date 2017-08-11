package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;

import java.util.List;

/**
 * A component object representing the content and actions within an Authority Detilas modal
 */
public class AuthorityDetailsModal extends AnalyticsComponent {

    public final By MODAL_WINDOW = By.className("window");

    public final By CAN_DO_LIST_ITEMS = By.cssSelector("#modalPanel .bull>li");
    public final By CLOSE_BUTTON = By.cssSelector(".modalSubmit>.cancel");

    /**
     * Creates a Component object for the Authority Details modal and waits for the modal window to be clickable.
     * @param aDriver   Browser should be logged in to Analytics and
     *                  the Create Filter button should have been clicked
     */
    public AuthorityDetailsModal(RemoteWebDriver aDriver) {
        super(aDriver);
        initModal(MODAL_WINDOW);
    }

    public String[] getCanDoList(){
        List<WebElement> canDoItems = driver.findElements(CAN_DO_LIST_ITEMS);
        String[] canDoStrings = new String[canDoItems.size()];
        for(int i = 0; i<canDoItems.size(); i++){
            canDoStrings[i] = canDoItems.get(i).getText().trim();
        }
        return canDoStrings;
    }

/*
    public AuthorityDetailsModal setFilterName(String name){
        WebElement field = driver.findElement(FILTER_NAME_FIELD);
        field.clear();
        field.sendKeys(name);
        return this;
    }

    public AuthorityDetailsModal setFilterDefault(String defaultValue){
        WebElement field = driver.findElement(DEFAULT_VALUE_FIELD);
        field.clear();
        field.sendKeys(defaultValue);
        return this;
    }

*/
    public void clickClose(){
        driver.findElement(CLOSE_BUTTON).click();
    }

}

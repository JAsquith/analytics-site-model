package analytics.pages.config.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


/**
 * A component object representing the content and actions within a Create Filter modal
 */
public class CreateFilterModal extends AnalyticsComponent{

    private final By MODAL_WINDOW = By.className("window");

    private final By FILTER_NAME_FIELD = By.id("FilterGlobalCreate_Filter_Name");
    private final By DEFAULT_VALUE_FIELD = By.id("FilterGlobalCreate_Filter_DefaultVal");

    private final By CANCEL_BUTTON = By.cssSelector(".modalSubmit>.cancel");
    private final By CREATE_BUTTON = By.cssSelector(".modalSubmit>.green");

    /**
     * Creates a Component object for the Create Filter modal and waits for the modal window to be clickable.
     * @param aDriver   Browser should be on the Filter Management page and
     *                  the Create Filter button should have been clicked
     */
    public CreateFilterModal(AnalyticsDriver aDriver) {
        super(aDriver);
        initModal(MODAL_WINDOW);
    }

    public CreateFilterModal setFilterName(String name){
        WebElement field = driver.findElement(FILTER_NAME_FIELD);
        field.clear();
        field.sendKeys(name);
        return this;
    }

    public CreateFilterModal setFilterDefault(String defaultValue){
        WebElement field = driver.findElement(DEFAULT_VALUE_FIELD);
        field.clear();
        field.sendKeys(defaultValue);
        return this;
    }

    public void clickSave(){
        driver.findElement(CREATE_BUTTON).click();
    }

    public void clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
    }

}

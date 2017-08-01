package pages.data.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Represents the contents and interactive elements of the Side menu when in the Data area of EAP
 */
public class DataSideMenu extends AnalyticsComponent {

// FIELDS
    public final By COMPONENT = By.cssSelector("#menuSide .EAPtabs");
    public final By ACTIVE_TABS = By.cssSelector("#menuSide .EAPtabs>.EAPtoggle>a");
    public final By SELECTED_TAB = By.cssSelector("#menuSide .EAPtabs>.EAPtoggle>a.withInfo");
    public final By MENU_OPTIONS_ACTIVE = By.cssSelector("#menuSide h3:not(.disabled)>*");
    public final By MENU_OPTION_SELECTED = By.cssSelector("#menuSide h3:not(.disabled)>span");
    public final By MENU_OPTIONS_ACTIVE_NOT_SELECTED = By.cssSelector("#menuSide h3:not(.disabled)>a");
    public final By MENU_SUB_OPTIONS_ACTIVE = By.cssSelector("#menuSide li>a");
    public final By MENU_SUB_OPTION_SELECTED = By.cssSelector("#menuSide li.selected>a");
    public final By MENU_SUB_OPTIONS_NOT_SELECTED = By.cssSelector("#menuSide li:not(.selected)>a");
    public final By PUBLISH_TAB_DATA = By.cssSelector("#menuSide .button.disabled");

    // CONSTRUCTOR
    public DataSideMenu(RemoteWebDriver aDriver) {
        super(aDriver);
        waitMedium.until(ExpectedConditions.elementToBeClickable(COMPONENT));
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public List<WebElement> getActiveTabs(){
        return driver.findElements(ACTIVE_TABS);
    }
    public WebElement getTab(String tabText){
        List<WebElement> activeTabs = getActiveTabs();
        for (WebElement activeTab : activeTabs) {
            if (activeTab.getText().trim().equals(tabText)) {
                return activeTab;
            }
        }
        return null;
    }
    public WebElement getSelectedTab(){
        return driver.findElement(SELECTED_TAB);
    }

    public List<WebElement> getActiveMenuOptions(){
        return driver.findElements(MENU_OPTIONS_ACTIVE);
    }
    public WebElement getMenuOption(String optionText){
        List<WebElement> activeOptions = getActiveMenuOptions();
        for (WebElement activeOption : activeOptions) {
            if (activeOption.getText().trim().equals(optionText)) {
                return activeOption;
            }
        }
        return null;
    }
    public WebElement getSelectedMenuOption(){
        return driver.findElement(MENU_OPTION_SELECTED);
    }
    public List<WebElement> getUnselectedMenuOptions(){
        return driver.findElements(MENU_OPTIONS_ACTIVE_NOT_SELECTED);
    }

    public List<WebElement> getActiveSubMenuOptions(){
        return driver.findElements(MENU_SUB_OPTIONS_ACTIVE);
    }
    public WebElement getSubMenuOption(String optionText){
        List<WebElement> activeOptions = getActiveSubMenuOptions();
        for (WebElement activeOption : activeOptions) {
            if (activeOption.getText().trim().equals(optionText)) {
                return activeOption;
            }
        }
        return null;
    }
    public WebElement getSelectedSubMenuOption(){
        return driver.findElement(MENU_SUB_OPTION_SELECTED);
    }
    public WebElement getUnselectedSubMenuOptions(){
        return driver.findElement(MENU_SUB_OPTIONS_NOT_SELECTED);
    }

    //  - ACCESSORS FOR SPECIFIC INFORMATION
    public String getSelectedTabText(){
        return getSelectedTab().getText().trim();
    }
    public String getSelectedMenuOptionText(){
        return getSelectedMenuOption().getText().trim();
    }
    public String getSelectedSubMenuOptionText(){
        return getSelectedSubMenuOption().getText().trim();
    }

    public boolean isTabActive(String tabText){
        return getTab(tabText) != null;
    }
    public boolean isTabSelected(String tabText){
        return getSelectedTabText().equals(tabText);
    }

    public boolean isMenuOptionActive(String optionText){
        return getMenuOption(optionText) != null;
    }
    public boolean isMenuOptionSelected(String optionText){
        return getSelectedMenuOptionText().equals(optionText);
    }

    public boolean isSubMenuOptionActive(String optionText){
        return getSubMenuOption(optionText) != null;
    }
    public boolean isSubMenuOptionSelected(String optionText){
        return getSelectedSubMenuOptionText().equals(optionText);
    }

    // ACTIONS (UPDATING CURRENT PAGE OR NAVIGATING TO A NEW PAGE)
    public void clickTab(String tabText){
        WebElement tab = getTab(tabText);
        if (tab == null) {
            throw new IllegalStateException("No tab with text '" + tabText + "' could be found");
        }
        tab.click();
        waitMedium.until(ExpectedConditions.textToBe(SELECTED_TAB, tabText));
        waitForLoadingWrapper(SHORT_WAIT);
    }
    public void clickMenuOption(String optionText){
        WebElement option = getMenuOption(optionText);
        if (option == null) {
            throw new IllegalStateException("No Menu Option with text '" + optionText + "' could be found");
        }
        option.click();
        if(optionText.contains("Publish")){
            waitMedium.until(ExpectedConditions.textToBePresentInElementLocated(PAGE_TITLE, "Publish"));
        }else {
            waitMedium.until(ExpectedConditions.textToBe(MENU_OPTION_SELECTED, optionText));
        }
        waitForLoadingWrapper(MEDIUM_WAIT);
    }
    public void clickSubMenuOption(String optionText){
        WebElement option = getSubMenuOption(optionText);
        if (option == null) {
            throw new IllegalStateException("No Sub Menu Option with text '" + optionText + "' could be found");
        }
        option.click();
        waitMedium.until(ExpectedConditions.textToBe(MENU_SUB_OPTION_SELECTED, optionText));
        waitForLoadingWrapper(MEDIUM_WAIT);
    }
    public void clickPublishButton(){
        driver.findElement(PUBLISH_TAB_DATA).click();
        waitForLoadingWrapper(MEDIUM_WAIT);
    }

}

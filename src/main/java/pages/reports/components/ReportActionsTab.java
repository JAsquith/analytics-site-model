package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;

import java.util.List;

public class ReportActionsTab extends AnalyticsComponent {

    protected static final By CONTENTS_EXPAND_BUTTON = By.cssSelector(".showMore");
    protected static By RESET_ICON = By.cssSelector(".im12_Reset");
    protected static By RESET_YES = By.cssSelector(".resetPop a");
    protected static By RESET_NO = By.cssSelector(".resetPop em");


    protected String tabName;
    protected String tabClass;

    protected By tabButtonBy;
    protected By tabContentsBy;

    protected WebElement tabButton;
    protected WebElement tabContentsDiv;

    public ReportActionsTab(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public boolean isEnabled(){
        return getTabButton()!=null;
    }
    public boolean isDisabled(){
        return !isEnabled();
    }

    public boolean isActive(){
        return getTabContentsDiv()!=null;
    }
    public boolean isInactive() {
        return !isActive();
    }

    public boolean isExpanded(){
        if(isInactive()){
            throw new IllegalStateException("Tab '" + tabName + "' is not currently active");
        }
        return getTabContentsDiv().getAttribute("class").contains("open");
    }
    public boolean canExpand(){
        if(isInactive()){
            throw new IllegalStateException("Tab '" + tabName + "' can't be explanded as it is not currently selected");
        }
        List<WebElement> expandButtons = getTabContentsDiv().findElements(CONTENTS_EXPAND_BUTTON);
        return (expandButtons.size() > 0 && !isExpanded());
    }

    public ReportActionsTab selectTab(){
        if (!getTabButton().getAttribute("class").contains("active")){
            tabButton.click();
        }
        return this;
    }

    public ReportActionsTab expandTab(){
        if(isInactive()){
            throw new IllegalStateException("Tab '" + tabName + "' can't be explanded as it is not currently selected");
        }
        List<WebElement> expandButtons = getTabContentsDiv().findElements(CONTENTS_EXPAND_BUTTON);
        if (expandButtons.size()==0){
            return this;
        }
        WebElement expandButton = expandButtons.get(0);
        if (!expandButton.isDisplayed()){
            return this;
        }
        expandButton.click();
        waitShort.until(expansionComplete());
        return this;
    }
    public ReportActionsTab collapseTab(){
        if(isInactive()){
            throw new IllegalStateException("Tab '" + tabName + "' can't be collapsed as it is not currently active");
        }
        if (!isExpanded()){
            throw new IllegalStateException("Tab '" + tabName + "' can't be collapsed as it is not currently expanded");
        }

        WebElement expandButton = getTabContentsDiv().findElement(CONTENTS_EXPAND_BUTTON);
        if (!expandButton.isDisplayed()){
            return this;
        }
        expandButton.click();
        waitShort.until(expansionComplete());
        return this;
    }

    public void resetTab(boolean confirm){
        if(isDisabled()){
            throw new IllegalStateException("Tab '" + tabName + "' cannot be reset because it is disabled");
        }
        List<WebElement> resetButtons = tabButton.findElements(RESET_ICON);
        if(resetButtons.size()==0){
            throw new IllegalStateException("There are no applied options to be reset on Tab '"+tabName+"'");
        }
        resetButtons.get(0).click();
        WebElement yesButton = waitShort.until(resetPopupDisplayed());

        if (confirm) {
            yesButton.click();
            waitForLoadingWrapper();
        }
    }

    protected WebElement getTabButton(){
        boolean refresh = false;
        if (tabButton==null){
            refresh = true;
        } else {
            try {
                // Calling any method forces a staleness check
                tabButton.isEnabled();
            } catch (StaleElementReferenceException expected) {
                refresh = true;
            }
        }
        if (refresh) {
            List<WebElement> tabButtons = driver.findElements(tabButtonBy);
            tabButton = tabButtons.size() > 0 ? tabButtons.get(0) : null;
        }
        return tabButton;
    }

    protected WebElement getTabContentsDiv(){
        boolean refresh = false;
        if (tabContentsDiv==null){
            refresh = true;
        } else {
            try {
                // Calling any method forces a staleness check
                tabContentsDiv.isEnabled();
            } catch (StaleElementReferenceException expected) {
                refresh = true;
            }
        }
        if (refresh) {
            List<WebElement> tabContentDivs = driver.findElements(tabContentsBy);
            tabContentsDiv = tabContentDivs.size()>0 ? tabContentDivs.get(0) : null;
        }
        return tabContentsDiv;
    }

    private ExpectedCondition<Boolean> tabSwitchComplete() {
        List<WebElement> expandHideButtons = getTabContentsDiv().findElements(CONTENTS_EXPAND_BUTTON);
        if (expandHideButtons.size()==0){
            return ExpectedConditions.attributeContains(getTabButton(), "class", "active");
        } else {
            return ExpectedConditions.textToBePresentInElement(expandHideButtons.get(0), "Hide");
        }
    }

    private ExpectedCondition<Boolean> expansionComplete() {
        return ExpectedConditions.attributeContains(getTabContentsDiv(), "class", "open");
    }

    private ExpectedCondition<WebElement> resetPopupDisplayed() {
        return ExpectedConditions.elementToBeClickable(tabButton.findElement(RESET_YES));
    }


}

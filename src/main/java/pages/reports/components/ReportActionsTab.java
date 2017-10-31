package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
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
        return !isDisabled();
    }
    public boolean isDisabled(){
        if (getTabButton()==null) return true;
        return tabButton.getAttribute("class").contains("disabled");
    }

    public boolean isActive(){
        if (getTabContentsDiv()==null){
            return false;
        }
        return tabContentsDiv.isDisplayed();
    }
    public boolean isInactive() {
        return !isActive();
    }

    public boolean resizeable(){
        if(isInactive()){
            throw new IllegalStateException("Tab '" + tabName + "' is not currently active");
        }
        List<WebElement> expandButtons = getTabContentsDiv().findElements(CONTENTS_EXPAND_BUTTON);
        if(expandButtons.size()==0){
            return false;
        }
        return expandButtons.get(0).isDisplayed();
    }
    public boolean isExpanded(){
        if(isInactive()){
            throw new IllegalStateException("Tab '" + tabName + "' is not currently active");
        }
        waitShort.until(resizeComplete());
        return getTabContentsDiv().getAttribute("class").contains("open");
    }
    public boolean isCollapsed(){
        return resizeable() && !isExpanded();
    }

    public boolean canExpand(){
        return resizeable() && !isExpanded();
    }

    public ReportActionsTab selectTab(){
        if (!getTabButton().getAttribute("class").contains("active")){
            getTabButton().click();
            waitShort.until(tabSwitchComplete());
            waitForLoadingWrapper();
        }
        return this;
    }

    public ReportActionsTab expandTab(){
        waitShort.until(resizeComplete());
        if(canExpand()){
            List<WebElement> expandButtons = getTabContentsDiv().findElements(CONTENTS_EXPAND_BUTTON);
            expandButtons.get(0).click();
            waitShort.until(resizeComplete());
        }
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
        waitShort.until(resizeComplete());
        return this;
    }

    public void selectAndExpandTab(){
        selectTab();
        //expandTab();
    }

    public void resetTab(){
        resetTab(true);
    }

    public void resetTab(boolean confirm){
        if(isDisabled()){
            throw new IllegalStateException("Tab '" + tabName + "' cannot be reset because it is disabled");
        }
        List<WebElement> resetButtons = getTabButton().findElements(RESET_ICON);
        if(resetButtons.size()==0){
            throw new IllegalStateException("There are no applied options to be reset on Tab '"+tabName+"'");
        }
        resetButtons.get(0).click();
        waitForLoadingWrapper();
        WebElement yesButton = waitShort.until(resetPopupDisplayed());

        if (confirm) {
            yesButton.click();
            waitForLoadingWrapper();
        }
    }

    protected WebElement getTabButton(){
        try {
            tabButton.isEnabled();
        } catch (StaleElementReferenceException | NullPointerException e) {
            List<WebElement> tabButtons = driver.findElements(tabButtonBy);
            tabButton = tabButtons.size() > 0 ? tabButtons.get(0) : null;
        }
        return tabButton;
    }

    protected WebElement getTabContentsDiv(){
        try {
            // Calling any method forces a staleness check
            tabContentsDiv.isEnabled();
        } catch (StaleElementReferenceException | NullPointerException e) {
            List<WebElement> tabContentDivs = driver.findElements(tabContentsBy);
            tabContentsDiv = tabContentDivs.size()>0 ? tabContentDivs.get(0) : null;
        }
        return tabContentsDiv;
    }

    protected ExpectedCondition<Boolean> tabSwitchComplete() {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (!tabButton.getAttribute("class").contains("active")) return null;
                    return true;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {

                String s = tabButton.getAttribute("class");
                return String.format("the tab button's class attribute (%s) to contain 'active'",s);
            }
        };
    }

    private ExpectedCondition<Boolean> resizeComplete() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (getTabContentsDiv().findElements(CONTENTS_EXPAND_BUTTON).size()>0){
                        if (getTabContentsDiv().getAttribute("style").contains("overflow")) return null;
                    }
                    return true;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                String s = getTabContentsDiv().getAttribute("style");
                return String.format ("the Tab (div.pan) element's style attribute (%s) to not contain 'overflow'", s);
            }
        };
    }

    private ExpectedCondition<WebElement> resetPopupDisplayed() {
        return ExpectedConditions.elementToBeClickable(tabButton.findElement(RESET_YES));
    }


}

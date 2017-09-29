package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;
import pages.reports.EAPView;

import java.util.List;

public class ReportTabs_Other
        extends AnalyticsComponent
        {

    private String genericsFor;

    private By tabButton;
    private By activeTabBtn;
    private By disabledTabBtn;

    private By tabContent;
    private By tabExpandBtn;
    private By addFiltersBtn;

    private static By RESET_ICON = By.cssSelector(".im12_Reset");
    private static By RESET_YES = By.cssSelector(".resetPop a");
    private static By RESET_NO = By.cssSelector(".resetPop em");

    // CONSTRUCTOR
    public ReportTabs_Other(RemoteWebDriver aDriver){
        super(aDriver);
        genericsFor = "";
    }

    public boolean isDisabled(String tabType){
        buildGenericBys(tabType);

        List<WebElement> tabs = driver.findElements(disabledTabBtn);
        if(tabs.size()>0){
            for(WebElement tab : tabs){
                if (tab.getText().toLowerCase().contains(tabType)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEnabled(String tabType){
        buildGenericBys(tabType);

        List<WebElement> tabs = driver.findElements(tabButton);
        return tabs.size() > 0;
    }

    public boolean isActive(String tabType){
        buildGenericBys(tabType);
        List<WebElement> tabs = driver.findElements(activeTabBtn);
        if(tabs.size()>0){
            for(WebElement tab : tabs){
                if (tab.getAttribute("class").contains("active")){
                    return true;
                }
            }
        }
        return false;
    }

    public WebElement getTabButton(String tabType){
        buildGenericBys(tabType);
        List<WebElement> tabsFound = driver.findElements(tabButton);
        if(tabsFound.size()>0){
            return tabsFound.get(0);
        } else {
            return null;
        }
    }

    public WebElement getTab(String tabType){
        buildGenericBys(tabType);
        List<WebElement> tabsFound = driver.findElements(tabContent);
        if(tabsFound.size()>0){
            return tabsFound.get(0);
        } else {
            return null;
        }
    }

    public EAPView selectTab(String tabType){
        buildGenericBys(tabType);
        WebElement tab = driver.findElement(tabButton);
        if (!tab.getAttribute("class").contains("active")){
            tab.click();
        }
        return new EAPView(driver);
    }

    void expand(String tabType){
        buildGenericBys(tabType);
        List<WebElement> expandButtons = driver.findElements(tabExpandBtn);
        if (expandButtons.size()==0){
            return;
        }
        WebElement expandButton = expandButtons.get(0);
        WebElement tabContentDiv = driver.findElement(tabContent);
        String contentsClassAttr = tabContentDiv.getAttribute("class");
        if (!expandButton.isDisplayed() || contentsClassAttr.contains("open")){
            return;
        }
        expandButton.click();
        waitShort.until(isExpanded(tabContentDiv));
    }

    void collapse(String tabType){
        buildGenericBys(tabType);
        WebElement tabContentDiv = driver.findElement(tabContent);
        String contentsClassAttr = tabContentDiv.getAttribute("class");
        if (contentsClassAttr.contains("open")){
            driver.findElement(tabExpandBtn).click();
            waitShort.until(isExpanded(tabContentDiv));
        }
    }

    private ExpectedCondition<Boolean> isExpanded(WebElement tabContentDiv) {
        return ExpectedConditions.attributeContains(tabContentDiv, "class", "open");
    }

    private ExpectedCondition<Boolean> isCollapsed(WebElement tabContentDiv) {
        return ExpectedConditions.attributeContains(tabContentDiv, "style", "height: 88px");
    }

    public void expandStudentFiltersTab(){
        expand("filter");
    }
    public void expandMeasureFiltersTab() { expand("measure"); }
    public void expandResidualExclusionsTab() {expand("residual");}

    public ReportViewModal_Filters openStudentFiltersModal(){
        if (isEnabled("filter")) {
            selectTab("filter");
            driver.findElement(addFiltersBtn).click();
            return new ReportViewModal_Filters(driver);
        }
        throw new IllegalStateException("The Student Filters tab is not enabled");
    }

    public ReportViewModal_Measures openMeasureFiltersModal(){
        if(!isEnabled("measure")){
            throw new IllegalStateException("The Measure Filters tab is not enabled");
        }
        if (!isActive("measure")){
            selectTab("measure");
        }
        driver.findElement(addFiltersBtn).click();
        return new ReportViewModal_Measures(driver);
    }

    public ReportViewModal_Residuals openResidualExclusionsModal(){
        if (!isEnabled("residual")){
            selectTab("residual");
            driver.findElement(addFiltersBtn).click();
            return new ReportViewModal_Residuals(driver);
        }
        throw new IllegalStateException("The Residual Exclusions tab is not enabled");
    }

    public EAPView resetStudentFilters(){
        return resetTab("filter");
    }

    public EAPView resetMeasureFilters(){
        return resetTab("measure");
    }

    public EAPView resetResidualExceptions(){
        return resetTab("residual");
    }

    public EAPView resetOptions() {
        return resetTab("option");
    }

    public EAPView resetTab(String tabType) {
        return resetTab(tabType, true);
    }

    public EAPView resetTab(String tabType, boolean confirm){
        // Look for a reset button on the relevant tab
        WebElement tab = getTabButton(tabType);
        if (tab == null){
            throw new IllegalStateException("The '" + tabType + "' tab is not available");
        }
        List<WebElement> resetButtons = tab.findElements(RESET_ICON);
        if (resetButtons.size()==0){
            // No reset button, so nothing needs doing
            return new EAPView(driver);
        }

        // Click the reset button
        resetButtons.get(0).click();
        waitShort.until(ExpectedConditions.elementToBeClickable(tab.findElement(RESET_YES)));

        // Confirm by clicking the yes link
        tab.findElement(RESET_YES).click();

        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public boolean isEnabled(){
        return true;
    }

    private String normaliseTabType(String tabType){
        tabType = tabType.toLowerCase();
        if (tabType.endsWith("s"))
            tabType = tabType.substring(0,tabType.length()-1);
        if (tabType.equals("student"))
            tabType = "filter";
        return tabType;
    }

    private void buildGenericBys(String tabType){
        tabType = normaliseTabType(tabType);
        if (genericsFor.equals(tabType))
            return;

        String classTabType = tabType +"s";

        // Locators related to the tab button
        tabButton = By.cssSelector(".tabbutton[data-tab='"+ tabType +"']");
        activeTabBtn = By.cssSelector(".tabbutton.active[data-tab='"+ tabType +"']");
        disabledTabBtn = By.cssSelector(".tabbutton.disabled");

        // Locators related to the tab contents
        tabContent = By.cssSelector("."+classTabType+".pan");
        tabExpandBtn = By.cssSelector("."+classTabType+".pan .showMore");

        // Specifically for the Student Filters, Measure Filters and Residual Exclusions tabs
        addFiltersBtn = By.cssSelector("."+classTabType+">*>.button.noIcon");

        // Don't keep re-building the locators
        genericsFor = tabType;
    }

}

package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;

import java.util.List;

public class Report_Tabs extends AnalyticsComponent {

    private String genericsFor;
    private By genericTabBy;
    private By genericAddBy;
    private static By RESET_ICON = By.cssSelector(".im12_Reset");
    private static By RESET_YES = By.cssSelector(".resetPop a");
    private static By RESET_NO = By.cssSelector(".resetPop em");
    private By genericActiveTabBy;
    private By genericDisabledTabBy;

    // CONSTRUCTOR
    public Report_Tabs(RemoteWebDriver aDriver){
        super(aDriver);
        genericsFor = "";
    }

    public boolean isDisabled(String tabType){
        buildGenericBys(tabType);

        List<WebElement> tabs = driver.findElements(genericDisabledTabBy);
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

        List<WebElement> tabs = driver.findElements(genericTabBy);
        return tabs.size() > 0;
    }

    public boolean isActive(String tabType){
        buildGenericBys(tabType);
        List<WebElement> tabs = driver.findElements(genericActiveTabBy);
        if(tabs.size()>0){
            for(WebElement tab : tabs){
                if (tab.getAttribute("class").contains("active")){
                    return true;
                }
            }
        }
        return false;
    }

    public WebElement getTab(String tabType){
        buildGenericBys(tabType);
        List<WebElement> tabsFound = driver.findElements(genericTabBy);
        if(tabsFound.size()>0){
            return tabsFound.get(0);
        } else {
            return null;
        }
    }

    public EAPListView selectTab(String tabType){
        buildGenericBys(tabType);
        WebElement tab = driver.findElement(genericTabBy);
        if (!tab.getAttribute("class").contains("active")){
            tab.click();
        }
        return new EAPListView(driver);
    }

    public Report_AddStudentFilters openStudentFiltersModal(){
        if (isEnabled("filter")) {
            selectTab("filter");
            driver.findElement(genericAddBy).click();
            return new Report_AddStudentFilters(driver);
        }
        throw new IllegalStateException("The Student Filters tab is not enabled");
    }

    public Report_AddMeasureFilters openMeasureFiltersModal(){
        if (!isEnabled("measure")){
            selectTab("measure");
            driver.findElement(genericAddBy).click();
            return new Report_AddMeasureFilters(driver);
        }
        throw new IllegalStateException("The Measure Filters tab is not enabled");
    }

    public Report_AddResidualExclusions openResidualExclusionsModal(){
        if (!isEnabled("residual")){
            selectTab("residual");
            driver.findElement(genericAddBy).click();
            return new Report_AddResidualExclusions(driver);
        }
        throw new IllegalStateException("The Residual Exclusions tab is not enabled");
    }

    public EAPListView resetStudentFilters(){
        return resetTab("filter");
    }

    public EAPListView resetMeasureFilters(){
        return resetTab("measure");
    }

    public EAPListView resetResidualExceptions(){
        return resetTab("residual");
    }

    public EAPListView resetOptions() {
        return resetTab("option");
    }

    public EAPListView resetTab(String tabType) {
        return resetTab(tabType, true);
    }

    public EAPListView resetTab(String tabType, boolean confirm){
        // Look for a reset button on the relevant tab
        WebElement tab = getTab(tabType);
        if (tab == null){
            throw new IllegalStateException("The '" + tabType + "' tab is not available");
        }
        List<WebElement> resetButtons = tab.findElements(RESET_ICON);
        if (resetButtons.size()==0){
            // No reset button, so nothing needs doing
            return new EAPListView(driver);
        }

        // Click the reset button
        resetButtons.get(0).click();
        waitShort.until(ExpectedConditions.elementToBeClickable(tab.findElement(RESET_YES)));

        // Confirm by clicking the yes link
        tab.findElement(RESET_YES).click();

        waitForLoadingWrapper();
        return new EAPListView(driver);
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

        genericTabBy = By.cssSelector(".tabbutton[data-tab='"+ tabType +"']");
        genericAddBy = By.cssSelector("."+classTabType+">*>.button.noIcon");
        genericActiveTabBy = By.cssSelector(".tabbutton.active[data-tab='"+ tabType +"']");
        genericDisabledTabBy = By.cssSelector(".tabbutton.disabled");
        genericsFor = tabType;
    }

}

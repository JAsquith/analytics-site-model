package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;
import pages.reports.Report;

import java.util.List;

public class Report_FilterTabs extends AnalyticsComponent {

    public static final By TAB_FILTERS = By.cssSelector(".tabbutton[data-tab='filter']");
    public static final By ADD_FILTERS = By.cssSelector(".filters>*>.button.noIcon");
    public static final By CLEAR_FILTERS = By.cssSelector(".filters>*>.button.cancel");

    public static final By TAB_MEASURES = By.cssSelector(".tabbutton[data-tab='measure']");
    public static final By ADD_MEASURES = By.cssSelector(".measures>*>.button.noIcon");
    public static final By CLEAR_MEASURES = By.cssSelector(".measures>*>.button.cancel");

    public static final By TAB_RESIDUALS = By.cssSelector(".tabbutton[data-tab='residual']");
    public static final By ADD_RESIDUALS = By.cssSelector(".residuals>*>.button.noIcon");
    public static final By CLEAR_RESIDUALS = By.cssSelector(".residuals>*>.button.cancel");

    private String genericsFor;
    private By genericTabBy;
    private By genericAddBy;
    private By genericClearBy;
    private By genericActiveTabBy;
    private By genericDisabledTabBy;

    // CONSTRUCTOR
    public Report_FilterTabs(RemoteWebDriver aDriver){
        super(aDriver);
        genericsFor = "";
    }

    public boolean isDisabled(String tabType){
        tabType = normaliseTabType(tabType);
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
        tabType = normaliseTabType(tabType);
        buildGenericBys(tabType);

        List<WebElement> tabs = driver.findElements(genericTabBy);
        if(tabs.size()>0){
            return true;
        }
        return false;
    }

    public boolean isActive(String tabType){
        tabType = normaliseTabType(tabType);
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

    public Report selectTab(String tabType){
        buildGenericBys(tabType);
        return new Report(driver);
    }

    // Todo: replicate this method for AddMeasureFilters and AddResidualExclusions once those components are built
    public Report_AddStudentFilters openStudentFiltersModal(){
        if (!isActive("filter")){
            selectTab("filter");
        }
        driver.findElement(ADD_FILTERS).click();
        return new Report_AddStudentFilters(driver);
    }

    // Todo: replicate this method for AddMeasureFilters and AddResidualExclusions once those components are built
    public Report clearAllFilters(){
        List<WebElement> clearFiltersButtons = driver.findElements(CLEAR_FILTERS);
        if (clearFiltersButtons.size()==0){
            return new Report(driver);
        }
        clearFiltersButtons.get(0).click();
        waitForLoadingWrapper();
        return new Report(driver);
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

        if (genericsFor.equals(tabType))
            return;

        String classTabType = tabType +"s";

        genericTabBy = By.cssSelector(".tabbutton[data-tab='"+ tabType +"']");
        genericAddBy = By.cssSelector("."+classTabType+">*>.button.noIcon");
        genericClearBy = By.cssSelector("."+classTabType+">*>.button.cancel");
        genericActiveTabBy = By.cssSelector(".tabbutton.active[data-tab='"+ tabType +"']");
        genericDisabledTabBy = By.cssSelector(".tabbutton.disabled");
        genericsFor = tabType;
    }

}

package pages.reports;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsPage;
import pages.reports.components.ReportActionsTab_Dataset;
import pages.reports.components.ReportActionsTab_Options;
import pages.reports.components.ReportActions_DisplayOptions;
import pages.reports.components.ReportTabs_Other;

import java.util.List;

public class EAPView extends AnalyticsPage{

    private static final By KEY_CHARACTERISTICS_ICON = By.cssSelector(".icoEAP.act_key");


    private static final By AREAS = By.cssSelector(".area");
    private static final By AREA_ACTIVE = By.cssSelector(".area.active");
    private static final By AREA_SELECTED = By.cssSelector(".area.selected");

    private static final By REPORT_GROUPS = By.cssSelector(".rptGroup");
    private static final By REPORT_GRP_ACTIVE = By.cssSelector(".rptGroup.active");

    private static final By LEVELS_VISIBLE = By.cssSelector(".lvls[style*='display: block']");

    // Locators for dataset DDLs are public in the ReportActionsTab_Dataset class
    public ReportActionsTab_Dataset dsOptions;

    // Locators for Grade filters (On Track, Faculty, Class, Grade Type, etc)
    // are public in the ReportActionsTab_Options class
    public ReportActionsTab_Options gradeFilters;

    // Locators for Filters/Measures/Residual Exclusions tabs (and the buttons within them)
    // are public in the ReportTabs_Other class
    public ReportTabs_Other reportTabs;

    // Locators for View Options (Column Sort, Count/Percent, Standard/Cumulative, Breakdown, StuInfo, A8 Basket, Sub/Whole)
    public ReportActions_DisplayOptions viewOptions;

    // CONSTRUCTORS
    public EAPView(RemoteWebDriver aDriver){
        super(aDriver);
        dsOptions = new ReportActionsTab_Dataset(driver);
        gradeFilters = new ReportActionsTab_Options(driver);
        reportTabs = new ReportTabs_Other(driver);
        viewOptions = new ReportActions_DisplayOptions(driver);
        try {
            waitMedium.until(ExpectedConditions.elementToBeClickable(KEY_CHARACTERISTICS_ICON));
        } catch (TimeoutException e){
            throw new IllegalStateException("Timeout waiting for Key Characteristics icon to be clickable on EAPView");
        }
    }

// METHODS
    //  - CHANGING THE STATE OF THE PAGE
    public WebElement selectArea(String areaName){
        try {
            WebElement area = driver.findElement(getAreaDivSelector(areaName));
            if (!area.getAttribute("class").contains("selected")){
                area.click();
                waitShort.until(reportGroupsDisplayedFor(area));// TimeoutException
            }
            return area;
        } catch (NoSuchElementException e){
            throw new WebDriverException("NSEE trying to select Area '"+areaName+"'; "+e.getMessage());
        } catch (TimeoutException e){
            throw new WebDriverException("Timeout Exception waiting for ReportGroups");
        }
    }

    public EAPView selectReport(String reportName){
        WebElement selectedArea;
        try{
            selectedArea = driver.findElement(AREA_SELECTED);
        } catch (NoSuchElementException e){
            throw new IllegalStateException("Can't select a Report because no Area is selected");
        }
        return selectReport(selectedArea, reportName);
    }

    private EAPView selectReport(WebElement selectedArea, String reportName){
        try{
            String currentReport = driver.findElement(REPORT_GRP_ACTIVE).getText();
            if (reportName.equals(currentReport)){
                return this;
            }
            WebElement reportLink = selectedArea.findElement(By.linkText(reportName));
            reportLink.click();
            waitForLoadingWrapper();
            return this;
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("Report '"+reportName+"' could not be found in the currently selected area");
        }
    }

    public EAPView selectLevel(String levelName){
        WebElement activeAreaDiv; WebElement selectedAreaDiv;
        try {
            activeAreaDiv = driver.findElement(AREA_ACTIVE);
            selectedAreaDiv = driver.findElement(AREA_SELECTED);
            String activeAreaName = activeAreaDiv.getAttribute("data-name");
            String selectedAreaName = selectedAreaDiv.getAttribute("data-name");
            if (!activeAreaName.equals(selectedAreaName)){
                throw new IllegalStateException(
                        "Can't select a Level - the active Area '" + activeAreaName +
                                "' is not the selected Area '" + selectedAreaName +"'");
            }
        } catch (NoSuchElementException e){
            throw new IllegalStateException("Can't select a Level - either no Area is active or no Area is selected");
        }
        try{
            WebElement levelLinks = driver.findElement(LEVELS_VISIBLE);
            WebElement levelLink = levelLinks.findElement(By.partialLinkText(levelName));
            levelLink.click();
            waitForLoadingWrapper();
            return this;
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("Can't select Level '" + levelName +
                    "' - no link with that text could be found");
        }
    }

    public EAPView openView(String areaName, String reportName, String levelName){

        // Show the table of links to views within the given areaName
        WebElement area = selectArea(areaName);
        selectReport(area, reportName).
                selectLevel(levelName);

        waitForLoadingWrapper();
        return new EAPView(driver);
    }

    // Methods used in subclasses
    protected int findNamedTableIndex(String tableName){

        List<WebElement> tableTitleElements = driver.findElements(By.className("tableTitle"));
        if (tableTitleElements.size()==0){
            // There are no report tables visible on the page
            return -2;
        }

        if (tableName.equals("")){
            // No report table was specified, assume we want the first one
            return 1;
        }

        int tableIndex = -1;
        for (WebElement titleElement: tableTitleElements){
            String title = titleElement.getText();
            List<WebElement> subTitles = titleElement.findElements(By.className("smallInfo"));
            if (subTitles.size()>0){
                if (title.trim().equals(tableName)) {
                    tableIndex = tableTitleElements.indexOf(titleElement) + 1;
                    break;
                }
                title = title.replace(titleElement.findElement(By.className("smallInfo")).getText(),"");
            }
            if (title.trim().equals(tableName)) {
                tableIndex = tableTitleElements.indexOf(titleElement) + 1;
                break;
            }
        }

        return tableIndex;
    }

    // PRIVATE HELPER METHODS FOR THE PUBLIC METHODS
    private By getAreaDivSelector(String areaDataName){
        return By.cssSelector(".area[data-name='" + areaDataName + "']");
    }

    private ExpectedCondition<Boolean> reportGroupsDisplayedFor(WebElement area) {
        WebElement rptGroup = area.findElement(REPORT_GROUPS);
        return ExpectedConditions.attributeContains(rptGroup, "style", "display: block");
    }

}

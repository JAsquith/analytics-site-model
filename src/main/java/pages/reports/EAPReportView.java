package pages.reports;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsPage;
import pages.reports.components.Report_DatasetOptions;
import pages.reports.components.Report_GradeFilters;
import pages.reports.components.Report_Tabs;
import pages.reports.components.Report_ViewOptions;

import java.util.List;

public class EAPReportView extends AnalyticsPage{

    private static final By KEY_CHARACTERISTICS_ICON = By.cssSelector(".icoEAP.act_key");


    public static final By AREAS = By.className("area");

    public static final By AREA_BUTTONS = By.cssSelector(".area .rept");
    public static final By REPORT_NAV_AREA_LINKS = By.cssSelector(".list-grid td>a");
    public static final By REPORT_NAV_VIEW_LABELS = By.cssSelector("td.title-y:nth-of-type(2)");
    public static final By REPORT_NAV_LEVEL_LABELS = By.cssSelector("td.title-x");
    public static final By LIST_GRID_ROWS = By.cssSelector("tr.btn");
    public static final By LIST_GRID_COLS = By.cssSelector("tr:nth-of-type(3)>td");
    private static final By REPORT_GROUPS = By.cssSelector(".rptGroup");

    // Locators for dataset DDLs are public in the Report_DatasetOptions class
    public Report_DatasetOptions dsOptions;

    // Locators for Filters/Measures/Residual Exclusions tabs (and the buttons within them)
    // are public in the Report_Tabs class
    public Report_Tabs reportTabs;

    // Locators for Grade filters (On Track, Faculty, Class, Grade Type, etc)
    // are public in the Report_GradeFilters class
    public Report_GradeFilters gradeFilters;

    // Locators for View Options (Column Sort, Count/Percent, Standard/Cumulative, Breakdown, StuInfo, A8 Basket, Sub/Whole)
    public Report_ViewOptions viewOptions;

    // CONSTRUCTORS
    public EAPReportView(RemoteWebDriver aDriver){
        super(aDriver);
        try {
            waitMedium.until(ExpectedConditions.elementToBeClickable(KEY_CHARACTERISTICS_ICON));
        } catch (TimeoutException e){
            throw new IllegalStateException("Timeout waiting for Key Characteristics icon to be clickable on EAPReportView");
        }
    }

// METHODS
    //  - CHANGING THE STATE OF THE PAGE
    public EAPReportView selectArea(String areaName){
        try {
            WebElement area = driver.findElement(By.cssSelector(".area[data-name='" + areaName + "']")); //NoSuchElementException
            if (!area.getAttribute("class").contains("selected")){
                area.click();
                waitShort.until(clickableReportGroupsFor(area));// TimeoutException
            }
        } catch (NoSuchElementException e){
            throw new WebDriverException("NSEE trying to select Area '"+areaName+"'; "+e.getMessage());
        } catch (TimeoutException e){
            throw new WebDriverException("Exception");
        }
        return new EAPReportView(driver);
    }

    private ExpectedCondition<WebElement> clickableReportGroupsFor(WebElement area) {
        return ExpectedConditions.elementToBeClickable(area.findElement(REPORT_GROUPS));
    }


    public EAPReportView openView(String areaName, String reportName, String levelName){

        // Show the table of links to views within the given areaName
        WebElement area = expandAreaGrid(areaName);

        // Find the link to the given level of the given report
        WebElement button = getViewLinkFromListGrid(area, reportName, levelName);
        if (button.getText().trim().equals("Active")){
            // The requested view is already active, close the area grid and exit
            area.click();
            return this;
        }
        // The requested view is not currently active, click the button and wait for the reload
        button.click();
        waitForLoadingWrapper();
        return this;
    }

    public EAPReportView selectTab(String tabType){
        return reportTabs.selectTab(tabType);
    }


    // PRIVATE HELPER METHODS FOR THE PUBLIC METHODS
    protected WebElement getViewLinkFromListGrid(WebElement area, String reportName, String levelName){

        List<WebElement> rowLabels = area.findElements(REPORT_NAV_VIEW_LABELS);
        //int rowIndex = area.findElements(LIST_GRID_ROWS).size() - rowLabels.size();
        int rowIndex = 2;

        for (WebElement label: rowLabels){
            if (label.getText().trim().equals(reportName)){
                rowIndex += rowLabels.indexOf(label) + 1;
                break;
            }
        }

        List<WebElement> colLabels = area.findElements(REPORT_NAV_LEVEL_LABELS);
        int colIndex = area.findElements(LIST_GRID_COLS).size() - colLabels.size();

        for (WebElement label: colLabels){
            if (label.getText().trim().equals(levelName)){
                colIndex += colLabels.indexOf(label) + 1;
                break;
            }
        }
        String cssString = "tr:nth-of-type(" + rowIndex + ")>td:nth-of-type(" + colIndex + ")>a";
        WebElement link;
        try {
            link = area.findElement(By.cssSelector(cssString));
        } catch (NoSuchElementException nsee) {
            // Temporary logging for updated grid layout
            String activeArea = area.findElement(By.tagName("span")).getText();
            System.out.println("Active Area: " + activeArea);
            System.out.println("CSS Selector: " + cssString);
            throw new IllegalArgumentException("A link to report '" + reportName +
                    "' at level '" + levelName + "' could not be found.");
        }
        return link;
    }

    protected WebElement expandAreaGrid(String areaName){
        // Given an area name (like "Grades", "Matrix", "Student Detail", etc) click the button which
        // expands the 'list-grid' of views in that area
        List<WebElement> areas = driver.findElements(AREAS);
        List<WebElement> areaButtons = driver.findElements(AREA_BUTTONS);
        for (WebElement button: areaButtons){
            if (button.getText().trim().toUpperCase().equals(areaName.toUpperCase())){
                WebElement area = areas.get(areaButtons.indexOf(button));
                button.click();
                waitForListGridDisplay(area);
                return area;
            }
        }
        throw new IllegalArgumentException("A Report Area named '" + areaName + "' was not found");
    }

    protected List<WebElement> waitForListGridDisplay(WebElement area){
        return waitMedium.until(ExpectedConditions.visibilityOfAllElements(area.findElements(REPORT_NAV_AREA_LINKS)));
    }

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


}

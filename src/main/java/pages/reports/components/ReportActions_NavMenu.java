package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActions_NavMenu extends AnalyticsComponent implements IReportActionGroup {

    /*
     Each Area wraps one Area_Button (labelled "Headlines", "Grades", etc) and one Report_Group
     */
    private static final By AREAS = By.cssSelector(".area");
    private static final By AREAS_INACTIVE = By.cssSelector(".area:not(.active)");
    private static final By AREA_ACTIVE = By.cssSelector(".area.active");
    private static final By AREA_SELECTED = By.cssSelector(".area.selected");

    private static final By AREA_BUTTON = By.cssSelector(".areaName");

    /*
     Each Report_Group wraps:
          - one or more REPORT_BUTTONS (labelled "Summary", "Overview", etc)
          - the _same number_ of GROUPING_SETS
     */
    private static final By REPORT_GROUPS = By.cssSelector(".rptGroup");
    private static final By REPORT_GROUP_ACTIVE = By.cssSelector(".rptGroup.active");

    /*
     Report_Buttons:
     */
    private static final By REPORT_BUTTONS_FOR_ACTIVE_REPORT = By.cssSelector(".rptGroup.active.selected .rptBtn");
    private static final By REPORT_BUTTON_SELECTED = By.cssSelector(".rptBtn.selected");
    private static final By REPORT_LINKS_FOR_AREA = By.cssSelector(".rptBtn>span");

    /*
     Each Grouping_Set wraps:
          - one or more Grouping_Buttons
     */
    private static final By GROUPING_SETS = By.cssSelector(".lvls");
    private static final By GROUPING_SET_FOR_ACTIVE_REPORT = By.cssSelector(".rptGroup.active.selected .lvls");
    private static final By GROUPING_SET_VISIBLE = By.cssSelector(".lvls[style*='display: block']");

    private static final By GROUPING_SELECTED = By.cssSelector(".lvls[style*='display: block'] span");
    private static final By GROUPINGS_AVAILABLE = By.cssSelector(".lvls[style*='display: block'] a,.lvls[style*='display: block'] span");


    public ReportActions_NavMenu(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public WebElement selectArea(String areaName){
        try {
            WebElement area = driver.findElement(getAreaDivSelector(areaName));
            return selectArea(area);
        } catch (NoSuchElementException e){
            throw new WebDriverException("NSEE trying to select Area '"+areaName+"'; "+e.getMessage());
        } catch (TimeoutException e){
            throw new WebDriverException("Timeout Exception waiting for ReportGroups");
        }
    }

    public EAPView selectReport(String reportName){
        WebElement selectedArea = getSelectedArea();
        if(selectedArea == null) {
            throw new IllegalStateException("Can't select a Report because no Area is selected");
        }
        return selectReport(selectedArea, reportName);
    }

    public EAPView selectReport(WebElement selectedArea, String reportName){
        try{
            WebElement reportLink = getNamedReportButton(selectedArea, reportName);
            reportLink.click();
            return new EAPView(driver);
        } catch (ElementNotVisibleException e){
            throw new IllegalStateException("The '" + reportName + "' button is not currently visible!");
        }
    }

    public EAPView selectGrouping(String targetGrouping){
        WebElement selectedAreaDiv = getSelectedArea();

        WebElement currentGroupingSet;
        try {
            currentGroupingSet = selectedAreaDiv.findElement(GROUPING_SET_VISIBLE);
        } catch (NoSuchElementException e){
            throw new IllegalStateException("There are no Grouping/Level buttons visible");
        }

        WebElement currentGrouping;
        try{
            currentGrouping = selectedAreaDiv.findElement(GROUPING_SELECTED);
            if (currentGrouping.getText().trim().equals(targetGrouping)) return new EAPView(driver);
        } catch (NoSuchElementException e) {
            // This caters for the case that the active and selected area/report are different
            // Bad coding style, but f*** it!
        }
        try{
            WebElement groupingLink = currentGroupingSet.findElement(By.partialLinkText(targetGrouping));
            groupingLink.click();
            waitForLoadingWrapper();
            return new EAPView(driver);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("Can't select Level '" + targetGrouping +
                    "' - no link with that text could be found");
        }
    }

    /*  */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();

        actions.add(ReportAction.NEW_AREA_REPORT_AND_GROUPING);

/*      Oct 17, 2017 - TWO ACTIONS DISABLED:
            * The Report Buttons have changed from links (<a> elements) to spans
            * This means there is no page reload until a grouping/level button is clicked
            * So NEW_AREA_AND_REPORT and NEW_REPORT are not 'complete' actions
        if (driver.findElements(AREAS_INACTIVE).size()>0)
            actions.add(ReportAction.NEW_AREA_AND_REPORT);

        actions.add(ReportAction.NEW_REPORT);
*/
        actions.add(ReportAction.NEW_GROUPING);

        return actions;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        switch(action){
            case NEW_AREA_REPORT_AND_GROUPING:
                return getAreaChangeOptions();
/*      Oct 17, 2017 - TWO ACTIONS DISABLED:
            case NEW_AREA_AND_REPORT:
                return getAreaChangeOptions();
            case NEW_REPORT:
                return getReportsForArea(driver.findElement(AREA_ACTIVE));
*/
            case NEW_GROUPING:
                return getAvailableReportLevels(driver.findElement(AREA_ACTIVE));
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Navigation Menu");
        }
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        switch(action){
            case NEW_AREA_REPORT_AND_GROUPING:
                String areaName = option.split("\\[")[0];
                String reportName = option.split("\\[")[1];
                reportName = reportName.substring(0, reportName.length()-1);
                WebElement area = selectArea(areaName);
                return selectReport(area, reportName);
/*      Oct 17, 2017 - TWO ACTIONS DISABLED:
            case NEW_AREA_AND_REPORT:
                String areaName = option.split("\\[")[0];
                String reportName = option.split("\\[")[1];
                reportName = reportName.substring(0, reportName.length()-1);
                WebElement area = selectArea(areaName);
                return selectReport(area, reportName);
            case NEW_REPORT:
                return selectReport(option);
*/
            case NEW_GROUPING:
                return selectGrouping(option);
        }
        return null;
    }

    @Override
    public String getName() {
        return "navMenu";
    }

    /*  */
    private By getAreaDivSelector(String areaDataName){
        return By.cssSelector(".area[data-name='" + areaDataName + "']");
    }

    private WebElement selectArea(WebElement targetArea){
        try {
            if (!areasAreEqual(getSelectedArea(), targetArea)){
                targetArea.click();
                waitTiny.until(reportGroupDisplayed(targetArea));// TimeoutException
            }
            return targetArea;
        } catch (NoSuchElementException e){
            throw new WebDriverException("NSEE trying to select Area '"+targetArea.getText()+"'; "+e.getMessage());
        } catch (TimeoutException e){
            throw new WebDriverException("Timeout Exception waiting for ReportGroups");
        }
    }

    private WebElement getSelectedArea(){
        WebElement area;
        try {
            area = driver.findElement(AREA_SELECTED);
        } catch (NoSuchElementException e){
            return null;
        }
        return area;
    }

    private List<String> getAreaChangeOptions(){
        List<String> areaNames = new ArrayList<String>();
        for(WebElement area : driver.findElements(AREAS_INACTIVE)){
            for(String reportName : getReportsForArea(area)){
                areaNames.add(area.findElement(AREA_BUTTON).getText().trim()+"["+reportName+"]");
            }
        }
        return areaNames;
    }

    private List<String> getReportsForArea(WebElement area){
        List<String> reportNames = new ArrayList<String>();
        selectArea(area);
        for(WebElement reportLink : area.findElements(REPORT_LINKS_FOR_AREA)){
            reportNames.add(reportLink.getText().trim());
        }
        return reportNames;
    }

    private List<String> getAvailableReportLevels(WebElement area){
        WebElement selectedReport = driver.findElement(REPORT_BUTTON_SELECTED);
        if (!selectedReport.isDisplayed()) {
            selectArea(area);
        }
        try {
            waitTiny.until(ExpectedConditions.visibilityOfElementLocated(GROUPINGS_AVAILABLE));
        } catch (TimeoutException e){
            throw e;
        }

        List<String> levelNames = new ArrayList<String>();
        for(WebElement levelButton : driver.findElements(GROUPINGS_AVAILABLE)){
            levelNames.add(levelButton.getText().trim());
        }
        return levelNames;
    }

    private WebElement getNamedReportButton(WebElement area, String reportName){
        for (WebElement button : area.findElements(REPORT_LINKS_FOR_AREA)){
            if (button.getText().trim().equals(reportName)){
                return button;
            }
        }
        throw new IllegalArgumentException("No button could be found for '"+reportName+"'");
    }

    private boolean areasAreEqual(WebElement area_1, WebElement area_2){

        if (area_1 == null && area_2 != null) return false;
        if (area_2 == null && area_1 != null) return false;
        String areaName_1 = area_1.getAttribute("data-name");
        String areaName_2 = area_2.getAttribute("data-name");

        return areaName_1.equals(areaName_2);
    }

    /* */
    protected ExpectedCondition<Boolean> reportGroupDisplayed(WebElement area) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement reportGroup = area.findElement(REPORT_GROUPS);
                    String style = reportGroup.getAttribute("style");
                    if (style.contains("scroll")) return null;
                    if (style.contains("overflow")) return null;
                    //if (style.equals("")) return true;
                    if (style.equals("display: block;")) return true;
                    return true;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                String areaName = area.findElement(By.cssSelector(".areaName")).getText().trim();
                return "The Report Group for " + areaName + " has stopped scrolling";
            }
        };
    }

}

package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import pages.AnalyticsComponent;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActions_NavMenu extends AnalyticsComponent implements IReportActionGroup {

    /*
     Each Area wraps an Area_Name and a Report_Group
     */
    private static final By AREAS = By.cssSelector(".area");
    private static final By AREAS_INACTIVE = By.cssSelector(".area:not(.active)");
    private static final By AREA_ACTIVE = By.cssSelector(".area.active");
    private static final By AREA_SELECTED = By.cssSelector(".area.selected");

    private static final By AREA_NAME = By.cssSelector(".areaName");

    /*
     Each Report_Group wraps:
          - one or more REPORT_BUTTONS
          - the _same number_ of GROUPING_SETS
     */
    private static final By REPORT_GROUPS = By.cssSelector(".rptGroup");
    private static final By REPORT_GROUP_ACTIVE = By.cssSelector(".rptGroup.active");

    private static final By REPORT_BUTTONS_FOR_ACTIVE_REPORT = By.cssSelector(".rptGroup.active.selected .rptBtn");
    private static final By REPORT_BUTTON_SELECTED = By.cssSelector(".rptBtn.selected");
    private static final By REPORT_LINKS_FOR_AREA = By.cssSelector(".rptBtn>a");

    /*
     Each Grouping_Set wraps:
          - one or more Grouping_Buttons
     */
    private static final By GROUPING_SETS = By.cssSelector(".lvls");
    private static final By GROUPING_SET_FOR_ACTIVE_REPORT = By.cssSelector(".rptGroup.active.selected .lvls");
    private static final By GROUPING_SET_VISIBLE = By.cssSelector(".lvls[style*='display: block']");

    private static final By GROUPING_SELECTED = By.cssSelector(".lvls[style*='display: block'] span");
    private static final By GROUPINGS_AVAILABLE = By.cssSelector(".lvls[style*='display: block'] a");


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
        WebElement selectedArea;
        try{
            selectedArea = driver.findElement(AREA_SELECTED);
        } catch (NoSuchElementException e){
            throw new IllegalStateException("Can't select a Report because no Area is selected");
        }
        return selectReport(selectedArea, reportName);
    }

    public EAPView selectReport(WebElement selectedArea, String reportName){
        try{
/*
            for(WebElement reportLink : selectedArea.findElements(REPORT_LINKS_FOR_AREA)){
                if (reportLink.getText().trim().equals(reportName)){
                    reportLink.click();
                    return new EAPView(driver);
                }
            }
            String currentReport = selectedArea.findElement(REPORT_BUTTON_SELECTED).getText().trim();
            if (reportName.equals(currentReport)){
                return new EAPView(driver);
            }
*/
            WebElement reportLink = selectedArea.findElement(By.linkText(reportName));
            reportLink.click();
            return new EAPView(driver);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("Report '"+reportName+"' could not be found in the currently selected area");
        }
    }

    public EAPView selectGrouping(String targetGrouping){
        WebElement selectedAreaDiv;
        try {
            selectedAreaDiv = driver.findElement(AREA_SELECTED);
            if (!areasAreEqual(driver.findElement(AREA_ACTIVE), selectedAreaDiv)){
                throw new IllegalStateException(
                        "Can't select a Level - the active and selected Areas are not the same");
            }
        } catch (NoSuchElementException e){
            throw new IllegalStateException("Can't select a Level - either no Area is active or no Area is selected");
        }

        WebElement currentGrouping = selectedAreaDiv.findElement(GROUPING_SELECTED);
        if (currentGrouping.getText().trim().equals(targetGrouping)) return new EAPView(driver);

        WebElement currentGroupingSet = selectedAreaDiv.findElement(GROUPING_SET_VISIBLE);

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

        if (driver.findElements(AREAS_INACTIVE).size()>0)
            actions.add(ReportAction.NEW_AREA_AND_REPORT);

        actions.add(ReportAction.NEW_REPORT);

        if (driver.findElements(GROUPINGS_AVAILABLE).size()>0)
            actions.add(ReportAction.NEW_GROUPING);

        return actions;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        switch(action){
            case NEW_AREA_AND_REPORT:
                return getAreaChangeOptions();
            case NEW_REPORT:
                return getReportsForArea(driver.findElement(AREA_ACTIVE));
            case NEW_GROUPING:
                return getCurrentReportLevels(driver.findElement(AREA_ACTIVE));
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Navigation Menu");
        }
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        switch(action){
            case NEW_AREA_AND_REPORT:
                String areaName = option.split("\\[")[0];
                String reportName = option.split("\\[")[1];
                reportName = reportName.substring(0, reportName.length()-1);
                WebElement area = selectArea(areaName);
                return selectReport(area, reportName);
            case NEW_REPORT:
                return selectReport(option);
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
            if (!areasAreEqual(driver.findElement(AREA_SELECTED), targetArea)){
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

    private List<String> getAreaChangeOptions(){
        List<String> areaNames = new ArrayList<String>();
        for(WebElement area : driver.findElements(AREAS_INACTIVE)){
            for(String reportName : getReportsForArea(area)){
                areaNames.add(area.findElement(AREA_NAME).getText().trim()+"["+reportName+"]");
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

    private List<String> getCurrentReportLevels(WebElement area){
        area.click();
        waitTiny.until(reportGroupDisplayed(area));

        List<String> levelNames = new ArrayList<String>();
        for(WebElement levelButton : area.findElements(GROUPINGS_AVAILABLE)){
            levelNames.add(levelButton.getText().trim());
        }
        return levelNames;
    }

    private boolean areasAreEqual(WebElement area_1, WebElement area_2){
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
                    if (style.equals("")) return true;
                    if (style.equals("display: block;")) return true;
                    if (!style.contains("scroll")) return null;
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

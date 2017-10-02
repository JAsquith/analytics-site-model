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

    private static final By AREAS = By.cssSelector(".area");
    private static final By AREAS_INACTIVE = By.cssSelector(".area:not(.active)");
    private static final By AREA_ACTIVE = By.cssSelector(".area.active");
    private static final By AREA_SELECTED = By.cssSelector(".area.selected");
    private static final By AREA_NAME = By.cssSelector(".areaName");

    private static final By REPORT_GROUPS = By.cssSelector(".rptGroup");
    private static final By REPORT_GRP_ACTIVE = By.cssSelector(".rptGroup.active");

    private static final By LEVELS_VISIBLE = By.cssSelector(".lvls[style*='display: block']");
    private static final By LEVELS_VISIBLE_LINKS = By.cssSelector("a.lvls[style*='display: block']");

    private static final By REPORT_GROUP_FOR_AREA = By.cssSelector(".rptGroup");
    private static final By REPORT_LINKS_FOR_AREA = By.cssSelector(".rptBtn>a");

    public ReportActions_NavMenu(RemoteWebDriver aDriver){
        super(aDriver);
    }

    private By getAreaDivSelector(String areaDataName){
        return By.cssSelector(".area[data-name='" + areaDataName + "']");
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
            String currentReport = driver.findElement(REPORT_GRP_ACTIVE).getText().trim();
            if (reportName.equals(currentReport)){
                return new EAPView(driver);
            }
            WebElement reportLink = selectedArea.findElement(By.linkText(reportName));
            reportLink.click();
            return new EAPView(driver);
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
            return new EAPView(driver);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("Can't select Level '" + levelName +
                    "' - no link with that text could be found");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();
        actions.add(ReportAction.NEW_AREA_AND_REPORT);
        actions.add(ReportAction.NEW_REPORT);
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
                selectArea(areaName);
                return selectReport(reportName);
            case NEW_REPORT:
                return selectReport(option);
            case NEW_GROUPING:
                return selectLevel(option);
        }
        return null;
    }

    private WebElement selectArea(WebElement area){
        try {
            if (!area.getAttribute("class").contains("selected")){
                area.click();
                waitTiny.until(reportGroupDisplayed(area));// TimeoutException
            }
            return area;
        } catch (NoSuchElementException e){
            throw new WebDriverException("NSEE trying to select Area '"+area.getText()+"'; "+e.getMessage());
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
        for(WebElement levelButton : area.findElements(LEVELS_VISIBLE_LINKS)){
            levelNames.add(levelButton.getText().trim());
        }
        return levelNames;
    }

    protected ExpectedCondition<Boolean> reportGroupDisplayed(WebElement area) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement reportGroup = area.findElement(REPORT_GROUP_FOR_AREA);
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

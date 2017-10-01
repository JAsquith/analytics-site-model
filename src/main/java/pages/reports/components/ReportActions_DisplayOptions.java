package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActions_DisplayOptions extends AnalyticsComponent implements IReportActionGroup{

    public final By COL_SORT_DDL = By.cssSelector("select#ReportOptions_RPTColSort_ColNameType");
    private final By COL_SORT_DDL_OPTIONS = By.tagName("select#ReportOptions_RPTColSort_ColNameType>option");
    public final By COL_SORT_DIRECTION_TOGGLE = By.cssSelector("label.icon.sortDir");
    public final By BREAKDOWN_DDL = By.cssSelector("select#ReportOptions_Filter_ID");
    public final By FIG_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(2)");
    public final By CALC_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(3)");
    public final By STUDENT_INFO_DDL = By.cssSelector("#ReportOptions_RPTFilterTagDisplay");
    public final By SUB_WHOLE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(4)");
    public final By SUB_WHOLE_TOGGLE_SUB = By.cssSelector(".chgOptFmSub.icon.sub");
    public final By SUB_WHOLE_TOGGLE_WHOLE = By.cssSelector(".chgOptFmSub.icon.whole");

    private final By ENABLED_FIELDS = By.cssSelector(".sort-toggle:not(.disabled) input,.sort-toggle:not(.disabled) select");

    public ReportActions_DisplayOptions(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public EAPView selectColSort(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COL_SORT_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Column Sort is not available");
        }
        if (!optionText.equals("")) {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
            waitForLoadingWrapper();
            //waitForColResort();
        }
        return new EAPView(driver);
    }

    public EAPView setColSortDirection(String sortOrder){
        WebElement sortIcon = driver.findElement(COL_SORT_DIRECTION_TOGGLE);
        String bgPosition = sortIcon.getCssValue("background-position");
        boolean sortedAscending = bgPosition.endsWith("-60px");
        boolean sortAsc = sortOrder.toLowerCase().equals("ascending");
        if (sortedAscending != sortAsc){
            sortIcon.click();
            waitForLoadingWrapper();
            //waitForColResort();
        }
        return new EAPView(driver);
    }

    public EAPView toggleColSortDirection(){
        driver.findElement(COL_SORT_DIRECTION_TOGGLE).click();
        waitForLoadingWrapper();
        return new EAPView(driver);
    }

    public EAPView selectBreakdown(String optionText){
        List<WebElement> targetDDLs = driver.findElements(BREAKDOWN_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Breakdown is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPView(driver);
    }

    public EAPView setFigType(String figType){
        // checks current availability and setting, clicks label if req.
        if (figType.equals("")){
            return new EAPListView(driver);
        }
        WebElement figToggle = driver.findElement(FIG_TYPE_TOGGLE);
        if (figToggle.getAttribute("class").contains("disabled")){
            throw new IllegalStateException("The Figure Type options are disabled");
        }
        String oldType = figToggle.findElement(By.tagName("div")).getText().trim();
        String newType;
        switch (figType){
            case "%":case "Percentage":
                newType = "Percentage";
                break;
            case "#":case "Count":
                newType = "Count";
                break;
            default:
                throw new IllegalArgumentException("Unexpected Figure Type '" + figType +
                        "'. Expected '#', '%', 'Count' or 'Percentage'");
        }
        if (oldType.startsWith(newType)){
            return new EAPListView(driver);
        }
        figToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return new EAPView(driver);
    }

    public EAPView setCalcType(String calcType){
        // checks current availability and setting, clicks label if req.
        if (calcType.equals("")){
            return new EAPListView(driver);
        }
        WebElement calcToggle = driver.findElement(CALC_TYPE_TOGGLE);
        if (calcToggle.getAttribute("class").contains("disabled")){
            throw new IllegalStateException("The Calculation Type options are disabled");
        }
        String oldType = calcToggle.findElement(By.tagName("div")).getText().trim();
        String newType;
        switch (calcType){
            case "Std":case "Standard":
                newType = "Standard";
                break;
            case "Cum":case "Cumulative":
                newType = "Cumulative";
                break;
            default:
                throw new IllegalArgumentException("Unexpected Calculation Type '" + calcType +
                        "'. Expected 'Std', 'Cum', 'Standard' or 'Cumulative'");
        }
        if (oldType.startsWith(newType)){
            return new EAPListView(driver);
        }
        calcToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView selectStudentInfo(String optionText){
        List<WebElement> targetDDLs = driver.findElements(STUDENT_INFO_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Student Info is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView setSubWhole(String subOrWhole){
        WebElement icon;
        switch (subOrWhole.toLowerCase()){
            case "sub":
                icon = driver.findElement(SUB_WHOLE_TOGGLE_SUB);
                break;
            case "whole":
                icon = driver.findElement(SUB_WHOLE_TOGGLE_WHOLE);
                break;
            default:
                throw new IllegalArgumentException("Argument must be either 'sub' or 'whole'");
        }
        if (icon.getCssValue("cursor").equals("pointer")){
            icon.click();
        }
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public boolean isOptionDisabled(By locator){
        return driver.findElement(locator).getAttribute("class").toLowerCase().contains("disabled");
    }

    public boolean isOptionEnabled(By locator){
        return !isOptionDisabled(locator);
    }

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    public boolean isEnabled(){
        return driver.findElements(ENABLED_FIELDS).size() > 0;
    }

    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();
        if (isOptionEnabled(COL_SORT_DDL)){
            actions.add(ReportAction.SORT_COLUMN);
        }
        if (isOptionEnabled(COL_SORT_DIRECTION_TOGGLE)){
            actions.add(ReportAction.SORT_DIRECTION);
        }
        if(isOptionEnabled(BREAKDOWN_DDL)){
            actions.add(ReportAction.BREAKDOWN);
        }
        if(isOptionEnabled(FIG_TYPE_TOGGLE)){
            actions.add(ReportAction.FIGURE_TYPE);
        }
        if(isOptionEnabled(CALC_TYPE_TOGGLE)){
            actions.add(ReportAction.CALCULATION_TYPE);
        }
        if(isOptionEnabled(STUDENT_INFO_DDL)){
            actions.add(ReportAction.STUDENT_INFO);
        }
        if(isOptionEnabled(SUB_WHOLE_TOGGLE_SUB)||isOptionEnabled(SUB_WHOLE_TOGGLE_WHOLE)){
            actions.add(ReportAction.SUB_WHOLE);
        }
        return actions;
    }

    public List<String> getOptionsForAction(ReportAction action) {
        List<String> options = new ArrayList<String>();
        switch(action){
            case SORT_COLUMN:
                return getDDLOptions(COL_SORT_DDL_OPTIONS);
            case BREAKDOWN:
                return getDDLOptions(BREAKDOWN_DDL);
            case SORT_DIRECTION:
            case FIGURE_TYPE:
            case CALCULATION_TYPE:
            case SUB_WHOLE:
                options.add("Toggle");
                return options;
            case STUDENT_INFO:
                return getDDLOptions(STUDENT_INFO_DDL);
            default:
                throw new IllegalArgumentException(action.toString()+
                        " is not a valid ReportAction for the Display Options Row");
        }
    }

    public EAPView applyActionOption(ReportAction action, String option) {
        switch(action){
            case SORT_COLUMN:
                return selectColSort(option);
            case BREAKDOWN:
                return selectBreakdown(option);
            case SORT_DIRECTION:
                return toggleColSortDirection();
            case FIGURE_TYPE:
                return toggleSetting(FIG_TYPE_TOGGLE);
            case CALCULATION_TYPE:
                return toggleSetting(CALC_TYPE_TOGGLE);
            case SUB_WHOLE:
                return toggleSetting(SUB_WHOLE_TOGGLE);
            case STUDENT_INFO:
                return selectStudentInfo(option);
            default:
                throw new IllegalArgumentException(action.toString()+
                        " is not a valid ReportAction for the Display Options Row");

        }
    }

    /*Actions/state queries used within more than one public method */
    private List<String> getDDLOptions(By ddlLocator){
        List<String> options = new ArrayList<String>();
        for(WebElement option : driver.findElements(ddlLocator)){
            options.add(option.getText());
        }
        return options;
    }

    private String getToggleSetting(WebElement setting){
        String label = setting.findElement(By.tagName("div")).getText().trim();
        return label.replace(":","").trim();
    }

    private EAPListView toggleSetting(By settingLocator){
        WebElement setting = driver.findElement(settingLocator);
        return toggleSetting(setting);
    }

    private EAPListView toggleSetting(WebElement setting){
        String newOption = "";
        switch(getToggleSetting(setting)){
            case "Count":
                newOption = "Percentage";
                break;
            case "Percentage":
                newOption = "Count";
                break;
            case "Standard":
                newOption = "Cumulative";
                break;
            case "Cumulative":
                newOption = "Standard";
                break;
            case "Sub":
                newOption = "Whole";
                break;
            case "Whole":
                newOption = "Sub";
        }
        setting.findElement(By.cssSelector("label[title='" + newOption + "']")).click();
        return new EAPListView(driver);
    }

    /* Expected conditions specific to this component */

}

package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;

import java.util.List;

public class ReportDisplayOptions extends AnalyticsComponent {

    public final By COL_SORT_DDL = By.cssSelector("select#ReportOptions_RPTColSort_ColNameType");
    private final By COL_SORT_DDL_OPTIONS = By.tagName("select#ReportOptions_RPTColSort_ColNameType>option");
    public final By COL_SORT_DIRECTION_TOGGLE = By.cssSelector("label.icon.sortDir");
    public final By BREAKDOWN_DDL = By.cssSelector("select#ReportOptions_Filter_ID");
    public final By FIG_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(2)");
    public final By CALC_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(3)");
    public final By STUDENT_INFO_DDL = By.cssSelector("#ReportOptions_RPTFilterTagDisplay");
    public final By IN_A8_BASKET_DDL = By.cssSelector("#ReportOptions_RPTInA8Basket_ID");
    public final By SUB_WHOLE_TOGGLE_SUB = By.cssSelector(".chgOptFmSub.icon.sub");
    public final By SUB_WHOLE_TOGGLE_WHOLE = By.cssSelector(".chgOptFmSub.icon.whole");

    public ReportDisplayOptions(RemoteWebDriver aDriver){
        super(aDriver);
    }

    private void waitForColResort(){
        waitShort.until(ExpectedConditions.elementToBeClickable(COL_SORT_DDL));
        waitShort.until(ExpectedConditions.elementToBeClickable(COL_SORT_DIRECTION_TOGGLE));
        waitShort.until(ExpectedConditions.numberOfElementsToBeMoreThan(COL_SORT_DDL_OPTIONS,0));
    }

    public EAPListView selectColSort(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COL_SORT_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Column Sort is not available");
        }
        if (!optionText.equals("")) {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
            waitForLoadingWrapper();
            //waitForColResort();
        }
        return new EAPListView(driver);
    }

    public EAPListView setColSortDirection(String sortOrder){
        WebElement sortIcon = driver.findElement(COL_SORT_DIRECTION_TOGGLE);
        String bgPosition = sortIcon.getCssValue("background-position");
        boolean sortedAscending = bgPosition.endsWith("-60px");
        boolean sortAsc = sortOrder.toLowerCase().equals("ascending");
        if (sortedAscending != sortAsc){
            sortIcon.click();
            waitForLoadingWrapper();
            //waitForColResort();
        }
        return new EAPListView(driver);
    }

    public EAPListView toggleColSortDirection(){
        driver.findElement(COL_SORT_DIRECTION_TOGGLE).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectBreakdown(String optionText){
        List<WebElement> targetDDLs = driver.findElements(BREAKDOWN_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Breakdown is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView setFigType(String figType){
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
        return new EAPListView(driver);
    }

    public EAPListView setCalcType(String calcType){
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

    public EAPListView selectStudentInfo(String optionText){
        List<WebElement> targetDDLs = driver.findElements(STUDENT_INFO_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Student Info is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectInA8Basket(String optionText){
        List<WebElement> targetDDLs = driver.findElements(IN_A8_BASKET_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because In A8 Basket is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView setSubWhole(String subOrWhole){
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

    public boolean isDisabled(By locator){
        return driver.findElement(locator).getAttribute("class").toLowerCase().contains("disabled");
    }



}

package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.EAPReport;

import java.util.List;

public class Report_ViewOptions extends AnalyticsComponent {

    public final By COL_SORT_DDL = By.cssSelector("select#ReportOptions_RPTColSort_ColName");
    public final By COL_SORT_DIRECTION_TOGGLE = By.cssSelector("label.icon.sortDir");
    public final By BREAKDOWN_DDL = By.cssSelector("select#ReportOptions_Filter_ID");
    public final By FIG_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(2)");
    public final By CALC_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(3)");
    public final By STUDENT_INFO_DDL = By.cssSelector("#ReportOptions_RPTFilterTagDisplay");
    public final By IN_A8_BASKET_DDL = By.cssSelector("#ReportOptions_RPTInA8Basket_ID");
    public final By SUB_WHOLE_TOGGLE_SUB = By.cssSelector(".chgOptFmSub.icon.sub");
    public final By SUB_WHOLE_TOGGLE_WHOLE = By.cssSelector(".chgOptFmSub.icon.whole");

    public Report_ViewOptions(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public EAPReport selectColSort(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COL_SORT_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Column Sort is not available");
            }
            return new EAPReport(driver); // The Column Sort DDL is not currently available
        }
        if (!optionText.equals("")) {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
            waitForLoadingWrapper();
        }
        return new EAPReport(driver);
    }

    public EAPReport setColSortDirection(String sortOrder){
        WebElement sortIcon = driver.findElement(COL_SORT_DIRECTION_TOGGLE);
        String bgPosition = sortIcon.getCssValue("background-position");
        boolean sortedAscending = bgPosition.endsWith("-60px");
        boolean sortAsc = sortOrder.toLowerCase().equals("ascending");
        if (sortedAscending != sortAsc){
            sortIcon.click();
        }
        return new EAPReport(driver);
    }

    public EAPReport toggleColSortDirection(){
        driver.findElement(COL_SORT_DIRECTION_TOGGLE).click();
        return new EAPReport(driver);
    }

    public EAPReport selectBreakdown(String optionText){
        List<WebElement> targetDDLs = driver.findElements(BREAKDOWN_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Breakdown is not available");
            }
            return new EAPReport(driver); // The Breakdown DDL is not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }

    public EAPReport setFigType(String figType){
        // checks current availability and setting, clicks label if req.
        if (figType.equals("")){
            return new EAPReport(driver);
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
            return new EAPReport(driver);
        }
        figToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }

    public EAPReport setCalcType(String calcType){
        // checks current availability and setting, clicks label if req.
        if (calcType.equals("")){
            return new EAPReport(driver);
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
            return new EAPReport(driver);
        }
        calcToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }

    public EAPReport selectStudentInfo(String optionText){
        List<WebElement> targetDDLs = driver.findElements(STUDENT_INFO_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Student Info is not available");
            }
            return new EAPReport(driver); // The Student Info DDL is not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }

    public EAPReport selectInA8Basket(String optionText){
        List<WebElement> targetDDLs = driver.findElements(IN_A8_BASKET_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because In A8 Basket is not available");
            }
            return new EAPReport(driver); // The In A8 Basket DDL is not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }

    public EAPReport setSubWhole(String subOrWhole){
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
        return new EAPReport(driver);
    }

    public boolean isDisabled(By locator){
        return driver.findElement(locator).getAttribute("class").toLowerCase().contains("disabled");
    }



}

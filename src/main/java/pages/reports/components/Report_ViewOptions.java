package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.Report;

import java.util.List;

public class Report_ViewOptions extends AnalyticsComponent {

    public static final By COL_SORT_DDL = By.cssSelector("select#ReportOptions_RPTColSort_ColName");
    public static final By COL_SORT_DIRECTION_TOGGLE = By.cssSelector("label[for='RPTColSort_Desc']");
    public static final By BREAKDOWN_DDL = By.cssSelector("select#ReportOptions_Filter_ID");
    public static final By FIG_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(2)");
    public static final By CALC_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(3)");

    public Report_ViewOptions(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public Report selectColSort(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COL_SORT_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Column Sort is not available");
            }
            return new Report(driver); // The Column Sort DDL is not currently available
        }
        if (!optionText.equals("")) {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
            waitForLoadingWrapper();
        }
        return new Report(driver);
    }

    public Report toggleColSortDirection(){
        driver.findElement(COL_SORT_DIRECTION_TOGGLE).click();
        return new Report(driver);
    }

    public Report selectBreakdown(String optionText){
        List<WebElement> targetDDLs = driver.findElements(BREAKDOWN_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Breakdown is not available");
            }
            return new Report(driver); // The Breakdown DDL is not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }

    public Report setFigType(String figType){
        // checks current availability and setting, clicks label if req.
        if (figType.equals("")){
            return new Report(driver);
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
            return new Report(driver);
        }
        figToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return new Report(driver);
    }

    public Report setCalcType(String calcType){
        // checks current availability and setting, clicks label if req.
        if (calcType.equals("")){
            return new Report(driver);
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
            return new Report(driver);
        }
        calcToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return new Report(driver);
    }



}

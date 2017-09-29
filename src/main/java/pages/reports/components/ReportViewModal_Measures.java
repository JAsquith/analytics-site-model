package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AnalyticsComponent;


/**
 * Represents the contents and interactive elements on the Add/Remove Measures modal
 */
public class ReportViewModal_Measures extends AnalyticsComponent {

    protected static final By GROUP_HEADINGS = By.cssSelector(".measureTbl tr:not(:nth-of-type(1))>th:nth-of-type(1)");
    protected static final By CANCEL_BUTTON = By.cssSelector(".modalClose.button.cancel");
    protected static final By APPLY_BUTTON = By.cssSelector(".button.green");

    // CONSTRUCTOR
    public ReportViewModal_Measures(RemoteWebDriver aDriver){
        super(aDriver);
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(GROUP_HEADINGS));
        waitForLoadingWrapper();
    }

    public ReportViewModal_Measures clickMeasureFilterOption(String measureName, String measureOption){

        //System.out.println("Selecting Measure Filter [" + measureName + " > " + measureOption + "]");
        int measureOptionIndex;

        switch (measureOption){
            case "Include": measureOptionIndex = 1; break;
            case "Exclude": measureOptionIndex = 2; break;
            case "CompareInclude": measureOptionIndex = 3; break;
            case "CompareExclude": measureOptionIndex = 4; break;
            default:
                throw new IllegalArgumentException("measureOption ("+measureOption+") must be one of: " +
                "'Include', 'Exclude', 'CompareInclude', 'CompareExclude'");
        }

        String cssNamesLocator = ".measureTbl tr:not(:nth-of-type(1))>td:nth-of-type(1):not([colspan=\\'3\\'])";
        String cssCBLocator = "td:nth-of-type("+(measureOptionIndex+1)+")>input";
        String js = "var measureName = '"+measureName+"';";
        js += "var cbIndex = "+measureOptionIndex+";";
        js += "var names = document.querySelectorAll('"+cssNamesLocator+"');";
        js += "var cbElement;";
        js += "var tdElement; var parent;";
        js += "for (i = 0; i < names.length; i++) {";
        js += "  var nameText = names[i].textContent.trim();";
        js += "  if (nameText == measureName) {";
        js += "    tdElement = names[i];";
        js += "    parent = tdElement.parentElement;";
        js += "    cbElement = parent.querySelector('"+cssCBLocator+"');";
        js += "  }";
        js += "}";
        js += "cbElement.scrollIntoView(true);";
        js += "return cbElement";

        try {
            WebElement label = (WebElement) driver.executeScript(js);
            label.click();
        } catch (Exception e){
            System.err.println("Check Box for Measure [" + measureName + " > " + measureOption + "] not found");
            System.err.println(e.getMessage());
            //this.cancel();
        }
        return this;
    }

    public void apply(){
        driver.findElement(APPLY_BUTTON).click();
        waitForLoadingWrapper();
    }
    public void cancel(){
        driver.findElement(CANCEL_BUTTON).click();
        waitForLoadingWrapper();
    }

}

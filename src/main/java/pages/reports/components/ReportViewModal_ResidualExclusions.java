package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AnalyticsComponent;


/**
 * Represents the contents and interactive elements on the Add/Remove Residual Exclusions modal
 */
public class ReportViewModal_ResidualExclusions extends AnalyticsComponent {

    protected static final By QUAL_NAMES = By.cssSelector(".measureTbl td:nth-of-type(1)");
    protected static final By CANCEL_BUTTON = By.cssSelector(".modalClose.button.cancel");
    protected static final By APPLY_BUTTON = By.cssSelector(".button.green");

    // CONSTRUCTOR
    public ReportViewModal_ResidualExclusions(RemoteWebDriver aDriver){
        super(aDriver);
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(QUAL_NAMES));
        waitForLoadingWrapper();
    }

    public ReportViewModal_ResidualExclusions toggleQualExclusion(String qualName){

        //System.out.println("Toggling Residual Exclusion for [" + qualName + "]");
        String cssNamesLocator = ".measureTbl td:nth-of-type(1)";
        String js = "var names = document.querySelectorAll('"+cssNamesLocator+"');";
        js += "var cbElement;";
        js += "var parent;";
        js += "for (i = 0; i < names.length; i++) {";
        js += "  var nameText = names[i].textContent.trim();";
        js += "  if (nameText == '"+qualName+"') {";
        js += "    parent = names[i].parentElement;";
        js += "    cbElement = parent.querySelector('td:nth-of-type(2)>input');";
        js += "  }";
        js += "}";
        js += "return cbElement";

        try {
            WebElement label = (WebElement) driver.executeScript(js);
            driver.executeScript("arguments[0].scrollIntoView(true);", label);
            label.click();
        } catch (Exception e){
            System.err.println("Qualification [" + qualName + "] not found");
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

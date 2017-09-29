package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.interfaces.IReportModal;

import java.util.List;


/**
 * Represents the contents and interactive elements on the Add/Remove Residual Exclusions modal
 */
public class ReportViewModal_Residuals extends ReportViewModal implements IReportModal{

    protected static final By QUAL_NAMES = By.cssSelector(".measureTbl td:nth-of-type(1)");

    // CONSTRUCTOR
    public ReportViewModal_Residuals(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public ReportViewModal_Residuals toggleQualExclusion(String qualName){

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

    public List<String> getGroupsList() {
        return null;
    }

    public List<String> getOptionsListForGroup(String group) {
        return null;
    }

}

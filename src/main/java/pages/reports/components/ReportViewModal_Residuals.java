package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.interfaces.IReportModal;

import java.util.ArrayList;
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

    public ReportViewModal_Residuals toggleOption(String qualName, String optionName){

        String js = getJSForCheckBox(qualName, optionName);

        try {
            WebElement label = (WebElement) driver.executeScript(js);
            driver.executeScript("arguments[0].scrollIntoView(true);", label);
            label.click();
        } catch (Exception e){
            System.err.println("Qualification [" + qualName + "] not found");
            System.err.println(e.getMessage());
        }
        return this;
    }

    public List<String> getGroupsList() {
        List<String> qualNames = new ArrayList<String>();
        List<WebElement> qualNameElements = driver.findElements(QUAL_NAMES);
        for(WebElement qualNameElement : qualNameElements){
            qualNames.add(qualNameElement.getText().trim());
        }
        return qualNames;
    }

    public List<String> getValuesForGroup(String group) {
        List<String> options = new ArrayList<String>();
        options.add("Include");
        options.add("Exclude");
        return options;
    }

    private String getJSForCheckBox(String qualName, String checkBox){
        String js = "var names = document.querySelectorAll('"+QUAL_NAMES+"');";
        js += "var cbElement;";
        js += "var parent;";
        js += "var optionType = " + ((checkBox == "Exclude") ? 3 : 2);
        js += "for (i = 0; i < names.length; i++) {";
        js += "  var nameText = names[i].textContent.trim();";
        js += "  if (nameText == '"+qualName+"') {";
        js += "    parent = names[i].parentElement;";
        js += "    cbElement = parent.querySelector('td:nth-of-type('+optionType+')>input');";
        js += "  }";
        js += "}";
        js += "return cbElement";
        return js;
    }

}

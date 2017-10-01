package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.interfaces.IReportModal;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the contents and interactive elements on the Add/Remove Measures modal
 */
public class ReportViewModal_Measures extends ReportViewModal implements IReportModal {

    protected static final String MEASURE_LABELS_CSS =
            ".measureTbl tr:not(:nth-of-type(1))>td:nth-of-type(1):not([colspan=\"3\"])";
    protected static final By MEASURE_LABELS = By.cssSelector(MEASURE_LABELS_CSS);

    private List<String> measures = null;

    // CONSTRUCTOR
    public ReportViewModal_Measures(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public ReportViewModal toggleOption(String measureName, String measureOption){

        String js = getJSForOptionCBLocator(measureName, measureOption);

        try {
            WebElement label = (WebElement) driver.executeScript(js);
            label.click();
        } catch (Exception e){
            System.err.println("Check Box for Measure [" + measureName + " > " + measureOption + "] not found");
            System.err.println(e.getMessage());
        }
        return this;
    }

    public List<String> getGroupsList() {
        if(measures==null) {
            measures = new ArrayList<String>();
            for (WebElement listItem : driver.findElements(MEASURE_LABELS)) {
                String groupName = listItem.getText().trim();
                if (!groupName.equals("")) {
                    measures.add(groupName);
                }
            }
        }
        return measures;
    }

    public List<String> getValuesForGroup(String group) {
        String optionsIDClass = getOptionsIDFor(group);
        List<WebElement> optionCBs = driver.findElements(By.cssSelector("."+optionsIDClass));
        List<String> optionNames = new ArrayList<String>();
        for(WebElement optionCB : optionCBs){
            String optionClass = optionCB.getAttribute("class");
            String optionName = "";
            if (optionClass.contains("cd")){
                optionName = "Compare";
            }
            if (optionClass.contains("_y")){
                optionName += "Include";
            } else {
                optionName += "Exclude";
            }
            optionNames.add(optionName);
        }
        return optionNames;
    }

    private String getJSForOptionCBLocator(String measureName, String measureOption){
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

        String cssCBLocator = "td:nth-of-type("+(measureOptionIndex+1)+")>input";
        String js = "var measureName = '"+measureName+"';";
        js += "var cbIndex = "+measureOptionIndex+";";
        js += "var names = document.querySelectorAll('"+MEASURE_LABELS_CSS+"');";
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
        return js;
    }

    private String getOptionsIDFor(String measureName){
        String js = "var measureName = '"+measureName+"';";
        js += "var names = document.querySelectorAll('"+MEASURE_LABELS_CSS+"');";
        js += "var inputElement;";
        js += "for (i = 0; i < names.length; i++) {";
        js += "  var nameText = names[i].textContent.trim();";
        js += "  if (nameText == measureName) {";
        js += "    inputElement = names[i].querySelector('input');";
        js += "    break;";
        js += "  }";
        js += "}";
        js += "return inputElement";
        WebElement measureInput = null;
        try {
            measureInput = (WebElement) driver.executeScript(js);
        } catch (JavascriptException e){
            System.err.println("measureName: " + measureName);
            System.err.println("Exception running javascript:");
            System.err.println(js);
        }
        String inputID = "";
        try {
            inputID = measureInput.getAttribute("id");
        } catch (NullPointerException e){
            System.err.println("measureName: " + measureName);
            System.err.println("Javascript did not return an element:");
            System.err.println(js);
        }
        try {
            inputID = "id_" + inputID.split("_")[1];
        } catch (Exception e){
            System.err.println("measureName: " + measureName);
            System.err.println("inputID = " + inputID + "after javascript execution:");
            System.err.println(js);
        }
        return inputID;
    }
}

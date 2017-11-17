package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.interfaces.IReportModal;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the contents and interactive elements on the Add/Remove Filters modal
 */
public class ReportViewModal_Filters extends ReportViewModal implements IReportModal {

    protected static final By FILTER_TITLES = By.cssSelector(".title.addFilter");
    protected static final By FILTER_GROUP_BUTTONS_SPAN = By.tagName("span");
    protected static final By UNCHECK_ALL_BUTTON = By.id("checkClear");
    private static final By FILTER_CHECK_ALL = By.cssSelector("span.all");
    private static final By FILTER_UNCHECK_ALL = By.cssSelector("span.none");
    private List<String> filters = null;

    // CONSTRUCTOR
    public ReportViewModal_Filters(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public ReportViewModal_Filters toggleOption(String filterTitle, String filterValue){

        try {
            WebElement label = getFilterValueElement(filterTitle, filterValue);
            label.click();
        } catch (Exception e){
            System.err.println("Filter [" + filterTitle + " > " + filterValue + "] not found");
            System.err.println(e.getMessage());
        }
        return this;
    }

    public ReportViewModal_Filters uncheckAll(){
        driver.findElement(UNCHECK_ALL_BUTTON).click();
        waitForLoadingWrapper();
        return this;
    }

    public ReportViewModal_Filters checkAllForGroup(String filterTitle){
        WebElement filterGroup = getFilterGrpElement(filterTitle);
        driver.executeScript("arguments[0].scrollIntoView(true);", filterGroup);

        filterGroup.findElement(FILTER_CHECK_ALL).click();
        return this;
    }
    public ReportViewModal_Filters uncheckAllForGroup(String filterTitle){
        WebElement filterGroup = getFilterGrpElement(filterTitle);
        driver.executeScript("arguments[0].scrollIntoView(true);", filterGroup);

        filterGroup.findElement(FILTER_UNCHECK_ALL).click();
        return this;
    }

    public List<String> getGroupsList() {
        if(filters==null) {
            filters = new ArrayList<String>();
            for (WebElement listItem : driver.findElements(FILTER_TITLES)) {
                WebElement buttonsSpan = listItem.findElement(FILTER_GROUP_BUTTONS_SPAN);
                String titleText = listItem.getText().replace(buttonsSpan.getText(), "");
                filters.add(titleText.trim());
            }
        }
        return filters;
    }

    public List<String> getValuesForGroup(String filterTitle) {

        WebElement filterGroup = getFilterGrpElement(filterTitle);
        //driver.executeScript("arguments[0].scrollIntoView(true);", filterGroup);

        List<String> options = new ArrayList<String>();
        By valuesLocator = By.cssSelector(
                "."+filterGroup.getAttribute("data-grp")+" label"
        );

        for (WebElement valueLabel : driver.findElements(valuesLocator)){
            options.add(valueLabel.getText());
        }

        return options;
    }

    private String getJSForFilterLocator(String filterTitle){
        String js = "var filterTitle = '" + filterTitle + "';";
        js += "var titles = document.querySelectorAll('li.title.addFilter');";
        js += "var found = false;";
        js += "var liElement;";
        js += "for (var i=0; i<titles.length; i++){";
        js += "  var titleBarText = titles[i].textContent.replace('Check All','');";
        js += "  var titleBarText = titleBarText.replace('Uncheck All','');";
        js += "  if (titleBarText.trim() == filterTitle) {";
        js += "    liElement = titles[i];";
        js += "        found = true;";
        js += "  }";
        js += "}";
        js += "return liElement";
        return js;
    }
    private String getJSForFilterValueLocator(String filterTitle, String filterValue){
        String js = "var filterTitle = '" + filterTitle + "';";
        js += "var filterValue = '" + filterValue + "';";
        js += "var titles = document.querySelectorAll('li.title.addFilter');";
        js += "var found = false;";
        js += "var labelElement;";
        js += "var liElement;";
        js += "for (var i=0; i<titles.length; i++){";
        js += "  var titleBarText = titles[i].textContent.replace('Check All','');";
        js += "  var titleBarText = titleBarText.replace('Uncheck All','');";
        js += "  if (titleBarText.trim() == filterTitle) {";
        js += "    liElement = titles[i].nextElementSibling;";
        js += "    while (found == false){";
        js += "      labelElement = liElement.getElementsByTagName('label')[0];";
        js += "      if (labelElement.textContent == filterValue){";
        js += "        found = true;";
        js += "      }";
        js += "      liElement= liElement.nextElementSibling;";
        js += "    }";
        js += "  }";
        js += "}";
        js += "labelElement.scrollIntoView(true);";
        js += "return labelElement";
        return js;
    }

    private WebElement getFilterGrpElement(String filterTitle){
        String js = getJSForFilterLocator(filterTitle);
        return (WebElement) driver.executeScript(js);
    }
    private WebElement getFilterValueElement(String filterTitle, String filterValue){
        String js = getJSForFilterValueLocator(filterTitle, filterValue);
        return (WebElement) driver.executeScript(js);
    }

}

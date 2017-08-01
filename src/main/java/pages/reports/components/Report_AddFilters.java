package pages.reports.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Represents the contents and interactive elements on the Add/Remove Filters modal
 */
public class Report_AddFilters extends AnalyticsComponent {

    protected static final By FILTER_TITLES = By.cssSelector(".title.addFilter");
    protected static final By CANCEL_BUTTON = By.cssSelector(".modalClose.button.cancel");
    protected static final By UNCHECK_ALL_BUTTON = By.id("checkClear");
    protected static final By APPY_BUTTON = By.cssSelector(".button.green");

    // CONSTRUCTOR
    public Report_AddFilters(RemoteWebDriver aDriver){
        super(aDriver);
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(FILTER_TITLES));
        waitForLoadingWrapper();
    }

    public Report_AddFilters toggleFilterValue(String filterTitle, String filterValue){

        //System.out.println("Selecting Student Filter [" + filterTitle + " > " + filterValue + "]");

        String js = "var filterTitle = '" + filterTitle + "';";
        js += "var filterValue = '" + filterValue + "';";
        js += "var titles = document.querySelectorAll('li.title.addFilter');";
        js += "var found = false;";
        js += "var labelElement;";
        js += "var liElement;";
        js += "for (var i=0; i<titles.length; i++){";
        js += "  var titleBarText = titles[i].textContent.replace('Check All','');";
        js += "  titleBarText = titleBarText.replace('Uncheck All','');";
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
        js += "return labelElement";

        try {
            WebElement label = (WebElement) driver.executeScript(js);
            driver.executeScript("arguments[0].scrollIntoView(true);", label);
            label.click();
        } catch (Exception e){
            System.err.println("Filter [" + filterTitle + " > " + filterValue + "] not found");
            System.err.println(e.getMessage());
            //this.cancel();
        }
        return this;
    }

    public void apply(){
        driver.findElement(APPY_BUTTON).click();
        waitForLoadingWrapper();
    }
    public void cancel(){
        driver.findElement(CANCEL_BUTTON).click();
        waitForLoadingWrapper();
    }
    public Report_AddFilters uncheckAllButtons(){
        driver.findElement(UNCHECK_ALL_BUTTON).click();
        waitForLoadingWrapper();
        return this;
    }

}

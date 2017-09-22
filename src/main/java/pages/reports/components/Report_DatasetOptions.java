package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;

import java.util.List;

public class Report_DatasetOptions extends AnalyticsComponent {

    public final By DATASET_TAB = By.cssSelector(".dsTabs>div:nth-of-type(1)");
    public final By TRACKER_TAB = By.cssSelector(".dsTabs>div:nth-of-type(2)");

    public final By DATASET_DDL = By.id("actualSelect");
    public final By COMPARE_WITH_DDL = By.id("compareSelect");


    public Report_DatasetOptions(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public boolean isDisabled(By locator){
        return driver.findElement(locator).getAttribute("class").toLowerCase().contains("disabled");
    }

    public EAPListView selectMainFocus(String optionText){

        

        WebElement select = driver.findElement(DATASET_DDL);
        select.click();

        List<WebElement> allOptions = select.findElements(By.tagName("option"));
        WebElement choice = select.findElement(By.xpath("option[contains(text(),'"+optionText+"')]"));
        int choiceIndex = -1;
        for(WebElement option : allOptions){
            if (option.getText().equals(choice.getText())){
                choiceIndex = allOptions.indexOf(option);
                break;
            }
        }
        if (choiceIndex == -1){
            throw new IllegalArgumentException("Could not match '"+optionText+"' to a visible Data Set option");
        }

        new Select(select).selectByIndex(choiceIndex);
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DDL));
        return new EAPListView(driver);
    }

    public EAPListView selectCompareWith(String optionText){
        List<WebElement> compWithDDLs = driver.findElements(COMPARE_WITH_DDL);
        if (compWithDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't compare with '" + optionText + "' because Compare With is not available");
            }
            return new EAPListView(driver); // The Compare With DDL is not currently available
        }
        WebElement select = driver.findElement(COMPARE_WITH_DDL);
        select.click();
        List<WebElement> allOptions = select.findElements(By.tagName("option"));
        WebElement choice = select.findElement(By.xpath("option[contains(text(),'"+optionText+"')]"));
        int choiceIndex = -1;
        for(WebElement option : allOptions){
            if (option.getText().equals(choice.getText())){
                choiceIndex = allOptions.indexOf(option);
                break;
            }
        }
        if (choiceIndex == -1){
            throw new IllegalArgumentException("Could not match '"+optionText+"' to a visible Compare With option");
        }

        new Select(select).selectByIndex(choiceIndex);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView switchTab(String newTab){
        By tabLocator = (newTab.equals("Dataset")) ? DATASET_TAB : TRACKER_TAB;
        WebElement tab = driver.findElement(tabLocator);
        String classAttr = tab.getAttribute("class");
        if (classAttr.indexOf("active") == 0){
            tab.click();
        }
        return new EAPListView(driver);
    }

}

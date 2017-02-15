package analytics.pages.data.basedata;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the components and actions on an Import EAP page
 */
public class ImportEAPData extends AnalyticsPage {

    public final By FILE_BROWSER_FIELD = By.cssSelector("input[type='file']");
    public final By CANCEL_BUTTON = By.linkText("Cancel");
    public final By IMPORT_BUTTON = By.cssSelector(".submitButtons>button.button.green");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on the Import EAP page
     */
    public ImportEAPData(AnalyticsDriver aDriver) {
        super(aDriver);
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(FILE_BROWSER_FIELD));
    }

    /**
     * Overwrites the text in the file control with the specified file path
     * @param filePath  A file path string
     * @return  The current ImportEAPData page object (to facilitate method daisy-chaining)
     */
    public ImportEAPData setImportFile(String filePath){
        WebElement field = driver.findElement(FILE_BROWSER_FIELD);
        field.clear();
        field.sendKeys(filePath);
        return this;
    }

    /**
     * Clicks the Cancel button
     */
    public void clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
    }

    /**
     * Clicks the Import button
     */
    public void clickImport(){
        driver.findElement(IMPORT_BUTTON).click();
    }

}
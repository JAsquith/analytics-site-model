package pages.data.basedata;

import pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Represents the components and actions on an EAP Detail page
 */
public class EAPDetail extends AnalyticsPage {

    public final By IMPORT_EAP_BUTTON = By.linkText("Import EAP");
    public final By EXPORT_EAP_BUTTON = By.linkText("Export EAP");
    public final By EDIT_EAP_BUTTON = By.linkText("Edit EAP");
    public final By CLEAR_EAP_BUTTON = By.linkText("Clear EAP");
    public final By BACK_TO_LIST_LINK = By.linkText("Back to EAP list");


    /**
     * Simple constructor
     * @param aDriver   The browser should be on the EAP Detail page
     */
    public EAPDetail(RemoteWebDriver aDriver) {
        super(aDriver);
        waitMedium.until(ExpectedConditions.and(
                ExpectedConditions.elementToBeClickable(EXPORT_EAP_BUTTON),
                ExpectedConditions.elementToBeClickable(IMPORT_EAP_BUTTON),
                ExpectedConditions.elementToBeClickable(EDIT_EAP_BUTTON),
                ExpectedConditions.elementToBeClickable(CLEAR_EAP_BUTTON)
        ));

    }

    /**
     * Clicks the Import EAP button
     * @return  A new {@link ImportEAPData} page object
     */
    public ImportEAPData clickImportEAP(){
        driver.findElement(IMPORT_EAP_BUTTON).click();
        waitForLoadingWrapper(LONG_WAIT);
        return new ImportEAPData(driver);
    }

    /**
     * Yet to be implemented
     * @return  The current EAPDetail page object (to facilitate method daisy-chaining)
     */
    public EAPDetail clickExportEAP(){
        return this;
    }

    /**
     * Clicks the Edit EAP button
     * @return  An {@link EditEAP} page object
     */
    public EditEAP clickEditEAP(){
        driver.findElement(EDIT_EAP_BUTTON).click();
        return new EditEAP(driver);
    }

    /**
     * Clicks the Clear EAP button and accepts the resulting JS confirm
     * @return  The current ImportEAPData page object (to facilitate method daisy-chaining)
     */
    public EAPDetail clickClearEAP(){
        driver.findElement(CLEAR_EAP_BUTTON).click();
        WebDriverWait wait = new WebDriverWait(driver, SHORT_WAIT);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        driver.switchTo().defaultContent();
        return this;
    }

    public EAPList clickBackToListLink(){
        driver.findElement(BACK_TO_LIST_LINK).click();
        return new EAPList(driver, false);
    }

}
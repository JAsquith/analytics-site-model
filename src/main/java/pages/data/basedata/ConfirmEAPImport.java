package pages.data.basedata;

import pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Represents the components and actions on an EAP Detail page
 */
public class ConfirmEAPImport extends AnalyticsPage {

    public final By CANCEL_BUTTON = By.id("btnDelete_a");
    public final By IMPORT_BUTTON = By.id("btnUpload_a");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on the Create EAP page (/EAPAdmin/EAP/Create)
     */
    public ConfirmEAPImport(RemoteWebDriver aDriver) {
        super(aDriver);
    }

    /**
     * Clicks the Cancel button
     * @return  The current ImportEAPData page object (to facilitate method daisy-chaining)
     */
    public ConfirmEAPImport clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
        return this;
    }

    /**
     * Clicks the Import button
     * @return  The EAPDetail page
     */
    public EAPDetail clickConfirm(){
        driver.findElement(IMPORT_BUTTON).click();
        return new EAPDetail(driver);
    }

}
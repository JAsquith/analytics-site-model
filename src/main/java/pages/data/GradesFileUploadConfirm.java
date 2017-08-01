package pages.data;

import pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Represents the Grades File upload Health Check/confirmation page which is common to
 * the baselines and grades upload areas.
 * Accessed via the {@link GradesFileUpload} page
 */
public class GradesFileUploadConfirm extends AnalyticsPage {

    public final By CANCEL_BUTTON = By.id("btnDelete_a");
    public final By CONTINUE_BUTTON = By.id("btnUpload_a");

    /**
     * Constructor which waits for the url to contain "ConfirmUpload" and then
     * waits for the green submitButton to be clickable
     * @param aDriver   The browser should be on the Confirm Upload page
     */
    public GradesFileUploadConfirm(RemoteWebDriver aDriver){
        super(aDriver);
        WebDriverWait wait = new WebDriverWait(driver, LONG_WAIT);
        wait.until(ExpectedConditions.urlContains("ConfirmUpload"));
        wait.until(ExpectedConditions.elementToBeClickable(CANCEL_BUTTON));
    }

    /**
     * Clicks the Cancel The Upload button
     */
    public void clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
    }

    /**
     * Clicks the Continue With The Upload button. Assuming validation passes, in some cases this
     * will redirect to the Confirm Upload Qualification Names page, and in others back to the
     * page from which the Grade File Upload was initiated
     */
    public void continueUpload(){
        driver.findElement(CONTINUE_BUTTON).click();
    }
}

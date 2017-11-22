package pages.data.basedata;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.data.DataPage;
import pages.data.components.DataSideMenu;

import java.util.List;

/**
 * Represents the contents and interactive elements on the Publish KS2 / EAP page of EAP
 */
public class PublishBaseData extends DataPage {

    public final String PAGE_URL = "/EAPAdmin/Publish/KS2EAP";

    public final By PUBLISH_BUTTON = By.id("smtBtQue");
    public final By LOCAL_PUBLISH_BUTTON = By.id("smtBt");
    public final By CLOSE_PUB_PROG_MODAL_BUTTON = By.cssSelector(".pubFinished");
    public final By LAST_PUBLISHED_INFO_TEXT = By.cssSelector(".smallInfo>span");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on the Publish KS2 / EAP page
     */
    public PublishBaseData(RemoteWebDriver aDriver){
        super(aDriver);
    }

    /**
     * Clicks the Publish button;
     * Waits up to {@code PUBLISH_WAIT} seconds for the Publish progress modal to show the Close button;
     * Clicks the Close button
     * @return  The current PublishBaseData page object (to facilitate method daisy-chaining)
     */
    public PublishBaseData clickPublishWaitAndClose(){
        return clickPublishWaitAndClose(0);
    }

    /**
     * Clicks either the Publish button or the Local button depending on the publishTypeID param
     * @param publishTypeID     {@code 0} = normal publish; {@code 1} = local publish
     *                                   (dev only - no check made to ensure button is present)
     * @return  The current PublishBaseData page object (to facilitate method daisy-chaining)
     */
    public PublishBaseData clickPublishWaitAndClose(int publishTypeID){
        clickPublishAndWait(publishTypeID);
        switch (publishTypeID){
            case 0:
                driver.findElement(CLOSE_PUB_PROG_MODAL_BUTTON).click();
                waitForLoadingWrapper();
                break;
            case 1:
                // Don't need to do anything - the page should have reloaded
        }
        return this;
    }

    public PublishBaseData clickPublishAndWait(){
        return clickPublishAndWait(0);
    }

    public PublishBaseData clickPublishAndWait(int publishTypeID){
        WebDriverWait wait = new WebDriverWait(driver, PUBLISH_WAIT);
        switch (publishTypeID){
            case 0:
                driver.findElement(PUBLISH_BUTTON).click();
                wait.until(ExpectedConditions.elementToBeClickable(CLOSE_PUB_PROG_MODAL_BUTTON));
                return this;
            case 1:
                driver.findElement(LOCAL_PUBLISH_BUTTON).click();
                waitForLoadingWrapper(PUBLISH_WAIT);
                return this;
            default:
                throw new IllegalArgumentException("publishTypeID ("+publishTypeID+") must be 0 or 1");
        }
    }

    public String getLastPublishedInfo() {
        return driver.findElement(LAST_PUBLISHED_INFO_TEXT).getText().trim();
    }

    public PublishBaseData load(String cohort, boolean loadByUrl){

        DataSideMenu sideMenu = this.openCohortInDataArea(cohort);

        if (loadByUrl){
            String targetUrl = getSiteBaseUrl() + PAGE_URL;
            if (driver.getCurrentUrl().equals(targetUrl))
            {
                return this;
            }
            driver.get(targetUrl);
        } else {
            if (sideMenu == null){
                sideMenu = new DataSideMenu(driver);
            }
            if (!sideMenu.isTabSelected("KS2 / EAP")){
                sideMenu.clickTab("KS2 / EAP");
            }
            sideMenu.clickMenuOption("Publish KS2 / EAP");
        }
        return this;
    }

    public PublishBaseData closeModal(){
        List<WebElement> closeButtons = driver.findElements(CLOSE_PUB_PROG_MODAL_BUTTON);
        if (closeButtons.size()>0){
            closeButtons.get(0).click();
        }
        return this;
    }

}

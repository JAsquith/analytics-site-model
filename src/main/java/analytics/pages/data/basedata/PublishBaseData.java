package analytics.pages.data.basedata;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.DataHome;
import analytics.pages.data.components.DataAdminSelect;
import analytics.pages.data.components.DataSideMenu;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Represents the contents and interactive elements on the Publish KS2 / EAP page of EAP
 */
public class PublishBaseData extends AnalyticsPage {

    public final String PAGE_URL = "/EAPAdmin/Publish/KS2EAP";

    public final By PUBLISH_BUTTON = By.id("smtBtQue");
    public final By LOCAL_PUBLISH_BUTTON = By.id("smtBt");
    public final By CLOSE_PUB_PROG_MODAL_BUTTON = By.cssSelector(".pubFinished");
    public final By LAST_PUBLISHED_INFO_TEXT = By.className("smallInfo");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on the Publish KS2 / EAP page
     */
    public PublishBaseData(AnalyticsDriver aDriver){
        super(aDriver);
    }

    /**
     * Clicks the Publish button;
     * Waits up to {@code PUBLISH_WAIT} seconds for the Publish progress modal to show the Close button;
     * Clicks the Close button
     * @return  The current PublishBaseData page object (to facilitate method daisy-chaining)
     */
    public PublishBaseData clickPublish(){
        return clickPublish(0);
    }

    /**
     * Clicks either the Publish button or the Local button depending on the publishTypeID param
     * @param publishTypeID     {@code 0} = normal publish; {@code 1} = local publish
     *                                   (dev only - no check made to ensure button is present)
     * @return  The current PublishBaseData page object (to facilitate method daisy-chaining)
     */
    public PublishBaseData clickPublish(int publishTypeID){
        // Uses a switch to future-proof for other publishing methods
        switch (publishTypeID){
            case 0:
                driver.findElement(PUBLISH_BUTTON).click();
                WebDriverWait wait = new WebDriverWait(driver, PUBLISH_WAIT);
                wait.until(ExpectedConditions.elementToBeClickable(CLOSE_PUB_PROG_MODAL_BUTTON)).click();
                return this;
            case 1:
                driver.findElement(LOCAL_PUBLISH_BUTTON).click();
                waitForLoadingWrapper(PUBLISH_WAIT);
                return this;
            default:
                // Todo - Handle bad TypeID (possible IllegalArgumentException)
        }
        return this;
    }

    public String getLastPublishedInfo() {
        return driver.findElement(LAST_PUBLISHED_INFO_TEXT).getText().trim();
    }

    public PublishBaseData load(String cohort, boolean loadByUrl){

        DataSideMenu sideMenu = null;

        if(!cohort.equals("")){
            DataHome dataPage = new DataHome(driver, true);
            DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
            modeAndCohort.selectMode("EAP");

            if (cohort.length() > 2){
                cohort = cohort.substring(cohort.length()-2);
            }
            sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        }
        if (loadByUrl){
            driver.get(driver.getDomainRoot()+PAGE_URL);
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
}

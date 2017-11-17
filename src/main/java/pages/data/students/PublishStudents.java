package pages.data.students;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;

import java.util.List;

/**
 * Represents the contents and interactive elements on the Publish Students Page of EAP
 */
public class PublishStudents extends AnalyticsPage {

    public final String PAGE_URL = "/EAPAdmin/Publish/Students";
    public final By PUBLISH_BUTTON = By.id("smtBtQue");
    public final By LOCAL_PUBLISH_BUTTON = By.id("smtBt");
    public final By CLOSE_PUB_PROG_MODAL_BUTTON = By.cssSelector(".pubFinished");
    public final By LAST_PUBLISHED_INFO_TEXT = By.cssSelector(".smallInfo>span");

    /**
     * Simple constructor
     *
     * @param aDriver The browser should be on the Publish Students page
     */
    public PublishStudents(RemoteWebDriver aDriver) {
        super(aDriver);
    }

    /**
     * Clicks the Publish button;
     * Waits up to {@code PUBLISH_WAIT} seconds for the Publish progress modal to show the Close button;
     * Clicks the Close button
     */
    public PublishStudents clickPublishWaitAndClose() {
        return clickPublishWaitAndClose(0);
    }

    /**
     * Clicks either the Publish button or the Local button depending on the publishTypeID param
     *
     * @param publishTypeID {@code 0} = normal publish; {@code 1} = local publish (dev only - no check made to ensure button is present)
     */
    public PublishStudents clickPublishWaitAndClose(int publishTypeID) {
        clickPublishAndWait(publishTypeID);
        switch (publishTypeID){
            case 0:
                driver.findElement(CLOSE_PUB_PROG_MODAL_BUTTON).click();
                waitForLoadingWrapper();
                break;
            case 1:
                // Shouldn't need to do anything - page should have reloaded
        }
        return this;
    }

    public PublishStudents clickPublishAndWait(){
        return clickPublishAndWait(0);
    }
    public PublishStudents clickPublishAndWait(int publishTypeID) {
        // Uses a switch to future-proof for other publishing methods
        switch (publishTypeID) {
            case 0:
                driver.findElement(PUBLISH_BUTTON).click();
                WebDriverWait wait = new WebDriverWait(driver, PUBLISH_WAIT);
                wait.until(ExpectedConditions.elementToBeClickable(CLOSE_PUB_PROG_MODAL_BUTTON));
                return this;
            case 1:
                driver.findElement(LOCAL_PUBLISH_BUTTON).click();
                return this;
            default:
                throw new IllegalArgumentException("publishTypeID ("+publishTypeID+") must be 0 or 1");
        }
    }

    public PublishStudents closeModal(){
        List<WebElement> closeButtons = driver.findElements(CLOSE_PUB_PROG_MODAL_BUTTON);
        if (closeButtons.size()>0){
            closeButtons.get(0).click();
        }
        return this;
    }

    public String getLastPublishedInfo() {
        return driver.findElement(LAST_PUBLISHED_INFO_TEXT).getText().trim();
    }

    public PublishStudents load(String cohort, boolean loadByUrl){
        DataSideMenu sideMenu = null;
        if(cohort.length()>0) {
            DataHome dataPage = new DataHome(driver, true);
            DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
            modeAndCohort.selectMode("EAP");

            if (cohort.length() > 2) {
                cohort = cohort.substring(cohort.length() - 2);
            }
            sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        }
        if (loadByUrl){
            driver.get(getSiteBaseUrl()+PAGE_URL);
        } else {
            if(sideMenu == null){
                sideMenu = new DataSideMenu(driver);
            }
            if (!sideMenu.isTabSelected("STUDENTS")){
                sideMenu.clickTab("STUDENTS");
            }
            sideMenu.clickMenuOption("Publish Students");
        }
        return this;
    }


}

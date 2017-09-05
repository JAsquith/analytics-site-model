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
    public final By LAST_PUBLISHED_INFO_TEXT = By.className("smallInfo");

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
    public void clickPublishAndWait() {
        clickPublishAndWait(0);
    }

    /**
     * Clicks either the Publish button or the Local button depending on the publishTypeID param
     *
     * @param publishTypeID {@code 0} = normal publish; {@code 1} = local publish (dev only - no check made to ensure button is present)
     */
    public void clickPublishAndWait(int publishTypeID) {
        // Uses a switch to future-proof for other publishing methods
        switch (publishTypeID) {
            case 0:
                driver.findElement(PUBLISH_BUTTON).click();
                WebDriverWait wait = new WebDriverWait(driver, PUBLISH_WAIT);
                wait.until(ExpectedConditions.elementToBeClickable(CLOSE_PUB_PROG_MODAL_BUTTON)).click();
                return;
            case 1:
                driver.findElement(LOCAL_PUBLISH_BUTTON).click();
                waitForLoadingWrapper(PUBLISH_WAIT);
                return;
            default:
                throw new IllegalArgumentException("publishTypeID must be 0 or 1");
        }
    }

    public void clickPublish(){
        clickPublish(0);
    }
    public void clickPublish(int publishTypeID) {
        // Uses a switch to future-proof for other publishing methods
        switch (publishTypeID) {
            case 0:
                driver.findElement(PUBLISH_BUTTON).click();
                WebDriverWait wait = new WebDriverWait(driver, PUBLISH_WAIT);
                wait.until(ExpectedConditions.elementToBeClickable(CLOSE_PUB_PROG_MODAL_BUTTON)).click();
                return;
            case 1:
                driver.findElement(LOCAL_PUBLISH_BUTTON).click();
                waitForLoadingWrapper(PUBLISH_WAIT);
                return;
            default:
                throw new IllegalArgumentException("publishTypeID must be 0 or 1");
        }
    }

    public void clickClose(){
        List<WebElement> closeButtons = driver.findElements(CLOSE_PUB_PROG_MODAL_BUTTON);
        if (closeButtons.size()>0){
            closeButtons.get(0).click();
        }
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
            driver.get(getCurrentDomain()+PAGE_URL);
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

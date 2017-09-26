package pages.config;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsPage;

/**
 * Provides locators and methods interact with the Student Data Profile page
 */
public class StudentDataProfile extends AnalyticsPage {

    public final By EDIT_COLUMNS_BUTTON = By.linkText("Edit Columns");
    public final By FILTER_11_SELECT = By.id("FilterList_10__Filter_ID");

    public final By SAVE_BUTTON = By.cssSelector(".button.green.submit");
    public final By CANCEL_BUTTON = By.cssSelector(".button.cancel");

    public final String PAGE_PATH = "/Config/Filters/StudentDataDefaults";
    public final String PAGE_TITLE_TEXT = "Student Data Profile";

    public StudentDataProfile(RemoteWebDriver aDriver, boolean loadByUrl) {
        super(aDriver);
        if (loadByUrl) {
            driver.get(getSiteBaseUrl() + PAGE_PATH);
            waitLong.until(ExpectedConditions.elementToBeClickable(this.MAIN_MENU_CONFIG));
        }
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(EDIT_COLUMNS_BUTTON));
    }

    public StudentDataProfile clickEditColumns(){
        driver.findElement(EDIT_COLUMNS_BUTTON).click();
        waitShort.until(ExpectedConditions.elementToBeClickable(FILTER_11_SELECT));
        return this;
    }

    public StudentDataProfile selectFilter(int filterNum, String filterName){
        getDDL(By.id("FilterList_" + (filterNum - 1) + "__Filter_ID")).
                selectByVisibleText(filterName);
        return this;
    }

    public StudentDataProfile clickSave(){
        driver.findElement(SAVE_BUTTON).click();
        waitMedium.until(
                ExpectedConditions.or(
                        ExpectedConditions.elementToBeClickable(EDIT_COLUMNS_BUTTON),
                        ExpectedConditions.numberOfElementsToBeMoreThan(VAL_MESSAGE_SPANS,0)));
        return this;
    }

    public StudentDataProfile clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
        waitMedium.until(ExpectedConditions.elementToBeClickable(EDIT_COLUMNS_BUTTON));
        return this;
    }
}

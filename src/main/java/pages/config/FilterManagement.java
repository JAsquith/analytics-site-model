package pages.config;

import pages.AnalyticsPage;
import pages.config.components.CreateFilterModal;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Provides locators and methods interact with the Student Data Profile page
 */
public class FilterManagement extends AnalyticsPage {

    public final By CREATE_FILTER_BUTTON = By.linkText("Create Filter");
    public final String PAGE_PATH = "/Config/Filters";
    public final String PAGE_TITLE_TEXT = "Filter Management";

    public FilterManagement(RemoteWebDriver aDriver, boolean loadByUrl) {
        super(aDriver);
        if (loadByUrl) {
            driver.get(getCurrentDomain() + PAGE_PATH);
        }
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(CREATE_FILTER_BUTTON));
    }

    public CreateFilterModal clickCreateFilter(){
        driver.findElement(CREATE_FILTER_BUTTON).click();
        return new CreateFilterModal(driver);
    }

}

package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportModal;

public abstract class ReportViewModal extends AnalyticsComponent implements IReportModal {
    protected static final By CANCEL_BUTTON = By.cssSelector(".modalClose.button.cancelChanges");
    protected static final By APPLY_BUTTON = By.cssSelector(".button.green");

    public ReportViewModal(RemoteWebDriver aDriver){
        super(aDriver);
        waitForLoadingWrapper();
        waitShort.until(modalDisplayed());
    }

    public EAPView applyChanges(){
        driver.findElement(APPLY_BUTTON).click();
        waitForLoadingWrapper();
        return new EAPView(driver);
    }
    public EAPView cancelChanges(){
        driver.findElement(CANCEL_BUTTON).click();
        waitForLoadingWrapper();
        return new EAPView(driver);
    }

    public ExpectedCondition<WebElement> modalDisplayed() {
        return ExpectedConditions.elementToBeClickable(APPLY_BUTTON);
    }

}

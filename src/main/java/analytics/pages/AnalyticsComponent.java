package analytics.pages;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AnalyticsComponent extends AbstractAnalyticsObject {

    protected WebElement modal;
    protected WebElement tableRow;

    public AnalyticsComponent(AnalyticsDriver aDriver){
        driver = aDriver;
        waitTiny = new WebDriverWait(driver, TINY_WAIT);
        waitShort = new WebDriverWait(driver, SHORT_WAIT);
        waitMedium = new WebDriverWait(driver, MEDIUM_WAIT);
        waitLong = new WebDriverWait(driver, LONG_WAIT);
    }

    protected void initModal(By modalLocator){
        waitLong.until(ExpectedConditions.elementToBeClickable(modalLocator));
        modal = driver.findElement(modalLocator);
    }

    protected void initModal(By modalLocator, By modalLoadedLocator, int timeout){
        waitLong.until(ExpectedConditions.elementToBeClickable(modalLoadedLocator));
        modal = driver.findElement(modalLocator);
    }
}

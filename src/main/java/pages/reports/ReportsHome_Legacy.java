package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

public class ReportsHome_Legacy extends ReportsHome {

    public final By PUBLISHED_REPORT_BOXES = By.cssSelector(".pub td.l_pub");
    public final By PUBLISHED_REPORT_LINK = By.cssSelector("em.line");
    public final By BUTTONS_SHIM = By.cssSelector("div.shim");
    public static final By BUTTONS_IN_SHIM = By.cssSelector("a.button");

    public ReportsHome_Legacy(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public WebElement showReportButtons(String datasetName){
        List<WebElement> reportBoxes = driver.findElements(PUBLISHED_REPORT_BOXES);
        for (WebElement reportBox : reportBoxes){
            if (reportBox.findElement(PUBLISHED_REPORT_LINK).getText().trim().equals(datasetName)){
                reportBox.click();
                waitForShimInsideReportBox(reportBox);
                return reportBox.findElement(BUTTONS_SHIM);
            }
        }
        // If we've gotten this far the dataset was not found!!
        throw new IllegalArgumentException(datasetName + " is not a dataset in the current cohort");
    }

    public void waitForShimInsideReportBox(WebElement td){
        int targetWidth = td.getSize().width - 20;
        waitMedium.until(minimumWidthOfElementLocatedBy(PUBLISHED_REPORT_BOXES, targetWidth));
    }


    /**
     * An expectation for checking if the the element that matches the given locator is at least as wide
     * as the given width (in pixels).
     *
     * @param locator used to find the element
     * @param width    the minimum width of the element to wait for
     * @return true once the first element located by locator is at least as wide as the width
     */
    public static ExpectedCondition<Boolean> minimumWidthOfElementLocatedBy(final By locator,
                                                                            final int width) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    int elementWidth = driver.findElement(locator).getSize().width;
                    return elementWidth >= width;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return String.format("minimum width of element found by ('%s') to be %s",
                        locator, width);
            }
        };
    }


}

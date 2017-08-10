package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ReportsHome_Legacy extends ReportsHome {

    public final By PUBLISHED_REPORT_BOXES = By.cssSelector(".pub td>.info");
    public final By PUBLISHED_REPORT_LINK = By.cssSelector("em.line");

    public ReportsHome_Legacy(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public void showReportButtons(String datasetName){
        List<WebElement> reportInfoDivs = driver.findElements(PUBLISHED_REPORT_BOXES);
        for (WebElement infoDiv : reportInfoDivs){
            if (infoDiv.findElement(PUBLISHED_REPORT_LINK).getText().trim().equals(datasetName)){
                infoDiv.click();
                waitForShimInsideInfoDiv(infoDiv);
            }
        }
        // If we've gotten this far the dataset was not found!!
    }

    public void waitForShimInsideInfoDiv(WebElement div){
        int targetWidth = div.getSize().width - 20;
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

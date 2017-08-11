package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

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
        waitMedium.until(minimumWidthOfElementLocatedBy(BUTTONS_SHIM, targetWidth));
     //   waitMedium.until(elementToTheRightOf(BUTTONS_SHIM, targetWidth));
    }

}

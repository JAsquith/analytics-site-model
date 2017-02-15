package analytics.pages.config;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.NoSuchElementException;

/**
 * Super class of page objects representing each step of the EAP Grade Method wizard;
 * Provides methods to navigate pages ({@link #clickNext()} & {@link #clickPrevious()} rather than
 * creating a object using each page's specific class, and to cancel the create/edit process
 */
public class GradeMethodDetail extends AnalyticsPage {

    public static final By CANCEL_BUTTON = By.cssSelector(".submitButtons>a.button.cancel");
    public static final By PREVIOUS_BUTTON = By.cssSelector(".submitButtons>a.button:not(.cancel)");
    public static final By NEXT_BUTTON = By.cssSelector(".submitButtons>button");
    public static final By PAGE_LEGEND = By.cssSelector("span.legend:nth-of-type(1)");

    /**
     * Creates a page object which can navigate between the steps of creating/editing an EAP Grade Method
     * @param aDriver   An AnalyticsDriver object logged in to a school in the given domain
     */
    public GradeMethodDetail(AnalyticsDriver aDriver) {
        super(aDriver);
        waitForLoadingWrapper(LONG_WAIT);
        //System.out.println("Loaded Page:" + getPageLegend());
    }

    /**
     * Clicks the Cancel button and 'accepts' (clicks OK on) the resulting js confirm
     */
    public void clickCancel(){
        String pageLegend = driver.findElement(PAGE_LEGEND).getText();
        driver.findElement(CANCEL_BUTTON).click();
        driver.switchTo().alert().accept();
        waitMedium.until(ExpectedConditions.invisibilityOfElementWithText(PAGE_LEGEND, pageLegend));
    }

    /**
     * Clicks the Previous button
     * @throws org.openqa.selenium.NoSuchElementException if there is no Previous button (i.e. on step 1)
     */
    public void clickPrevious(){
        String pageLegend = driver.findElement(PAGE_LEGEND).getText();
        driver.findElement(PREVIOUS_BUTTON).click();
        waitMedium.until(ExpectedConditions.invisibilityOfElementWithText(PAGE_LEGEND, pageLegend));
    }

    /**
     * Clicks the Next button
     * @throws org.openqa.selenium.NoSuchElementException if there is no Next button (i.e. on step 6)
     */
    public void clickNext(){
        driver.findElement(NEXT_BUTTON).click();
    }
    /**
     * Simple accessor for the Page Legend &lt;span&gt; element
     * @return the Page Legend (i.e. "Step n of 6 - blah blah blah") as a String
     */
    public String getPageLegend(){
        return driver.findElement(PAGE_LEGEND).getText().trim();
    }

    private boolean validationFailOrNextPage(){


        throw new NoSuchElementException("");
    }

}

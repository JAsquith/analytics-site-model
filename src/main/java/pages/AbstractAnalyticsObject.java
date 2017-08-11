package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Super class of AnalyticsPage and AnalyticsComponent;
 * Provides methods to implement universal functions like waiting for the loading wrapper to go away
 */

public abstract class AbstractAnalyticsObject {

    protected RemoteWebDriver driver;
    protected WebDriverWait waitTiny;
    protected WebDriverWait waitShort;
    protected WebDriverWait waitMedium;
    protected WebDriverWait waitLong;

    public final int TINY_WAIT = 1;
    public final int SHORT_WAIT = 10;
    public final int MEDIUM_WAIT = 20;
    public final int LONG_WAIT = 30;
    public final int PUBLISH_WAIT = 120;

    public final By PAGE_TITLE = By.id("menuSub");

    protected final By OVERLAY_LOADING_WRAPPER = By.id("loadingWrapper");
    protected final By UNDERLAY_MODAL = By.cssSelector("#page .modal");
    protected final By UNDERLAY_NON_MODAL = By.id("invisiModal");

    private final By VAL_MESSAGE_LIST = By.cssSelector("div.validation-summary-errors>ul");
    protected final By VAL_MESSAGE_SPANS = By.cssSelector(".field-validation-error");
    protected final By VAL_MESSAGE_LIST_ITEMS = By.cssSelector(".validation-summary-errors>ul>li");

    /**
     * Calls {@link #waitForLoadingWrapper(int)} with {@link #MEDIUM_WAIT}
     *
     * @return  Boolean True is returned if the Loading message is hidden within the timeout period
     * @throws  org.openqa.selenium.TimeoutException if timeout expires
     */
    public Boolean waitForLoadingWrapper(){
        return waitForLoadingWrapper(MEDIUM_WAIT);
    }

    /**
     * Initially waits (timeout / 2) seconds for the "Loading..." message to not be visible, if still displayed after that
     * refreshes the current page and then waits {@code timeout}
     *
     * @param timeout   The number of seconds to wait
     * @return  Boolean True is returned if the Loading message is hidden within the timeout period
     * @throws  org.openqa.selenium.TimeoutException if timeout expires
     */
    public Boolean waitForLoadingWrapper(int timeout){
        return waitForLoadingWrapper(timeout, true);
    }

    public Boolean waitForLoadingWrapper(int timeout, boolean hideOnTimeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        Boolean retVal;
        try {
            retVal = wait.until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_LOADING_WRAPPER));
        } catch (TimeoutException e){
            if (hideOnTimeout){
                String script = "document.querySelector(\"#loadingWrapper\").setAttribute('style', 'display:none');";
                driver.executeScript(script);
                retVal = true;
            } else {
                driver.get(driver.getCurrentUrl());
                retVal = wait.until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_LOADING_WRAPPER));
            }
        }
        return retVal;
    }

    /**
     * Simple accessor for the validation messages list element
     * @return  A {@link WebElement} object representing the &lt;ul&gt; element showing any validaation messages
     */
    public WebElement getValidationMsgList(){
        return driver.findElement(VAL_MESSAGE_LIST);
    }

    /**
     * Accessor for any validation messages displayed on the page/component.
     * Allows for messages in a list structure ('.validation-summary-errors>ul>li'), or
     * messages within span elements ('.field-validation-error>span').
     * Add additional locators as required.
     * @return
     */
    public List<WebElement> getValidationMessages(){
        return driver.findElements(
                new ByAll(
                        VAL_MESSAGE_SPANS,
                        VAL_MESSAGE_LIST_ITEMS));
    }

    protected AbstractAnalyticsObject setFieldValue(By fieldLocator, String newValue){
        WebElement field = driver.findElement(fieldLocator);
        field.clear();
        field.sendKeys(newValue);
        return this;
    }

    protected Select getDDL(By fieldLocator){
        return new Select(driver.findElement(fieldLocator));
    }

    /**
     * Gets the domain part of the current URL up to the end of the TLD.  This normally results in
     * either "https://www.sisraanalytics.co.uk" or "http://dev.sisraanalytics.co.uk"
     * @return A String representing the site domain
     */
    protected String getCurrentDomain(){
        String location = driver.getCurrentUrl();
        int domainEnd = location.indexOf("sisraanalytics.co.uk") + 20;
        return location.substring(0, domainEnd);
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

    /**
     * An expectation for checking if the left side of the element that matches the given locator is
     * positioned to the right of the given x coordinate.
     *
     * @param locator used to find the element
     * @param x_pos    the minimum left alignment of the element to wait for
     * @return true once the first element located by locator is to the right of x_pos
     */
    public static ExpectedCondition<Boolean> elementToTheRightOf(final By locator,
                                                                 final int x_pos) {

        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    int elementX = driver.findElement(locator).getLocation().x;
                    return elementX >= x_pos;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return String.format("minimum width of element found by ('%s') to be %s",
                        locator, x_pos);
            }
        };
    }



}

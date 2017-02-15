package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import analytics.utils.HtmlLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class Base {

    public static final By MODAL_BG_FILLER = By.cssSelector("div.modal.filp");
    // Private fields
    public AnalyticsDriver driver;
    public SISRATest test;

    public final By LOADING_WRAPPER = By.id("loadingWrapper");
    public final By OVERLAY = By.id("invisiModal");
    /** Identifies the page sub-title button (&lt;div&gt; element) available on most pages.
     * */
    public static final By PAGE_SUB_TITLE = By.id("menuSub");
    protected final int DEFAULT_TIMEOUT = 30;
    protected final int DEFAULT_SHORT_TIMEOUT = 20;
    protected final int DEFAULT_LONG_TIMEOUT = 50;

    public static final String PROTOCOL = "https://";
    public static final String DEV_SUB_DOMAIN = "dev.";
    //public static final String DEV_SUB_DOMAIN = "eap.";
    public static final String TEST_SUB_DOMAIN = "dev.";
    public static final String LIVE_SUB_DOMAIN = "www.";
    public static final String SERVICE_DOMAIN = "sisraanalytics.co.uk/";

    public static final String DEV_HOME = PROTOCOL + DEV_SUB_DOMAIN + SERVICE_DOMAIN;
    public static final String TEST_HOME = PROTOCOL + TEST_SUB_DOMAIN + SERVICE_DOMAIN;
    public static final String LIVE_HOME = PROTOCOL + LIVE_SUB_DOMAIN + SERVICE_DOMAIN;

    public final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public final DateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");

    // Private static methods
    private static String getCloudUser(ITestContext context){return context.getCurrentXmlTest().getParameter("grid-user");}
    private static String getCloudPassword(ITestContext context){return context.getCurrentXmlTest().getParameter("grid-pass");}

    // Constructor
    public Base(AnalyticsDriver driver){
        this.driver = driver;
    }

    public Base (SISRATest test){
        this.driver = test.driver;
        this.test = test;
    }

    // Public non-static (instance) methods
    // Methods providing synonyms for WebDriver functions to enhance readability
    public void visit(String url){
        String realUrl;
        switch (url.toUpperCase()){
            case "LIVE":
                realUrl = LIVE_HOME;
                break;
            case "TEST":case "HOME":
                realUrl = TEST_HOME;
                break;
            case "DEV":
                realUrl = DEV_HOME;
                break;
            default:
                realUrl = url;
        }
        driver.get(realUrl);
    }
    public WebElement find (By locator){return driver.findElement(locator);}
    public WebElement find (WebElement parent, By locator){return parent.findElement(locator);}

    public List<WebElement> findAll (By locator){return driver.findElements(locator);}
    public List<WebElement> findAll (WebElement parent, By locator){return parent.findElements(locator);}

    public void click(By locator) {find(locator).click();}
    public void click(WebElement element){element.click();}

    public void clickAt(By locator, int xOffset, int yOffset) {
        Actions actions = new Actions(driver);
        actions.moveToElement(find(locator), xOffset, yOffset).click().perform();
    }

    public void clear(By locator){find(locator).clear();}
    public void clear(WebElement element){element.clear();}

    public void type(String input, By intoLocator){type(input, find(intoLocator));}
    public void type(String input, WebElement intoField){intoField.sendKeys(input);}

    public void clearAndType(String input, By intoLocator){overType(input, intoLocator);}
    public void clearAndType(String input, WebElement intoField){overType(input, intoField);}

    public void overType(String input, By intoLocator){
        clear(intoLocator);
        type(input, intoLocator);
    }
    public void overType(String input, WebElement intoField){
        clear(intoField);
        type(input, intoField);
    }

    public void submit(By locator){find(locator).submit();}

    public boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (NoSuchElementException e){
            return false;
        }
    }

    // Methods providing synonyms for WebDriverWait expected conditions
    // to enhance readability
    public boolean waitForInvisibilityOf(By locator, int timeout) throws TimeoutException{
        WebDriverWait wait = new WebDriverWait(driver,timeout);
        boolean result;
        try {
            result = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e){
            if (test != null){
                test.logWithScreenshot(HtmlLogger.Level.WARN,
                        "Timed out waiting for invisibility of element located by "
                                + locator.toString(),
                        "TimeoutException");
            }
            throw e;
        }
        return result;
    }
    public boolean waitForInvisibilityOf(By locator) throws TimeoutException{
        return this.waitForInvisibilityOf(locator, DEFAULT_TIMEOUT);
    }

    public WebElement waitForVisibilityOf(By locator, int timeout) throws TimeoutException{
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement result;
        try {
            result = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e){
            if (test != null){
                test.logWithScreenshot(HtmlLogger.Level.WARN,
                        "Timed out waiting for visibility of element located by "
                                + locator.toString(),
                        "TimeoutException");
            }
            throw e;
        }
        return result;
    }
    public WebElement waitForVisibilityOf(By locator) throws TimeoutException{
        return this.waitForVisibilityOf(locator, DEFAULT_TIMEOUT);
    }

    public WebElement waitForElementVisible(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        WebElement result;
        try {
            result = wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e){
            if (test != null){
                test.logWithScreenshot(HtmlLogger.Level.WARN,
                        "Timed out waiting for visibility of element "
                                + element.toString(),
                        "TimeoutException");
            }
            throw e;
        }
        return result;
    }

    public WebElement waitForClickabilityOf(WebElement element) {
        return waitForClickabilityOf(element, true);
    }

    public WebElement waitForClickabilityOf(WebElement element, boolean exceptionOnTimeout) {
        return waitForClickabilityOf(element, DEFAULT_TIMEOUT, exceptionOnTimeout);
    }

    public WebElement waitForClickabilityOf(WebElement element, int timeout, boolean exceptionOnTimeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement result = null;
        try {
            result = wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            if (exceptionOnTimeout)
                throw e;
        }
        return result;
    }

    public WebElement waitForClickabilityOf(By locator, int timeout) throws TimeoutException {
        return waitForClickabilityOf(locator, timeout, true);
    }

    public WebElement waitForClickabilityOf(By locator, int timeout, boolean exceptionOnTimeout) throws TimeoutException{
        WebDriverWait wait = new WebDriverWait (driver, timeout);
        WebElement result = null;
        try {
            result = wait.until(elementToBeClickable(locator));
        } catch (TimeoutException e){
            if (test != null){
                test.logWithScreenshot(HtmlLogger.Level.WARN,
                        "Timed out waiting for visibility of element located by "
                                + locator.toString(),
                        "TimeoutException");
            }
            if (exceptionOnTimeout)
                throw e;
        }
        return result;
    }
    public WebElement waitForClickabilityOf(By locator) throws TimeoutException{
        return this.waitForClickabilityOf(locator, DEFAULT_TIMEOUT);
    }

    // Methods providing Analytics specific waits/expected conditions/common actions
    public void waitForReload(int timeout) throws TimeoutException {
        //if(!isDisplayed(LOADING_WRAPPER))
        //    waitForClickabilityOf(LOADING_WRAPPER,1,false);
        waitForInvisibilityOf(LOADING_WRAPPER, timeout);
    }

    public void waitForReload() throws TimeoutException {waitForReload(DEFAULT_TIMEOUT);}

    public void removeOverlay(){
        if (isDisplayed(OVERLAY))
            try {clickAt(OVERLAY, 25, 25);}
            catch (Exception e) {
                if (isDisplayed(OVERLAY)) {
                    test.log(HtmlLogger.Level.WARN, "*** Now you see it, now you don't...(now you do again!)");
                    throw e;
                }
            }
    }
    public void waitForModalFilp (){
        waitForInvisibilityOf(MODAL_BG_FILLER);
    }

}

package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;

/**
 * Provides Selenium locators (as static fields) used to locate
 * <a href='https://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/WebElement.html'>
 * WebElement</a>s found on the MAIN MENU of
 * <a href='https://www.sisraanalytics.co.uk/'>SISRA Analytics</a>, and convenience methods to
 * {@code click} or {@code clickAndWait} for each button on the menu.
 * <br><br>
 *   Apart from the HOME and LOGOUT buttons, using any of these locators with
 *   driver.findElement (or similar) method may throw a org.openqa.selenium.NotFoundException
 *   (e.g. if the current user does not have access to that area of the service).
 *
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-18
 */
public class MainMenu extends Base{

    /** Identifies the 'HOME' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics.
     */
    public static final By MENU_HOME = By.cssSelector("img[alt='Home']");
    /** Identifies the 'DATA' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics, or
     * the logged in user does not have access to the DATA area.
     */
    public static final By MENU_DATA = By.cssSelector("img[alt='Data']");
    /** Identifies the 'CONFIG.' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics, or
     * the logged in user does not have access to the CONFIG. area.
     */
    public static final By MENU_CONFIG = By.cssSelector("img[alt='Config.']");
    /** Identifies the 'USERS' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics, or
     * the logged in user does not have access to the USERS area.
     */
    public static final By MENU_USERS = By.cssSelector("img[alt='Users']");
    /** Identifies the 'REPORTS' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics, or
     * the logged in user does not have access to the REPORTS area.
     */
    public static final By MENU_REPORTS = By.cssSelector("img[alt='Reports']");
    /** Identifies the 'HELP' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics, or
     * the logged in user does not have access to the HELP area.
     */
    public static final By MENU_HELP = By.cssSelector("img[alt='Help']");
    /** Identifies the 'LOGOUT' button (&lt;img&gt; element).
     * <br><br>
     * Not supported when: a user is not logged in to Analytics.
     */
    public static final By MENU_LOGOUT = By.cssSelector("img[alt='Logout']");

    // Constructors
    public MainMenu(AnalyticsDriver aDriver){super(aDriver);}

    // Methods for clicking Menu buttons
    /**
     * Clicks the HOME button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics.
     */
    public void clickHome(){
        click(MENU_HOME);
    }
    /**
     * Clicks the HOME button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickHomeAndWait(){
        this.clickHome();
        this.waitForReload();
    }
    /**
     * Clicks the HOME button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickHomeAndWait(int timeout){
        this.clickHome();
        this.waitForReload(timeout);
    }

    /**
     * Clicks the DATA button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the DATA area.
     */
    public void clickData(){
        click(MENU_DATA);
    }
    /**
     * Clicks the DATA button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the DATA area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickDataAndWait(){
        this.clickData();
        this.waitForReload();
    }
    /**
     * Clicks the DATA button on the main menu of SISRA Analytics
     * and waits for the page loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the DATA area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickDataAndWait(int timeout){
        this.clickData();
        this.waitForReload(timeout);
    }

    /**
     * Clicks the CONFIG.<!-- --> button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the CONFIG. area.
     */
    public void clickConfig(){
        click(MENU_CONFIG);
    }
    /**
     * Clicks the CONFIG.<!-- --> button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the CONFIG. area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickConfigAndWait(){
        this.clickConfig();
        this.waitForReload();
    }
    /**
     * Clicks the CONFIG.<!-- --> button on the main menu of SISRA Analytics
     * and waits for the page loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the CONFIG. area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickConfigAndWait(int timeout){
        this.clickConfig();
        this.waitForReload(timeout);
    }

    /**
     * Clicks the USERS button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the USERS area.
     */
    public void clickUsers(){
        click(MENU_USERS);
    }
    /**
     * Clicks the USERS button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the USERS area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickUsersAndWait(){
        this.clickUsers();
        this.waitForReload();
    }
    /**
     * Clicks the USERS button on the main menu of SISRA Analytics
     * and waits for the page loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the USERS area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickUsersAndWait(int timeout){
        this.clickUsers();
        this.waitForReload(timeout);
    }

    /**
     * Clicks the REPORTS button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the REPORTS area.
     */
    public void clickReports(){
        click(MENU_REPORTS);
    }
    /**
     * Clicks the REPORTS button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the REPORTS area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickReportsAndWait(){
        this.clickReports();
        this.waitForReload(DEFAULT_SHORT_TIMEOUT);
    }
    /**
     * Clicks the REPORTS button on the main menu of SISRA Analytics
     * and waits for the page loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the REPORTS area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickReportsAndWait(int timeout){
        this.clickReports();
        this.waitForReload(timeout);
    }

    /**
     * Clicks the HELP button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the HELP area.
     */
    public void clickHelp(){
        click(MENU_HELP);
    }
    /**
     * Clicks the HELP button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the HELP area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickHelpAndWait(){
        this.clickHelp();
        this.waitForReload();
    }
    /**
     * Clicks the HELP button on the main menu of SISRA Analytics
     * and waits for the page loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics
     * with access to the HELP area.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickHelpAndWait(int timeout){
        this.clickHelp();
        this.waitForReload(timeout);
    }

    /**
     * Clicks the LOGOUT button on the main menu of SISRA Analytics. <br>
     * Does not wait for page reload to complete before returning control.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics.
     */
    public void clickLogout(){
        click(MENU_LOGOUT);
    }
    /**
     * Clicks the LOGOUT button on the main menu of SISRA Analytics and waits for the page
     * loading to complete (up to the timeout value of the {@link AnalyticsDriver} instance's
     * WebDriverWait object). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     */
    public void clickLogoutAndWait(){
        this.clickLogout();
        this.waitForReload();
    }
    /**
     * Clicks the LOGOUT button on the main menu of SISRA Analytics
     * and waits for the page loading to complete (up to {@code timeout} seconds). <br>
     * Does not check that the correct page has loaded.
     *
     * @throws org.openqa.selenium.NotFoundException if not logged in to Analytics.
     * @throws org.openqa.selenium.TimeoutException if the page reload takes too long
     *
     * @param timeout number of seconds to wait for the page reload to complete
     */
    public void clickLogoutAndWait(int timeout){
        this.clickLogout();
        this.waitForReload(timeout);
    }
}

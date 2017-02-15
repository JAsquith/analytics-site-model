package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Provides Selenium locators (as static fields) used to locate
 * <a href='https://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/WebElement.html'>
 * WebElement</a>s found in the CONFIG.&nbsp;area of
 * <a href='https://www.sisraanalytics.co.uk/'>SISRA Analytics</a>, and convenience methods for
 * many actions commonly performed in that area.
 *
 * @author Milton
 * @version 1.0
 * @since 2016-02-23.
 */
public class ConfigArea extends BaseWithMenu {

    // Private fields
    private AnalyticsDriver driver;
    private int driverTimeout;

    // Elements visible on most/all pages

    /** Identifies the Student Data link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_STUDENT_DATA = By.linkText("Student Data");

    /** Identifies the Student Data Profile link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_STUDENT_DATA_PROFILE = By.linkText("Student Data Profile");

    /** Identifies the Filter Management link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_STUDENT_DATA_FILTERS = By.linkText("Filter Management");

    /** Identifies the Grade Methods link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_GRADE_METHODS = By.linkText("Grade Methods");

    /** Identifies the KS2 Methods link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_GRADE_METHODS_KS2 = By.linkText("KS2 Methods");

    /** Identifies the KS3 Methods link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_GRADE_METHODS_KS3 = By.linkText("KS3 Methods");

    /** Identifies the KS4 Methods link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_GRADE_METHODS_KS4 = By.linkText("KS4 Methods");

    /** Identifies the KS5 Methods link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_GRADE_METHODS_KS5 = By.linkText("KS5 Methods");

    /** Identifies the Data Sets link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_DATA_SETS = By.linkText("Data Sets");

    /** Identifies the KS3 Data Sets link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_DATA_SETS_KS3 = By.linkText("KS3 Data Sets");

    /** Identifies the KS4 Data Sets link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_DATA_SETS_KS4 = By.linkText("KS4 Data Sets");

    /** Identifies the KS5 Data Sets link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_DATA_SETS_KS5 = By.linkText("KS5Data Sets");

    /** Identifies the Facuty link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_FACULTY = By.linkText("Faculty");

    /** Identifies the Settings link (&lt;a&gt; element) on the CONFIG.&nbsp;side menu <br><br>
     * Not supported when: The CONFIG.&nbsp; is not active.
     */
    public static final By SIDE_MENU_SETTINGS = By.linkText("Settings");
    // N.B. While on this page, use css Selector "[data-themeid="2"]" to
    //      identify the "Preview" buttons for the second Data Theme, and
    //      "[data-themeid="2"]+a" to identify the "Use This Theme" buttons

    // Constructors

    /**
     * Creates a new ConfigArea instance with the given AnayticsDriver.
     *
     * @param aDriver An AnalyticsDriver
     */
    public ConfigArea(AnalyticsDriver aDriver) {
        super(aDriver);
    }


    // TODO Common actions as public methods

    /**
     * EXAMPLE:
     * Selects the given filters for the given columns in the Student Data Columns list. <br>
     * Throws a {@link NotFoundException} if the Student Data Profile page is not open.
     *
     * @param columnIndexes A List of Strings containing one or more values between
     *                      "10" and "19" identifying the uder-defined columns to change.
     * @param filterNames   A List of Strings containing one or more names of filters
     *                      to be selected in the columns identified in {link #columnIndexes).
     * @return {@code true} if the Filter Names were successfully selected.<br>
     *         {@code false} Otherwise.
     */
    public boolean setColumnFilters(List<String> columnIndexes, List<String> filterNames) {
        if (filterNames.size()!=columnIndexes.size())
            throw new IllegalArgumentException("The parameters must be lists of the same size.");
        if (filterNames.size()>10)
            throw new IllegalArgumentException("The parameters cannot contain more than 10 values.");

        driver.findElement(By.linkText("Edit Columns")).click();
        try {
            this.waitForClickabilityOf(By.id("FilterList_10__Filter_ID"),DEFAULT_TIMEOUT);
        } catch (TimeoutException e){
            throw new NotFoundException("Could not find the dropdown field for Filter 11 (index 10)");
        }
        for (String columnID: columnIndexes){
            if (Integer.getInteger(columnID)<10 || Integer.getInteger(columnID) > 19)
                throw new IllegalArgumentException("All values in columnIndexes must be between \"10\" and \"19\".");

            String selectID = "FilterList_"+columnID+"__Filter_ID";
            Select dropdown = new Select(driver.findElement(By.id(selectID)));
            try {
                dropdown.selectByVisibleText(filterNames.get(columnIndexes.indexOf(columnID)));
            } catch (NotFoundException e) {
                throw new IllegalArgumentException("All values in filterNames must be names of available filters.");
            }
        }
        return true;
    }

    // Any private methods...
}

package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Select;

/**
 * Provides Selenium locators (as static fields) used to locate
 * <a href='https://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/WebElement.html'>
 * WebElement</a>s found in the DATA area of
 * <a href='https://www.sisraanalytics.co.uk/'>SISRA Analytics</a>, and convenience methods for
 * many actions commonly performed in that area.
 *
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-17
 */
public class DataArea extends BaseWithMenu {

    // Elements visible on most/all pages
    /** Identifies the 'Keystage:' ddl (&lt;select&gt; element). <br><br>
     * Not supported when: not in the DATA area of Analytics
     */
    public static final By KEYSTAGE_SELECT = By.id("SelectedKS");
    /** Identifies the 'Year 11 in:' (or 'Year 13 in:' for KS5) ddl (&lt;select&gt;
     * element). <br><br>
     * Not supported when: the element identified by {@link #KEYSTAGE_SELECT} is null.
     */
    public static final By ADMIN_YEAR_SELECT = By.id("SelectedAdminYr");

    // Tabs on the side menu
    /** Identifies the 'Data' tab (&lt;a&gt; element) on the DATA side menu. <br><br>
     * Not supported when: either of the elements identified by {@link #KEYSTAGE_SELECT}
     * or {@link #ADMIN_YEAR_SELECT} is null
     */
    public static final By DATA_TAB = By.cssSelector(".tz_Data");
    /** Identifies the 'Uploads' tab (&lt;a&gt; element) on the DATA side menu. <br><br>
     * Not supported when: either of the elements identified by {@link #KEYSTAGE_SELECT}
     * or {@link #ADMIN_YEAR_SELECT} is null
     */
    public static final By UPLOADS_TAB = By.cssSelector(".tz_Uploads");

    // Links on the side menu Data tab
    /** Identifies the 'Publish Reports' link (&lt;a&gt; element) of the DATA side menu. <br><br>
     * Not supported when: the element identified by {@link #DATA_TAB} is not active
     */
    public static final By PUBLISH_REPORTS = By.linkText("Publish Reports");

    // Links on the side menu Uploads tab
    /** Identifies the 'Student Data' link (&lt;a&gt; element) of the DATA side menu. <br><br>
     * Not supported when: the element identified by {@link #UPLOADS_TAB} is not active
     */
    public static final By STUDENT_DATA_LINK = By.linkText("Student Data");
    /** Identifies the 'Assessment Grades' link (&lt;a&gt; element) of the DATA side menu. <br><br>
     * Not supported when: the element identified by {@link #UPLOADS_TAB} is not active
     */
    public static final By ASSESSMENT_GRADES_LINK = By.linkText("Assessment Grades");

    // Constructors
    /**
     * Creates a new DataArea instance with the given AnayticsDriver and default timeout.
     * @param aDriver   An AnalyticsDriver
     */
    public DataArea(AnalyticsDriver aDriver){
        super(aDriver);
    }
    public DataArea (SISRATest aTest){super(aTest);}

    /**
     * Selects the given keystage from the {@link #KEYSTAGE_SELECT} drop down list. <br><br>
     * Throws a {@link NotFoundException} if the {@link #KEYSTAGE_SELECT} drop down list is
     * not found on the page.
     *
     * @param keystageID    The keystage to select (3, 4 or 5)
     * @return              {@code true} if the keystage is available to be selected.<br>
     *                      {@code false} if the keystage is not listed in the drop down.
     */
    public boolean selectKeystage(String keystageID) {
        Select dropdown = new Select(waitForClickabilityOf(KEYSTAGE_SELECT));
        try {
            dropdown.selectByValue(keystageID);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Calls {@link #selectKeystage(String)} and {@link #waitForReload(int)}.
     * @param keystageID    The id of the Keystage to select ("3", "4", or "5")
     * @param timeout       How many seconds to wait for the page to reload after selecting
     * @return              true if the keystageID was available and selection worked, else false
     * @throws TimeoutException If the timeout expires before the mainMenu is clickable.
     */
    public boolean selectKeystageAndWait(String keystageID, int timeout) throws TimeoutException{
        boolean retval = this.selectKeystage(keystageID);
        this.waitForReload(timeout);
        return retval;
    }

    /**
     * Selects the given keystage from the {@link #ADMIN_YEAR_SELECT} drop down list. <br><br>
     * Throws a {@link NotFoundException} if the {@link #ADMIN_YEAR_SELECT} drop down list is
     * not found on the page.
     *
     * @param cohortID  The Admin Year/cohort to select.
     * @return          {@code true} if the cohort is available to be selected.<br>
     *                  {@code false} if the cohort is not listed in the drop down.
     */
    public boolean selectCohort(String cohortID) {
        Select dropdown = new Select(waitForClickabilityOf(ADMIN_YEAR_SELECT));
        try {
            dropdown.selectByValue(cohortID);
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * Calls {@link #selectCohort(String)} and {@link #waitForReload(int)}
     * @param cohortID  The id of the cohort to be selected (for "Leavers (14/15)", this would be "15")
     * @param timeout   How many seconds to wait for the page to reload after selecting
     * @return          true if the cohortID was available and selection worked, else false
     * @throws TimeoutException If the timeout expires before the mainMenu is clickable.
     */
    public boolean selectCohortAndWait(String cohortID, int timeout) throws TimeoutException{
        this.selectCohort(cohortID);
        this.waitForReload(timeout);
        return true;
    }

    /**
     * Shortcut method for selecting Keystage and cohort in one call. <br>
     * Simply calls the {@link #selectKeystage(String)} and {@link #selectCohort(String)}
     * methods.
     * @param keystage  The id of the Keystage to select
     * @param cohort    The id of the cohort/admin year to select
     * @return          {@code true} if the ks &amp; cohort were both successfully selected.<br>
     *                  {@code false} if either id was not selectable.
     */
    public boolean selectKSAndCohort(String keystage, String cohort){
        if (selectKeystage(keystage))
            if (selectCohort(cohort))
                return true;
        return false;
    }

    /**
     * Click the Uploads tab on the side menu
     */
    public void clickUploadsTab(){
        find(UPLOADS_TAB).click();
    }

    /**
     * Calls clickUploadsTab, waits for the "Student Data" link to be clickable,
     * then clicks that link.
     */
    public void openStudentDataUploads(){
        clickUploadsTab();
        waitForClickabilityOf(STUDENT_DATA_LINK);
    }

    /**
     * Calls clickUploadsTab, waits for the "Assessment Grades" link to be
     * clickable, then clicks that link.
     */
    public void openAssessmentGradesUploads(){
        clickUploadsTab();
        waitForClickabilityOf(ASSESSMENT_GRADES_LINK).click();
    }

}

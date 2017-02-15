package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.io.File;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * Created by masquith on 25/04/2016.
 */
public class DataArea_UploadsTab extends DataArea {

    // Elements on individual pages in Uploads
    /** Identifies the 'Create Collection' button (&lt;a&gt; element) on the
     * Student Data Uploads page. <br><br>
     * Not supported when: 'Student Data Uploads' is not the current page.
     */
    public static final By CREATE_COLLECTION_LINK = By.linkText("Create Collection");
    /** Condition to test for the existence of collapsible Collection accordions
     * (&lt;h3&gt; elements) on the Student or Grades Upload pages. <br><br>
     * Not supported when: the current page is not a Student or Grades Upload page.
     */
    public static final ExpectedCondition<WebElement> COLLECTION_ACCORDION_DISPLAYED =
            elementToBeClickable(By.cssSelector("h3.ui-accordion-header"));
    /** Identifies any/all of the collapsible Collection accordions (&lt;h3&gt; elements)
     *  on the Student or Grades Upload pages.  <br><br>
     * Not supported when: the current page is not a Student or Grades Upload page,
     * or if no Collections exist
     */
    public static final By COLLECTION_ACCORDION = By.cssSelector("h3.ui-accordion-header");
    /** Identifies an 'Upload Student Data' button (&lt;a&gt; element) within a particular
     * Collection accordion on the Student Data Uploads page. <br><br>
     * Not supported when: 'Student Data Uploads' is not the current page.
     * <br><br>
     * Use of this selector is only supported in the context of a WebElement representing
     * an &lt;h3&gt; Collection Accordion.
     * <br><br>
     * e.g.&nbsp;{@code driver.findElement(}{@link #COLLECTION_ACCORDION}
     * {@code ).findElement(UPLOAD_STUDENTS_BUTTON_FOR_COLLECTION)}.
     */
    public static final By UPLOAD_STUDENTS_BUTTON_FOR_COLLECTION = By.xpath(".//../../div/a");
    /** Identifies an 'Upload Grades' button (&lt;a&gt; element) within a particular
     * Collection accordion on the Student Data Uploads page. <br><br>
     * Not supported when: 'Assessment Grades Uploads' is not the current page.
     * <br><br>
     * Use of this selector is only supported in the context of a WebElement representing
     * an &lt;h3&gt; Collection Accordion.
     * <br><br>
     * e.g.&nbsp;{@code driver.findElement(}{@link #COLLECTION_ACCORDION}
     * {@code ).findElement(UPLOAD_GRADES_BUTTON_FOR_COLLECTION)}.
     */
    public static final By UPLOAD_GRADES_BUTTON_FOR_COLLECTION = By.xpath(".//../div/a");
    /** Identifies the Cancel button (&lt;a&gt; element) on a 'Create Collection' modal overlay.
     * <br><br>
     * Not supported when: the Create Collection modal is not displayed.
     */
    public static final By NEW_COLLECTION_MODAL_CANCEL =
            By.cssSelector("#cancelBar > .modalClose.button.cancel");
    /** Identifies the 'Create an empty collection' radio button (&lt;label&gt; element)
     * on a 'Create Collection' modal overlay. <br><br>
     * Not supported when: the Create Collection modal is not displayed.
     */
    public static final By EMPTY_NEW_COLLECTION_RADIO = By.cssSelector("[for='createEmptyBtn']");
    /** Identifies the 'Collection Name' field (&lt;input&gt; element)
     * on a 'Create Collection' modal overlay. <br><br>
     * Not supported when: the {@link #EMPTY_NEW_COLLECTION_RADIO} or COPY_COLLECTION_RADIO
     * option on a Create Collection modal is not displayed.
     */
    public static final By NEW_COLLECTION_NAME_FIELD = By.id("CollectionCreate_Coll_Name");
    /** Identifies the 'Date (dd/mm/yyyy)' field (&lt;input&gt; element)
     * on a 'Create Collection' modal overlay. <br><br>
     * Not supported when: the {@link #EMPTY_NEW_COLLECTION_RADIO} or COPY_COLLECTION_RADIO
     * option on a Create Collection modal is not displayed.
     */
    public static final By NEW_COLLECTION_DATE_FIELD = By.id("dateNew");
    /** Identifies the 'Create' or 'Copy' button (&lt;button&gt; element)
     * on a 'Create Collection' modal overlay. <br><br>
     * Not supported when: the {@link #EMPTY_NEW_COLLECTION_RADIO} or COPY_COLLECTION_RADIO
     * option on a Create Collection modal is not displayed.
     */
    public static final By NEW_COLLECTION_SUBMIT_BUTTON = By.cssSelector(".button.green");
    /** Identifies the 'Browse...' button (&lt;input&gt; element)
     * on an '[Upload File - Student Data|Uploads for [Exams|Assessments|Other Grades]]' page.
     * <br><br>
     * Not supported when: a file upload page is not displayed.
     */
    public static final By FILE_UPLOAD_CONTROL = By.cssSelector("input[name='file']");
    /** Identifies the 'File Title:' field (&lt;input&gt; element)
     * on an '[Upload File - Student Data|Uploads for [Exams|Assessments|Other Grades]]' page.
     * <br><br>
     * Not supported when: a file upload page is not displayed.
     */
    public static final By FILE_UPLOAD_TITLE = By.id("FileTitle");
    /** Identifies the 'Upload' button (&lt;button&gt; element)
     * on an '[Upload File - Student Data|Uploads for [Exams|Assessments|Other Grades]]' page.
     * <br><br>
     * Not supported when: a file upload page is not displayed.
     */
    public static final By FILE_UPLOAD_SUBMIT = By.id("btnUpload_a");
    /** Identifies any/all rows (&lt;row&gt; elements) on the
     * 'Confirm Upload Qualification Names' page. <br><br>
     * Not supported when: the 'Confirm Qualification Upload Names' page is not displayed.
     */
    public static final By CONFIRM_QUAL_NAMES_ROW = By.cssSelector(".fkfieldset>table>tbody>tr");
    /**
     * String constant to match against the page sub-title on the
     * 'Confirm Upload Qualification Names' page */
    public static final String CONFIRM_QUAL_NAMES_SUB_TITLE = "Confirm Upload Qualification Names";

    // Constructors
    public DataArea_UploadsTab(AnalyticsDriver aDriver) {
        super(aDriver);
    }
    public DataArea_UploadsTab(SISRATest aTest){super(aTest);}

    /***
     * Creates an empty Student Data Collection
     *
     * @param collDate The date for the Collection (must be unique in the cohort)
     * @param collName The name of the Collection to be created
     *
     * @return  {@code true} if the collection was created.<br>
     *          {@code false} if the collection could not be created.
     */
    public boolean createEmptyCollection(String collDate, String collName) {

        waitForClickabilityOf(CREATE_COLLECTION_LINK);

        // Check there isn't already a Collection for the specified date
        By collectionDateSpan = By.xpath("//h3/span[.='" + collDate + " ']");
        if (findAll(collectionDateSpan).size() > 0) {
            Assert.fail("A collection already exists with date '" + collDate + "'!");
        }

        // Click to create a collection & wait for the modal to appear
        click(CREATE_COLLECTION_LINK);
        waitForClickabilityOf(NEW_COLLECTION_MODAL_CANCEL);

        // If this is the first Collection for the cohort,
        // the "Create empty..."/"Copy existing..." part of the modal will not be shown.
        List<WebElement> radios = findAll(EMPTY_NEW_COLLECTION_RADIO);
        if(radios.size() > 0){
            radios.get(0).click();
        }

        // Enter the Collection name & date
        waitForClickabilityOf(NEW_COLLECTION_NAME_FIELD);
        clear(NEW_COLLECTION_NAME_FIELD);
        type(collName, NEW_COLLECTION_NAME_FIELD);

        waitForClickabilityOf(NEW_COLLECTION_DATE_FIELD);
        clear(NEW_COLLECTION_DATE_FIELD);
        type(collDate, NEW_COLLECTION_DATE_FIELD);

        // Create the Collection
        submit(NEW_COLLECTION_SUBMIT_BUTTON);
        try {
            waitForClickabilityOf(collectionDateSpan);
        } catch (Exception e) {
            Assert.fail("Failed to create the collection for date '" + collDate + "'!");
        }
        return true;
    }

    /***
     * From the Student Data Uploads page, uploads the given Student Data file
     *
     * @param stuFile A File object pointing to a Student Data file
     * @param collName A String identifying a unique Collection
     * @return  {@code true} if the file was uploaded.<br>
     *          {@code false} otherwise.
     */
    public boolean uploadStudentFile(File stuFile, String collName) {

        // Ensure the Collection we want to add student data to is:
        //  - Present
        //  - Expanded
        WebElement coll = getExpandedCollection(collName);

        // Click to "Upload Student Data" for the Collection identified by collDate
        click(coll.findElement(UPLOAD_STUDENTS_BUTTON_FOR_COLLECTION));
        setUploadFileFields(stuFile);

        // TODO - Check the upload summary is clean?

        // Complete the upload
        waitForClickabilityOf(FILE_UPLOAD_SUBMIT).click();
        return true;
    }

    /***
     * From the Assessment Grades Uploads page, uploads the given grades file
     *
     * @param gradeFile A File object pointing to a Grades file
     * @param collName A String identifying a unique Collection
     * @return  {@code true} if the file was uploaded.<br>
     *          {@code false} otherwise.
     */
    public boolean uploadGradesFile(File gradeFile, String collName) {

        // Ensure the Collection we want to add grade data to is:
        //  - Present
        //  - Expanded
        WebElement coll = getExpandedCollection(collName);

        // Click to "Upload Grade File" for the Collection identified by testID
        coll.findElement(UPLOAD_GRADES_BUTTON_FOR_COLLECTION).click();

        // Put the gradeFile into the file upload field
        setUploadFileFields(gradeFile);

        // Complete the upload
        waitForClickabilityOf(FILE_UPLOAD_SUBMIT).click();

        // We may have qualification names to match?
        String menuSub = waitForClickabilityOf(PAGE_SUB_TITLE).getText();
        if (menuSub.equals(CONFIRM_QUAL_NAMES_SUB_TITLE)) {
            matchQualNames();
        }

        return true;
    }

    /***
     * Matches uploaded to existing qualifications as best it can
     * If no match is found, the qualification is marked as "New"
     */
    private void matchQualNames() {
        List<WebElement> qualRows = findAll(CONFIRM_QUAL_NAMES_ROW);
        for (int i = 1; i < qualRows.size(); i++) {
            WebElement tableCell = qualRows.get(i).findElement(By.tagName("td"));
            String uploadName = tableCell.getText().trim();
            String optValue = "New";
            WebElement qualSelect = tableCell.findElement(By.tagName("select"));
            List<WebElement> options = qualSelect.findElements(By.tagName("option"));
            for (WebElement option : options) {
                if (option.getText().contains(uploadName)) {
                    optValue = option.getText();
                    break;
                }
            }
            new Select(qualSelect).selectByVisibleText(optValue);
        }
        find(FILE_UPLOAD_SUBMIT).click();
    }

    /***
     * Searches through the Collection accordions on the current page
     *
     * @return A WebElement object representing the Collection with a name matching the collName parameter
     */
    private WebElement getExpandedCollection(String collName) {
        // Find the Collection created by a previous test method
        List<WebElement> collections = findAll(COLLECTION_ACCORDION);
        WebElement coll = null;
        for (WebElement element : collections) {
            String nameText = element.getText().trim();
            if (nameText.startsWith(collName+" - No Files") ) {
                coll = element;
                break;
            }
        }

        // If the Collection can't be found the test has FALED!
        Assert.assertNotNull(coll, "Collection '" + collName + "' could not be found!");

        if (!coll.findElement(UPLOAD_GRADES_BUTTON_FOR_COLLECTION).isDisplayed()) {
            coll.click();
        }
        return coll;
    }

    /***
     * @param file A file to be uploaded
     */
    private void setUploadFileFields(File file) {
        // Put the stuFile into the file upload field
        waitForClickabilityOf(FILE_UPLOAD_CONTROL);
        clear(FILE_UPLOAD_CONTROL);
        type(file.getAbsolutePath(), FILE_UPLOAD_CONTROL);

        // Enter the testID as the File Title
        waitForClickabilityOf(FILE_UPLOAD_TITLE);
        clear(FILE_UPLOAD_TITLE);
        type(file.getName().replace(".csv",""), FILE_UPLOAD_TITLE);

        // Submit the file upload form
        submit(FILE_UPLOAD_TITLE);
    }

}

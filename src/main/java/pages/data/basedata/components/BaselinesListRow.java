package pages.data.basedata.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * A component object representing the content and actions within one row of the
 * Uploaded KS2/Baselines table on /EAPAdmin/Progress/GradeManagement/EAPBaseline
 */
public class BaselinesListRow extends AnalyticsComponent{

    private List<WebElement> searchElements;
    private WebElement nameField;
    private WebElement gradeTypeField;
    private WebElement coreNomField;

    private boolean editSubjectsMode;
    private int rowIndex;

    public final By SUBJECT_NAMES_READ = By.cssSelector("fieldset:nth-of-type(1) td:nth-of-type(1)");
    public final By SUBJECT_NAMES_EDIT = By.cssSelector("td>input[type='text']");
    public final By SUBJECT_GRADE_COUNTS_READ = By.cssSelector("fieldset:nth-of-type(1) td:nth-of-type(2)");
    public final By SUBJECT_GRADE_COUNTS_EDIT = By.cssSelector("form td:nth-of-type(2)");
    public final By SUBJECT_GRADE_TYPES_READ = By.cssSelector("fieldset:nth-of-type(1) td:nth-of-type(3)");
    public final By SUBJECT_GRADE_TYPES_EDIT = By.cssSelector("td:nth-of-type(3)>select");

    public final By SUBJECT_CORE_NOMS_READ = By.cssSelector("fieldset:nth-of-type(1) td:nth-of-type(5)");
    public final By SUBJECT_CORE_NOMS_EDIT = By.cssSelector("td:nth-of-type(5)>select");
/*
    public final By SUBJECT_CORE_NOMS_READ = By.cssSelector("fieldset:nth-of-type(1) td:nth-of-type(4)");
    public final By SUBJECT_CORE_NOMS_EDIT = By.cssSelector("td:nth-of-type(4)>select");
*/

    public final By SUBJECT_GRADE_LIST_LINK_RELATIVE = By.tagName("a");
    public final By SUBJECT_DELETE_LINK_RELATIVE = By.tagName("label");

    /**
     * Creates a Component object for the row of the Uploaded KS2/Baseline table identified by the subjectName param.
     * @param aDriver       Browser should be on the EAP Baseline Management page
     * @param subjectName   Used to identify the row which should be represented by this object
     */
    public BaselinesListRow(RemoteWebDriver aDriver, String subjectName) {
        super(aDriver);

        searchElements = driver.findElements(SUBJECT_NAMES_EDIT);
        editSubjectsMode = true;

        if (searchElements.size()==0){
            // Not in Edit mode
            searchElements = driver.findElements(SUBJECT_NAMES_READ);
            editSubjectsMode = false;
        }

        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(searchElements.get(0)));

        init(subjectName);
    }

    /**
     * Changes the value of the text field in the Subject column. Only valid when the table is in edit mode
     * @param newName   The text to overwrite the existing subject name with
     */
    public void updateSubjectName(String newName){
        if (editSubjectsMode == false){
            // Todo - Throw an IllegalStateException if Edit methods are called while in Read mode
        }
        nameField.clear();
        nameField.sendKeys(newName);
    }

    /**
     * Changes the value of the select field in the Grade Type column. Only valid when the table is in edit mode
     * @param newType   The visible text of the option to choose from the drop down list
     */
    public void updateGradeType(String newType){
        if (editSubjectsMode == false){
            // Todo - Throw an IllegalStateException if Edit methods are called while in Read mode
        }
        new Select(gradeTypeField).selectByVisibleText(newType);
    }
    /**
     * Changes the value of the select field in the Core column. Only valid when the table is in edit mode
     * @param newNomination   The visible text of the option to choose from the drop down list
     */
    public void updateCoreNom(String newNomination){
        if (editSubjectsMode == false){
            // Todo - Throw an IllegalStateException if Edit methods are called while in Read mode
        }
        new Select(coreNomField).selectByVisibleText(newNomination);
    }

    /**
     * Todo - Yet to be implemented
     */
    public void delete(){
    }

    /**
     * Todo - Yet to be implemented
     * Todo - this method should probably return a BaselineGradeList page object
     */
    public void drillDownToGradeList(){
    }

    /**
     * Changes the row the object represents to be the one on which the given subject is found
     * @param subjectName   Used to identify the row to switch to
     */
    public void switchRow(String subjectName){
        init(subjectName);
    }

    /**
     * Find the row containing the named subject and set the object's fields to point to
     * the correct WebElements in that row
     * @param subjectName   Used to identify the row which should be represented by this object
     */
    private void init(String subjectName){

        rowIndex = -1;

        for (WebElement element : searchElements) {
            rowIndex++;

            if (editSubjectsMode) {
                if (element.getAttribute("value").equals(subjectName)) {
                    break;
                }
            } else {
                if (element.getText().trim().equals(subjectName)){
                    break;
                }
            }
        }
        // If rowIndex is still -1, we didn't find a match for the keyValue
        if (rowIndex == -1) {
            throw new IllegalArgumentException("The value '" + subjectName + "' was not found in the table");
        } else {
            nameField = searchElements.get(rowIndex);
            gradeTypeField = driver.findElements(SUBJECT_GRADE_TYPES_EDIT).get(rowIndex);
            coreNomField = driver.findElements(SUBJECT_CORE_NOMS_EDIT).get(rowIndex);
        }
    }

}

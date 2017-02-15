package analytics.pages.data.students;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.students.components.ActiveStudentListRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents the contents and interactive elements on the Students list page in EAP mode
 */
public class StudentsList extends AnalyticsPage {

// FIELDS
    protected static final String PAGE_PATH = "/EAPAdmin/Students";
    protected static final By CREATE_STUDENT = By.linkText("Create Student");
    protected static final By SEARCH_STUDENT_NAME = By.id("SrchStuName");
    protected static final By SEARCH_STUDENT_STATUS = By.id("SrchStat_ID");
    protected static final By SEARCH_STUDENT_RESET = By.linkText("Reset");
    protected static final By SEARCH_STUDENT_APPLY = By.linkText("Apply");
    protected static final By SEARCH_STUDENT_FIELD_LABELS = By.cssSelector(".tblcl .editor-label");
    protected static final By INACTIVE_STUDENTS = By.cssSelector(".fieldset.ftRed");
    protected static final By INACTIVE_STUDENTS_LEGEND = By.cssSelector(".fieldset.ftRed>.legend");
    protected static final By INACTIVE_STUDENTS_SUMMARY_TEXT = By.cssSelector(".fieldset.ftRed>.st");
    protected static final By INACTIVE_STUDENTS_SELECT_ALL = By.cssSelector(".fieldset.ftRed .checkAll");
    protected static final By INACTIVE_STUDENTS_SELECT_NONE = By.cssSelector(".fieldset.ftRed .checkClear");
    protected static final By INACTIVE_STUDENTS_CHANGE_STATUS = By.id("btnChgStatusInactive");
    protected static final By INACTIVE_STUDENTS_CHANGE_DELETE = By.cssSelector("deleteInactive");
    protected static final By INACTIVE_STUDENTS_STU_ID_CELLS = By.cssSelector(".fieldset.ftRed td:nth-of-type(1)");
    protected static final By INACTIVE_STUDENTS_STU_NAME_CELLS = By.cssSelector(".fieldset.ftRed td:nth-of-type(2)");
    protected static final By INACTIVE_STUDENTS_STU_ACTIONS_CELLS = By.cssSelector(".fieldset.ftRed td:nth-of-type(4)");

// CONSTRUCTOR
    public StudentsList(AnalyticsDriver aDriver){
        super(aDriver);
    }
    public StudentsList(AnalyticsDriver aDriver, boolean loadByUrl){
        super(aDriver);
        if (loadByUrl){
            driver.get(driver.getDomainRoot()+PAGE_PATH);
        }
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public ActiveStudentListRow getStudentRowByID(String type, String StuID){
        return new ActiveStudentListRow(driver, "");
    }
    public ActiveStudentListRow getStudentRowByID(String stuID){
        return new ActiveStudentListRow(driver, "");
    }
    public ActiveStudentListRow getStudentRowByName(String type, String name){
        return new ActiveStudentListRow(driver, "");
    }
    public ActiveStudentListRow getStudentRowByName(String name){
        return new ActiveStudentListRow(driver, "");
    }
    public ActiveStudentListRow getFirstStudentRow(String type){
        return new ActiveStudentListRow(driver, "");
    }

    //  - ACCESSORS FOR SPECIFIC INFORMATION
    public boolean isDisabled(WebElement element){
        return false;
    }

    // ACTIONS TO AFFECT THE STATE
    public void clickCreateStudentButton(){

    }
    public void searchForStudents(String stuName, String status){

    }
    public void selectAllStudents(String type){

    }
    public void selectNone(String type){

    }
    public void clickChangeStatus(String type){

    }
    public void clickDelete(){

    }

    // INTERNAL HELPER METHODS
    private boolean isValidListType(String type){
        return false;
    }
}

package pages.data.students.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Represents the contents and interactive elements of the table containing Active Students on the EAP students List page
 */
public class ActiveStudentsTable extends AnalyticsComponent {

// FIELDS
    protected final By LEGEND = By.cssSelector(".fieldset.ftGreen>.legend");
    protected final By SUMMARY_TEXT = By.cssSelector(".fieldset.ftGreen>.st");
    protected final By SELECT_ALL = By.cssSelector(".fieldset.ftGreen .checkAll");
    protected final By SELECT_NONE = By.cssSelector(".fieldset.ftGreen .checkClear");
    protected final By CHANGE_STATUS = By.id("btnChgStatusActive");
    protected final By STUDENT_ID_CELLS = By.cssSelector(".fieldset.ftGreen td:nth-of-type(1)");

    public ActiveStudentsTable(RemoteWebDriver aDriver) {
        super(aDriver);
    }

    public String getLegendText(){
        return driver.findElement(LEGEND).getText().trim();
    }
    public String getSummaryText(){
        return driver.findElement(SUMMARY_TEXT).getText().trim();
    }
    public WebElement getSelectAllButton(){
        return driver.findElement(SELECT_ALL);
    }
    public WebElement getSelectNoneButton(){
        return driver.findElement(SELECT_NONE);
    }
    public WebElement getChangeStatusButton(){
        return driver.findElement(CHANGE_STATUS);
    }
    public ActiveStudentListRow getFirstStudentRow(){
        return new ActiveStudentListRow(driver, "First:[first]");
    }
    public ActiveStudentListRow getFirstStudentRowOfStatus(String status){
        return new ActiveStudentListRow(driver, "First:["+status+"]");
    }
    public ActiveStudentListRow getRandomStudentRow(){
        return new ActiveStudentListRow(driver, "Random:[random]");
    }
    public ActiveStudentListRow getRandomStudentRowOfStatus(String status){
        return new ActiveStudentListRow(driver, "Random:["+status+"]");
    }
    public ActiveStudentListRow getStudentRowByID(String studentID){
        return new ActiveStudentListRow(driver, "ID:["+studentID+"]");
    }
    public ActiveStudentListRow getStudentRowByName(String studentName){
        return new ActiveStudentListRow(driver, "Name:["+studentName+"]");
    }

    public int getRowCount(){
        return driver.findElements(STUDENT_ID_CELLS).size();
    }


}

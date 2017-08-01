package pages.data.students.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the contents and interactive elements of a single row of the Active Students table on the EAP Students List page
 */
public class ActiveStudentListRow extends AnalyticsComponent{

// FIELDS
    private List<WebElement> searchCells;
    private int rowIndex = -1;
    private final By STUDENT_ID_CELLS = By.cssSelector(".fieldset.ftGreen td:nth-of-type(1)");
    private final By STUDENT_NAME_CELLS = By.cssSelector(".fieldset.ftGreen td:nth-of-type(2)");
    private final By STUDENT_STATUS_CELLS = By.cssSelector(".fieldset.ftGreen td:nth-of-type(3)");
    private final By STUDENT_ACTIONS_CELLS = By.cssSelector(".fieldset.ftGreen td:nth-of-type(4)");
    private final By STUDENT_RECORD_LINK_RELATIVE = By.tagName("a");
    private final By CHECK_BOX_LABEL_RELATIVE = By.tagName("label");

    public ActiveStudentListRow(RemoteWebDriver aDriver, String key) {
        super(aDriver);

        String keyValue = splitKey(key);

        // Only need to search if we haven't already got a valid row index
        if (rowIndex == -1) {
            for (WebElement cell : searchCells) {
                rowIndex++;
                if (cell.getText().trim().equals(keyValue)) {
                    break;
                }
            }
            // If rowIndex is still -1, we didn't find a match for the keyValue
            if (rowIndex == -1) {
                throw new IllegalArgumentException("The value '" + keyValue + "' was not found in the table");
            }
        }
    }

    public WebElement getCellContainingID(){
        return driver.findElements(STUDENT_ID_CELLS).get(rowIndex);
    }
    public WebElement getCellContainingName(){
        return driver.findElements(STUDENT_NAME_CELLS).get(rowIndex);
    }
    public WebElement getLinkToStudentRecord(){
        return getCellContainingName().findElement(STUDENT_RECORD_LINK_RELATIVE);
    }
    public WebElement getCellContainingStatus(){
        return driver.findElements(STUDENT_STATUS_CELLS).get(rowIndex);
    }
    public WebElement getCellContainingActions(){
        return driver.findElements(STUDENT_ACTIONS_CELLS).get(rowIndex);
    }
    public WebElement getSelectCheckBox(){
        return getCellContainingActions().findElement(CHECK_BOX_LABEL_RELATIVE);
    }

    public String getStudentIDText(){
        return getCellContainingID().getText().trim();
    }
    public String getStudentNameText(){
        return getCellContainingName().getText().trim();
    }
    public String getStatusText(){
        return getCellContainingStatus().getText().trim();
    }
    public boolean isCheckBoxDisplayed(){
        return getSelectCheckBox().isDisplayed();
    }

    public void drillDown(){
        getLinkToStudentRecord().click();
    }
    public void select(){
        getSelectCheckBox().click();
    }

    private String splitKey(String key){

        int sep = key.indexOf(":[");
        String keyType = key.substring(0, sep - 1);
        String keyValue = key.substring(sep + 2, key.length() - 1);

        searchCells = driver.findElements(STUDENT_ID_CELLS);

        switch (keyType){
            case "First":
                rowIndex = 0;
                break;
            case "ID":
                break;
            case "Name":
                searchCells = driver.findElements(STUDENT_NAME_CELLS);
                break;
            case "Random":
                rowIndex = ThreadLocalRandom.current().nextInt(0, searchCells.size());
                break;
            default:
                throw new IllegalArgumentException("Unknown key type found: '" + keyType + "'");
        }

        return keyValue;
    }

}

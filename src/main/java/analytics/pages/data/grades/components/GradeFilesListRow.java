package analytics.pages.data.grades.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * A component object representing the content and actions within one row of an Uploads For [DatasetName] page
 */
public class GradeFilesListRow extends AnalyticsComponent{

    private int rowIndex;
    private final By FILE_TITLE_CELLS = By.cssSelector("td:nth-of-type(4)");
    private final By EDIT_ACTION_ICON = By.className("im_Edit");
    private final By VIEW_ACTION_ICON = By.className("im_View");
    private final By DELETE_ACTION_ICON = By.className("im_Delete");

    /**
     * Creates a Component object for a row of a [DatasetName] Uploads table identified by the fileTitle param.
     * @param aDriver       Browser should be on an Uploads for [DatasetName] page
     * @param fileTitle     Used to identify the row which should be represented by this object
     */
    public GradeFilesListRow(AnalyticsDriver aDriver, String fileTitle) throws IllegalArgumentException {
        super(aDriver);
        initByTitle(fileTitle);
    }

    /**
     * Creates a Component object for a row of a [DatasetName] Uploads table identified by the fileIndex param.
     * @param aDriver       Browser should be on an Uploads for [DatasetName] page
     * @param fileIndex     Used to identify the row which should be represented by this object
     */
    public GradeFilesListRow(AnalyticsDriver aDriver, int fileIndex) throws IllegalArgumentException {
        super(aDriver);
        initByIndex(fileIndex);
    }

    public GradeFilesListRow clickEditIcon(){
        tableRow.findElement(EDIT_ACTION_ICON).click();
        return this;
    }
    public GradeFilesListRow clickViewIcon(){
        tableRow.findElement(VIEW_ACTION_ICON).click();
        return this;
    }
    public GradeFilesListRow clickDeleteIcon(){
        tableRow.findElement(DELETE_ACTION_ICON).click();
        return this;
    }

    // Initialisation code abstracted out in case we want to add a 'switchRow' method
    private void initByTitle(String fileTitle) throws IllegalArgumentException {
        rowIndex = -1;
        int rowCssIndex = -1;

        List<WebElement> titleCells = driver.findElements(FILE_TITLE_CELLS);
        for(WebElement titleCell : titleCells){
            rowIndex++;
            if(titleCell.getText().equals(fileTitle)){
                rowCssIndex = rowIndex+2;
                break;
            }
        }
        if(rowIndex == -1){
            // The specified file title was not found
            // Todo - deal with this properly, for now just throw an IllegalArgumentException
            throw new IllegalArgumentException("The specified file title was not found in the table ("+fileTitle+")");
        }

        tableRow = driver.findElement(By.cssSelector("#fm_edtGradeFile tr:nth-of-type("+rowCssIndex+")"));
    }

    private void initByIndex(int fileIndex){
        rowIndex = fileIndex - 1;
        int rowCssIndex = fileIndex + 1;
        tableRow = driver.findElement(By.cssSelector("#fm_edtGradeFile tr:nth-of-type("+rowCssIndex+")"));
    }

}

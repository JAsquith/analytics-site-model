package pages.data.grades.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * A component object representing the content and actions within one row of the Publish Assessments table
 */
public class PublishAssessmentsYearRow extends AnalyticsComponent{

    private int cssRowIndex;
    public final By PUBLISH_BUTTON = By.linkText("Publish");
    public final By TERM_NAME_CELLS = By.cssSelector(".pubAdminTable tr td[rowspan='4']");

    /**
     * Creates a Component object for a row of the Publish Assessments table identified by the term and slot params.
     * @param aDriver   Browser should be on the Publish Grades page
     * @param term      1, 2, or 3
     * @param slot      1 or 2
     */
    public PublishAssessmentsYearRow(RemoteWebDriver aDriver, int term, int slot){
        super(aDriver);
        initByTermAndSlot(term, slot);
    }

    // Initialisation code abstracted out in case we want to add a 'switchRow' method
    private void initByTermAndSlot(int term, int slot) {

        List<WebElement> nameCells = driver.findElements(TERM_NAME_CELLS);
        int termIndex = 0;
        for(WebElement cell : nameCells){
            termIndex++;
            if (cell.getText().endsWith("Term "+term)){
                break;
            }
        }
        if(termIndex==0){
            throw new IllegalArgumentException("A PublishAssessmentYearRow for term " + term + "was not found");
        }
        /*
        t = 1, s = 1 -> row 3
        t = 1, s = 2 -> row 5
        t = 2, s = 1 -> row 7
        t = 2, s = 2 -> row 9
        t = 3, s = 1 -> row 11
        t = 3, s = 2 -> row 13
        => row = t*4 + (((s-1)*2) - 1)
         */

        int rowIndex = (term * 4) + (((slot-1)*2)-1);
        initByIndex(rowIndex);
    }

    public PublishGradesModal publish(){
        tableRow.findElement(PUBLISH_BUTTON).click();
        waitForLoadingWrapper();
        return new PublishGradesModal(driver);
    }

    private void initByIndex(int rowIndex){
        cssRowIndex = rowIndex;
        tableRow = driver.findElement(By.cssSelector(".pubAdminTable tr:nth-of-type(" + cssRowIndex + ")"));
    }

}

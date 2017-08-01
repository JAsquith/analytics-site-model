package pages.data.basedata.components;

import pages.AnalyticsComponent;
import pages.data.basedata.EAPDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * A component object representing the content and actions within one row of the
 * EAPs table on the EAP List page (/EAPAdmin/EAP)
 */
public class EAPsListRow extends AnalyticsComponent{

    private boolean rowEditMode;

    private final By EAP_NAME_CELLS = By.cssSelector(".fieldset td:nth-of-type(1)");
    private WebElement eapRow;

    private final By EAP_NAME_CELL = By.cssSelector("td:nth-of-type(1)");
    private final By EAP_NAME_LINK = By.cssSelector("td:nth-of-type(1)>a");
    private final By BASELINE_TYPE_CELL = By.cssSelector("td:nth-of-type(2)");
    private final By KS2_BASELINE_CELL = By.cssSelector("td:nth-of-type(3)");
    private final By IN_USE_CELL = By.cssSelector("td:nth-of-type(4)");

    // Action button locators - un-comment when implementing accessor methods
    // Read mode actions:
    //private final By EDIT_BUTTON = By.cssSelector("a.im_Edit");
    //private final By DELETE_BUTTON = By.cssSelector("a.im_Delete");
    //private final By NO_DELETE_BUTTON = By.cssSelector("span.im_noDelete");
    //private final By DELETE_OR_NO_DELETE_ICON = By.cssSelector(".btnImg16:nth-child(2)");
    // Edit mode actions:
    //private final By SAVE_BUTTON = By.cssSelector(".button.small.green");
    //private final By CANCEL_BUTTON = By.linkText("Cancel");

    /**
     * Creates a Component object for the row of the EAPs List table identified by the eapName param.
     * @param aDriver   Browser should be on the EAP List page
     * @param eapName   Used to identify the row which should be represented by this object
     */
    public EAPsListRow(RemoteWebDriver aDriver, String eapName) {
        super(aDriver);

        int rowIndex = 0;
        List<WebElement> nameCells = driver.findElements(EAP_NAME_CELLS);
        for(WebElement nameCell : nameCells){
            rowIndex++;
            if (nameCell.getText().equals(eapName)){
                eapRow = driver.findElement(By.cssSelector(".fieldset tr:nth-of-type("+rowIndex+")"));
                // Todo - initialise the rowEditMode object field
                return;
            }
        }
        // if we get this far without setting the eapRow field and returning, then the eapName was not found
        // throw an IllegalArgumentException?
    }

    /**
     * Yet to be implemented
     * @return boolean - {@code true} if the EAP is currently being edited; @code{false} if not
     */
    public boolean isInEditMode(){
        return false;
    }

    /**
     * Yet to be implemented
     * @param newEapName    The new value for the NAME field
     */
    public void setEapName(String newEapName){
    }

    /**
     * Returns the text in the EAP Name column of this row
     * @return String - The text in the EAP Name column
     */
    public String getEapName(){
        return eapRow.findElement(EAP_NAME_CELL).getText();
    }

    /**
     * Returns the text in the Baseline Type column of this row
     * @return String - The text in the Baseline Type column
     */
    public String getBaselineType(){
        return eapRow.findElement(BASELINE_TYPE_CELL).getText();
    }

    /**
     * Returns the text in the KS2 Baseline column of this row
     * @return String - the text in the KS2 Baseline column
     */
    public String getKS2Baseline(){
        return eapRow.findElement(KS2_BASELINE_CELL).getText();
    }

    /**
     * Returns the text in the In Use column of this row
     * @return String - the text in the In Use column
     */
    public String getInUse(){
        return eapRow.findElement(IN_USE_CELL).getText();
    }

    /**
     * Yet to be implemented
     */
    public void delete(){
    }

    /**
     * Yet to be implemented
     */
    public void edit(){
    }

    /**
     * Yet to be implemented
     */
    public void saveChanges(){
    }

    /**
     * Yet to be implemented
     */
    public void cancelChanges(){
    }

    /**
     * Clicks the link in the EAP Name column
     * @return  An {@link EAPDetail} page object representing the EAP Detail page that has been opened
     */
    public EAPDetail drillDownToEAP(){
        eapRow.findElement(EAP_NAME_LINK).click();
        return new EAPDetail(driver);
    }

    /**
     * Yet to be implemented
     * @param eapName   Identifies the row to switch to
     * @return          This object (so dot notation can be daisy-chained)
     */
    public EAPsListRow switchRow(String eapName){
    //    init(subjectName);
        return this;
    }

}

package pages.data.students.components;

import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Represents the elements and actions on the Class Assignment modal accessed via the
 * Students - Classes - Assign Quals list page
 */
public class ClassAssignmentsModal extends AnalyticsComponent {

    private final By COMPONENT = By.id("fm_EditClassAssignments");
    private final By QUAL_CHECKBOX_LABELS = By.cssSelector(".inp_50pc>label");
    private final By CANCEL_BUTTON = By.cssSelector(".modalClose.cancel");
    private final By SAVE_BUTTON = By.cssSelector(".modalSubmit>button.green");

    /**
     * Creates a page component object for the Assign Classes modal; waits for the modal to be clickable
     * @param aDriver   The browser should be on the Classes - Assign Quals page and the modal should be displayed
     */
    public ClassAssignmentsModal(RemoteWebDriver aDriver) {
        super(aDriver);
        waitForComponent(1);
    }

    /**
     * Ensures a tick is placed in the check box for the given qualification name
     * @param qualName  The text of the label next to the check box to tick
     */
    public void checkQualificationByName(String qualName){
        setQualificationByName(qualName, 1);
    }

    /**
     * Ensures no tick is present in the check box for the given qualification name
     * @param qualName  The text of the label next to the check box to untick
     */
    public void uncheckQualificationByName(String qualName){
        setQualificationByName(qualName, -1);
    }

    /**
     * Toggles the check box for the given qualification name (i.e. clicks the label)
     * @param qualName  The text of the label next to the check box to toggle
     */
    public void toggleQualificationByName(String qualName){
        setQualificationByName(qualName, 0);
    }

    /**
     * Clicks the cancel button; waits for the modal to be hidden
     */
    public void cancel(){
        driver.findElement(CANCEL_BUTTON).click();
        waitForComponent(0);
    }

    /**
     * Clicks the Save button; waits for the modal to be hidden
     */
    public void save(){
        driver.findElement(SAVE_BUTTON).click();
        waitForComponent(0);
    }

    /**
     * Private method allowing the check, uncheck and toggle methods to re-use the code that actually does the work
     * @param qualName      The name of a qualification listed on the Assign Quals modal
     * @param actionID     {@code -1} = uncheck; {@code 0} = toggle; {@code 1} = check;
     */
    private void setQualificationByName(String qualName, int actionID){
        WebElement label = getCheckBoxLabelByQualName(qualName);
        if (label == null){
            // todo - throw an IllegalArgumentException, or make the method return a boolean indicating success?
            return;
        }
        if (actionID == 0){
            label.click();
            return;
        }

        String checkBoxID = label.getAttribute("for");
        WebElement checkBox = driver.findElement(By.id(checkBoxID));
        switch (actionID) {
            case -1:
                if (checkBox.isSelected()) {
                    label.click();
                }
                return;
            case 1:
                if (checkBox.isSelected() == false) {
                    label.click();
                }
                return;
            default:
                // todo - handle invalid action parameter
        }
    }

    /**
     * Private method to find the label element for a particular qualification
     * @param qualName  The name of a qualification listed on the Assign Quals modal
     * @return          A WebElement object representing the label or {@code null} if no match is found
     */
    private WebElement getCheckBoxLabelByQualName(String qualName){
        List<WebElement> checkBoxLabels = driver.findElements(QUAL_CHECKBOX_LABELS);
        for(WebElement label : checkBoxLabels){
            if (label.getText().equals(qualName)){
                return label;
            }
        }
        return null;
    }

    /**
     *
     * @param statusID  {@code 0} = isHidden; {@code 1} = isClickable;
     */
    private void waitForComponent(int statusID){
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        switch (statusID){
            case 0:
                driverWait.until(ExpectedConditions.invisibilityOfElementLocated(COMPONENT));
                return;
            case 1:
                driverWait.until(ExpectedConditions.elementToBeClickable(COMPONENT));
                return;
            default:
                // todo - deal with invalid statusID
        }
    }
}

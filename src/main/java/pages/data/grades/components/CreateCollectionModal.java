package pages.data.grades.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.data.grades.UploadsForAssessments;


/**
 * A component object representing the content and actions within a Create Collection modal
 */
public class CreateCollectionModal extends AnalyticsComponent{

    private final By MODAL_WINDOW = By.className("window");

    private final By COLL_NAME_FIELD = By.id("CollectionCreate_Coll_Name");
    private final By COLL_DATE_FIELD = By.id("dateNew");
    private final By COLL_TERM_SELECT = By.id("CollectionCreate_EAPTerm_ID");
    private final By COLL_DESC_FIELD = By.id("CollectionCreate_Coll_Desc");

    private final By CANCEL_BUTTON = By.cssSelector(".modalSubmit>.cancelChanges");
    private final By CREATE_BUTTON = By.cssSelector(".modalSubmit>.green");

    /**
     * Creates a Component object for the Create Collection modal and waits for the modal window to be clickable.
     * @param aDriver   Browser should be on the Uploads For Assessment page and
     *                  the Create New Collection button should have been clicked
     */
    public CreateCollectionModal(RemoteWebDriver aDriver) {
        super(aDriver);
        initModal(MODAL_WINDOW);
    }

    /**
     * Overwrites the value in the Name field
     * @param text  The new value for the field
     * @return  The component object (to facilitate method daisy-chaining)
     */
    public CreateCollectionModal setName(String text){
        WebElement field = modal.findElement(COLL_NAME_FIELD);
        field.clear();
        field.sendKeys(text);
        return this;
    }
    /**
     * Overwrites the value in the Date field
     * @param text  The new value for the field
     * @return  The component object (to facilitate method daisy-chaining)
     */
    public CreateCollectionModal setDate(String text){
        WebElement field = modal.findElement(COLL_DATE_FIELD);
        field.clear();
        field.sendKeys(text);
        modal.findElement(COLL_NAME_FIELD).click();
        return this;
    }
    /**
     * Overwrites the value in the Description field
     * @param text  The new value for the field
     * @return  The component object (to facilitate method daisy-chaining)
     */
    public CreateCollectionModal setDescription(String text){
        WebElement field = modal.findElement(COLL_DESC_FIELD);
        field.clear();
        field.sendKeys(text);
        return this;
    }
    /**
     * Chooses an option from the Term drop down list
     * @param text  The visible text of the option to choose
     * @return  The component object (to facilitate method daisy-chaining)
     */
    public CreateCollectionModal selectTerm(String text){
        new Select(modal.findElement(COLL_TERM_SELECT)).selectByVisibleText(text);
        return this;
    }

    /**
     * Clicks the Cancel button
     */
    public void clickCancel(){
        modal.findElement(CANCEL_BUTTON);
        waitForLoadingWrapper();
    }

    /**
     * Clicks the Create button
     * @return  A refreshed {@link UploadsForAssessments} page object
     *          (which should contain an accordion for the new collection)
     */
    public void clickCreate(){
        modal.findElement(CREATE_BUTTON).click();
        waitForLoadingWrapper();
        new UploadsForAssessments(driver);
    }

}

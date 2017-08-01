package pages.data.students;


import pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Allows interaction with the Student Data Import page in EAP Mode
 */
public class StudentImportComplete extends AnalyticsPage {

    // Locators for interactive elements within the component

    protected final By UPDATE_CLASSES_YES = By.cssSelector("label[for='crtA']");
    protected final By UPDATE_CLASSES_NO = By.cssSelector("label[for='crtB']");
    protected final By UPDATE_FILTERS_YES = By.cssSelector("label[for='crtC']");
    protected final By UPDATE_FILTERS_NO = By.cssSelector("label[for='crtD']");
    protected final By UPDATE_STATUSES_CB = By.id("mostRecent");
    protected final By UPDATE_STATUSES = By.cssSelector("label[for='mostRecent']");
    protected final By CANCEL_IMPORT_BUTTON = By.id("btnDelete_a");
    protected final By COMPLETE_IMPORT_BUTTON = By.id("btnImport_a");

    // Locators for static page content
    protected final By IMPORTING_MESSAGE = By.id("workingMessage");
    protected final By FIELD_LABELS = By.cssSelector(".editor-label");

    // CONSTRUCTOR

    /**
     *
     * @param aDriver   The browser should be showing the Student File Import page
     */
    public StudentImportComplete(RemoteWebDriver aDriver){
        super(aDriver);
        waitLong.until(ExpectedConditions.invisibilityOfElementLocated(IMPORTING_MESSAGE));
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(COMPLETE_IMPORT_BUTTON));
    }

    // ACTIONS (UPDATING CURRENT PAGE OR NAVIGATING TO A NEW PAGE)
    public void setUpdateClassesRadio(boolean updateClasses){
        if (updateClasses){
            driver.findElement(UPDATE_CLASSES_YES).click();
        } else {
            driver.findElement(UPDATE_CLASSES_NO).click();
        }
    }
    public void setUpdateClassesRadio(String updateClasses){
        if (updateClasses.equals("Yes")){
            driver.findElement(UPDATE_CLASSES_YES).click();
        } else {
            driver.findElement(UPDATE_CLASSES_NO).click();
        }
    }
    public void setUpdateFiltersRadio(boolean importFilters){
        if (importFilters){
            driver.findElement(UPDATE_FILTERS_YES).click();
        } else {
            driver.findElement(UPDATE_FILTERS_NO).click();
        }
    }
    public void setUpdateFiltersRadio(String importFilters){
        if (importFilters.equals("Yes")){
            driver.findElement(UPDATE_FILTERS_YES).click();
        } else {
            driver.findElement(UPDATE_FILTERS_NO).click();
        }
    }
    public void setUpdateStatuses(boolean updateStatuses){
        WebElement updateStatusesCB = driver.findElement(UPDATE_STATUSES_CB);
        boolean updateIsChecked = updateStatusesCB.isSelected();
        if (updateStatuses != updateIsChecked){
            driver.findElement(UPDATE_STATUSES).click();
        }
    }
    public void setUpdateStatuses(String updateStatuses){
        if (updateStatuses.toLowerCase().equals("yes")){
            setUpdateStatuses(true);
        } else {
            setUpdateStatuses(false);
        }
    }

    public void clickCompleteImport(){
        driver.findElement(COMPLETE_IMPORT_BUTTON).click();
        waitForLoadingWrapper();
    }

    public void clickCancelImport(){
        driver.findElement(CANCEL_IMPORT_BUTTON).click();
        waitForLoadingWrapper();
    }
}

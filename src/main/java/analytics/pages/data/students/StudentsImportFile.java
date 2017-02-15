package analytics.pages.data.students;


import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

import java.io.File;

/**
 * Allows interaction with the Student Data Import page in EAP Mode
 */
public class StudentsImportFile extends AnalyticsPage {

    // Locators for interactive elements within the component
    protected final By KEY_STAGE_SELECT = By.id("YearType_ID");
    protected final By FILE_BROWSE = By.id("File");
    protected final By IMPORT_BUTTON = By.cssSelector(".submitButtons>.button.green");

    // Locators for static page content
    protected final By IMPORTING_MESSAGE = By.id("workingMessage");
    protected final By IMPORT_FORMAT_LEGEND = By.cssSelector(".fieldset>.legend");
    protected final By IMPORT_FORMAT_EXAMPLE = By.cssSelector("table.excel");
    protected final By IMPORT_FORMAT_COL_LETTERS = By.cssSelector("table.excel th");
    protected final By IMPORT_FORMAT_COL_NAMES = By.cssSelector("table.excel>tbody>tr:nth-of-type(2)>td");
    protected final By IMPORT_LEGEND = By.cssSelector(".fkfieldset>.legend");
    protected final By KEY_STAGE_3_CLASSES_INFO = By.cssSelector("span.yearsForKS3");
    protected final By KEY_STAGE_4_CLASSES_INFO = By.cssSelector("span.yearsForKS4");
    protected final By FIELD_LABELS = By.cssSelector(".editor-label");

    // CONSTRUCTOR

    /**
     *
     * @param aDriver   The browser should be showing the Student File Import page
     */
    public StudentsImportFile(AnalyticsDriver aDriver){
        super(aDriver);
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(IMPORT_BUTTON));
    }

    // ACTIONS (UPDATING CURRENT PAGE OR NAVIGATING TO A NEW PAGE)
    public void selectKeyStage(String keyStage){
        Select ksDDL = new Select(driver.findElement(KEY_STAGE_SELECT));
        ksDDL.selectByVisibleText(keyStage);
        By ksInfoLocator = By.className("yearsForKS"+keyStage);
        waitShort.until(visibilityOfElementLocated(ksInfoLocator));
    }

    public void setUploadFile(String testResourcePath){
        WebElement fileInput = driver.findElement(FILE_BROWSE);
        String fileDir = System.getProperty("user.dir") + File.separator
                + "test-resources" + File.separator
                + testResourcePath;
        File file = new File(fileDir);
        fileInput.clear();
        fileInput.sendKeys(file.getAbsolutePath());
    }

    public boolean clickImport(){

        driver.findElement(IMPORT_BUTTON).click();
        try {
            waitShort.until(visibilityOfElementLocated(IMPORTING_MESSAGE));
            return true;
        } catch (TimeoutException e){
            return false;
        }
    }

}

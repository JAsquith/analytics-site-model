package analytics.pages.data.grades;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.GradesFileUpload;
import analytics.pages.data.grades.components.GradeFilesListRow;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Represents the components and actions on an Uploads For Exams/[Dataset] page.
 * N.B. The EAP Term drop down is not available on the Exams version, so methods acting on that field
 *      are not supported in that context
 */
public class UploadsForDatasets extends AnalyticsPage {

    private final By EAP_TERM_SELECT = By.id("EAPTerm_ID");
    private final By UPLOAD_GRADE_FILE_BUTTON = By.linkText("Upload Grade File");
    private final By EXPORT_BUTTON = By.linkText("Export");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on (or opening) the Uploads For Assessments page
     */
    public UploadsForDatasets(AnalyticsDriver aDriver) {
        super(aDriver);
        waitForLoadingWrapper(LONG_WAIT);
    }

    public UploadsForDatasets selectEAPTerm(String text){
        new Select(driver.findElement(EAP_TERM_SELECT)).selectByVisibleText(text);
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(UPLOAD_GRADE_FILE_BUTTON));
        return this;
    }

    public GradesFileUpload clickUploadGradeFile(){
        driver.findElement(UPLOAD_GRADE_FILE_BUTTON).click();
        return new GradesFileUpload(driver);
    }

    public GradeFilesListRow getFilesTableRow(String fileTitle){
        return new GradeFilesListRow(driver, fileTitle);
    }

    public GradeFilesListRow getFilesTableRow(int fileIndex){
        return new GradeFilesListRow(driver, fileIndex);
    }
    public UploadsForDatasets clickExport(){
        driver.findElement(EXPORT_BUTTON).click();
        // Todo - Deal with the pop-up window and file download
        return this;
    }


}
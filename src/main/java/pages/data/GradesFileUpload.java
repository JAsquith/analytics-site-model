package pages.data;

import pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the Grades File upload page which is common to the baselines and grades upload areas.
 * Allows uploading of grades in a 4 column list file format
 * N.B. This is <em>not</em> the Grades File Upload page which is a shared component for baselines and grades
 * see pages.data.basedata.UploadsForBaselinesList to access this component for baseline files
 * see pages.data.grades () to access this component for grades files
 */
public class GradesFileUpload extends AnalyticsPage {

    private final By FILE_BROWSE_FIELD = By.id("File");
    private final By FILE_TITLE_FIELD = By.id("FileTitle");
    private final By RESULTS_DATE_FIELD = By.id("dateNew");
    private final By CANCEL_BUTTON = By.linkText("Cancel");
    private final By UPLOAD_BUTTON = By.cssSelector(".submitButtons>.green");

    /**
     * Simple Constructor
     * @param aDriver   The browser should be pointing to a grades file upload page for
     *                  either Baselines or one of the data sets (e.g. Exams, Data Set 1, Assessments)
     */
    public GradesFileUpload(RemoteWebDriver aDriver){
        super(aDriver);
        waitForLoadingWrapper(MEDIUM_WAIT);
        waitMedium.until(ExpectedConditions.elementToBeClickable(FILE_BROWSE_FIELD));
    }

    /**
     * Sets the File browser input element to the given file path
     * @param localFilePath The path to the file to be uploaded
     */
    public void setFilePath(String localFilePath){
        /*
        We may need this version here:
                WebElement fileInput = driver.findElement(FILE_BROWSE);
                File file = new File(filePath);
                fileInput.clear();
                fileInput.sendKeys(file.getAbsolutePath());
         */
        WebElement field = driver.findElement(FILE_BROWSE_FIELD);
        field.clear();
        field.sendKeys(localFilePath);
    }

    /**
     * Overwrites the File Title field with the given text
     * @param fileTitle     The title to give the newly uploaded file (on submit)
     */
    public void setFileTitle(String fileTitle){
        WebElement field = driver.findElement(FILE_TITLE_FIELD);
        field.clear();
        field.sendKeys(fileTitle);
    }

    /**
     * Overwrites the Results Date field with the given text
     * @param resultsDate   The date string to put into the Results Date field
     */
    public void setResultsDate(String resultsDate){
        WebElement field = driver.findElement(RESULTS_DATE_FIELD);
        field.clear();
        field.sendKeys(resultsDate);
    }

    /**
     * Clicks the Cancel button
     */
    public void clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
    }

    /**
     *
     * @return This GradesFileUpload object
     */
    public GradesFileUpload clickUpload(){
        driver.findElement(UPLOAD_BUTTON).click();
        return this;
    }
}

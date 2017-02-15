package analytics.pages.data.basedata;


import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.GradesFileUpload;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Allows interaction with the Uploads for KS2/Baselines page in EAP Mode.
 * This is the page which lists the grade files previously uploaded to the KS2/Baselines area, and
 * provides a button/link to the Grades File Upload page/component to upload another.
 * N.B. This is <em>not</em> the Grades File Upload page which is a shared component for baselines and grades
 * @see AnalyticsComponent
 */
public class UploadsForBaselinesList extends AnalyticsPage {

    private final By UPLOAD_GRADES_FILE_BUTTON = By.cssSelector("#fm_edtGradeFile>p>.button");
    private final By UPLOADED_FILES_TITLES = By.cssSelector("#fm_edtGradeFile td:nth-of-type(4)");

    /**
     * Simple Constructor
     * @param aDriver   The browser should be on the Uploads for Baselines page
     */
    public UploadsForBaselinesList(AnalyticsDriver aDriver){
        super(aDriver);
    }

    /**
     * Clicks the Upload Grades File button (opening the shared Upload Grades File page/component)
     * @return a new {@link GradesFileUpload} object
     */
    public GradesFileUpload uploadGradesFile(){
        driver.findElement(UPLOAD_GRADES_FILE_BUTTON).click();
        return new GradesFileUpload(driver);
    }

    /**
     * Searches the table of previously uploaded baseline grades files for one with the given title;
     * Todo - Refactor to return a component object representing the table row (allowing interaction with the edit/view/delete buttons)
     * @param fileTitle     The title to look for in the list
     * @return              {@code true} if the title is found; {@code false} is it not found.
     */
    public boolean isListed(String fileTitle){
        List<WebElement> listedTitleTDs = driver.findElements(UPLOADED_FILES_TITLES);
        for (WebElement titleCell : listedTitleTDs){
            if(titleCell.getText().equals(fileTitle)){
                return true;
            }
        }
        return false;
    }
}

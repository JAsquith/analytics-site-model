package pages.data.basedata;


import pages.AnalyticsPage;
import pages.data.GradesFileUpload;
import pages.data.basedata.components.BaselinesListRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Allows interaction with the KS2/Baselines page in EAP Mode.
 * This is the page which lists the subjects from previously baseline grade files, and provides
 * links to the grade lists and delete actions for each subject.
 * Any Baseline Groups are also listed on this page.
 * Objects of this class give access to the
 * {@link BaselinesListRow} objects which represent individual rows
 * in the Uploaded KS2 / Baseline subjects table
 */
public class KS2Baselines extends AnalyticsPage {

    private final By UPLOAD_GRADES_FILE_BUTTON = By.cssSelector("#fm_edtGradeFile>p>.button");
    private final By UPLOADED_FILES_TITLES = By.cssSelector("#fm_edtGradeFile td:nth-of-type(4)");

    /**
     * Simple Constructor
     * @param aDriver   The browser should be on the KS2 / Baselines page
     */
    public KS2Baselines(RemoteWebDriver aDriver){
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

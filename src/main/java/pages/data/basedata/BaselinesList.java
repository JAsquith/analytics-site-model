package pages.data.basedata;

import pages.AnalyticsPage;
import pages.data.basedata.components.BaselinesListRow;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Allows interaction with the KS2/Baselines List page in EAP Mode.
 * This is the page which lists the Baseline Subjects previously uploaded to the KS2/Baselines area, and
 * the Baseline Groups created from those subjects.
 * For Baseline Subjects it allows the grade type &amp; Core nominations to be set, to drill down to the grade lists and
 * the subject to be deleted.
 * It allows access to the create new Baseline Group page, and to edit the name of a Baseline group or delete it.
 */
public class BaselinesList extends AnalyticsPage {

    public final By EDIT_BASELINE_SUBJECTS_BUTTON = By.cssSelector("fieldset:first-of-type .button.small.psnLink");
    public final By SAVE_BUTTON = By.cssSelector(".submitButtons>.button.green");

    /**
     * Simple Constructor
     * @param aDriver   The browser should be on the KS2/Baseline page
     */
    public BaselinesList(RemoteWebDriver aDriver){
        super(aDriver);
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(EDIT_BASELINE_SUBJECTS_BUTTON),
                ExpectedConditions.elementToBeClickable(SAVE_BUTTON)
        ));
    }

    /**
     * Clicks the Edit button (placing the Uploaded KS2 / Baseline table into edit mode)
     */
    public void clickEditButton(){
        driver.findElement(EDIT_BASELINE_SUBJECTS_BUTTON).click();
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON));
    }

    public void clickSaveButton(){
        driver.findElement(SAVE_BUTTON).click();
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(EDIT_BASELINE_SUBJECTS_BUTTON));
    }

    public BaselinesListRow getSubjectRow(String subjectName){
        return new BaselinesListRow(driver, subjectName);
    }

    /**
     * When implemented, this method will click the Add New button and return an EAPBaselineGroup page object
     */
    public void addNewBaselineGroup(){
        // Todo - Yet to be implemented
    }

    /**
     * When implemented this method will identify the row of the Baseline Groups table containing the named group
     * @param groupName     Identifies the group row to search for
     */
    public void getBaselineGroupRow(String groupName){
        // Todo - Yet to be implemented
    }
}

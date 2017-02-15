package analytics.pages.config;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Represents the page shown as Step 2 of the Grade Method creation process/wizard
 */
public class GradeMethodStep2 extends GradeMethodEdit {

    public final By SPLIT_DOUBLE_GUIDANCE_MODAL = By.cssSelector(".modalWrapper>.windowWrapper>.window");
    public final By CLOSE_GUIDANCE_BUTTON = By.cssSelector(".modalClose.button");
    public final By SHOW_GUIDANCE_MODAL = By.cssSelector("a.showSplitGradesInstructions");

    public final By WHOLE_GRADE_TEXT_FIELDS = By.cssSelector(".whole_input.grdTxt");
    public final By WHOLE_GRADE_POINTS_FIELDS = By.cssSelector(".whole_input.ptsTxt");
    public final By WHOLE_GRADE_ENTRIES_FIELDS = By.cssSelector(".whole_input.entrTxt");

    public final String PAGE_URL = "/Config/EAPGradesMethod/DefineWholeGrades";
    public final String EXPECTED_PAGE_LEGEND = "Step 2 of 6 - Define Whole Grades";

    /**
     * The AnalyticsDriver param must represent a browser open at step 2 of the EAP Grade Method edit process.
     * @param aDriver   An AnalyticsDriver object logged in to a school in the given domain
     */
    public GradeMethodStep2(AnalyticsDriver aDriver) {
        super(aDriver);
        waitShort.until(ExpectedConditions.urlContains(PAGE_URL));
        waitForLoadingWrapper();
        if (splitGradesModalDisplayed()){
            closeSplitGradesModal();
        }
        waitShort.until(ExpectedConditions.textToBe(PAGE_LEGEND,EXPECTED_PAGE_LEGEND));
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(SPLIT_DOUBLE_GUIDANCE_MODAL),
                ExpectedConditions.elementToBeClickable(NEXT_BUTTON)
        ));
    }

    /**
     * Determines whether or not the "Split Double Setup Instructions" modal is displayed
     * @return <code>true</code> or <code>false</code>
     */
    public boolean splitGradesModalDisplayed(){
        List<WebElement> modals = driver.findElements(SPLIT_DOUBLE_GUIDANCE_MODAL);
        if (modals.size()==0){
            return false;
        }
        return modals.get(0).isDisplayed();
    }

    public boolean splitGradeModalHidden(){
        return !splitGradesModalDisplayed();
    }

    /**
     * If the "Split Double Setup Instructions" are not currently visible, clicks the button to display them
     */
    public void showSplitGradesModal(){
        if (splitGradesModalDisplayed()){
            return;
        }
        driver.findElement(SHOW_GUIDANCE_MODAL).click();
    }

    /**
     * If the "Split Double Setup Instructions" are currently visible, clicks the button to hide them
     */
    public void closeSplitGradesModal(){
        if (splitGradesModalDisplayed()) {
            driver.findElement(CLOSE_GUIDANCE_BUTTON).click();
        }
    }

    /**
     * Overwrites the text in the nth field of the "Whole Grade" column
     * @param gradeIndex    An int between 0 and 11 (not validated)
     * @param gradeText     A String of up to 6 chars (not validated)
     *
     * @throws org.openqa.selenium.NoSuchElementException if <code>gradeIndex</code> is not within the required range
     */
    public void setWholeGradeText(int gradeIndex, String gradeText){
        try {
            By locator = By.id("EAPWholeGradeList_" + gradeIndex + "__EAPWholeGrade_Text");
            WebElement field = driver.findElement(locator);
            field.clear();
            field.sendKeys(gradeText);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Overwrites the text in the nth field of the "Points (up to 3dp)" column
     * @param gradeIndex    An int between 0 and 11 (not validated)
     * @param gradePoints   A numeric value (may be decimal) as a String (not validated)
     *
     * @throws org.openqa.selenium.NoSuchElementException if <code>gradeIndex</code> is not within the required range
     */
    public void setWholeGradePoints(int gradeIndex, String gradePoints){
        WebElement field = driver.findElement(By.id("EAPWholeGradeList_" + gradeIndex + "__EAPWholeGrade_Points"));
        field.clear();
        field.sendKeys(gradePoints);
    }

    /**
     * Overwrites the text in the nth field of the "Entries" column
     * @param gradeIndex    An int between 0 and 11 (not validated)
     * @param gradeEntries  A numeric value (may be decimal) as a String (not validated)
     *
     * @throws org.openqa.selenium.NoSuchElementException if <code>gradeIndex</code> is not within the required range
     */
    public void setWholeGradeEntries(int gradeIndex, String gradeEntries){
        WebElement field = driver.findElement(By.id("EAPWholeGradeList_" + gradeIndex + "__EAPWholeGrade_Entries"));
        field.clear();
        field.sendKeys(gradeEntries);
    }
}

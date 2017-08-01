package pages.config;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the page shown as Step 3 of the Grade Method creation process/wizard
 */
public class GradeMethodStep3 extends GradeMethodEdit {

    public final By SUB_GRADE_TEXT_FIELDS = By.cssSelector("input[type='text']");

    public final String EXPECTED_PAGE_LEGEND = "Step 3 of 6 - Define Sub Grades";

    /**
     * The AnalyticsDriver param must represent a browser open at step 3 of the EAP Grade Method edit process.
     * @param aDriver   An AnalyticsDriver object logged in to a school in the given domain
     */
    public GradeMethodStep3(RemoteWebDriver aDriver) {
        super(aDriver);
        waitShort.until(ExpectedConditions.textToBe(PAGE_LEGEND,EXPECTED_PAGE_LEGEND));
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
    }

    /**
     * Overwrites the text of the sub grade suffix field identified by
     * {@code subGradeIndex} to {@code subGradeText}
     * @param subGradeIndex an integer between 0 and 9 (not validated)
     * @param subGradeText a string of up to 4 characters (not validated)
     * @throws org.openqa.selenium.NoSuchElementException if <code>subGradeIndex</code>is not within the required range
     */
    public void setSubGradeText(int subGradeIndex, String subGradeText){
        WebElement field = driver.findElement(By.id("EAPSubGradeList_" + subGradeIndex + "__EAPSubGrade_Text"));
        field.clear();
        field.sendKeys(subGradeText);
    }
}

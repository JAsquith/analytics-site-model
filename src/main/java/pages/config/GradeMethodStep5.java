package pages.config;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Represents the page shown as Step 5 of the Grade Method creation process/wizard
 */
public class GradeMethodStep5 extends GradeMethodEdit {

    public final By EQUIVALENCY_SELECTS = By.tagName("select");

    public final String EXPECTED_PAGE_LEGEND = "Step 5 of 6 - Define Sub Grade Equivalencies";

    /**
     * Constructor; The AnalyticsDriver param must represent a browser open at step 5 of the EAP Grade Method edit process.
     * @param aDriver   An AnalyticsDriver object logged in to a school in the given domain
     */
    public GradeMethodStep5(RemoteWebDriver aDriver) {
        super(aDriver);
        waitShort.until(ExpectedConditions.textToBe(PAGE_LEGEND,EXPECTED_PAGE_LEGEND));
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
    }

    /**
     * Selects a sub grade equivalent for a given Whole Grade
     * @param gradeIndex            Index of the whole grade; Starts at {@code 0}
     * @param subGradeEquivalent    Text of the sub grade as shown in the relevant DDL
     */
    public void setSubGradeEquivalent(int gradeIndex, String subGradeEquivalent){
        String ddlID = "EAPMasterGradeList_" + gradeIndex + "__EAPMasterGrade_EquivID";
        Select ddl = new Select(driver.findElement(By.id(ddlID)));
        ddl.selectByVisibleText(subGradeEquivalent);
    }
}

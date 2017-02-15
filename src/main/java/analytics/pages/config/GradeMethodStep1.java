package analytics.pages.config;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the page shown as Step 1 of the Grade Method creation process/wizard
 */
public class GradeMethodStep1 extends GradeMethodEdit {

    public final By METHOD_NAME_FIELD = By.id("EAPGradesMethod_EAPGradesMethod_Name");

    public final By POINTS_TYPE_RADIO_A8 = By.cssSelector(".styledr[for='att8EquivBtn']");
    public final By POINTS_TYPE_RADIO_NONE = By.cssSelector(".styledr[for='noPointsBtn']");
    public final By POINTS_TYPE_RADIO_CUSTOM = By.cssSelector(".styledr[for='bespokeBtn']");

    public final By SUB_GRADES_YES = By.cssSelector(".styledr[for='useSubGradesBtn']");
    public final By SUB_GRADES_NO = By.cssSelector(".styledr[for='noSubGradesBtn']");

    public final By SPLIT_GRADES_NO = By.cssSelector(".styledr[for='noSplitGradesBtn']");
    public final By SPLIT_GRADES_YES = By.cssSelector(".styledr[for='useSplitGradesBtn']");

    public final String EXPECTED_PAGE_LEGEND = "Step 1 of 6 - Set up Grades Method";

    public final String PAGE_URL = "/Config/EAPGradesMethod/Create/6";

    /**
     * The AnalyticsDriver param must represent a browser open at step 1 of the EAP Grade Method
     * edit process or a {@link org.openqa.selenium.TimeoutException} will be thrown
     * @param aDriver   An AnalyticsDriver object logged in to a school in the given domain
     */
    public GradeMethodStep1(AnalyticsDriver aDriver) {
        super(aDriver);
        waitShort.until(ExpectedConditions.textToBe(PAGE_LEGEND,EXPECTED_PAGE_LEGEND));
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
    }

    /**
     * Overwrites the current text in the Method Name field with the String value passed in
     * @param methodName    The new method name
     */
    public void setMethodName (String methodName){
        WebElement field = driver.findElement(METHOD_NAME_FIELD);
        field.clear();
        field.sendKeys(methodName);
    }

    /**
     * Selects one of the radio buttons in the "Points" section of the page
     * @param pointsType    Valid values are "Attainment 8", "None" and "Custom"
     */
    public boolean setPointsType (String pointsType){
        switch (pointsType.toLowerCase()){
            case "":case "attainment 8":case "a8":
                driver.findElement(POINTS_TYPE_RADIO_A8).click();
                return true;
            case "none":
                driver.findElement(POINTS_TYPE_RADIO_NONE).click();
                return false;
            case "custom":case "bespoke":
                driver.findElement(POINTS_TYPE_RADIO_CUSTOM).click();
                return false;
            default:
                throw new IllegalArgumentException("\"" + pointsType +"\" is not a valid points type. " +
                        "Valid values are \"Attainment 8\", \"A8\", \"None\" and \"Custom\"");
        }
    }

    /**
     * Selects one of the radio buttons in the "Subgrades?" section of the page
     * @param subGrades     <code>true</code> or <code>false</code>; if true, "Yes - ..." is selected;
     */
    public void setSubGrades(boolean subGrades){
        if (subGrades){
            driver.findElement(SUB_GRADES_YES).click();
        } else {
            driver.findElement(SUB_GRADES_NO).click();
        }
    }

    /**
     * Selects one of the radio buttons in the
     * "Double Award / Split Grade Options for A8 Qualifications" section of the page
     * @param splitGrades   <code>true</code> or <code>false</code>; if true, then
     *                      the "This method does have split grades" option is selected
     */
    public void setSplitGrades(boolean splitGrades){
        if(splitGrades){
            driver.findElement(SPLIT_GRADES_YES).click();
        } else {
            driver.findElement(SPLIT_GRADES_NO).click();
        }
    }

    @Override
    public void clickNext(){
        super.clickNext();
        // Some wait stuff maybe?
    }
}

package analytics.pages.data.basedata;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the components and actions on an Edit EAP page
 */
public class EditEAP extends AnalyticsPage {

    public final String PAGE_URL = "/EAPAdmin/EAP/Edit";
    public final String PAGE_TITLE_TEXT = "Edit EAP";

    public final By EAP_NAME_FIELD = By.id("EAP_EAP_Name");
    public final By EAP_DESCRIPTION_FIELD = By.id("EAP_EAP_Desc");
    public final By KS2_BASELINE_SELECT = By.id("EAP_EAPBaseline_ID");
    public final By FINAL_EXAM_GRADES_SELECT = By.id("TermList_15__EAPGradesMethod_ID");

    public final By CANCEL_BUTTON = By.cssSelector(".submitButtons>a.button.cancel");
    public final By SAVE_BUTTON = By.cssSelector(".submitButtons>button");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on the Edit EAP page (/EAPAdmin/EAP/Create)
     */
    public EditEAP(AnalyticsDriver aDriver) {
        super(aDriver);
        waitMedium.until(ExpectedConditions.elementToBeClickable(EAP_NAME_FIELD));
        waitForLoadingWrapper();
    }

    public EditEAP setEAPName(String newName){
        return (EditEAP) this.setFieldValue(EAP_NAME_FIELD, newName);
    }

    public EditEAP setEAPDescription(String newDescription){
        return (EditEAP) this.setFieldValue(EAP_DESCRIPTION_FIELD, newDescription);
    }

    public EditEAP selectKS2Baseline_Edit(String visibleText){
        this.getDDL(KS2_BASELINE_SELECT).selectByVisibleText(visibleText);
        return this;
    }

    public EditEAP selectFinalExamGrades(String visibleText){
        this.getDDL(FINAL_EXAM_GRADES_SELECT).selectByVisibleText(visibleText);
        return this;
    }

    public boolean getByTermFlag(int year){
        if (year < 7 || year > 11){
            throw new IllegalArgumentException("year must be between '7' and '11'");
        }
        return driver.findElement(By.id("chk_year_"+year)).isSelected();
    }

    public EditEAP setByTermFlag(int year){
        if (getByTermFlag(year)){
            return this;
        }
        driver.findElement(By.cssSelector("label{for='chk_year_"+year+"']")).click();
        return this;
    }

    public EditEAP selectAssessmentMethod(int year, int term, String methodName){
        boolean isByTerm = getByTermFlag(year);
        By ddlLocator;
        if((term==0 && isByTerm) || (term>0 && !isByTerm)){
            throw new IllegalArgumentException("The By Term flag for Year " + year + " is set to " + isByTerm + ".");
        }

        switch (term){
            case 0:
                ddlLocator = By.id("YearList_" + (year - 7) + "__EAPGradesMethod_ID");
                break;
            case 1:case 2:case 3:
                int termListIndex = (year - 7) * (term - 1);
                ddlLocator = By.id("TermList_" + termListIndex + "__EAPGradesMethod_ID");
                break;
            default:
                throw new IllegalArgumentException("The 'term' argument is invalid (" + term + ")." +
                        " It must be between 0 and 3!");
        }

        getDDL(ddlLocator).selectByVisibleText(methodName);

        return this;
    }

    public EditEAP selectFinalExamMethod(String methodName){
        getDDL(FINAL_EXAM_GRADES_SELECT).selectByVisibleText(methodName);
        return this;
    }

    public void clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
    }

    public void clickSave(){
        driver.findElement(SAVE_BUTTON).click();
    }

}
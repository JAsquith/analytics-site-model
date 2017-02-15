package analytics.pages.data.basedata;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Calendar;

/**
 * Created by masquith on 18/01/2017.
 */
public class CreateEAP extends EditEAP {

    public final String PAGE_URL = "/EAPAdmin/EAP/Create";
    public final String PAGE_TITLE_TEXT = "Create EAP";

    public final By KS2_BASELINE_TYPE_SELECT = By.id("blMethodDDL");
    public final By KS2_BASELINE_SELECT = By.id("blDDL");
    public final By KS2_BASELINE_OPTIONS = By.cssSelector("#blDDL>option");

    protected final By EAP_TYPE_CREATED = By.name("EAP.EAP_TypeCreated");

    public final By CREATE_NEW_RADIO = By.id("radioButtonCreateNewEAP");
    public final By CREATE_NEW_LABEL = By.cssSelector("label[for='radioButtonCreateNewEAP']");

    public final By COPY_EXISTING_RADIO = By.id("radioButtonCopyExistingEAP");
    public final By COPY_EXISTING_LABEL = By.cssSelector("label[for='radioButtonCopyExistingEAP']");

    public final By COPY_FROM_COHORT_SELECT = By.id("cohortDDL");
    public final By COPY_FROM_EAP_SELECT = By.id("eapDDL");
    public final By COPY_FROM_EAP_OPTIONS = By.cssSelector("#eapDDL>option");

    public final By CREATE_BUTTON = By.cssSelector(".submitButtons>button");
    public final By CANCEL_BUTTON = By.cssSelector(".submitButtons>a.button.cancel");

    public CreateEAP (AnalyticsDriver aDriver){
        super(aDriver);
    }

    public CreateEAP selectKS2BaselineType(String visibleText){
        this.getDDL(KS2_BASELINE_TYPE_SELECT).selectByVisibleText(visibleText);
        waitShort.until(ExpectedConditions.numberOfElementsToBeMoreThan(KS2_BASELINE_OPTIONS, 1));
        waitForLoadingWrapper(MEDIUM_WAIT, true);
        return this;
    }

    public CreateEAP selectKS2Baseline_Create(String visibleText){
        this.getDDL(KS2_BASELINE_SELECT).selectByVisibleText(visibleText);
        return this;
    }

    public boolean isFirstEAP(){
        if(driver.findElement(EAP_TYPE_CREATED).getAttribute("type").equals("radio")){
            return false;
        }
        return true;
    }

    public CreateEAP chooseCreateNewEAP(){
        driver.findElement(CREATE_NEW_LABEL).click();
        waitMedium.until(ExpectedConditions.elementSelectionStateToBe(CREATE_NEW_RADIO, true));
        waitMedium.until(ExpectedConditions.elementToBeClickable(FINAL_EXAM_GRADES_SELECT));
        return this;
    }

    public CreateEAP chooseCopyExistingEAP(){
        driver.findElement(COPY_EXISTING_LABEL).click();
        waitMedium.until(ExpectedConditions.elementSelectionStateToBe(COPY_EXISTING_RADIO, true));
        waitMedium.until(ExpectedConditions.elementToBeClickable(COPY_FROM_COHORT_SELECT));
        return this;
    }

    public String getCreateOrCopySetting(){
        if (this.isFirstEAP()){
            return "Create";
        }
        if(driver.findElement(CREATE_NEW_RADIO).isSelected()){
            return "Create";
        }
        if(driver.findElement(COPY_EXISTING_RADIO).isSelected()){
            return "Copy";
        }
        return "";
    }

    public CreateEAP selectCohortToCopyByLabel(String visibleText){
        getDDL(COPY_FROM_COHORT_SELECT).selectByVisibleText(visibleText);
        waitForLoadingWrapper(MEDIUM_WAIT, true);
        waitMedium.until(ExpectedConditions.elementToBeClickable(COPY_FROM_COHORT_SELECT));
        return this;
    }

    public CreateEAP selectCohortToCopyByCohortNum(String cohort){
        int cohortYear = Integer.valueOf(cohort);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 2000;

        String cohortLabel1 = (cohortYear - 1) + "/" + cohortYear;
        String cohortLabel2 = cohortLabel1;

        if (cohortYear < currentYear){
            selectCohortToCopyByLabel(cohortLabel1 + " (Leavers)");
        } else {
            if (cohortYear == currentYear){
                cohortLabel1 = cohortLabel1 + " (Leavers)";
                cohortLabel2 = cohortLabel2 + " (Current Yr 11)";
            } else {
                int academicYear = 11 - (cohortYear - currentYear);
                cohortLabel1 = cohortLabel1 + " (Current Yr " + (academicYear - 1) + ")";
                cohortLabel2 = cohortLabel2 + " (Current Yr " + academicYear + ")";
            }

            try {
                selectCohortToCopyByLabel(cohortLabel1);
            } catch (Exception e) {
                selectCohortToCopyByLabel(cohortLabel2);
            }
        }
        return this;
    }

    public CreateEAP selectEAPToCopy(String visibleText){
        getDDL(COPY_FROM_EAP_SELECT).selectByVisibleText(visibleText);
        return this;
    }

    public void clickCreate(){
        driver.findElement(CREATE_BUTTON).click();
    }

    public void clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
    }

}

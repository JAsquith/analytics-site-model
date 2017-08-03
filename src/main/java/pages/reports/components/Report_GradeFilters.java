package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.Report;

import java.util.List;

public class Report_GradeFilters extends AnalyticsComponent {

    public static final By ON_TRACK_MENU = By.cssSelector(".onTrack.active");
    public static final By FACULTY_DDL = By.cssSelector("select#ReportOptions_Faculty_ID");
    public static final By QUALIFICATION_DDL = By.cssSelector("select#ReportOptions_Qual_ID");
    public static final By CLASS_DDL = By.cssSelector("select#ReportOptions_TchGrp_ID");
    public static final By GRADE_TYPE_DDL = By.cssSelector("select#ReportOptions_EAPGradesMethod_ID");
    public static final By AWARD_CLASS_DDL = By.cssSelector("select#ReportOptions_RPTQualType_ID");
    public static final By KS2_CORE_DDL = By.cssSelector("select#ReportOptions_KS2Baseline_ID");
    public static final By GRADE_FILTER_TYPE_DDL = By.cssSelector("select#ReportOptions_Grade_RPTOperand_ID");
    public static final By GRADE_FILTER_WHOLE_DDL = By.cssSelector("select#ReportOptions_EAPWholeGrade_ID");
    public static final By GRADE_FILTER_SUB_DDL = By.cssSelector("select#ReportOptions_EAPSubGrade_Order");

    public Report_GradeFilters(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public Report filterByTrack(String trackStatus){
        if (trackStatus.equals("")){
            trackStatus = "All";
        }
        List<WebElement> onTrackMenus = driver.findElements(ON_TRACK_MENU);
        if (onTrackMenus.size() == 0){
            return new Report(driver); // The Tracking menu is not currently available
        }
        onTrackMenus.get(0).findElement(By.cssSelector("[title='" + trackStatus + "']")).click();
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectFaculty(String optionText){
        List<WebElement> facultyDDLs = driver.findElements(FACULTY_DDL);
        if (facultyDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
            }
            return new Report(driver); // The Faculty DDL is not currently available
        }
        new Select(facultyDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectQualification(String optionText){
        List<WebElement> qualificationDDLs = driver.findElements(QUALIFICATION_DDL);
        if (qualificationDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Qualification is not available");
            }
            return new Report(driver); // The Qualification DDL is not currently available
        }
        new Select(qualificationDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectClass(String optionText){
        List<WebElement> classDDLs = driver.findElements(CLASS_DDL);
        if (classDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
            }
            return new Report(driver); // The Class DDL is not currently available
        }
        new Select(classDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectGradeType(String optionText){
        // NB. if field is 'Locked', the option will still be selected and the form submitted, but
        //      when the page is reloaded the previous option will be the active one
        List<WebElement> gradeTypeDDLs = driver.findElements(GRADE_TYPE_DDL);
        if (gradeTypeDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Type is not available");
            }
            return new Report(driver); // The Grade Type DDL is not currently available
        }
        new Select(gradeTypeDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectAwardClass(String optionText){
        List<WebElement> awardClassDDLs = driver.findElements(AWARD_CLASS_DDL);
        if (awardClassDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because GCSE/Non-GCSE is not available");
            }
            return new Report(driver); // The GCSE/Non-GCSE DDL is not currently available
        }
        new Select(awardClassDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectKS2Core(String optionText){
        List<WebElement> ks2CoreDDLs = driver.findElements(KS2_CORE_DDL);
        if (ks2CoreDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because KS2 Core is not available");
            }
            return new Report(driver); // The KS2 Core DDL is not currently available
        }
        new Select(ks2CoreDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectGradeFilterType(String optionText){

        switch (optionText.toLowerCase()){
            case "":
                return new Report(driver);
            case "less than":
                optionText = "<";
                break;
            case "equal to":
                optionText = "=";
                break;
            case "greater or equal":
                optionText = ">=";
                break;
            case "greater than":
                optionText = ">";
                break;
            default:
                throw new IllegalArgumentException("Unknown Grade Filter Type '" + optionText +
                        "'. Expected 'less than', 'equal to', 'greater or equal' or 'greater than'.");
        }

        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_TYPE_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
        }

        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectGradeFilterWhole(String optionText){
        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_WHOLE_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
            }
            return new Report(driver); // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }
    public Report selectGradeFilterSub(String optionText){
        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_SUB_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
            }
            return new Report(driver); // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new Report(driver);
    }


}

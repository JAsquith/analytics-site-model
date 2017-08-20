package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.EAPReport;

import java.util.List;

public class Report_GradeFilters extends AnalyticsComponent {

    public final By ON_TRACK_MENU = By.cssSelector(".onTrack");
    public final By FACULTY_DDL = By.id("ReportOptions_Faculty_ID");
    public final By QUALIFICATION_DDL = By.id("ReportOptions_Qual_ID");
    public final By CLASS_DDL = By.id("ReportOptions_TchGrp_ID");
    public final By GRADE_TYPE_DDL = By.id("ReportOptions_EAPGradesMethod_ID");
    public final By AWARD_CLASS_DDL = By.id("ReportOptions_RPTQualType_ID");
    public final By KS2_CORE_DDL = By.id("ReportOptions_KS2Baseline_ID");
    public final By GRADE_FILTER_TYPE_DDL = By.id("ReportOptions_Grade_RPTOperand_ID");
    public final By GRADE_FILTER_WHOLE_DDL = By.id("ReportOptions_EAPWholeGrade_ID");
    public final By GRADE_FILTER_SUB_DDL = By.id("ReportOptions_EAPSubGrade_Order");
    public final By COMP_GRADE_FILTER_TYPE_DDL = By.id("ReportOptions_Comp_Grade_RPTOperand_ID");
    public final By COMP_GRADE_FILTER_WHOLE_DDL = By.id("ReportOptions_Comp_EAPWholeGrade_ID");
    public final By COMP_GRADE_FILTER_SUB_DDL = By.id("ReportOptions_Comp_EAPSubGrade_Order");
    public final By STUDENT_FILTER_DDL = By.id("ReportOptions_Stu_ID");

    public Report_GradeFilters(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public EAPReport filterByTrack(String trackStatus){
        if (trackStatus.equals("")){
            trackStatus = "All";
        }
        List<WebElement> onTrackMenus = driver.findElements(ON_TRACK_MENU);
        if (onTrackMenus.size() == 0){
            return new EAPReport(driver); // The Tracking menu is not currently available
        }
        onTrackMenus.get(0).findElement(By.cssSelector("[title='" + trackStatus + "']")).click();
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectFaculty(String optionText){
        List<WebElement> facultyDDLs = driver.findElements(FACULTY_DDL);
        if (facultyDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
            }
            return new EAPReport(driver); // The Faculty DDL is not currently available
        }
        new Select(facultyDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectQualification(String optionText){
        List<WebElement> qualificationDDLs = driver.findElements(QUALIFICATION_DDL);
        if (qualificationDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Qualification is not available");
            }
            return new EAPReport(driver); // The Qualification DDL is not currently available
        }
        new Select(qualificationDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectClass(String optionText){
        List<WebElement> classDDLs = driver.findElements(CLASS_DDL);
        if (classDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
            }
            return new EAPReport(driver); // The Class DDL is not currently available
        }
        new Select(classDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectGradeType(String optionText){
        // NB. if field is 'Locked', the option will still be selected and the form submitted, but
        //      when the page is reloaded the previous option will be the active one
        List<WebElement> gradeTypeDDLs = driver.findElements(GRADE_TYPE_DDL);
        if (gradeTypeDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Type is not available");
            }
            return new EAPReport(driver); // The Grade Type DDL is not currently available
        }
        new Select(gradeTypeDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectAwardClass(String optionText){
        List<WebElement> awardClassDDLs = driver.findElements(AWARD_CLASS_DDL);
        if (awardClassDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because GCSE/Non-GCSE is not available");
            }
            return new EAPReport(driver); // The GCSE/Non-GCSE DDL is not currently available
        }
        new Select(awardClassDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectKS2Core(String optionText){
        List<WebElement> ks2CoreDDLs = driver.findElements(KS2_CORE_DDL);
        if (ks2CoreDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because KS2 Core is not available");
            }
            return new EAPReport(driver); // The KS2 Core DDL is not currently available
        }
        new Select(ks2CoreDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectGradeFilterType(String optionText){

        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_TYPE_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because Grade Filters are not available");
            }
            return new EAPReport(driver); // The Grade Filter DDLs are not currently available
        }
        switch (optionText.toLowerCase()){
            case "":
                break;
            case "less than": case "<":
                optionText = "<";
                break;
            case "equal to": case "=":
                optionText = "=";
                break;
            case "greater or equal": case ">=":
                optionText = ">=";
                break;
            case "greater than": case ">":
                optionText = ">";
                break;
            default:
                throw new IllegalArgumentException("Unknown Grade Filter Type '" + optionText +
                        "'. Expected 'less than', 'equal to', 'greater or equal' or 'greater than'.");
        }

        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
        }

        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectGradeFilterWhole(String optionText){
        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_WHOLE_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because Grade Filters are not available");
            }
            return new EAPReport(driver); // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectGradeFilterSub(String optionText){
        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_SUB_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because Grade Filters are not available");
            }
            return new EAPReport(driver); // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectCompGradeFilterType(String optionText){

        List<WebElement> targetDDLs = driver.findElements(COMP_GRADE_FILTER_TYPE_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because Compare Grade Filters are not available");
            }
            return new EAPReport(driver); // The Compare Grade Filter DDLs are not currently available
        }
        switch (optionText.toLowerCase()){
            case "":
                return new EAPReport(driver);
            case "less than": case "<":
                optionText = "<";
                break;
            case "equal to": case "=":
                optionText = "=";
                break;
            case "greater or equal": case ">=":
                optionText = ">=";
                break;
            case "greater than": case ">":
                optionText = ">";
                break;
            default:
                throw new IllegalArgumentException("Unknown Grade Filter Type '" + optionText +
                        "'. Expected 'less than', 'equal to', 'greater or equal' or 'greater than'.");
        }

        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
        }

        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectCompGradeFilterWhole(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COMP_GRADE_FILTER_WHOLE_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because Compare Grade Filters are not available");
            }
            return new EAPReport(driver); // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectCompGradeFilterSub(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COMP_GRADE_FILTER_SUB_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because Compare Grade Filters are not available");
            }
            return new EAPReport(driver); // The Compare Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }
    public EAPReport selectStudent(String optionText){
        List<WebElement> targetDDLs = driver.findElements(STUDENT_FILTER_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText +
                        "' because the Student Filter is not available");
            }
            return new EAPReport(driver); // The Student Filter DDL is not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPReport(driver);
    }

    public boolean isDisabled(By locator){

        WebElement testElement;
        testElement = driver.findElement(locator);
        // If we're looking for anything except the OnTrack menu, we need to check the enclosing parent element
        if (locator != ON_TRACK_MENU) {
            testElement = testElement.findElement(By.xpath(".."));
        }
        return testElement.getAttribute("class").contains("disabled");
    }

    public boolean isEnabled(By locator){
        WebElement testElement;
        testElement = driver.findElement(locator);
        // If we're looking for the OnTrack menu, we can check the menu itself *DOES NOT* have the disabled class
        if(locator == ON_TRACK_MENU){
            return !testElement.getAttribute("class").contains("disabled");
        }
        // For all others (DDLs), we can just use the WebElement.isEnabled method:
        return testElement.isEnabled();
    }

    public boolean isDisplayed(By locator){
        WebElement testElement;
        testElement = driver.findElement(locator);
        // If we're looking for the OnTrack menu, we can check the menu itself isDisplayed
        if (locator == ON_TRACK_MENU) {
            return testElement.isDisplayed();
        }
        // For all others (DDLs), we need to check the parent as well (in case the select has been replaced with a span):
        WebElement parent = testElement.findElement(By.xpath(".."));
        return testElement.isDisplayed() || parent.isDisplayed();
    }

}

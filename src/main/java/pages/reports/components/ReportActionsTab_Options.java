package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;

import java.util.List;

public class ReportActionsTab_Options extends AnalyticsComponent
{
    private static final String TAB_NAME = "option";
    private static final String TAB_CLASS = "options";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");
    private static final By CONTENTS_EXPAND_BUTTON = By.cssSelector("."+TAB_CLASS+".pan .showMore");

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
    public final By IN_A8_BASKET_DDL = By.id("ReportOptions_RPTInA8Basket_ID");
    public final By STUDENT_FILTER_DDL = By.id("ReportOptions_Stu_ID");

    public ReportActionsTab_Options(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public EAPListView filterByTrack(String trackStatus){
        // NB - The filter tracking options are outside the tabs so we don't need to use #selectTab() & expandTab() combo
        //      If the tracking options are moved into the tab simply add calls to those methods at the start of this method
        if (trackStatus.equals("")){
            trackStatus = "All";
        }
        List<WebElement> onTrackMenus = driver.findElements(ON_TRACK_MENU);
        if (onTrackMenus.size() == 0){
            throw new IllegalStateException("The Tracking filter is not currently available");
        }
        onTrackMenus.get(0).findElement(By.cssSelector("[title='" + trackStatus + "']")).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectFaculty(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> facultyDDLs = driver.findElements(FACULTY_DDL);
        if (facultyDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
        }
        new Select(facultyDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPListView selectQualification(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> qualificationDDLs = driver.findElements(QUALIFICATION_DDL);
        if (qualificationDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Qualification is not available");
        }
        new Select(qualificationDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPListView selectClass(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> classDDLs = driver.findElements(CLASS_DDL);
        if (classDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
        }
        new Select(classDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectGradeType(String optionText){
        // NB. if field is 'Locked', the option will still be selected and the form submitted, but
        //      when the page is reloaded the previous option will be the active one
        // MJA 25/09/2017: I doubt the veracity of the previous comment made by my past self

        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> gradeTypeDDLs = driver.findElements(GRADE_TYPE_DDL);
        if (gradeTypeDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Grade Type is not available");
        }
        new Select(gradeTypeDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPListView selectAwardClass(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> awardClassDDLs = driver.findElements(AWARD_CLASS_DDL);
        if (awardClassDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because GCSE/Non-GCSE is not available");
        }
        new Select(awardClassDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectKS2Core(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> ks2CoreDDLs = driver.findElements(KS2_CORE_DDL);
        if (ks2CoreDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because KS2 Core is not available");
        }
        new Select(ks2CoreDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectGradeFilterType(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();


        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_TYPE_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because Grade Filters are not available");
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
        return new EAPListView(driver);
    }
    public EAPListView selectGradeFilterWhole(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_WHOLE_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because Grade Filters are not available");
        }
        try {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("'"+optionText+"' is not a grade in the Focus Dataset!");
        }
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPListView selectGradeFilterSub(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_SUB_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because Grade Filters are not available");
        }
        try {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("'"+optionText+"' is not a sub grade in the Focus Dataset!");
        }
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectCompGradeFilterType(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();


        List<WebElement> targetDDLs = driver.findElements(COMP_GRADE_FILTER_TYPE_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because Compare Grade Filters are not available");
        }
        switch (optionText.toLowerCase()){
            case "":
                return new EAPListView(driver);
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
        return new EAPListView(driver);
    }
    public EAPListView selectCompGradeFilterWhole(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> targetDDLs = driver.findElements(COMP_GRADE_FILTER_WHOLE_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because Compare Grade Filters are not available");
        }

        try {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("'"+optionText+"' is not a grade in the Compare Dataset!");
        }
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPListView selectCompGradeFilterSub(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> targetDDLs = driver.findElements(COMP_GRADE_FILTER_SUB_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because Compare Grade Filters are not available");
        }
        try {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("'"+optionText+"' is not a sub grade in the Compare Dataset!");
        }
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectInA8Basket(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> targetDDLs = driver.findElements(IN_A8_BASKET_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because In A8 Basket is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView selectStudent(String optionText){
        // Switch to the Options tab & expand it if required
        selectMe();
        expandMe();

        List<WebElement> targetDDLs = driver.findElements(STUDENT_FILTER_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because the Student Filter is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
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
/*
        WebElement testElement;
        testElement = driver.findElement(locator);
        return testElement.isDisplayed();
*/
        return driver.findElement(locator).isDisplayed();
    }

    public ReportActionsTab_Options selectMe(){
        WebElement tab = driver.findElement(TAB_BUTTON);
        if (!tab.getAttribute("class").contains("active")){
            tab.click();
        }
        return this;

    }
    public ReportActionsTab_Options expandMe(){
        List<WebElement> expandButtons = driver.findElements(CONTENTS_EXPAND_BUTTON);
        if (expandButtons.size()==0){
            return this;
        }
        WebElement expandButton = expandButtons.get(0);
        WebElement tabContentDiv = driver.findElement(CONTENTS_DIV);
        String contentsClassAttr = tabContentDiv.getAttribute("class");
        if (!expandButton.isDisplayed() || contentsClassAttr.contains("open")){
            return this;
        }
        expandButton.click();
        waitShort.until(isExpanded(tabContentDiv));
        return this;
    }

    private ExpectedCondition<Boolean> isExpanded(WebElement tabContentDiv) {
        return ExpectedConditions.attributeContains(tabContentDiv, "class", "open");
    }
}

package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import pages.reports.EAPListView;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActionsTab_Options extends ReportActionsTab implements IReportActionGroup {
    private static final String TAB_NAME = "option";
    private static final String TAB_CLASS = "options";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");
    private static final By CONTENTS_EXPAND_BUTTON = By.cssSelector("."+TAB_CLASS+".pan .showMore");

    public final By ON_TRACK_MENU = By.cssSelector(".onTrack");

    public final By FACULTY_DDL = By.id("ReportOptions_Faculty_ID");
    public final By QUALIFICATION_DDL = By.id("ReportOptions_Qual_ID");
    public final By CLASS_DDL = By.id("ReportOptions_TchGrp_ID");
    private static final By GRADE_TYPE_DDL = By.id("ReportOptions_EAPGradesMethod_ID");
    private static final By AWARD_CLASS_DDL = By.id("ReportOptions_RPTQualType_ID");
    private static final By KS2_CORE_DDL = By.id("ReportOptions_KS2Baseline_ID");
    private static final By GRADE_FILTER_TYPE_DDL = By.id("ReportOptions_Grade_RPTOperand_ID");
    private static final By GRADE_FILTER_WHOLE_DDL = By.id("ReportOptions_EAPWholeGrade_ID");
    private static final By GRADE_FILTER_SUB_DDL = By.id("ReportOptions_EAPSubGrade_Order");
    private static final By COMP_GRADE_FILTER_TYPE_DDL = By.id("ReportOptions_Comp_Grade_RPTOperand_ID");
    private static final By COMP_GRADE_FILTER_WHOLE_DDL = By.id("ReportOptions_Comp_EAPWholeGrade_ID");
    private static final By COMP_GRADE_FILTER_SUB_DDL = By.id("ReportOptions_Comp_EAPSubGrade_Order");
    private static final By IN_A8_BASKET_DDL = By.id("ReportOptions_RPTInA8Basket_ID");
    public final By STUDENT_FILTER_DDL = By.id("ReportOptions_Stu_ID");

    private static final By REQUIRED_FIELD = By.cssSelector(".switch-toggle.error");

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Options(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    /* Actions available within this component
    * ToDo: Javadoc */
    public EAPView filterByTrack(String trackStatus){
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

    public EAPView selectFaculty(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> facultyDDLs = driver.findElements(FACULTY_DDL);
        if (facultyDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
        }
        new Select(facultyDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPView selectQualification(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> qualificationDDLs = driver.findElements(QUALIFICATION_DDL);
        if (qualificationDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Qualification is not available");
        }
        new Select(qualificationDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPView selectClass(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> classDDLs = driver.findElements(CLASS_DDL);
        if (classDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Faculty is not available");
        }
        new Select(classDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView selectGradeType(String optionText){
        // NB. if field is 'Locked', the option will still be selected and the form submitted, but
        //      when the page is reloaded the previous option will be the active one
        // MJA 25/09/2017: I doubt the veracity of the previous comment made by my past self

        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> gradeTypeDDLs = driver.findElements(GRADE_TYPE_DDL);
        if (gradeTypeDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Grade Type is not available");
        }
        new Select(gradeTypeDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPView selectAwardClass(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> awardClassDDLs = driver.findElements(AWARD_CLASS_DDL);
        if (awardClassDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because GCSE/Non-GCSE is not available");
        }
        new Select(awardClassDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView selectKS2Core(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> ks2CoreDDLs = driver.findElements(KS2_CORE_DDL);
        if (ks2CoreDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because KS2 Core is not available");
        }
        new Select(ks2CoreDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView selectGradeFilterType(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();


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
    public EAPView selectGradeFilterWhole(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

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
    public EAPView selectGradeFilterSub(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

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

    public EAPView selectCompGradeFilterType(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();


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
    public EAPView selectCompGradeFilterWhole(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

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
    public EAPView selectCompGradeFilterSub(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

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

    public EAPView selectInA8Basket(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> targetDDLs = driver.findElements(IN_A8_BASKET_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because In A8 Basket is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView selectStudent(String optionText){
        // Switch to the Options tab & expand it if required
        super.selectAndExpandTab();

        List<WebElement> targetDDLs = driver.findElements(STUDENT_FILTER_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText +
                    "' because the Student Filter is not available");
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    /* Querying the state of the component */
    public List<WebElement> requiredFields(){
        return driver.findElements(REQUIRED_FIELD);
    }

    public ReportAction getRequiredAction(){
        WebElement fieldWrapperDiv = driver.findElement(REQUIRED_FIELD);
        WebElement fieldLabelDiv = fieldWrapperDiv.findElement(By.tagName("div"));

        switch(fieldLabelDiv.getText().trim()){
            case "Qualification":
                return ReportAction.QUALIFICATION;
            case "Grade type":
                return ReportAction.GRADE_TYPE;
            case "Student":
                return ReportAction.STUDENT;
            default:
                throw new IllegalStateException("Unknown Required Field on the Options Tab");
        }
    }

    public boolean optionIsDisabled(By locator){

        WebElement testElement;
        testElement = driver.findElement(locator);
        // If we're looking for anything except the OnTrack menu, we need to check the enclosing parent element
        if (locator != ON_TRACK_MENU) {
            testElement = testElement.findElement(By.xpath(".."));
        }
        return testElement.getAttribute("class").contains("disabled");
    }

    public boolean optionIsEnabled(By locator){
        WebElement testElement;
        testElement = driver.findElement(locator);
        // If we're looking for the OnTrack menu, we can check the menu itself *DOES NOT* have the disabled class
        if(locator == ON_TRACK_MENU){
            return !testElement.getAttribute("class").contains("disabled");
        }
        // For all others (DDLs), we can just use the WebElement.isEnabled method:
        return testElement.isEnabled() && testElement.isDisplayed();
    }

    public boolean optionIsDisplayed(By locator){
/*
        WebElement testElement;
        testElement = driver.findElement(locator);
        return testElement.isDisplayed();
*/
        return driver.findElement(locator).isDisplayed();
    }

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    @Override
    public List<ReportAction> getValidActionsList() {
        selectAndExpandTab();
        List<ReportAction> actions = new ArrayList<ReportAction>();
        if(optionIsEnabled(ON_TRACK_MENU)) actions.add(ReportAction.EAP_TRACKING);
        if(optionIsEnabled(QUALIFICATION_DDL)) actions.add(ReportAction.QUALIFICATION);
        if(optionIsEnabled(CLASS_DDL)) actions.add(ReportAction.CLASS);
        if(optionIsEnabled(GRADE_TYPE_DDL)) actions.add(ReportAction.GRADE_TYPE);
        if(optionIsEnabled(AWARD_CLASS_DDL)) actions.add(ReportAction.AWARD_CLASS);
        if(optionIsEnabled(KS2_CORE_DDL)) actions.add(ReportAction.KS2_CORE);
        if(optionIsEnabled(GRADE_FILTER_TYPE_DDL)) actions.add(ReportAction.FOCUS_GRADE_OPERATOR);
        if(optionIsEnabled(GRADE_FILTER_WHOLE_DDL)) actions.add(ReportAction.FOCUS_GRADE);
        if(optionIsEnabled(GRADE_FILTER_SUB_DDL)) actions.add(ReportAction.FOCUS_SUB_GRADE);
        if(optionIsEnabled(COMP_GRADE_FILTER_TYPE_DDL)) actions.add(ReportAction.COMPARE_GRADE_OPERATOR);
        if(optionIsEnabled(COMP_GRADE_FILTER_WHOLE_DDL)) actions.add(ReportAction.COMPARE_GRADE);
        if(optionIsEnabled(COMP_GRADE_FILTER_SUB_DDL)) actions.add(ReportAction.COMPARE_SUB_GRADE);
        if(optionIsEnabled(IN_A8_BASKET_DDL)) actions.add(ReportAction.IN_A8_BASKET);
        if(optionIsEnabled(STUDENT_FILTER_DDL)) actions.add(ReportAction.STUDENT);
        return actions;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        super.selectAndExpandTab();
        switch(action){
            case EAP_TRACKING:
                return getEAPTrackingOptions();
            case FACULTY:
            case QUALIFICATION:
            case CLASS:
            case GRADE_TYPE:
            case AWARD_CLASS:
            case KS2_CORE:
            case FOCUS_GRADE_OPERATOR:
            case FOCUS_GRADE:
            case FOCUS_SUB_GRADE:
            case COMPARE_GRADE_OPERATOR:
            case COMPARE_GRADE:
            case COMPARE_SUB_GRADE:
            case IN_A8_BASKET:
            case STUDENT:
                return getDDLOptions(action);
            default:
                throw new IllegalArgumentException(action.name()+" is not a valid action on the Options tab");
        }
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        super.selectAndExpandTab();
        if(action.equals(ReportAction.EAP_TRACKING)){
            filterByTrack(option);
        } else {
            WebElement targetDDL = driver.findElement(getLocatorForDDLAction(action));
            new Select(targetDDL).selectByVisibleText(option);
            waitForLoadingWrapper();
        }
        return new EAPListView(driver);
    }

    /*  */
    private List<String> getEAPTrackingOptions(){
        List<String> options = new ArrayList<String>();
        WebElement menu = driver.findElement(ON_TRACK_MENU);
        List<WebElement> inputs = menu.findElements(By.tagName("input"));
        for(WebElement input : inputs){
            if(!input.isSelected()){
                options.add(menu.findElement(By.id(input.getAttribute("for"))).getText());
            }
        }
        return options;
    }

    private List<String> getDDLOptions(ReportAction action){
        List<String> optionNames = new ArrayList<String>();
        WebElement select = driver.findElement(getLocatorForDDLAction(action));
        List<WebElement> options = select.findElements(By.tagName("option"));
        for(WebElement option : options){
            if(!option.getAttribute("class").contains("disabled")){
                optionNames.add(option.getText());
            }
        }
        return optionNames;
    }

    private By getLocatorForDDLAction(ReportAction action){
        switch(action){
            case FACULTY:
                return FACULTY_DDL;
            case QUALIFICATION:
                return QUALIFICATION_DDL;
            case CLASS:
                return CLASS_DDL;
            case GRADE_TYPE:
                return GRADE_TYPE_DDL;
            case AWARD_CLASS:
                return AWARD_CLASS_DDL;
            case KS2_CORE:
                return KS2_CORE_DDL;
            case FOCUS_GRADE_OPERATOR:
                return GRADE_FILTER_TYPE_DDL;
            case FOCUS_GRADE:
                return GRADE_FILTER_WHOLE_DDL;
            case FOCUS_SUB_GRADE:
                return GRADE_FILTER_SUB_DDL;
            case COMPARE_GRADE_OPERATOR:
                return COMP_GRADE_FILTER_TYPE_DDL;
            case COMPARE_GRADE:
                return COMP_GRADE_FILTER_WHOLE_DDL;
            case COMPARE_SUB_GRADE:
                return COMP_GRADE_FILTER_SUB_DDL;
            case IN_A8_BASKET:
                return IN_A8_BASKET_DDL;
            case STUDENT:
                return STUDENT_FILTER_DDL;
            default:
                throw new IllegalArgumentException(action.name()+" is not a valid action on the Options tab");
        }
    }

}

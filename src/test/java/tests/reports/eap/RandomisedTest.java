package tests.reports.eap;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.ReportViewModal_Filters;
import pages.reports.components.ReportViewModal_Measures;
import pages.reports.components.ReportViewModal_Residuals;
import pages.reports.components.ReportsHome_EAPYearGroup;
import pages.reports.interfaces.IReportActionGroup;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.fail;

public abstract class RandomisedTest extends BaseTest {

    protected EAPView report;
    private ReportsHome_EAP reportsHome;
    private ReportsHome_EAPYearGroup yearDataGroup;
    private WebElement reportSet;
    private EAPView reportView;

    @BeforeTest()
    @Step ( "Login, Open the required Report, and apply required Options " )
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass){

        // Check against list
        // fail("Skipped test - " + testContext.getName() + " your name's not down you're not coming in")
        // Creates the browser session and initialises some global test properties
        String initResult = super.initialise(testContext);
        if (!initResult.equals("")){
            fail(initResult);
        }

        try {
            // Login
            login(user, pass, true);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_SetupFail.png");
            if (driver!=null){
                driver.quit();
            }
            e.printStackTrace();
            fail("Test Setup Failed! Exception: "+e.getMessage());
        }
    }

    @Test
    @Parameters( { "cohort", "maxLoops" } )
    public void testRandomReportActions(String cohort, int maxLoops){
        try {
            gotoReportsHome();
            selectReportsCohort(cohort);
            expandEAPYearGroup(selectRandomEAPYearGroup());
            expandReportSet(selectRandomReportSet());
            WebElement reportAreaButton = selectRandomReportButton();
            reportView = openReport(reportAreaButton, reportAreaButton.getText());
        } catch (Exception e){
            saveScreenshot(context.getName()+"_InitialReportOpen.png");
            throw e;
        }
        for (int loopIndex = 1; loopIndex <= maxLoops; loopIndex++){
        }

    }

    @Step( "Go to the EAP Reports Home page" )
    private void gotoReportsHome(){
        reportsHome = new ReportsHome_EAP(driver,true);
    }

    @Step( "Select ${cohort}" )
    private void selectReportsCohort(String cohort) {
        reportsHome.selectCohortByUserAction(cohort);
    }

    private String selectRandomEAPYearGroup(){
        List<WebElement> yearGroups = reportsHome.countEAPYearGroups();
        int yearGroupIndex = new Random().nextInt(yearGroups.size());
        return yearGroups.get(yearGroupIndex).getAttribute("data-year");
    }

    @Step( "Click Year Group ${year}" )
    private void expandEAPYearGroup(String year){
        yearDataGroup = new ReportsHome_EAPYearGroup(driver, year);
        yearDataGroup.expandYear();
    }

    private String selectRandomReportSet(){
        List<WebElement> reportSets = yearDataGroup.getPublishedReportSets();
        int pubReportIndex = new Random().nextInt(reportSets.size());
        reportSet = reportSets.get(pubReportIndex);
        return yearDataGroup.getReportSetName(reportSet);
    }

    @Step( "Expand Report Set ${datasetName}" )
    private void expandReportSet(String datasetName){
        yearDataGroup.expandPublishedReport(datasetName);
    }

    private WebElement selectRandomReportButton(){
        List<WebElement> buttons = yearDataGroup.getReportButtons(reportSet);
        int buttonIndex = new Random().nextInt(buttons.size());
        return buttons.get(buttonIndex);
    }

    @Step( "Open Report Area ${areaName}" )
    private EAPView openReport(WebElement button, String areaName){
        button.click();
        return new EAPView(driver);
    }

    private void selectRandomActionGroup(){
        /*
        The following actionGroups are always available:
            - ViewSwitchActions
            - DatasetTabActions
            - OptionsTabActions
            - DisplayRowActions
        */
        List<IReportActionGroup> actionGroups = new ArrayList<IReportActionGroup>();
        actionGroups.add(report.dsOptions);
        actionGroups.add(report.gradeFilters);

        /*
        The following actionGroups are available depending on context:
            - FilterTabActions
            - MeasureTabActions
            - ResidualExclTabActions
            - DrillDownActions
        */

    }

/*
    @DataProvider(name = "reportFigures")
    public Iterator<Object[]> createData() throws IOException {
        List<Object []> testCases = new ArrayList<>();
        String[] data;

        BufferedReader expectedBr = new BufferedReader(new FileReader(expectedFiguresFilePath));
        BufferedReader actualBr = new BufferedReader(new FileReader(actualFiguresFilePath));
        String expectedLine; String actualLine;
        int rowNum = 0;
        while ((expectedLine = expectedBr.readLine()) != null &&
                (actualLine = actualBr.readLine()) != null){
            rowNum++;
            data = (rowNum+"~"+expectedLine+"~"+actualLine).split("~");
            testCases.add(data);
        }
        return testCases.iterator();
    }
*/


    @Step( "Apply report options" )
    public void applyReportOptions(){
        // Actions in the Dataset/Tracker tabs
        applyAllDatasetOptions();

        // Actions in the Student Filters tab
        applyAllStudentFilters();

        // Actions in the Measure Filters tab
        applyAllMeasureFilters();

        // Actions in the Residual Exclusions tab
        applyAllResidualExclusions();

        // Actions in the Grade Filters area
        applyAllGradeFilterOptions();

        // Actions in the View Display Options area
        applyAllViewOptions();

    }


    public void applyAllDatasetOptions(){
        String[] datasetOptions = getArrayParam("dataset-options");
        if (!datasetOptions[0].equals("")) {
            for (String datasetOption : datasetOptions) {
                int delimIndex = datasetOption.indexOf("=");
                String field = datasetOption.substring(0, delimIndex);
                String value = datasetOption.substring(delimIndex + 1);
                applyReportDatasetOption(field, value);
            }
        }
        if(debugMode){
            saveScreenshot(context.getName()+"-Post-apply_DatasetOptions.png");
        }
    }

    @Step( "Apply Dataset option '{field}' = '{value}'" )
    public void applyReportDatasetOption(String field, String value){
        switch (field){
            case "Tab":
                report = report.dsOptions.showFocusDataAs(value);
                break;
            case "Actual":
                report = report.dsOptions.selectFocusDataset(value);
                break;
            case "Compare":
                report = report.dsOptions.selectCompareDataset(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown Dataset Option '"+field+"'");
        }

    }


    public void applyAllStudentFilters(){
        String[] filters = getArrayParam("filters");
        if(!filters[0].equals("")){
            ReportViewModal_Filters stuFiltersModal = report.reportTabs.openStudentFiltersModal();
            for (String filter : filters){
                int delimIndex = filter.indexOf("=");
                String filterName = filter.substring(0,delimIndex);
                String filterValue = filter.substring(delimIndex+1);
                toggleStudentFilter(stuFiltersModal, filterName, filterValue);
            }
            stuFiltersModal.apply();
            if (debugMode) {
                report.reportTabs.expandStudentFiltersTab();
                saveScreenshot(context.getName()+"-Post-apply_StuFilters.png");
            }
        }
    }

    @Step( "Toggle Student Filter {filterName}[{filterVal}]" )
    public void toggleStudentFilter(ReportViewModal_Filters stuFiltersModal, String filterName, String filterVal){
        stuFiltersModal.toggleFilterValue(filterName, filterVal);
    }


    public void applyAllMeasureFilters(){
        String[] measures = getArrayParam("measures");
        if(!measures[0].equals("")){
            ReportViewModal_Measures measFiltersModal = report.reportTabs.openMeasureFiltersModal();
            for (String measure : measures){
                int delimIndex = measure.indexOf("=");
                String measureName = measure.substring(0,delimIndex);
                String measureValue = measure.substring(delimIndex+1);
                String[] actCompValues = {"",""};

                if (measureValue.contains("+")){
                    actCompValues = measureValue.split("\\+");
                    actCompValues[1] = "Compare"+actCompValues[1];
                } else {
                    actCompValues[0] = measureValue;
                }
                applyMeasureFilter(measFiltersModal, measureName, actCompValues[0], actCompValues[1]);
            }
            measFiltersModal.apply();
            if (debugMode) {
                report.reportTabs.expandMeasureFiltersTab();
                saveScreenshot(context.getName()+"-Post-apply_MeasFilters.png");
            }
        }
    }

    @Step ( "Click {actualValue}|{compValue} Measure Filter Options for {measureName}" )
    public void applyMeasureFilter(ReportViewModal_Measures measFiltersModal, String measureName, String actualValue, String compValue){
        if (!actualValue.equals("")){
            measFiltersModal.clickMeasureFilterOption(measureName, actualValue);
        }
        if (!compValue.equals("")){
            measFiltersModal.clickMeasureFilterOption(measureName, compValue);
        }
    }


    public void applyAllResidualExclusions(){
        String[] residuals = getArrayParam("residuals");
        if(!residuals[0].equals("")){
            ReportViewModal_Residuals exclusionsModal = report.reportTabs.openResidualExclusionsModal();
            for(String residual : residuals){
                toggleResidualExclusion(exclusionsModal, residual);
            }
            exclusionsModal.apply();
            if (debugMode) {
                report.reportTabs.expandResidualExclusionsTab();
                saveScreenshot(context.getName()+"-Post-apply_ResidExcl.png");
            }
        }
    }

    @Step( "Toggle the residual exclusion setting for {qualName}" )
    public void toggleResidualExclusion(ReportViewModal_Residuals exclusionsModal, String qualName){
        exclusionsModal.toggleQualExclusion(qualName);
    }


    public void applyAllGradeFilterOptions(){
        String[] gradeFilterOptions = getArrayParam("grade-filter-options");
        if (!gradeFilterOptions[0].equals("")) {
            for (String gradeFilterOption : gradeFilterOptions){
                int delimIndex = gradeFilterOption.indexOf("=");
                String field = gradeFilterOption.substring(0,delimIndex);
                String value = gradeFilterOption.substring(delimIndex+1);
                applyGradeFilterOptions(field, value);
            }
            if (debugMode) {
                report.gradeFilters.selectAndExpandTab();
                saveScreenshot(context.getName()+"-Post-apply_GradeFilters.png");
            }
        }
    }

    @Step( "Apply Grade Filter option '{field}' = '{value}'" )
    private void applyGradeFilterOptions(String field, String value){
        switch (field){
            case "OnTrack":
                report = report.gradeFilters.filterByTrack(value);
                break;
            case "Faculty":
                report = report.gradeFilters.selectFaculty(value);
                break;
            case "Qualification":
                report = report.gradeFilters.selectQualification(value);
                break;
            case "Class":
                report = report.gradeFilters.selectClass(value);
                break;
            case "Grade Type":
                report = report.gradeFilters.selectGradeType(value);
                break;
            case "GCSE/Non-GCSE":
                report = report.gradeFilters.selectAwardClass(value);
                break;
            case "KS2 Core":
                report = report.gradeFilters.selectKS2Core(value);
                break;
            case "Actual Grade Operator":
                report = report.gradeFilters.selectGradeFilterType(value);
                break;
            case "Actual Grade Grade":
                report = report.gradeFilters.selectGradeFilterWhole(value);
                break;
            case "Actual Grade Subgrade":
                report = report.gradeFilters.selectGradeFilterSub(value);
                break;
            case "Compare Grade Operator":
                report = report.gradeFilters.selectCompGradeFilterType(value);
                break;
            case "Compare Grade Grade":
                report = report.gradeFilters.selectCompGradeFilterWhole(value);
                break;
            case "Compare Grade Subgrade":
                report = report.gradeFilters.selectCompGradeFilterSub(value);
                break;
            case "Student":
                report = report.gradeFilters.selectStudent(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown Grade Filter Option '"+field+"'");
        }
    }


    public void applyAllViewOptions(){
        String[] viewOptions = getArrayParam("view-options");
        if (!viewOptions[0].equals("")) {
            for (String viewOption : viewOptions){
                int delimIndex = viewOption.indexOf("=");
                String field = viewOption.substring(0,delimIndex);
                String value = viewOption.substring(delimIndex+1);
                applyViewOption(field, value);
            }
            if (debugMode) {
                saveScreenshot(context.getName()+"-Post-apply_ViewOptions.png");
            }
        }
    }

    @Step( "Apply View option '{field}' = '{value}'" )
    private void applyViewOption(String field, String value){
        if (debugMode) {
            saveScreenshot(context.getName()+"-Pre-apply_"+field+"["+value+"].png");
        }
        switch (field){
            case "Column Sort":
                report = report.viewOptions.selectColSort(value);
                break;
            case "Column Sort Direction":
                report = report.viewOptions.setColSortDirection(value);
                report = report.viewOptions.toggleColSortDirection();
                report = report.viewOptions.toggleColSortDirection();
                break;
            case "FigureType": case "Figure Type":
                report = report.viewOptions.setFigType(value);
                break;
            case "StdCum":
                report = report.viewOptions.setCalcType(value);
                break;
            case "Breakdown":
                report = report.viewOptions.selectBreakdown(value);
                break;
            case "Student Info":
                report = report.viewOptions.selectStudentInfo(value);
                break;
            case "In A8 Basket":
                // ToDo: Investigate breaking the config to move setting this into the #applyGradeFilterOptions method
                report = report.gradeFilters.selectInA8Basket(value);
                break;
            case "Sub Whole":
                report = report.viewOptions.setSubWhole(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown View Option '"+field+"'");
        }
        if (debugMode) {
            saveScreenshot(context.getName()+"-Post-apply_"+field+"["+value+"].png");
        }
    }

}

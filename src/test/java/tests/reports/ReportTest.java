package tests.reports;

import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pages.AnalyticsPage;
import pages.reports.EAPListView;
import pages.reports.EAPReportView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.Report_AddMeasureFilters;
import pages.reports.components.Report_AddResidualExclusions;
import pages.reports.components.Report_AddStudentFilters;
import tests.BaseTest;

import java.net.MalformedURLException;

import static org.testng.Assert.fail;

public abstract class ReportTest extends BaseTest {

    protected EAPReportView report;

    @BeforeTest()
    @Step ( "Login, Open the required Report, and apply required Options " )
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass)
            throws MalformedURLException{
        super.initialise(testContext);

        try {
            // Login
            login(user, pass, true);

            // Open the reports for the correct dataset
            report = openTestDataset(getStringParam("cohort"),
                    getStringParam("year"),
                    getStringParam("dataset"),
                    getStringParam("button", "Grades"));

            // Open the test view at the right level
            report = openTestView(report,
                    getStringParam("report-area"),
                    getStringParam("report-view"),
                    getStringParam("report-level"));

            // Apply any report options, filters, etc. defined in the test parameters
            applyReportOptions();

        } catch (Exception e){
            saveScreenshot(context.getName()+"_SetupFail.png");
            if (driver!=null){
                new AnalyticsPage(driver).clickMenuLogout();
                driver.quit();
                e.printStackTrace();
                fail("Test Setup Failed! Was the data in the Restore school copied over? Exception: "+e.getMessage());
            }
        }
    }

    @Step( "Open the {cohort} > {year} > {dataset} > {button} report" )
    public EAPListView openTestDataset(String cohort, String year, String dataset, String button){
        ReportsHome_EAP reports = new ReportsHome_EAP(driver,true);
        return reports.
                selectCohortByUrl(cohort).
                getYearAccordion(year).
                gotoPublishedReport(dataset, false, button);
    }

    @Step( "Switch to the {area} > {view} report at {level} Level" )
    public EAPReportView openTestView(EAPReportView report, String area, String view, String level){
        return report.openView(area,view,level);
    }

    @Step( "Apply report options" )
    public void applyReportOptions(){
        String field; String value; int delimIndex;

        String[] datasetOptions = getArrayParam("dataset-options");
        if (!datasetOptions[0].equals("")) {
            applyAllDatasetOptions(datasetOptions);
        }

        String[] gradeFilterOptions = getArrayParam("grade-filter-options");
        if (!gradeFilterOptions[0].equals("")) {
            applyAllGradeFilterOptions(gradeFilterOptions);
        }

        String[] viewOptions = getArrayParam("view-options");
        if (!viewOptions[0].equals("")) {
            applyAllViewOptions(viewOptions);
        }

        String[] filters = getArrayParam("filters");
        if(!filters[0].equals("")){
            applyAllStudentFilters(filters);
        }

        String[] measures = getArrayParam("measures");
        if(!measures[0].equals("")){
            applyAllMeasureFilters(measures);
        }

        String[] residuals = getArrayParam("residuals");
        if(!residuals[0].equals("")){
            Report_AddResidualExclusions exclusionsModal = report.filterTabs.openResidualExclusionsModal();
            for(String residual : residuals){
                toggleResidualExclusion(exclusionsModal, residual);
            }
            exclusionsModal.apply();
        }
    }

    public void applyAllDatasetOptions(String[] datasetOptions){
        String field; String value; int delimIndex;
        for (String datasetOption : datasetOptions) {
            delimIndex = datasetOption.indexOf("=");
            field = datasetOption.substring(0, delimIndex);
            value = datasetOption.substring(delimIndex + 1);
            applyReportDatasetOption(field, value);
        }
    }

    @Step( "Apply Dataset option '{field}' = '{value}'" )
    public void applyReportDatasetOption(String field, String value){
        switch (field){
            case "Tab":
                report = report.dsOptions.switchTab(value);
                break;
            case "Actual":
                report = report.dsOptions.selectDataset(value);
                break;
            case "Compare":
                report = report.dsOptions.selectCompareWith(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown Dataset Option '"+field+"'");
        }

    }

    public void applyAllGradeFilterOptions(String[] gradeFilterOptions){
        String field; String value; int delimIndex;
        for (String gradeFilterOption : gradeFilterOptions){
            delimIndex = gradeFilterOption.indexOf("=");
            field = gradeFilterOption.substring(0,delimIndex);
            value = gradeFilterOption.substring(delimIndex+1);
            applyGradeFilterOptions(field, value);
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

    public void applyAllViewOptions(String[] viewOptions){
        String field; String value; int delimIndex;
        for (String viewOption : viewOptions){
            delimIndex = viewOption.indexOf("=");
            field = viewOption.substring(0,delimIndex);
            value = viewOption.substring(delimIndex+1);
            applyViewOptions(field, value);
        }
    }

    @Step( "Apply View option '{field}' = '{value}'" )
    private void applyViewOptions(String field, String value){
        switch (field){
            case "Column Sort":
                report = report.viewOptions.selectColSort(value);
                break;
            case "Column Sort Direction":
                report = report.viewOptions.setColSortDirection(value);
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
                report = report.viewOptions.selectInA8Basket(value);
                break;
            case "Sub Whole":
                report = report.viewOptions.setSubWhole(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown View Option '"+field+"'");
        }
    }

    public void applyAllStudentFilters(String[] filters){
        String filterName; String filterValue; int delimIndex;
        Report_AddStudentFilters stuFiltersModal = report.filterTabs.openStudentFiltersModal();
        for (String filter : filters){
            delimIndex = filter.indexOf("=");
            filterName = filter.substring(0,delimIndex);
            filterValue = filter.substring(delimIndex+1);
            toggleStudentFilter(stuFiltersModal, filterName, filterValue);
        }
        stuFiltersModal.apply();
    }

    @Step( "Toggle Student Filter {filterName}[{filterVal}]" )
    public void toggleStudentFilter(Report_AddStudentFilters stuFiltersModal, String filterName, String filterVal){
        stuFiltersModal.toggleFilterValue(filterName, filterVal);
    }

    public void applyAllMeasureFilters(String[] measures){
        String measureName; String measureValue; int delimIndex;

        Report_AddMeasureFilters measFiltersModal = report.filterTabs.openMeasureFiltersModal();
        for (String measure : measures){
            delimIndex = measure.indexOf("=");
            measureName = measure.substring(0,delimIndex);
            measureValue = measure.substring(delimIndex+1);
            int compDelimIndex = measureValue.indexOf("¬");
            String actualValue; String compValue = "";
            if(compDelimIndex > -1){
                actualValue = measureValue.substring(0,compDelimIndex);
                compValue = "Compare"+measureValue.substring(compDelimIndex+1);
            } else {
                actualValue = measureValue;
            }
            applyMeasureFilter(measFiltersModal, measureName, actualValue, compValue);
        }
        measFiltersModal.apply();
    }

    @Step ( "Click {actualValue}|{compValue} Measure Filter Options for {measureName}" )
    public void applyMeasureFilter(Report_AddMeasureFilters measFiltersModal, String measureName, String actualValue, String compValue){
        if (!actualValue.equals("")){
            measFiltersModal.clickMeasureFilterOption(measureName, actualValue);
        }
        if (!compValue.equals("")){
            measFiltersModal.clickMeasureFilterOption(measureName, compValue);
        }
    }

    @Step( "Toggle the residual exclusion setting for {qualName}" )
    public void toggleResidualExclusion(Report_AddResidualExclusions exclusionsModal, String qualName){
        exclusionsModal.toggleQualExclusion(qualName);
    }
}

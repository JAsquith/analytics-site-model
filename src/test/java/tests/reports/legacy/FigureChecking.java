package tests.reports.legacy;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.basedata.PublishBaseData;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import pages.data.students.PublishStudents;
import pages.reports.EAPListView;
import pages.reports.EAPView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.ReportsHome_CohortsMenu;
import pages.reports.components.ReportsHome_EAPYearGroup;
import pages.reports.interfaces.IReportModal;
import tests.SISRATest;
import utils.ViewDataFileManager;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class FigureChecking extends SISRATest {

    @Test
    public void checkReportTables(){
        try {
            this.login();

            EAPView reportPage = this.openReport();
            reportPage = this.openView(reportPage);
            reportPage = this.applyStudentFilters(reportPage);
            reportPage = this.applyOptions(reportPage);

            String tableNames = getTestParam("tableNames");
            if(tableNames.equals("")) {
                this.checkMultiTableFigures(reportPage);
            } else {
                this.checkNamedTableFigures(reportPage);
            }

        } catch (AssertionError assertError){
            this.saveScreenshots();
            throw assertError;
        } catch (Exception other){
            this.saveScreenshots();
            throw other;
        }
    }

    private boolean republishIfNeeded(){
        if(getTestParamAsBool("publish-students")){
            this.publishStudentData();
        }
        if(getTestParamAsBool("publish-basedata")){

        }
        String publishDatasets[] = getTestParamAsStringArray("publish-datasets");
        if(!publishDatasets[0].equals("")){

        }
        if(getTestParamAsBool("generate-tracker")){

        }
        return true;
    }

    private void publishStudentData(){
        new AnalyticsPage(driver).waitForLoadingWrapper(30);
        DataHome dataPage = new DataHome(driver, true);
        DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
        modeAndCohort.selectMode("EAP");

        String cohort = getTestParam("eap-cohort");
        if (cohort.length() > 2){
            cohort = cohort.substring(cohort.length()-2);
        }

        DataSideMenu sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        if (!sideMenu.isTabSelected("STUDENTS")){
            sideMenu.clickTab("STUDENTS");
        }
        sideMenu.clickMenuOption("Publish Students");

        PublishStudents page = new PublishStudents(driver);
        int publishType = 0;
        if(getTestParam("publish-type").toLowerCase().equals("local")){
            publishType = 1;
        }
        page.clickPublishWaitAndClose(publishType);
    }

    private void publishBasedata(){
        String cohort = getTestParam("eap-cohort");
        if (cohort.length() > 2){
            cohort = cohort.substring(cohort.length()-2);
        }
        PublishBaseData page = new PublishBaseData(driver);
        page.load(cohort, true);

        int publishType = 0;
        if(getTestParam("publish-type").toLowerCase().equals("local")){
            publishType = 1;
        }
        page.clickPublishWaitAndClose(publishType);
    }

    private EAPView openReport(){
        // Choose Reports > KS > Cohort > Go To Reports (dataset)
        try {
            ReportsHome_EAP reportsHome = new ReportsHome_EAP(driver, true);
            currentPage++;
            ReportsHome_CohortsMenu cohortsMenu = reportsHome.getCohortsMenu();
            String cohortId = getTestParam("cohort");

            if (!cohortsMenu.getSelectedCohortID().equals(cohortId)){
                cohortsMenu.selectCohortByYearID(cohortId);
            }

            String trackerColumn = getTestParam("tracker-column");


            ReportsHome_EAPYearGroup accordion = reportsHome.getEAPYearGroup(
                    getTestParam("eapYear"),
                    trackerColumn);

            EAPView report = accordion.gotoPublishedReport(
                    getTestParam("dataset"),
                    trackerColumn);

            currentPage++;

            return report;
        }
        catch (Exception e){
            System.out.println("Exception navigating to KS 3/4 tests.reports and opening dataset");
            throw e;
        }
    }

    private EAPView openView(EAPView reportPage){
        // Choose the Area > Report > Level
        try {
            reportPage = reportPage.openView(
                    getTestParam("area"),
                    getTestParam("report"),
                    getTestParam("level"));

            if (reportPage == null){
                throw new IllegalStateException("Requested Area is disabled");
            }

            return reportPage;
        }
        catch (Exception e){
            System.out.println("Exception choosing Area > Report > Level");
            throw e;
        }
    }

    private EAPView applyStudentFilters (EAPView reportPage){

        String filterGroupsParam = getTestParam("filterGroups");
        String filterValuesParam = getTestParam("filterValues");

        if (filterGroupsParam.equals("")){
            return reportPage;
        }

        String[] groups = filterGroupsParam.split("¬");
        String[] values = filterValuesParam.split("¬");

        IReportModal filtersModal = reportPage.filtersTab.openModal();

        for(int i = 0; i < groups.length; i++){
            String group = groups[i];
            String value = values[i];
            filtersModal.toggleOption(group, value);
        }

        filtersModal.applyChanges();

        return new EAPListView(driver);

    }

    private EAPView applyOptions(EAPView reportPage){
        // Apply report/view options defined in test params
        try {
            if (!getTestParam("compWith").equals(""))
                reportPage.datasetsTab.selectCompareCollection(getTestParam("compWith"));
            if(!getTestParam("faculty").equals(""))
                reportPage.optionsTab.selectFaculty(getTestParam("faculty"));
            if(!getTestParam("qual").equals(""))
                reportPage.optionsTab.selectQualification(getTestParam("qual"));
            if(!getTestParam("class").equals(""))
                reportPage.optionsTab.selectClass(getTestParam("class"));
            if(!getTestParam("tracking").equals(""))
                reportPage.optionsTab.filterByTrack(getTestParam("tracking"));
            if(!getTestParam("gradeType").equals(""))
                reportPage.optionsTab.selectGradeType(getTestParam("gradeType"));
            if(!getTestParam("gcseOrNot").equals(""))
                reportPage.optionsTab.selectAwardClass(getTestParam("gcseOrNot"));
            if(!getTestParam("ks2Core").equals(""))
                reportPage.optionsTab.selectKS2Core(getTestParam("ks2Core"));
            if(!getTestParam("gradeSearchType").equals(""))
                reportPage.optionsTab.selectGradeFilterType(getTestParam("gradeSearchType"));
            if(!getTestParam("gradeSearchWhole").equals(""))
                reportPage.optionsTab.selectGradeFilterWhole(getTestParam("gradeSearchWhole"));
            if(!getTestParam("gradeSearchSub").equals(""))
                reportPage.optionsTab.selectGradeFilterSub(getTestParam("gradeSearchSub"));
            if(!getTestParam("colSort").equals(""))
                reportPage.viewOptions.selectColSort(getTestParam("colSort"));
            if(!getTestParam("figType").equals(""))
                reportPage.viewOptions.setFigType(getTestParam("figType"));
            if(!getTestParam("calcType").equals(""))
                reportPage.viewOptions.setCalcType(getTestParam("calcType"));
            if(!getTestParam("breakdown").equals(""))
                reportPage.viewOptions.selectBreakdown(getTestParam("breakdown"));
            if (getTestParam("sortDir").toLowerCase().equals("desc")){
                reportPage.viewOptions.toggleColSortDirection();
            }
            return new EAPListView(driver);
        }
        catch (Exception e){
            System.out.println("Exception setting report options");
            throw e;
        }
    }

    private void checkMultiTableFigures(EAPView reportPage){
        int diffResult;

        String expectedDataFile  = getTestParam("dataFiles");

        ViewDataFileManager fileMgr = new ViewDataFileManager();
        EAPListView report = (EAPListView)reportPage;
        fileMgr.createTableDataFileWithData("Found", expectedDataFile, report.readTableData());

        diffResult = fileMgr.findFileDifference(expectedDataFile, "Found", "Diff");

        System.out.println("Compared [" + expectedDataFile + "] with [Extracted Report Data]");

        switch (diffResult){
            case -2:
                System.out.println("Somehow managed not to compare anything");
                break;
            case -1:
                System.out.println("Something went wrong during the comparison");
                break;
            case 0:
                System.out.println(">>>>> The Files Match!");
                break;
            default:
                System.out.println(">>>>> " + diffResult + " rows are different between expected and actual table data");
                System.out.println("Differences can be examined in [" +
                        fileMgr.getTableDataDirectory() + File.separator +"Diff" + File.separator + "]");
        }

        assertThat("Rows with differences",
                diffResult,
                is(0));

    }

    private void checkNamedTableFigures(EAPView reportPage){
        String[] tables = getTestParamAsStringArray("tableNames");
        String[] files = getTestParamAsStringArray("dataFiles");

        List<String> tableNames = Arrays.asList(tables);
        List<String> expectedDataFiles = Arrays.asList(files);

        int diffResult = -2;

        for (String tableName: tableNames){
            String expectedDataFile = expectedDataFiles.get(tableNames.indexOf(tableName));

            ViewDataFileManager fileMgr = new ViewDataFileManager();
            EAPListView report = (EAPListView)reportPage;
            fileMgr.createTableDataFileWithData("Found", expectedDataFile, report.readTableData(tableName));

            diffResult = fileMgr.findFileDifference(expectedDataFile, "Found", "Diff");
            System.out.println("Compared [" + expectedDataFile + "] with [Extracted Report Data]");
            switch (diffResult){
                case -2:
                    System.out.println("Somehow managed not to compare anything");
                    break;
                case -1:
                    System.out.println("Something went wrong during the comparison");
                    break;
                case 0:
                    System.out.println(">>>>> The Files Match!");
                    break;
                default:
                    System.out.println(">>>>> " + diffResult + " rows are different between expected and actual table data");
                    System.out.println("Differences can be examined in [" +
                            fileMgr.getTableDataDirectory() + File.separator +"Diff" + File.separator + "]");
            }
        }

        assertThat("Rows with differences",
                diffResult,
                is(0));

    }

    @BeforeTest
    public void setup(ITestContext testContext) throws MalformedURLException {

        try {
            this.initialise(testContext);
        } catch (MalformedURLException e){
            return;
        }
    }

    @AfterTest
    public void tearDown() {
        try {
            if (driver.getSessionId() != null) {
                AnalyticsPage page = new AnalyticsPage(driver);
                page.waitForLoadingWrapper();
                page.clickMenuLogout();
                page.waitForLoadingWrapper();
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void successCriteria_ValidationPassed() {
        // Todo
    }

    @Override
    protected void checkForPageValidationFail(List<String> actualMessages){
        // This stops the version in the SISRATest super class from running??
    }
}

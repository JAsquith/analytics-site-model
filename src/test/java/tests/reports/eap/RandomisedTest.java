package tests.reports.eap;

import enums.ReportAction;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.*;
import pages.reports.EAPView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.ReportActions_Table;
import pages.reports.components.ReportsHome_EAPYearGroup;
import pages.reports.interfaces.IReportActionGroup;
import tests.BaseTest;

import java.util.*;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.testng.Assert.fail;

public class RandomisedTest extends BaseTest {

    protected EAPView report;
    private ReportsHome_EAP reportsHome;
    private ReportsHome_EAPYearGroup yearDataGroup;
    private WebElement reportSet;
    private IReportActionGroup group;
    private ReportAction action;
    private String option;
    private int zeroCohortCount = 0;
    private int zeroEntriesCount = 0;
    private int requiredFieldsCount = 0;
    private Map<ReportAction, List<String>> staticOptionsForAction = new HashMap<>();
    private Map<String, List<ReportAction>> actionOptionsToResetOn = new HashMap<>();

    @BeforeTest()
    @Step ( "Login, Open the required Report, and apply required Options " )
    @Parameters( { "username", "password", "cohort" })
    public void setup(ITestContext testContext, String user, String pass, String cohort){

        // Check against list
        // fail("Skipped test - " + testContext.getName() + " your name's not down you're not coming in")
        // Creates the browser session and initialises some global test properties
        String initResult = super.initialise(testContext);
        if (!initResult.equals("")){
            fail(initResult);
        }

        login(user, pass, true);
        setupNewReport(cohort);
    }

    @Test( description = "Open Random Report" )
    @Step( "Check the initial opening of a Report worked" )
    public void reportOpenedOK(){
        assertWithScreenshot("Opening a Report View should not error",
                report.getErrorMessage(), isEmptyOrNullString());
    }

    @Test( description = "Random Report Actions", dataProvider = "randomLoops", dependsOnMethods = { "reportOpenedOK" })
    public void testRandomReportActions(String loopIndex, String maxLoops){

        Random rnd = new Random();
        try {
            // logToAllure(++logCount, String.format("Test %s of %s",loopIndex, maxLoops));

            resetActionOptions();

            buildAction(rnd);

            report = applyRandomAction(group, group.getName(), action, option, action.name());

        } catch (Exception e){
            saveScreenshot(context.getName()+"4_testException.png");
            logToAllure(++logCount, "Exception during Test Method: "+e.getClass().getName());
            logToAllure(++logCount, "Exception message: "+e.getMessage());
            logToAllure(++logCount, "Stack trace: "+getStackTraceAsString(e));
            throw e;
        }
    }

    @AfterMethod
    public void postActionChecks(){

        // If we hit an error page, go back to ReportsHome and re-open a new report
        if(!report.getErrorMessage().equals("")){
            restartFromReportsHome();
            zeroCohortCount = 0;
            zeroEntriesCount = 0;
            return;
        }

        // If there are no Students (and we're not on a Student Detail report, or no entries
        // we may need to clear the options
        if (!report.isStudentDetailReport() && report.getCohortCount()==0)
            zeroCohortCount++;

        if (report.isZeroEntriesReport())
            zeroEntriesCount++;

        boolean compareDSRequired = report.datasetsTab.compareRequired();
        int requiredOptionFieldsCount = report.optionsTab.requiredFields().size();

        if (compareDSRequired || requiredOptionFieldsCount>0)
            requiredFieldsCount++;

        // If we've gone more than 1 round with required options blank, set them
        if (requiredFieldsCount>1) {
            Random rnd = new Random();
            while (compareDSRequired || requiredOptionFieldsCount > 0) {
                if (compareDSRequired) {
                    group = report.datasetsTab;
                    action = ReportAction.CHANGE_COMPARE;
                    List<String> options = group.getOptionsForAction(action);
                    option = options.get(rnd.nextInt(options.size() - 1) + 1);
                    report = applyRandomAction(group, group.getName(), action, option, action.name());
                    requiredFieldsCount--;
                    compareDSRequired=false;
                } else {
                    group = report.optionsTab;
                    action = report.optionsTab.getRequiredAction();
                    List<String> options = group.getOptionsForAction(action);
                    option = options.get(rnd.nextInt(options.size() - 1) + 1);
                    report = applyRandomAction(group, group.getName(), action, option, action.name());
                    requiredFieldsCount--;
                    requiredOptionFieldsCount--;
                }
            }
        }

        // If We've gone more than two rounds with zero students/grades, reset everything
        if (zeroCohortCount>2 || zeroEntriesCount>2){
            resetAll();
        }
    }

/*
    @Step( "Choosing random value from required field '{fieldLabel}'" )
    private EAPView setRequiredField(WebElement reqField, String fieldLabel){
        return report.optionsTab.setRequiredField(reqField);
    }
*/
    @Step( "Restarting from Reports Home Page after an Error" )
    private void restartFromReportsHome(){
        setupNewReport(getStringParam("cohort"));
    }
    @Step( "Reset All Options (after three actions with no students/entries)" )
    private void resetAll(){
        report = report.resetAllOptions();
        zeroCohortCount = 0;
        zeroEntriesCount = 0;
    }

    /* Setup Steps*/
    @Step( "Setup new report" )
    private void setupNewReport(String cohort){
        gotoReportsHome();
        selectReportsCohort(cohort);
        expandEAPYearGroup(selectRandomEAPYearGroup());
        expandReportSet(selectRandomReportSet());
        String reportArea = selectRandomReportButton();
        saveScreenshot(context.getName()+"_preReportOpen.png");
        report = clickReportAreaButton(reportArea);
        saveScreenshot(context.getName()+"_postReportOpen.png");
    }

    @Step( "Go to the EAP Reports Home page" )
    private void gotoReportsHome(){
        reportsHome = new ReportsHome_EAP(driver,true);
    }

    @Step( "Select {cohort}" )
    private void selectReportsCohort(String cohort) {
        reportsHome.selectCohortByUserAction(cohort);
    }

    private String selectRandomEAPYearGroup(){
        List<WebElement> yearGroups = reportsHome.countEAPYearGroups();
        int yearGroupIndex = new Random().nextInt(yearGroups.size());
        return yearGroups.get(yearGroupIndex).getAttribute("data-year");
    }

    @Step( "Click Year Group {year}" )
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

    @Step( "Expand Report Set {datasetName}" )
    private void expandReportSet(String datasetName){
        yearDataGroup.expandPublishedReport(datasetName);
    }

    /* Test Steps*/
    private String selectRandomReportButton(){
        List<WebElement> buttons = yearDataGroup.getReportButtons(reportSet);
        int buttonIndex = new Random().nextInt(buttons.size());
        return buttons.get(buttonIndex).getText();
    }

    @Step( "Open Report Area {areaName}" )
    private EAPView clickReportAreaButton(String areaName){
        for(WebElement button : yearDataGroup.getReportButtons(reportSet)){
            if(button.getText().equals(areaName)) {
                button.click();
                return new EAPView(driver);
            }
        }
        throw new IllegalArgumentException("The Report Button for '"+areaName+"' could not be found");
    }

    @Step( "Build a new TestAction" )
    private void buildAction(Random rnd){
        saveScreenshot(context.getName()+"1_beforeBuild.png");
        group = report.navMenu;
        action = ReportAction.NULL;
        option = "Null";

        chooseActionGroup(rnd);
        chooseAction(rnd);
        chooseActionOption(rnd);
    }

    private void chooseActionGroup(Random rnd) {
        try {
            List<IReportActionGroup> actionGroups = getActionGroupList();
            group = actionGroups.get(rnd.nextInt(actionGroups.size()));
            logToAllure(++logCount, "Chosen Group: " + group.getName());
        } catch (Exception e) {
            logToAllure(++logCount,"Exception getting valid Action Group: "+e.getClass().getName()+System.lineSeparator()+"Exception message: "+e.getMessage());
            throw e;
        }
    }

    private List<IReportActionGroup> getActionGroupList(){
        List<IReportActionGroup> actionGroups = new ArrayList<IReportActionGroup>();
        actionGroups.add(report.navMenu);
        actionGroups.add(report.datasetsTab);
        actionGroups.add(report.optionsTab);

        /* Todo: The following 'contingent' actionGroups need to be added:
            - DrillDownActions */
        if (report.filtersTab.isEnabled()) {
            actionGroups.add(report.filtersTab);
        }
        if (report.measuresTab.isEnabled()) {
            actionGroups.add(report.measuresTab);
        }
        if (report.residualsTab.isEnabled()) {
            actionGroups.add(report.residualsTab);
        }
        if (!report.isStudentDetailReport() && report.reportTables.size()>0){
            for (ReportActions_Table table : report.reportTables){
                actionGroups.add(table);
            }
        }

        String groupsDesc = "[";
        for(IReportActionGroup group : actionGroups){
            groupsDesc += group.getName()+",";
        }
        groupsDesc += "]";
        logToAllure(++logCount, "Valid ActionGroups: " + groupsDesc);

        return actionGroups;
    }

    private void chooseAction(Random rnd) {
        try {
            List<ReportAction> actions;
            actions = group.getValidActionsList();
            logToAllure(++logCount, "GroupActions: " + actions);
            action = actions.get(rnd.nextInt(actions.size()));
            logToAllure(++logCount, "Chosen Action: " + action.name.toUpperCase());
        } catch (Exception e) {
            logToAllure(++logCount,"Exception getting valid Test Action for Group: "+e.getClass().getName()+System.lineSeparator()+"Exception message: "+e.getMessage());
            throw e;
        }
        // Check whether we have any stored lists of actionOptions for actions which may change due to this current action:
        if(actionOptionsToResetOn.containsKey(action.name)){
            // We do!  Clear them out so if those actions are re-run the options list is re-built:
            for(ReportAction actionToReset : actionOptionsToResetOn.get(action.name)){
                staticOptionsForAction.remove(actionToReset);
            }
            actionOptionsToResetOn.remove(action.name);
        }
    }

    private void chooseActionOption(Random rnd) {
        try {
            List<String> actionOptions;
            if (action.optionsStatic){
                // The options for this action may be stored for re-use
                // Check if we have a copy of them from a previous test
                if (staticOptionsForAction.containsKey(action)) {
                    // We do :)! Use these
                    actionOptions = staticOptionsForAction.get(action);
                } else {
                    // We don't :(! Compile a list
                    actionOptions = group.getOptionsForAction(action);
                    // Store the list for any future tests of this action
                    staticOptionsForAction.put(action, actionOptions);

                    // Add this action to the list of those that will have their options list cleared if a certain
                    // other action ever happens:
                    List<ReportAction> actionOptionsToResetOnOld;
                    if(actionOptionsToResetOn.containsKey(action.staticUntil)){
                        actionOptionsToResetOnOld = actionOptionsToResetOn.get(action.staticUntil);
                    } else {
                        actionOptionsToResetOnOld = new ArrayList<>();
                    }
                    actionOptionsToResetOnOld.add(action);
                    actionOptionsToResetOn.put(action.staticUntil, actionOptionsToResetOnOld);
                }
            } else {
                actionOptions = group.getOptionsForAction(action);
            }
            logToAllure(++logCount, "TestActionOptions: " + actionOptions);
            option = actionOptions.get(rnd.nextInt(actionOptions.size()));
            logToAllure(++logCount, "Chosen Option: " + option);
        } catch (Exception e){
            logToAllure(++logCount,"Exception getting options for TestAction: "+e.getClass().getName()+System.lineSeparator()+ "Exception message: "+e.getMessage());
            throw e;
        }
    }

    @Step( "{groupName} > {actionName} > {option}" )
    private EAPView applyRandomAction(IReportActionGroup group, String groupName, ReportAction action, String option, String actionName){
        saveScreenshot(context.getName()+"2_beforeApply.png");
        EAPView newView = group.applyActionOption(action, option);
        assertWithScreenshot("Applying a Report Option/Action should not error",
                report.getErrorMessage(), isEmptyOrNullString());
        saveScreenshot(context.getName()+"3_success.png");
        return newView;
    }

    @Step( "Reset TestAction elements" )
    private void resetActionOptions(){
        group = null;
        action = ReportAction.NULL;
        option = "";
    }

    @DataProvider(name = "randomLoops")
    public Iterator<Object[]> createTestLoops(){
        List<Object []> testCases = new ArrayList<>();
        String[] data;

        int maxLoops = getIntegerParam("maxLoops", 25);
        for (int loopIndex = 1; loopIndex <= maxLoops; loopIndex++){
            data = (loopIndex+"~"+maxLoops).split("~");
            testCases.add(data);
        }
        return testCases.iterator();
    }

}
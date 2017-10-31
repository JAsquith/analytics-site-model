package tests.reports.eap;

import enums.ReportAction;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.ReportsHome_EAPYearGroup;
import pages.reports.interfaces.IReportActionGroup;
import tests.BaseTest;
import utils.ReportActionSet;

import java.util.*;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.testng.Assert.fail;

public class RandomisedTest extends BaseTest {

    protected EAPView report;
    private ReportsHome_EAP reportsHome;
    private ReportsHome_EAPYearGroup yearDataGroup;
    private WebElement reportSet;
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

            ReportActionSet actionSet = buildActionSet(rnd);

            report = applyRandomAction(actionSet, actionSet.getDescription());

        } catch (Exception e){
            saveScreenshot(context.getName()+"4_testException.png");
            logToAllure(++logCount, "Exception during Test Method: "+e.getClass().getName());
            logToAllure(++logCount, "Exception message: "+e.getMessage());
            logToAllure(++logCount, "Stack trace: "+getStackTraceAsString(e));
            throw e;
        }

        try {
            postActionChecks();
            assertWithScreenshot("PostActionChecks should not cause a a Report to error",
                    report.getErrorMessage(), isEmptyOrNullString());
        } catch (AssertionError assertionError){
            restartFromReportsHome();
            zeroCohortCount = 0;
            zeroEntriesCount = 0;
            throw assertionError;
        }

    }

//    @AfterMethod
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
                ReportActionSet actionSet = new ReportActionSet(report.datasetsTab);
                if (compareDSRequired) {
                    actionSet.action = ReportAction.CHANGE_COMPARE;
                    List<String> options = actionSet.group.getOptionsForAction(actionSet.action);
                    actionSet.option = options.get(rnd.nextInt(options.size() - 1) + 1);
                    report = applyRandomAction(actionSet, actionSet.getDescription());
                    requiredFieldsCount--;
                    compareDSRequired=false;
                } else {
                    actionSet.group = report.optionsTab;
                    actionSet.action = report.optionsTab.getRequiredAction();
                    List<String> options = actionSet.group.getOptionsForAction(actionSet.action);
                    actionSet.option = options.get(rnd.nextInt(options.size() - 1) + 1);
                    report = applyRandomAction(actionSet, actionSet.getDescription());
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
    private ReportActionSet buildActionSet(Random rnd){
        saveScreenshot(context.getName()+"1_beforeBuild.png");

        ReportActionSet actionSet = new ReportActionSet(report.navMenu);

        actionSet.group = chooseActionGroup(rnd);
        actionSet.action = chooseAction(actionSet.group, rnd);
        actionSet.option =  chooseActionOption(actionSet, rnd);

        return actionSet;
    }

    private IReportActionGroup chooseActionGroup(Random rnd) {
        IReportActionGroup actionGroup;
        try {
            List<IReportActionGroup> actionGroups = getActionGroupList();
            actionGroup = actionGroups.get(rnd.nextInt(actionGroups.size()));
            logToAllure(++logCount, "Chosen Group: " + actionGroup.getName());
        } catch (Exception e) {
            logToAllure(++logCount,"Exception getting valid Action Group: "+e.getClass().getName()+System.lineSeparator()+"Exception message: "+e.getMessage());
            throw e;
        }
        return actionGroup;
    }

    private List<IReportActionGroup> getActionGroupList(){
        List<IReportActionGroup> actionGroups = new ArrayList<IReportActionGroup>();
        actionGroups.add(report.navMenu);

        actionGroups.add(report.datasetsTab);
        actionGroups.add(report.optionsTab);

/*
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
*/

        String groupsDesc = "[";
        for(IReportActionGroup group : actionGroups){
            groupsDesc += group.getName()+",";
        }
        groupsDesc += "]";
        logToAllure(++logCount, "Valid ActionGroups: " + groupsDesc);

        return actionGroups;
    }

    private ReportAction chooseAction(IReportActionGroup actionGroup, Random rnd) {
        ReportAction reportAction;
        try {
            List<ReportAction> actions;
            actions = actionGroup.getValidActionsList();
            logToAllure(++logCount, "GroupActions: " + actions);
            reportAction = actions.get(rnd.nextInt(actions.size()));
            logToAllure(++logCount, "Chosen Action: " + reportAction.toString().toUpperCase());
        } catch (Exception e) {
            logToAllure(++logCount,"Exception getting valid Test Action for Group: "+e.getClass().getName()+System.lineSeparator()+"Exception message: "+e.getMessage());
            throw e;
        }
        // Check whether we have any stored lists of actionOptions for actions which may change due to this current action:
        if(actionOptionsToResetOn.containsKey(reportAction.name)){
            // We do!  Clear them out so if those actions are re-run the options list is re-built:
            for(ReportAction actionToReset : actionOptionsToResetOn.get(reportAction.name)){
                staticOptionsForAction.remove(actionToReset);
            }
            actionOptionsToResetOn.remove(reportAction.name);
        }
        return reportAction;
    }

    private String chooseActionOption(ReportActionSet actionSet, Random rnd) {
        IReportActionGroup group = actionSet.group;
        ReportAction action = actionSet.action;
        String actionOption = "";
        try {
            List<String> actionOptions;
            if (action.isStatic()){
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
            actionOption = actionOptions.get(rnd.nextInt(actionOptions.size()));
            logToAllure(++logCount, "Chosen Option: " + actionOption);
        } catch (Exception e){
            logToAllure(++logCount,"Exception getting options for TestAction: "+e.getClass().getName()+System.lineSeparator()+ "Exception message: "+e.getMessage());
            throw e;
        }
        return actionOption;
    }

    @Step( "{description}" )
    private EAPView applyRandomAction(ReportActionSet actionSet, String description){
        saveScreenshot(context.getName()+"2_beforeApply.png");
        EAPView newView = actionSet.group.applyActionOption(actionSet.action, actionSet.option);
        assertWithScreenshot("Applying a Report Option/Action should not error",
                report.getErrorMessage(), isEmptyOrNullString());
        saveScreenshot(context.getName()+"3_success.png");

        if(actionSet.action.subAction != null){
            ReportActionSet subActionSet = new ReportActionSet(actionSet.group);
            subActionSet.action = actionSet.action.subAction;
            subActionSet.option = chooseActionOption(subActionSet, new Random());
            newView = applyRandomAction(subActionSet, subActionSet.getDescription());
        }

        return newView;
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
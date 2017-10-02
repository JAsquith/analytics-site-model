package tests.reports.eap;

import enums.ReportAction;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.*;
import pages.reports.EAPView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.ReportsHome_EAPYearGroup;
import pages.reports.interfaces.IReportActionGroup;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.testng.Assert.fail;

public class RandomisedTest extends BaseTest {

    public static final String NO_ENTRIES_BANNER_TEXT = "There are no entries to display for this selection";
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
        openAReport(cohort);
    }

    @Test( description = "Random Report Actions", dataProvider = "randomLoops")
    @Parameters( { "maxLoops" } )
    public void testRandomReportActions(String loopIndex, String maxLoops){

        Random rnd = new Random();
        try {
            report = randomReportAction(rnd);
            assertWithScreenshot("Applying a Report Option/Action should not error",
                    report.getErrorMessage(), isEmptyOrNullString());
        } catch (Exception e){
            saveScreenshot(context.getName()+"_ErrorPage_"+loopIndex+" of "+maxLoops+".png");
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod
    public void postActionChecks(){

        // If we hit an error page, go back to ReportsHome and re-open a new report
        if(!report.getErrorMessage().equals("")){
            restartFromReportsHome();
        }

        if (report.getCohortCount()==0) zeroCohortCount++;

        String notificationText = report.getNotificationText();
        if (notificationText.equals(NO_ENTRIES_BANNER_TEXT)) zeroEntriesCount++;

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
                    report = applyRandomAction(group, action, option, action.name());
                    requiredFieldsCount--;
                    compareDSRequired=false;
                } else {
                    group = report.optionsTab;
                    action = report.optionsTab.getRequiredAction();
                    List<String> options = group.getOptionsForAction(action);
                    option = options.get(rnd.nextInt(options.size() - 1) + 1);
                    report = applyRandomAction(group, action, option, action.name());
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
        openAReport(getStringParam("cohort"));
    }
    @Step( "Reset All Options (after three actions with no students/entries)" )
    private void resetAll(){
        report = report.resetAllOptions();
        zeroCohortCount = 0;
        zeroEntriesCount = 0;
    }
    @Step( "" )
    private void selectRequiredField(){

    }

    /* Setup Steps*/
    private void openAReport(String cohort){
        gotoReportsHome();
        selectReportsCohort(cohort);
        expandEAPYearGroup(selectRandomEAPYearGroup());
        expandReportSet(selectRandomReportSet());
        String reportArea = selectRandomReportButton();
        report = clickReportAreaButton(reportArea);
        assertWithScreenshot("Opening a Report View should not error",
                report.getErrorMessage(), isEmptyOrNullString());
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

        return actionGroups;
    }

    private List<String> buildAction(Random rnd){
        group = report.navMenu;
        action = ReportAction.NULL;
        option = "Null";
        List<String> actionOptions;
        try{
            List<IReportActionGroup> actionGroups = getActionGroupList();
            group = actionGroups.get(rnd.nextInt(actionGroups.size()));
            List<ReportAction> actions = group.getValidActionsList();
            action = actions.get(rnd.nextInt(actions.size()));
            actionOptions = group.getOptionsForAction(action);
        } catch (Exception e){
            System.err.println("Exception building random ReportAction:");
            System.err.println("Group: "+group.getClass().getName());
            System.err.println("Action: "+action.name);
            System.err.println("Option: "+option);
            throw e;
        }
        return actionOptions;
    }

    private EAPView randomReportAction(Random rnd){

        group = report.navMenu;
        action = ReportAction.NULL;
        option = "Null";
        List<String> actionOptions = buildAction(rnd);
        while(actionOptions.size()==0){
            actionOptions = buildAction(rnd);
        }
        try {
            option = actionOptions.get(rnd.nextInt(actionOptions.size()));
            return applyRandomAction(group, action, option, action.name());
        } catch (Exception e){
            System.err.println("Exception applying ReportAction:");
            System.err.println("Group: "+group.getClass().getName());
            System.err.println("Action: "+action.name);
            System.err.println("Option: "+option);
            System.err.println(action + " > " + option);
            throw e;
        }
    }

    @Step( "{actionName} > {option}" )
    private EAPView applyRandomAction(IReportActionGroup group, ReportAction action, String option, String actionName){
        saveScreenshot("preAction.png");
        EAPView newView = group.applyActionOption(action, option);
        saveScreenshot("postAction.png");
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
/*
*/


}

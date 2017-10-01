package tests.reports.eap;

import enums.ReportAction;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPView;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.ReportsHome_EAPYearGroup;
import pages.reports.interfaces.IReportActionGroup;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.fail;

public class RandomisedTest extends BaseTest {

    protected EAPView report;
    private ReportsHome_EAP reportsHome;
    private ReportsHome_EAPYearGroup yearDataGroup;
    private WebElement reportSet;

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

        Random rnd = new Random();

        try {
            gotoReportsHome();
            selectReportsCohort(cohort);
            expandEAPYearGroup(selectRandomEAPYearGroup());
            expandReportSet(selectRandomReportSet());
            WebElement reportAreaButton = selectRandomReportButton();

            report = openReport(reportAreaButton, reportAreaButton.getText());

        } catch (Exception e){
            saveScreenshot(context.getName()+"_InitialReportOpen.png");
            throw e;
        }
        for (int loopIndex = 1; loopIndex <= maxLoops; loopIndex++){
            try {
                report = randomReportAction(rnd);
            } catch (Exception e){
                saveScreenshot(context.getName()+"_ErrorPage_"+loopIndex+".png");
                e.printStackTrace();
                throw e;
            }
        }

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

    private WebElement selectRandomReportButton(){
        List<WebElement> buttons = yearDataGroup.getReportButtons(reportSet);
        int buttonIndex = new Random().nextInt(buttons.size());
        return buttons.get(buttonIndex);
    }

    @Step( "Open Report Area {areaName}" )
    private EAPView openReport(WebElement button, String areaName){
        button.click();
        return new EAPView(driver);
    }

    private List<IReportActionGroup> getActionGroupList(){
        /* Todo: The following 'always-on' actionGroups need to be added:
            - ViewSwitchActions  */
        List<IReportActionGroup> actionGroups = new ArrayList<IReportActionGroup>();
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

    private EAPView randomReportAction(Random rnd){

        List<IReportActionGroup> actionGroups = getActionGroupList();
        IReportActionGroup group = actionGroups.get(rnd.nextInt(actionGroups.size()));
        List<ReportAction> actions = group.getValidActionsList();
        ReportAction action = actions.get(rnd.nextInt(actions.size()));
        List<String> actionOptions = group.getOptionsForAction(action);
        String option = actionOptions.get(rnd.nextInt(actionOptions.size()));
        applyRandomAction(group, action, option);

        return new EAPView(driver);
    }

    @Step( "{action} > {option}" )
    private EAPView applyRandomAction(IReportActionGroup group, ReportAction action, String option){
        return group.applyActionOption(action, option);
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


}

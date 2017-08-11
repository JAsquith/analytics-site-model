package tests.account;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.components.AuthorityDetailsModal;
import pages.reports.ReportsHome_EAP;
import pages.reports.ReportsHome_Legacy;
import pages.reports.components.ReportsHome_YearAccordion;
import tests.BaseTest;

import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

public abstract class AccessTest extends BaseTest {

    protected String[] actualCanDoList;
    protected String reportsHomeUrl;

    protected enum TestReport {
        KS4_LOCKED("17", "", "Exams"),
        KS4_NOT_LOCKED("17","","Targets"),
        KS5_LOCKED("16","","Spring 2 01/03/2017"),
        KS5_NOT_LOCKED("16","","Targets"),
        EAP_LOCKED("17","11","Y11 Spring 1"),
        EAP_NOT_LOCKED("17","11","Y11 Autumn 2");

        String cohort;String eapYear;String dataset;

        TestReport(String cohortNum, String eapYearNum, String datasetName){
            this.cohort = cohortNum;
            this.eapYear = eapYearNum;
            this.dataset = datasetName;
        }
    }


    @BeforeTest
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass) throws MalformedURLException {
        initialise(testContext);
        try {
            // Login, Go to reports, Open the dataset containing the test data
            login(user, pass, true);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_SetupFail.png");
            if (driver!=null){
                driver.quit();
                fail("Test Setup Failed!");
            }
        }
    }

    @Story( "The 'View Authority Details' modal should provide accurate information" )
    @Severity( SeverityLevel.MINOR )
    @Test
    @Step( "Compare actual and expected 'Can Do' lists" )
    public void viewAuthoritiesModalTest(){
        actualCanDoList = readAuthorityDetailsCanDoList();
        assertThat("'Can do' list on Authority Details Modal",
                getExpectedCanDoList(), is(actualCanDoList));
    }

    protected String[] getExpectedCanDoList(){
        String[] reportAreas = utils.getTestSettingAsArray("report-areas");
        String[] menuAreas = utils.getTestSettingAsArray("accessible-areas");
        boolean lockedAccess = utils.getTestSetting("locked-access", false);
        boolean embargoAccess = utils.getTestSetting("embargo-access", false);
        boolean accessUsers = false;
        boolean accessData = false;
        boolean announceAccess = utils.getTestSetting("announce-access", false);

        int canDoIndex = 0;
        if (!reportAreas[0].equals("")) {
            canDoIndex = reportAreas.length;
        }
        canDoIndex += (lockedAccess) ? 1 : 0;
        canDoIndex += (embargoAccess) ? 1 : 0;
        canDoIndex += (announceAccess) ? 1 : 0;

        for(int i=0; i<menuAreas.length; i++){
            if (menuAreas[i].equals("Data")) {
                accessData = true;
                canDoIndex++;
            }
            if (menuAreas[i].equals("Users")){
                accessUsers = true;
                canDoIndex++;
            }
        }
        String[] canDoList = new String[canDoIndex];
        canDoIndex = -1;
        for(int i=0; i<reportAreas.length; i++){
            if(!reportAreas[0].equals("")) {
                canDoIndex++;
                canDoList[i] = "View " + reportAreas[i].substring(0, reportAreas[i].length() - 1) + " Report";
            }
        }
        if (lockedAccess)
            canDoList[++canDoIndex] = "View Locked Reports";
        if (embargoAccess)
            canDoList[++canDoIndex] = "View Embargo Reports";
        if (accessUsers)
            canDoList[++canDoIndex] = "Access Users Section";
        if (accessData)
            canDoList[++canDoIndex] = "Access Data Section";
        if (announceAccess)
            canDoList[++canDoIndex] = "Create Announcement";

        return canDoList;
    }

    @Step( "Open and read the Authority Details Modal (with screenshot)" )
    protected String[] readAuthorityDetailsCanDoList(){
        AnalyticsPage page = new AnalyticsPage(driver);
        page.waitForLoadingWrapper();
        AuthorityDetailsModal modal = page.clickAccViewAuthority();
        String[] canDoList = modal.getCanDoList();
        saveScreenshot(context.getName()+"_AuthModal.png");
        modal.clickClose();
        return canDoList;
    }

    @Step( "Get Report buttons for KS{ks} > Cohort {cohort} > {dataset}" )
    protected String[] getReportButtonsFor(String ks, String cohort, String year, String dataset){
        driver.get(reportsHomeUrl+"?selectedKS="+ks);
        driver.get(reportsHomeUrl+"?selectedCohort="+cohort);
        if (Integer.parseInt(ks)<6){
            return getLegacyButtonsFor(dataset);
        }
        return getEAPButtonsFor(year, dataset);
    }

    @Step( "Check visible report button labels: {actual}" )
    protected void checkReportButtons(String[] expected, String[] actual){
        assertWithScreenshot("Available Report Buttons",
                actual, is(expected));
    }

    private String[] getLegacyButtonsFor(String dataset){
        WebElement shim = new ReportsHome_Legacy(driver).showReportButtons(dataset);
        List<WebElement> buttons = shim.findElements(ReportsHome_Legacy.BUTTONS_IN_SHIM);
        if (buttons.size()==0){
            return null;
        }
        String[] btnLabels = new String[buttons.size()];
        for (int i = 0; i < buttons.size(); i++){
            btnLabels[i] = buttons.get(i).getText().trim();
        }
        return btnLabels;
    }

    private String[] getEAPButtonsFor(String year, String dataset){
        ReportsHome_EAP reports = new ReportsHome_EAP(driver,true);
        WebElement pubRptInfo = reports.getYearAccordion(year).expandPublishedReport(dataset);
        List<WebElement> buttons = pubRptInfo.findElements(ReportsHome_YearAccordion.REPORT_BUTTONS);
        if (buttons.size()==0){
            return null;
        }
        String[] btnLabels = new String[buttons.size()];
        for (int i = 0; i < buttons.size(); i++){
            btnLabels[i] = buttons.get(i).getText().trim();
        }
        return btnLabels;
    }

}

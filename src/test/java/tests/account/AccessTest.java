package tests.account;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
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

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

public abstract class AccessTest extends BaseTest {

    protected String reportsHomeUrl;

    protected enum TestReport {
        KS4_LOCKED(4, "17", "", "Exams", "4780"),
        KS4_NOT_LOCKED(4, "17","","Targets", "4779"),
        KS5_LOCKED(5, "16","","Spring 2 01/03/2017", "4739"),
        KS5_NOT_LOCKED(5, "16","","Targets", "4735"),
        EAP_LOCKED(6, "17","11","Y11 Spring 1", "65/13"),
        EAP_NOT_LOCKED(6, "17","11","Y11 Autumn 2", "65/8");

        int keystage; String cohort;String eapYear;String dataset;
        String legacyPubID; String eapDatasetCollID;

        TestReport(int keystage, String cohortNum, String eapYearNum, String datasetName,
                   String reportID){
            this.keystage = keystage;
            this.cohort = cohortNum;
            this.dataset = datasetName;
            if (eapYearNum.equals("")){
                this.legacyPubID = reportID;
            } else {
                this.eapYear = eapYearNum;
                this.eapDatasetCollID = reportID;
            }
        }
    }
    protected enum ReportArea {
        HEADLINES("Headlines", "/Reports/Reports/Headlines?PublishedReport_ID="),
        QUALIFICATIONS("Qualifications", "/Reports/Reports/Qualifications?PublishedReport_ID="),
        STUDENTS("Students", "/Reports/Reports/Students?PublishedReport_ID=");

        String legacyArea; String urlPart; int eapAreaID; int eapLevelID;

        ReportArea(String legacyArea, String urlPart){
            this.legacyArea = legacyArea;
            this.urlPart = urlPart;
        }
    }


    @BeforeTest
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass) {
        String initResult = initialise(testContext);
        if (!initResult.equals("")){
            fail(initResult);
        }
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

    @Test( description = "View Authority Details Modal: Correct Permissions" )
    @Severity( SeverityLevel.MINOR )
    @Step( "Compare actual and expected 'Can Do' lists" )
    public void viewAuthoritiesModalTest(){
        assertWithScreenshot("'Can do' list on Authority Details Modal",
                readAuthorityDetailsCanDoList(), is(getExpectedCanDoList()));
    }

    protected String[] getExpectedCanDoList(){
        String[] reportAreas = getArrayParam("report-areas");
        String[] menuAreas = getArrayParam("accessible-areas");
        boolean lockedAccess = getBooleanParam("locked-access", false);
        boolean embargoAccess = getBooleanParam("embargo-access", false);
        boolean accessUsers = false;
        boolean accessData = false;
        boolean announceAccess = getBooleanParam("announce-access", false);

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
    protected String[] getReportButtonsFor(TestReport testReport){
        driver.get(reportsHomeUrl+"?selectedKS="+testReport.keystage);
        driver.get(reportsHomeUrl+"?selectedCohort="+testReport.cohort);
        if (testReport.keystage<6){
            return getLegacyButtonsFor(testReport.dataset);
        }
        return getEAPButtonsFor(testReport.eapYear, testReport.dataset);
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
        List<WebElement> buttons = pubRptInfo.findElements(ReportsHome_YearAccordion.REPORT_AREA_BUTTONS);
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

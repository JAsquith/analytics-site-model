package tests.reports;

import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pages.AnalyticsPage;
import pages.reports.EAPReport;
import pages.reports.ReportsHome_EAP;
import tests.BaseTest;

import java.net.MalformedURLException;

import static org.testng.Assert.fail;

public abstract class ReportTest extends BaseTest {

    protected EAPReport report;

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
                fail("Test Setup Failed! Was the data in the Restore school copied over? Exception: "+e.getMessage());
            }
        }
    }

    @Step( "Open the {cohort} > {year} > {dataset} > {button} report" )
    public EAPReport openTestDataset(String cohort, String year, String dataset, String button){
        ReportsHome_EAP reports = new ReportsHome_EAP(driver,true);
        return reports.
                selectCohortByUrl(cohort).
                getYearAccordion(year).
                gotoPublishedReport(dataset, false, button);
    }

    @Step( "Switch to the {area} > {view} report at {level} Level" )
    public EAPReport openTestView(EAPReport report, String area, String view, String level){
        return report.openView(area,view,level);
    }
    @Step( "Apply report options" )
    public void applyReportOptions(){
        //Todo: use the super.getStringParam to get any/all report options to be applied
    }

}

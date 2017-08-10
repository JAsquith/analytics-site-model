package tests.reports;

import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pages.AnalyticsPage;
import pages.reports.Report;
import pages.reports.ReportsHome_EAP;
import tests.BaseTest;
import static org.testng.Assert.fail;

import java.net.MalformedURLException;

public abstract class ReportTest extends BaseTest {

    protected Report report;

    @BeforeTest()
    @Step ( "Login, Open the required Report, and apply required Options " )
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass) throws MalformedURLException{
        super.initialise(testContext);

        try {
            // Login, Go to reports, Open the dataset containing the test data
            login(user, pass, true);
            report = openTestView();

            // Apply any report options, filters, etc. defined in the test parameters
            applyReportOptions();
        } catch (Exception e){
            if (driver!=null){
                new AnalyticsPage(driver).clickMenuLogout();
                driver.quit();
                fail("Test Setup Failed!");
            }
        }
    }

    @Step( "Navigate to the Headlines > Filters report for the test Cohort > Year > dataset" )
    public Report openTestView(){
        //Todo: Move this method to an intermediate abstract ReportTest class which subclasses BaseTest but is a superclass of any Test that takes place in a Report
        //Todo: navigate to the correct cohort > year > dataset > view
        ReportsHome_EAP reports = new ReportsHome_EAP(driver,true);
        return reports.
                selectCohortByUrl(getStringParam("cohort")).
                getYearAccordion(getStringParam("year")).
                gotoPublishedReport(getStringParam("dataset"), false, "Headlines")
                .openView(
                        getStringParam("report-area"),
                        getStringParam("report-view"),
                        getStringParam("report-level"));
    }
    @Step( "Apply report options" )
    public void applyReportOptions(){
        //Todo: Move this method to an intermediate abstract ReportTest class which subclasses BaseTest but is a superclass of any Test that takes place in a Report
        //Todo: use the TestUtils object (super.utils) to get any/all report options to be applied
        //Todo: tag appropriate methods in the Report Page Object with @Step annotations
    }

    @AfterTest
    public void tearDown(){
        report.clickMenuLogout();
        driver.quit();
    }

}

package tests.reports;

import io.qameta.allure.*;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.home.HomePage;
import pages.reports.Report;
import pages.reports.ReportsHome_EAP;
import tests.BaseTest;

import java.net.MalformedURLException;

@Epic( "EAP Reports - Headline Measures" )
@Feature( "Headlines > Filters view" )
public class HeadlineFiltersTests extends BaseTest {

    Report testPage;

    @BeforeTest()
    @Parameters( { "username", "password" })
    public void setup(String user, String pass, ITestContext testContext) throws MalformedURLException{
        initialise(testContext);

        // Login, Go to reports, Open the dataset containing the test data
        // Todo: Check that @Step annotated methods called from @BeforeTest methods are listed in Allure
        //       - If not, need to move these steps into a common method called from each @Test method
        login(user,pass,true);
        testPage = openTestView();

        // Apply any report options, filters, etc. defined in the test parameters
        applyReportOptions(testPage);
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }

    @Step( "Navigate to the Headlines > Filters report for the test Cohort > Year data > dataset" )
    public Report openTestView(){
        //Todo: Move this method to an intermediate abstract ReportTest class which subclasses BaseTest but is a superclass of any Test that takes place in a Report
        //Todo: navigate to the correct cohort > year > dataset > view
        ReportsHome_EAP reports = new ReportsHome_EAP(driver,true);
        return reports.
                selectCohortByUrl("17").
                getYearAccordion("11").
                gotoPublishedReport("Targets", false, "Headlines");
    }

    @Step( "Apply report options" )
    public void applyReportOptions(Report aReport){
        //Todo: Move this method to an intermediate abstract ReportTest class which subclasses BaseTest but is a superclass of any Test that takes place in a Report
        //Todo: use the TestUtils object (super.utils) to get any/all report options to be applied
        //Todo: tag appropriate methods in the Report Page Object with @Step annotations
    }

    @Story( "The Report/View Options should be available/disabled/locked as per the spec" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void optionsAvailability(){
        String optionAvailabilityErrors;

        // Student Filters tab is enabled

        // Measure Filters tab is enabled

        // Residual Exclusions tab is disabled

        // On Track grade filters are disabled

        // Faculty DDL is enabled

        // Qualification DDL is enabled

        // Class DDL is not present



    }

}

package tests.reports.eap.students;

import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import pages.reports.EAPReport;
import tests.reports.ReportTest;

import java.net.MalformedURLException;

public class FigureTests extends ReportTest{

    @BeforeClass
    @Step( "Extract the actual report figures so they can be compared to the expected ones" )
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass) throws MalformedURLException {

        // BaseTest.initialise creates the RemoteWebDriver and opens Analytcis
        // ReportTest.setup logs in and opens the report view defined in the test parameters
        super.setup(testContext, user, pass);

        // Extract the actual report figures to a csv file in the same folder as the expected report figures
        EAPReport reportPage = new EAPReport(driver);


    }

}

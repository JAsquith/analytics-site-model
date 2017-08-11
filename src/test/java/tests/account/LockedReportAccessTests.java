package tests.account;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.reports.ReportsHome;
import pages.reports.ReportsHome_Legacy;
import tests.BaseTest;

import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

@Epic("Account Security")
@Feature("Authority Groups Control Access to Locked Report")
public class LockedReportAccessTests extends BaseTest {

    private ReportsHome reportsHome;
    private String reportsHomeUrl;

    private enum TestReport {
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

    @BeforeTest()
    @Step( "Login" )
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass) throws MalformedURLException {
        super.initialise(testContext);

        try {
            // Login, Go to reports, Open the dataset containing the test data
            login(user, pass, true);
            reportsHome = new ReportsHome(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
        } catch (Exception e){
            if (driver!=null){
                driver.quit();
                fail("Test Setup Failed!");
            }
        }
    }

    @AfterTest
    public void tearDown(){
        try{
            new AnalyticsPage(driver).clickMenuLogout();
            driver.quit();
        } catch (Exception e){}
    }

    @Story( "Access to KS4 Reports with a status of 'Locked'" )
    @Test
    public void checkKS4LockedReportButtons(){
        TestReport testReport = TestReport.KS4_LOCKED;
        String [] expectedButtons = utils.getTestSettingAsArray("report-areas");
        String [] actualButtons = getReportButtonsFor("4", testReport.cohort, testReport.dataset);
        assertThat("Available Report Buttons",
                actualButtons, is(expectedButtons));

    }

    @Step( "Select KS{ks} > Cohort {cohort} on the Reports Homepage" )
    private void selectKSAndCohort(String ks, String cohort){
        driver.get(reportsHomeUrl+"?selectedKS="+ks);
        driver.get(reportsHomeUrl+"?selectedCohort="+cohort);
    }

    @Step( "Get Report buttons for KS{ks} > Cohort {cohort} > {dataset}" )
    private String[] getReportButtonsFor(String ks, String cohort, String dataset){
        driver.get(reportsHomeUrl+"?selectedKS="+ks);
        driver.get(reportsHomeUrl+"?selectedCohort="+cohort);
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

}

package tests.account;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.reports.ReportsHome;
import pages.reports.ReportsHome_Legacy;

import java.util.List;

import static org.hamcrest.Matchers.is;

@Epic("Account Security")
@Feature("Authority Groups Control Access to Locked Report")
public class LockedReportAccessTest extends AccessTest {

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

    @AfterTest
    public void tearDown(){
        try{
            new AnalyticsPage(driver).clickMenuLogout();
        } catch (Throwable t){}
        try{
            driver.quit();
        } catch (Throwable t){}
    }

    @Story( "Access to KS4 Reports with a status of 'Locked'" )
    @Test
    public void checkKS4LockedReportButtons(){
        new ReportsHome(driver, true);
        reportsHomeUrl = driver.getCurrentUrl();
        TestReport testReport = TestReport.KS4_LOCKED;
        String [] expectedButtons = utils.getTestSettingAsArray("report-areas");
        String [] actualButtons = getReportButtonsFor("4", testReport.cohort, testReport.dataset);
        assertWithScreenshot("Available Report Buttons",
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

package tests.account;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.reports.ReportsHome;

@Epic("Account Security")
@Feature("Authority Groups Control Access to Locked Report")
public class LockedReportAccessTest extends AccessTest {

    @AfterTest
    public void tearDown(){
        try{
            new AnalyticsPage(driver).clickMenuLogout();
        } catch (Throwable t){}
        try{
            driver.quit();
        } catch (Throwable t){}
    }

/*
    @Story( "Access to KS4 Reports with a status of 'Locked'" )
    @Test
    public void kS4Locked_VisibleButtons(){
        try {
            new ReportsHome(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
            TestReport testReport = TestReport.KS4_LOCKED;
            String[] expectedButtons = utils.getTestSettingAsArray("report-areas");
            String[] actualButtons = getReportButtonsFor("4", testReport.cohort, null, testReport.dataset);

            checkReportButtons(expectedButtons, actualButtons);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }
*/

    @Story( "Access to KS4 Reports with a status other than 'Locked'" )
    @Test
    public void ks4NotLocked_VisibleButtons(){
        try{
            new ReportsHome(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
            TestReport testReport = TestReport.KS4_NOT_LOCKED;
            String [] expectedButtons = utils.getTestSettingAsArray("report-areas");
            String [] actualButtons = getReportButtonsFor("4", testReport.cohort, null, testReport.dataset);

            checkReportButtons(expectedButtons, actualButtons);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "Access to KS5 Reports with a status of 'Locked'" )
    @Test
    public void ks5Locked_VisibleButtons(){
        try{
            new ReportsHome(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
            TestReport testReport = TestReport.KS5_LOCKED;
            String [] expectedButtons = utils.getTestSettingAsArray("report-areas");
            String [] actualButtons = getReportButtonsFor("5", testReport.cohort, null, testReport.dataset);

            checkReportButtons(expectedButtons, actualButtons);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

/*
    @Story( "Access to KS5 Reports with a status other than 'Locked'" )
    @Test
    public void ks5NotLocked_VisibleButtons(){
        try{
            new ReportsHome(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
            TestReport testReport = TestReport.KS5_NOT_LOCKED;
            String [] expectedButtons = utils.getTestSettingAsArray("report-areas");
            String [] actualButtons = getReportButtonsFor("5", testReport.cohort, null, testReport.dataset);

            checkReportButtons(expectedButtons, actualButtons);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "Access to EAP Reports with a status of 'Locked'" )
    @Test
    public void eapLocked_VisibleButtons(){
        try{
            new ReportsHome_EAP(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
            TestReport testReport = TestReport.EAP_LOCKED;
            String [] expectedButtons = utils.getTestSettingAsArray("accessible-eap-buttons");
            String [] actualButtons = getReportButtonsFor("6", testReport.cohort, testReport.eapYear, testReport.dataset);

            checkReportButtons(expectedButtons, actualButtons);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "Access to EAP Reports with a status other than 'Locked'" )
    @Test
    public void eapNotLocked_VisibleButtons(){
        try{
            new ReportsHome_EAP(driver, true);
            reportsHomeUrl = driver.getCurrentUrl();
            TestReport testReport = TestReport.EAP_NOT_LOCKED;
            String [] expectedButtons = utils.getTestSettingAsArray("accessible-eap-buttons");
            String [] actualButtons = getReportButtonsFor("6", testReport.cohort, testReport.eapYear, testReport.dataset);

            checkReportButtons(expectedButtons, actualButtons);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }
*/
}
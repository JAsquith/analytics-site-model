package tests.account;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.reports.ReportsHome;
import pages.reports.ReportsHome_EAP;

import static org.hamcrest.CoreMatchers.is;

/**
 * Tests that a given user has access to the right types of report.
 * The users should be defined in the testng.xml file including:
 *     username, password, Legacy and EAP reports type they can access, access to locked reports, etc.
 * The testng.xml parameters should match the settings in the user's Authority Group.
 * The reports to which access is tested are enumerated in the @link{AccessTest} class.
 * Access is tested via Reports Homepage links/buttons and url hacks.
 */
@Epic("Account Security")
@Feature("Authority Groups Control Access to Report")
public class ReportAccessTests extends AccessTest {

    private String[] expectedButtons;
    private String[] actualButtons;
    private String expectedTitle;
    private String actualPageTitle;

    @Story( "Buttons for KS4 Reports with a status of 'Locked'" )
    @Test
    public void kS4Locked_VisibleButtons(){
        try {
            setupButtonsTest(TestReport.KS4_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS4 Headlines Reports with a status of 'Locked'" )
    @Test
    public void ks4Locked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS4 Qualifications Reports with a status of 'Locked'" )
    @Test
    public void ks4Locked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS4 Students Reports with a status of 'Locked'" )
    @Test
    public void ks4Locked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Story( "Access to KS4 Reports with a status other than 'Locked'" )
    @Test
    public void ks4NotLocked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.KS4_NOT_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS4 Headlines Reports with a status other than 'Locked'" )
    @Test
    public void ks4NotLocked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_NOT_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS4 Qualifications Reports with a status other than 'Locked'" )
    @Test
    public void ks4NotLocked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_NOT_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS4 Students Reports with a status other than 'Locked'" )
    @Test
    public void ks4NotLocked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_NOT_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Story( "Access to KS5 Reports with a status of 'Locked'" )
    @Test
    public void ks5Locked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.KS5_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS5 Headlines Reports with a status of 'Locked'" )
    @Test
    public void ks5Locked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS5 Qualifications Reports with a status of 'Locked'" )
    @Test
    public void ks5Locked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS5 Students Reports with a status of 'Locked'" )
    @Test
    public void ks5Locked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Story( "Access to KS5 Reports with a status other than 'Locked'" )
    @Test
    public void ks5NotLocked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.KS5_NOT_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS5 Headlines Reports with a status other than 'Locked'" )
    @Test
    public void ks5NotLocked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_NOT_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS5 Qualifications Reports with a status other than 'Locked'" )
    @Test
    public void ks5NotLocked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_NOT_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Story( "URL access to KS5 Students Reports with a status other than 'Locked'" )
    @Test
    public void ks5NotLocked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_NOT_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Story( "Access to EAP Reports with a status of 'Locked'" )
    @Test
    public void eapLocked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.EAP_LOCKED);
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
            setupButtonsTest(TestReport.EAP_NOT_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    // Todo: url hack tests for EAP Reports

    @Step( "Determine the actual and expected button labels visible to this user" )
    private void setupButtonsTest(TestReport testReport){
        if(testReport.keystage<6){
            new ReportsHome(driver, true);
        }else{
            new ReportsHome_EAP(driver, true);
        }
        reportsHomeUrl = driver.getCurrentUrl();
        actualButtons = getReportButtonsFor(testReport);

        if (testReport.keystage<6) {
            expectedButtons = getArrayParam("report-areas");
        } else {
            expectedButtons = getArrayParam("accessible-eap-buttons");
        }
    }

    @Step( "Determine the actual and expected page titles after a url access attempt by this user" )
    protected void setupUrlAccessTest(TestReport testReport, ReportArea reportArea){
        String reportsAccess = getStringParam("report-areas");
        boolean lockedAccess = getBooleanParam("locked-access", false);
        if (lockedAccess){
            if (testReport.keystage<6){
                if (reportsAccess.contains(reportArea.legacyArea)){
                    expectedTitle = "KS"+testReport.keystage+" "+reportArea.legacyArea;
                } else {
                    expectedTitle = "Role Error";
                }
            } else {
                // Todo: Replace placeholder text with constructed value
                expectedTitle = "16/17 (Current Year 11)";
            }
        } else {
            expectedTitle = "Reports Homepage";
        }

        // Todo: Make generic so it works for EAP as well as Legacy
        driver.get(applicationUrl+reportArea.urlPart+testReport.legacyPubID);
        actualPageTitle = new AnalyticsPage(driver).getPageTitleText();
    }


}
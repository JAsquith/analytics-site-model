package tests.account;

import io.qameta.allure.*;
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
@Feature("Authority Groups Permissions")
@Story( "Access to Reports & Report Areas is controlled by Authority Group Roles" )
public class ReportAccessTests extends AccessTest {

    private String[] expectedButtons;
    private String[] actualButtons;
    private String expectedTitle;
    private String actualPageTitle;

    @Test( description = "Visibility of Links to Locked KS4 Reports")
    @Severity( SeverityLevel.NORMAL )
    public void kS4Locked_VisibleButtons(){
        try {
            setupButtonsTest(TestReport.KS4_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Locked KS4 Headlines Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks4Locked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Locked KS4 Quals Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks4Locked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Locked KS4 Students Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks4Locked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "Visibility of Links to Unlocked KS4 Reports" )
    @Severity( SeverityLevel.NORMAL )
    public void ks4NotLocked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.KS4_NOT_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Unlocked KS4 Headlines Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks4NotLocked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_NOT_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Unlocked KS4 Quals Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks4NotLocked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_NOT_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Unlocked KS4 Students Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks4NotLocked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS4_NOT_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Test( description = "Visibility of Links to Locked KS5 Reports" )
    @Severity( SeverityLevel.NORMAL )
    public void ks5Locked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.KS5_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Locked KS5 Headlines Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks5Locked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Locked KS5 Quals Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks5Locked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Locked KS5 Students Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks5Locked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Test( description = "Visibility of Links to Unlocked KS5 Reports" )
    @Severity( SeverityLevel.NORMAL )
    public void ks5NotLocked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.KS5_NOT_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Unlocked KS5 Headlines Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks5NotLocked_HeadlinesByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_NOT_LOCKED, ReportArea.HEADLINES);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Unlocked KS5 Quals Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks5NotLocked_QualificationsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_NOT_LOCKED, ReportArea.QUALIFICATIONS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "URL Accessibility of Unlocked KS5 Students Report" )
    @Severity( SeverityLevel.CRITICAL )
    public void ks5NotLocked_StudentsByUrl(){
        try{
            setupUrlAccessTest(TestReport.KS5_NOT_LOCKED, ReportArea.STUDENTS);
            assertWithScreenshot("Page Title after opening report url", actualPageTitle, is(expectedTitle));
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }


    @Test( description = "Visibility of Links to Locked EAP Reports" )
    @Severity( SeverityLevel.NORMAL )
    public void eapLocked_VisibleButtons(){
        try{
            setupButtonsTest(TestReport.EAP_LOCKED);
            checkReportButtons(expectedButtons, actualButtons);
        } catch (Exception e){
            saveScreenshot(context.getName()+"_Exception.png");
            throw e;
        }
    }

    @Test( description = "Visibility of Links to Unocked EAP Reports" )
    @Severity( SeverityLevel.NORMAL )
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
        ReportsHome reportsHomePage;
        if(testReport.keystage<6){
            reportsHomePage = new ReportsHome(driver, true);
        }else{
            reportsHomePage = new ReportsHome_EAP(driver, true);
        }
        reportsHomeUrl = reportsHomePage.getPagePath();
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
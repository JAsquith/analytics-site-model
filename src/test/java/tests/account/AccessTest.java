package tests.account;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.components.AuthorityDetailsModal;
import tests.BaseTest;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

public abstract class AccessTest extends BaseTest {

    protected String[] actualCanDoList;

    @BeforeTest
    @Parameters( { "username", "password" })
    public void setup(ITestContext testContext, String user, String pass) throws MalformedURLException {
        initialise(testContext);
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

    @Story( "The 'View Authority Details' modal should provide accurate information" )
    @Severity( SeverityLevel.MINOR )
    @Test
    @Step( "Compare actual and expected 'Can Do' lists" )
    public void viewAuthoritiesModalTest(){
        actualCanDoList = readAuthorityDetailsCanDoList();
        assertThat("'Can do' list on Authority Details Modal",
                getExpectedCanDoList(), is(actualCanDoList));
    }

    private String[] getExpectedCanDoList(){
        String[] reportAreas = utils.getTestSettingAsArray("report-areas");
        String[] menuAreas = utils.getTestSettingAsArray("accessible-areas");
        boolean lockedAccess = utils.getTestSetting("locked-access", false);
        boolean embargoAccess = utils.getTestSetting("embargo-access", false);
        boolean accessUsers = false;
        boolean accessData = false;
        boolean announceAccess = utils.getTestSetting("announce-access", false);

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
    private String[] readAuthorityDetailsCanDoList(){
        AnalyticsPage page = new AnalyticsPage(driver);
        page.waitForLoadingWrapper();
        AuthorityDetailsModal modal = page.clickAccViewAuthority();
        String[] canDoList = modal.getCanDoList();
        saveScreenshot(context.getName()+"_AuthModal.png");
        modal.clickClose();
        return canDoList;
    }

}

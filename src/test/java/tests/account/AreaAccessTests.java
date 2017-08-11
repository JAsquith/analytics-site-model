package tests.account;

import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.components.AuthorityDetailsModal;
import pages.home.HomePage;
import tests.BaseTest;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

/**
 *
 */
@Epic("Account Security")
@Feature("Authority Groups Control Access to Main Menu Areas")
public class AreaAccessTests extends BaseTest {

    protected AnalyticsPage homePage;

    private enum Area {
        HOME("Home", "HOME", "", "Welcome to SISRA Analytics"),
        DATA("Data", "DATA", "/Admin", "Data"),
        CONFIG("Config.", "CONFIG.", "/Config", "Config"),
        USERS("Users", "USERS", "/Users", "User List"),
        REPORTS("Reports", "REPORTS", "/ReportsHome", "Reports Homepage");

        String areaName;
        String menuLocatorText;
        String menuVisibleText;
        String defaultPageTitle;
        String pageURL;

        Area(String name, String menuText, String url, String title){
            this.areaName=name;
            this.menuLocatorText=name;
            this.menuVisibleText=menuText;
            this.defaultPageTitle=title;
            this.pageURL=url;
        }
        static Area get(String name){
            switch (name){
                case "Home":return HOME;
                case "Data":return DATA;
                case "Config.":return CONFIG;
                case "Users":return USERS;
                case "Reports":return REPORTS;
                default: return HOME;
            }
        }
        static String[] getAllAreaNames(){
            return new String[] { "Home", "Data", "Config.", "Users", "Reports" };
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

    @Story( "The 'View Authority Details' modal should provide accurate information" )
    @Severity( SeverityLevel.MINOR )
    @Test
    @Step( "Compare actual and expected 'Can Do' lists" )
    public void viewAuthoritiesModalTest(){
        AuthorityDetailsModal modal = openAuthorityDetailsModal();
        String[] actualCanDoList = modal.getCanDoList();
        modal.clickClose();
        assertThat("'Can do' list on Authority Details Modal",
                getExpectedCanDoList(), is(actualCanDoList));
    }

    @Step( "Open the Authority Details Modal" )
    private AuthorityDetailsModal openAuthorityDetailsModal(){
        AnalyticsPage page = new AnalyticsPage(driver);
        page.waitForLoadingWrapper();
        return page.clickAccViewAuthority();
    }
    private String[] getExpectedCanDoList(){
        String[] reportAreas = utils.getTestSettingAsArray("report-areas");
        String[] menuAreas = utils.getTestSettingAsArray("accessible-areas");
        boolean lockedAccess = utils.getTestSetting("locked-access", false);
        boolean embargoAccess = utils.getTestSetting("embargo-access", false);
        boolean accessUsers = false;
        boolean accessData = false;
        boolean announceAccess = utils.getTestSetting("announce-access", false);

        int canDoIndex = reportAreas.length;
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

    @Story( "The correct Main Menu options should be visible" )
    @Severity( SeverityLevel.BLOCKER )
    @Test
    @Step( "Check the visible Main Menu labels" )
    public void menuOptionsDisplayedTest(){
        homePage = new HomePage(driver, true);
        assertThat("Available Menu Options",
                getActualMenuLabels(), is(getExpectedMenuLabels()));
    }

    @Story( "The visible Main Menu options link to the correct pages" )
    @Severity( SeverityLevel.BLOCKER )
    @Test
    public void menuOptionLinksTest(){
        homePage = new AnalyticsPage(driver);
        String[] areaNames = utils.getTestSettingAsArray("accessible-areas");

        for(String areaName : areaNames){
            tryMenuLink(areaName);
        }
    }

    @Story( "Role Error page blocks access to Areas the user should not see" )
    @Severity( SeverityLevel.BLOCKER )
    @Test
    @Step ( "Check URL access to all blocked areas" )
    public void urlAreaAccessTest(){
        String[] areaNames = Area.getAllAreaNames();
        List<String> expAreaNames = Arrays.asList(utils.getTestSettingAsArray("accessible-areas"));
        String failExpectedTitles = "";
        String failActualTitles = "";

        for (String areaName : areaNames){
            if (!expAreaNames.contains(areaName)){
                String actualPageTitle = tryUrlHack(areaName);
                if(!actualPageTitle.equals("")) {
                    failExpectedTitles += "[Area: "+areaName + ", Page Title: Role Error];";
                    failActualTitles += "[Area: "+areaName + ", Page Title: " + actualPageTitle + "];";
                }
            }
        }
        if (!failExpectedTitles.equals("")){
            assertThat("Blocked area(s) show the Role Error page title",
                    failActualTitles, is(failExpectedTitles));
        }
    }

    @Step( "Menu Option '{option}' opens the expected page" )
    private void tryMenuLink(String areaName){
        Area testArea = Area.get(areaName);
        homePage.clickMenuOption(testArea.menuLocatorText);
        assertThat("Landing Page Title",
                homePage.getPageTitleText(),is(testArea.defaultPageTitle));
    }

    private String[] getActualMenuLabels(){
        List<WebElement> menuLinks = driver.findElements(homePage.MAIN_MENU_LINKS);
        String[] options = new String[menuLinks.size()-1];
        for(int i = 0; i < menuLinks.size()-1; i++){
            options[i] = menuLinks.get(i+1).getText().trim();
        }
        return options;
    }

    private String[] getExpectedMenuLabels(){
        String[] areaNames = utils.getTestSettingAsArray("accessible-areas");
        String[] menuOptions = new String[areaNames.length];
        for(int i=0; i<areaNames.length;i++){
            menuOptions[i]=Area.get(areaNames[i]).menuVisibleText;
        }
        return menuOptions;
    }

    @Step ( "Try to access the default URL for {areaName}" )
    private String tryUrlHack(String areaName){
        Area area = Area.get(areaName);
        driver.get(applicationUrl+area.pageURL);
        String actualPageTitle = new AnalyticsPage(driver).getPageTitleText();
        if (actualPageTitle.equals("Role Error")){
            return "";
        } else {
            return actualPageTitle;
        }
    }
    /*

    Types of Test:
        Universal:
            Authority Details modal shows correct info
            Create Announcement via links
            Create Announcement via URLs
        Report Group Specific Tests:
            Access to Legacy Headlines (links)
            Access to Legacy Headlines (urls)
            Access to Legacy Qualifications (links)
            Access to Legacy Qualifications (urls)
            Access to Legacy Classes (links)
            Access to Legacy Classes (urls)
            Access to Legacy Students (links)
            Access to Legacy Students (urls)
            Access to Legacy Locked (links)
            Access to Legacy Locked (urls)
            Access to EAP Headlines (links)
            Access to EAP Headlines (urls)
            Access to EAP Filters (links)
            Access to EAP Filters (urls)
            Access to EAP Faculties (links)
            Access to EAP Faculties (urls)
            Access to EAP Qualifications (links)
            Access to EAP Qualifications (urls)
            Access to EAP Classes (links)
            Access to EAP Classes (urls)
            Access to EAP Students (links)
            Access to EAP Students (urls)
            Access to EAP Locked (links)
            Access to EAP Locked (urls)
     */

}

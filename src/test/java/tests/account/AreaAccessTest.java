package tests.account;

import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.home.HomePage;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

/**
 *
 */
@Epic("Account Security")
@Feature("Authority Groups Control Access to Main Menu Areas")
public class AreaAccessTest extends AccessTest {

    protected AnalyticsPage homePage;
    private enum Area {
        HOME("Home", "HOME", "", "Welcome to SISRA Analytics"),
        DATA("Data", "DATA", "/Admin", "Data"),
        CONFIG("Config.", "CONFIG.", "/Config", "Config"),
        USERS("Users", "USERS", "/Users", "User List"),
        REPORTS("Reports", "REPORTS", "/ReportsHome", "Reports Homepage");

        String areaName;
        String imgAltText;
        String menuButtonLabel;
        String defaultPageURL;
        String defaultPageTitle;

        Area(String name, String menuText, String url, String title){
            this.areaName=name;
            this.imgAltText=name;
            this.menuButtonLabel=menuText;
            this.defaultPageURL=url;
            this.defaultPageTitle=title;
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

    @Story( "The correct Main Menu options should be visible" )
    @Severity( SeverityLevel.BLOCKER )
    @Test
    @Step( "Check the visible Main Menu labels" )
    public void menuOptionsDisplayedTest(){
        homePage = new HomePage(driver, true);
        assertWithScreenshot("Available Menu Options",
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
                if(actualPageTitle != null) {
                    failExpectedTitles += "[Area: "+areaName + ", Page Title: Role Error];";
                    failActualTitles += "[Area: "+areaName + ", Page Title: " + actualPageTitle + "];";
                }
            }
        }
        if (!failExpectedTitles.equals("")){
            assertWithScreenshot("Blocked area(s) show the Role Error page title",
                    failActualTitles, is(failExpectedTitles));
        }
    }

    @Step( "Menu Option '{areaName}' opens the expected page" )
    private void tryMenuLink(String areaName){
        Area testArea = Area.get(areaName);
        homePage.clickMenuOption(testArea.imgAltText);
        assertWithScreenshot("Landing Page Title",
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
            menuOptions[i]=Area.get(areaNames[i]).menuButtonLabel;
        }
        return menuOptions;
    }

    @Step ( "Try to access the default URL for {areaName}" )
    private String tryUrlHack(String areaName){
        Area area = Area.get(areaName);
        driver.get(applicationUrl+area.defaultPageURL);
        String actualPageTitle = new AnalyticsPage(driver).getPageTitleText();
        if (actualPageTitle.equals("Role Error")){
            return null;
        } else {
            saveScreenshot(context.getName()+"_"+areaName+".png");
            return actualPageTitle;
        }
    }
    /*

    Types of Test:
        Universal:
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

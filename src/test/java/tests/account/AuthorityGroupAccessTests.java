package tests.account;

import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AnalyticsPage;
import pages.home.HomePage;
import tests.BaseTest;

import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

/**
 *
 */
@Epic("Account Security")
@Feature("Authority Group Access Rights")
public class AuthorityGroupAccessTests extends BaseTest {

    protected AnalyticsPage homePage;

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

    @Story( "The correct Main Menu options should be visible" )
    @Severity( SeverityLevel.BLOCKER )
    @Test
    @Step( "Check the currently visible Main Menu options" )
    public void menuOptionsDisplayedTest(){
        homePage = new HomePage(driver, true);
        assertThat("Available Menu Options",
                getActualOptions(), is(utils.getTestSettingAsArray("expected-menu-options")));
    }

    @Story( "The visible Main Menu options link to the correct pages" )
    @Severity( SeverityLevel.BLOCKER )
    @Test
    public void menuOptionLinksTest(){
        homePage = new AnalyticsPage(driver);

        String[] options = utils.getTestSettingAsArray("expected-menu-options");
        String[] titles = utils.getTestSettingAsArray("expected-page-titles");

        for (int i=1; i < options.length; i++){
            checkMenuLink(options[i],titles[i-1]);
        }
        String[] actual = getActualOptions();
        assertThat("Available Menu Options", actual, is(options));
    }

    protected String[] getActualOptions(){
        List<WebElement> menuLinks = driver.findElements(homePage.MAIN_MENU_LINKS);
        String[] options = new String[menuLinks.size()];
        for(int i = 0; i < menuLinks.size(); i++){
            options[i] = menuLinks.get(i).getText().trim();
        }
        return options;
    }

    @Step( "Menu Option '{option}' opens the page with Title '{pageTitle}'" )
    private void checkMenuLink(String option, String pageTitle){
        homePage.clickMenuOption(option);
        assertThat("",homePage.getPageTitleText(),is(pageTitle));
    }

    /*

    Types of Test:
        Universal:
            Authority Details modal shows correct info
            Main Menu options displayed
            Access to areas via menu
            Access to areas via URLs
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

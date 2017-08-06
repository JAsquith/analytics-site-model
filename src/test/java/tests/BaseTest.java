package tests;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import pages.account.LoginPage;
import utils.TestUtils;

import java.net.MalformedURLException;

/**
 * An abstract class which takes care of functions common to all/most tests, for example: launching a new browser
 * session, logging in to Analytics, error reporting, etc.
 */
public abstract class BaseTest {
    // SISRATest Instance Variables
    /**
     *
     */
    public RemoteWebDriver driver;
    /**
     *
     */
    public String testDomain;

    public String testProtocol;

    /**
     *
     */
    protected TestUtils utils;

    /**
     * Initialises a new test by instantiating a new RemoteWebDriver (and some other test properties) based on
     * the settings found in the TestNG xml suite file and the test.properties file.
     *
     * @param context_TestNG an object of a class that implements the @link{org.testng.ITestContext} interface
     * @throws MalformedURLException if the @link{utils.TestUtils#getGridUrl()} method can't build a valid URL from
     * the given settings
     */
    protected void initialise(ITestContext context_TestNG) throws MalformedURLException {


        utils = new TestUtils(context_TestNG);
        try {
            driver = new RemoteWebDriver(utils.getGridUrl(), utils.getCapabilities());
        } catch (MalformedURLException e) {
            System.out.println("Exception starting web browser");
            e.printStackTrace();
            throw new MalformedURLException(e.getMessage());
        }
        //driver.manage().window().maximize();

        testDomain = utils.getTestSetting("test.domain");
        testProtocol = utils.getTestSetting("test.protocol");
        driver.get(testProtocol+"://"+testDomain+".sisraanalytics.co.uk/");
    }

    /**
     * Logs in to analytics using the given credentials. @link{#initialise} should have been called previously, or
     * the @link{#driver} otherwise initialised.
     * @param user The string to enter into the Username field
     * @param pass The string to enter into the Password field
     * @param endOtherSession If the user is already logged in and this argument is @code{true}, the End
     *                              Other Session And Login button will be clicked.  If the user is already logged in
     *                              and this argument is @code{false}, the Cancel button will be clicked.
     */
    protected void login(String user, String pass, boolean endOtherSession){
        if (driver == null){
            throw new IllegalStateException("The RemoteWebDriver has not been instantiated");
        }
        LoginPage loginPage = new LoginPage(driver, true);
        loginPage.loginWith(user, pass, endOtherSession);
    }

    /**
     * Accesses a given test parameter
     * @param name
     * @param defaultVal
     * @return
     */
    protected String getStringParam(String name, String defaultVal){
        return utils.getTestSetting(name, defaultVal);
    }

    protected String getStringParam(String name){
        return getStringParam(name, "");
    }

}

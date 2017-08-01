package tests;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import utils.TestUtils;

import java.net.MalformedURLException;

/**
 * An abstract class which takes care of functions common to all/most tests, for example: launching a new browser
 * session, logging in to Analytics, error reporting, etc.
 */
public abstract class BaseTest {
    // SISRATest Instance Variables
    public RemoteWebDriver driver;
    public String testDomain;

    /**
     * Initialises a new test by instantiating a new RemoteWebDriver (and some other test properties) based on
     * the settings found in the TestNG xml suite file and the test.properties file.
     *
     * @param context_TestNG an object of a class that implements the @link{org.testng.ITestContext} interface
     * @throws MalformedURLException if the @link{utils.TestUtils#getGridUrl()} method can't build a valid URL from
     * the given settings
     */
    protected void initialise(ITestContext context_TestNG) throws MalformedURLException {
        TestUtils utils = new TestUtils(context_TestNG);
        testDomain = utils.getTestSetting("test.domain");
        try {
            driver = new RemoteWebDriver(utils.getGridUrl(), utils.getCapabilities());
        } catch (MalformedURLException e) {
            System.out.println("Exception starting web browser");
            e.printStackTrace();
            throw new MalformedURLException(e.getMessage());
        }
        driver.manage().window().maximize();
    }
}

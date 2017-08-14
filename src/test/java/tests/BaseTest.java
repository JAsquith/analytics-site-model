package tests;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import pages.AnalyticsPage;
import pages.account.LoginPage;
import utils.TestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;

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

    public String applicationUrl;

    /**
     *
     */
    protected TestUtils utils;
    protected ITestContext context;

    @AfterTest
    public void tearDown(){
        try{
            new AnalyticsPage(driver).clickMenuLogout();
        } catch (Throwable t){}
        try{
            driver.quit();
        } catch (Throwable t){}
    }

    /**
     * Initialises a new test by instantiating a new RemoteWebDriver (and some other test properties) based on
     * the settings found in the TestNG xml suite file and the test.properties file.
     *
     * @param context_TestNG an object of a class that implements the @link{org.testng.ITestContext} interface
     * @throws MalformedURLException if the @link{utils.TestUtils#getGridUrl()} method can't build a valid URL from
     * the given settings
     */
    @Step( "Create a WebDriver and navigate to Analytics" )
    protected void initialise(ITestContext context_TestNG) throws MalformedURLException {
        context = context_TestNG;
        utils = new TestUtils(context_TestNG);
        try {
            driver = new RemoteWebDriver(utils.getGridUrl(), utils.getCapabilities());
        } catch (MalformedURLException e) {
            System.out.println("Exception starting web browser");
            e.printStackTrace();
            throw new MalformedURLException(e.getMessage());
        }
        //driver.manage().window().maximize();

        String testDomain = getStringParam("test.domain");
        String testProtocol = getStringParam("test.protocol");
        applicationUrl = testProtocol+"://"+testDomain+".sisraanalytics.co.uk";
        driver.get(applicationUrl);
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
    @Step( "Login to Analytics" )
    protected void login(String user, String pass, boolean endOtherSession){
        if (driver == null){
            throw new IllegalStateException("The RemoteWebDriver has not been instantiated");
        }
        LoginPage loginPage = new LoginPage(driver, true);
        loginPage.loginWith(user, pass, endOtherSession);
    }

    /**
     * Accesses a given test parameter and return its String value
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

    /**
     * Accesses a given test parameter and return its String value as an array
     * @param name
     * @param sep
     * @return
     */
    protected String[] getArrayParam(String name, String sep){
        return utils.getTestSettingAsArray(name, sep);
    }
    protected String[] getArrayParam(String name){
        return getArrayParam(name, "\\|");
    }

    protected int getIntegerParam(String name, int defaultVal){
        return utils.getTestSetting(name, defaultVal);
    }
    protected int getIntegerParam(String name){
        return getIntegerParam(name, 0);
    }

    protected boolean getBooleanParam(String name, boolean defaultVal){
        return utils.getTestSetting(name, defaultVal);
    }
    protected boolean getBooleanParam(String name){
        return getBooleanParam(name, false);
    }

    protected <T> void assertWithScreenshot(String reason, T actual, Matcher<? super T> matcher) {
        assertWithScreenshot(reason, actual, matcher, context.getName()+".png");
    }

    protected <T> void assertWithScreenshot(String reason, T actual, Matcher<? super T> matcher, String filename){
        try{
            assertThat(reason, actual, matcher);
        } catch (AssertionError ae){
            saveScreenshot(filename);
            throw ae;
        }
    }

    @Attachment(value = "{filename}")
    protected byte[] saveScreenshot(String filename){
        String base64Screenshot = driver.getScreenshotAs(OutputType.BASE64);
        byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot.getBytes());

        String dirPath = System.getProperty("user.home")+File.separator + "TestScreenshots";
        File directory = new File(String.valueOf(dirPath));
        directory.mkdir();

        filename = filename.replaceAll("[\\/:*?\"<>|]", "");
        filename = dirPath + File.separator + filename;
        File file = new File(filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedScreenshot);
            fos.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return decodedScreenshot;
    }
}

package tests;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matcher;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import pages.AnalyticsPage;
import pages.account.LoginPage;
import utils.TargetFileManager;
import utils.TestUtils;

import java.io.*;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static utils.TestConstants.TESTS_TO_RUN_FILE_NAME;


/**
 * An abstract class which takes care of functions common to all/most tests, for example: launching a new browser
 * session, logging in to Analytics, error reporting, etc.
 */
public abstract class BaseTest {
    /**
     *
     */
    public RemoteWebDriver driver;
    /**
     *
     */

    public String applicationUrl;
    protected boolean debugMode;

    protected TestUtils utils;
    protected ITestContext context;
    protected int logCount = 0;

    /**
     * Initialises a new test by instantiating a new RemoteWebDriver (and some other test properties) based on
     * the settings found in the TestNG xml suite file and the test.properties file.
     *
     * @param context_TestNG an object of a class that implements the @link{org.testng.ITestContext} interface
     *
     */
    @Step( "Create a WebDriver and navigate to Analytics" )
    protected String initialise(ITestContext context_TestNG) {
        initTestVariables(context_TestNG);

        String failMsg;
        if(!utils.runTest){
            failMsg = "Test is not listed in " + TESTS_TO_RUN_FILE_NAME;
            System.out.println(utils.testId+failMsg);
            return failMsg;
        }else{
            String dependsOnTestId = utils.getDependsOnTestId();
            if (!dependsOnTestId.equals("")) {
                System.out.println(utils.testId + " Depends On Test ID: " + dependsOnTestId);
                TargetFileManager fm = new TargetFileManager();
                if (!fm.testPassed(dependsOnTestId)) {
                    failMsg = "Predecessor test ("+dependsOnTestId+") either did not run, or ran but did not pass";
                    System.out.println(utils.testId+failMsg);
                    return failMsg;
                }
            }
        }

        try {
            driver = new RemoteWebDriver(utils.getGridUrl(), utils.getCapabilities());
        } catch (MalformedURLException e){
            e.printStackTrace();
            return "Failed to initialise RemoteWebDriver ["+e.getMessage()+"]";
        }

        driver.manage().window().setSize(new Dimension(1400,1600));

        driver.get(applicationUrl);
        return "";
    }

    @AfterTest
    public void tearDown(){
        try{
            new AnalyticsPage(driver).clickMenuLogout();
        } catch (Throwable t){}
        try{
            driver.quit();
        } catch (Throwable t){}
    }

    private void initTestVariables(ITestContext context_TestNG){
        context = context_TestNG;
        utils = new TestUtils(context_TestNG);
        debugMode = getBooleanParam("debug", false);

        applicationUrl = getStringParam("test.protocol")+"://"+
                getStringParam("test.domain") + ".sisraanalytics.co.uk";
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
            filename = filename.replaceAll("[\\/:*?\"<>|]", "");
            saveCurrentURL(filename.replace(".png",".url"));
            saveScreenshot(filename);
            throw ae;
        }
    }

    @Attachment(value = "{filename}")
    protected String saveCurrentURL(String filename){
        return driver.getCurrentUrl();
    }

    @Attachment(value = "[{logIndex}] Log Message")
    private String actualLogToAllure(int logIndex, String msg){
        System.out.println("["+logIndex+"] "+msg);
        logCount = logIndex;
        return msg;
    }

    protected String logToAllure(int logIndex, String msg){
        if (debugMode) return actualLogToAllure(logIndex, msg);
        return "";
    }

    @Attachment(value = "{filename}")
    protected byte[] saveScreenshot(String filename){

        String base64Screenshot = driver.getScreenshotAs(OutputType.BASE64);
        byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot.getBytes());

        String dirPath = System.getProperty("user.home")+File.separator + "TestScreenshots";
        File directory = new File(String.valueOf(dirPath));
        directory.mkdir();

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

    protected String getStackTraceAsString(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}

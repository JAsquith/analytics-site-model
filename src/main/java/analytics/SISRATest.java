package analytics;

import analytics.account.LoginPage;
import analytics.pages.AbstractAnalyticsObject;
import analytics.utils.HtmlLogger;
import analytics.utils.enums.LogLevel;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public abstract class SISRATest {

    //SISRATest Class Variables
    protected static long suiteStart;
    protected static long suiteEnd;
    protected static long suiteDuration;

    // SISRATest Instance Variables
    public AnalyticsDriver driver;
    protected String testNGCaseName;
    public Map<String, String> testParams;
    public String testDomain;
    public long testStart;
    public HtmlLogger htmlLog;
    public String logDir = null;
    public int logIndex = 0;
    public String logPrefix;
    public final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public final LogLevel logLevel = LogLevel.INFO;

    protected int valExpectedPage;
    protected int currentPage = 0;

    protected void initialise(ITestContext context_TestNG) throws MalformedURLException{
        testParams = context_TestNG.getCurrentXmlTest().getAllParameters();
        testDomain = getTestParam("test-domain");
        testNGCaseName = context_TestNG.getName();
        valExpectedPage = getTestParamAsInt("val-page");
        currentPage = 0;


        System.out.println(testNGCaseName + " on " + getHostName() + " testing " + testDomain);
        try {
            driver = new AnalyticsDriver(context_TestNG);
        } catch (MalformedURLException e) {
            System.out.println("Exception starting web browser");
            e.printStackTrace();
            throw new MalformedURLException(e.getMessage());
        }
        //driver.manage().window().setPosition(new Point(0,0));
        //driver.manage().window().setSize(new Dimension(1600,800));
        //driver.manage().window().maximize();
        //driver.manage().window().fullscreen();
    }

    /***
     * Simply sets the suite start time (suiteStart). <br>
     * TestNG Annotations: @BeforeSuite.
     */
    @BeforeSuite
    public void setupSuite(){
        suiteStart = System.currentTimeMillis();
        System.out.println("Suite started at: " + LocalTime.now());
    }

    /**
     * Simply logs that the suite has completed.
     * TestNG Annotations: @AfterSuite
     */
    @AfterSuite
    public void tearDownSuite() {
        suiteEnd = System.currentTimeMillis();
        suiteDuration = suiteEnd - suiteStart;
        Long seconds = suiteDuration/1000;
        System.out.println("Suite finished at: " + LocalTime.now());
        System.out.println("Duration: " + seconds + "." + (suiteDuration - (seconds*1000)));
        return;
    }

    public String getHostName(){

        String hostName;

        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (Exception e) {
            hostName = "Exception finding host name!";
        }

        return hostName;
    }
    public String getIPAddress(){
        String addresses = "";
        try {
            driver.get("https://diafygi.github.io/webrtc-ips/");
            List<WebElement> allAddressLIs = driver.findElements(By.cssSelector("body>ul>li"));
            if (allAddressLIs.size() == 0){
                addresses =  InetAddress.getLocalHost().getHostAddress();
            }

            String anAddress;
            for (WebElement address : allAddressLIs) {
                anAddress = address.getText();
                if (anAddress.startsWith("192.168") || anAddress.startsWith("10.10"))
                    addresses = anAddress + "; " + addresses;
            }
        } catch (Exception e) {
            addresses = "Exception finding IP";
        }
        return "IP Address(es): " + addresses;
    }

    public void failWith(String reason) {
        failWith(reason, false);
    }

    public void failWith(String reason, boolean screenshot){
        if (screenshot)
            logWithScreenshot(HtmlLogger.Level.ERROR, "Test failed because \""+reason+"\"", "TEST_FAILED");
        else
            log(HtmlLogger.Level.ERROR, "Test failed because \""+reason+"\"");

        Assert.fail(reason);
    }

    public void log(String msg) {
        log(HtmlLogger.Level.INFO,msg);
    }

    public void log(HtmlLogger.Level level, String msg){
        String test = (logPrefix==null)?"Suite":logPrefix;
        if (htmlLog == null) {
            System.out.println("Getting new Logger triggered by log call with: prefix='"+logPrefix+"'; msg='"+msg+"'");
            UUID uuid = UUID.randomUUID();
            htmlLog = new HtmlLogger(logDir, logLevel, uuid.toString());
            System.out.println("Logging to: " + htmlLog.getLogFileName());
            System.out.println("In: " + htmlLog.getLogDirectory());
        }

        // Messages at a lower level than the htmlLog's -> don't proceed
        if (htmlLog.getLevelInt() > level.index)
            return;

        if (!(msg.equals("") || test.equals("Suite")))
            logIndex++;

        try{
            switch(level){
                case DEBUG:htmlLog.debug(logIndex, "", test, msg);break;
                case INFO:htmlLog.info(logIndex, "", test, msg);break;
                case WARN:
                    htmlLog.warn(logIndex, "", test, msg);
                    flushLog();
                    break;
                case ERROR:
                    htmlLog.error(logIndex, "", test, msg);
                    flushLog();
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception writing to the log file");
            System.out.println("Log message was..");
            System.out.println(test + " - " + msg);
            e.printStackTrace();
        }
    }

    public void logWithScreenshot(HtmlLogger.Level level, String msg, String scrShotName){
        if (!msg.equals(""))
            log(level, msg);

        String test = (logPrefix==null)?"Suite":logPrefix;
        if (!scrShotName.equals("")) {
            scrShotName = logIndex + "_" + level.label + "_" + scrShotName;
            scrShotName = scrShotName.replace('*','^').replace("/","-");
            htmlLog.takeScreenshot(driver, logIndex, "", level, test, scrShotName);
        }
    }

    public void logWithCSVFile(HtmlLogger.Level level, String msg, String csvFileName, String reportData){
        log(level, msg);
        String test = (logPrefix==null)?"Suite":logPrefix;
        if (!csvFileName.equals("")) {
            csvFileName = logIndex + "_" + level.label + "_" + csvFileName;
            csvFileName = csvFileName.replace('*','^').replace("/","-");
            htmlLog.writeCSVFile(logIndex, "", level, test, csvFileName, reportData);
        }
    }

    public void flushLog(){
        try {htmlLog.updateFile();}
        catch (IOException io){
            io.printStackTrace();
        }
    }

    protected String getTestParam(String paramName){
        String paramValue = "";
        if (testParams.containsKey(paramName)){
            paramValue = testParams.get(paramName);
        }
        return paramValue;
    }

    protected String[] getTestParamAsStringArray(String paramName){
        return getTestParam(paramName).split("\\|");
    }

    protected boolean getTestParamAsBool(String paramName){
        String paramText = getTestParam(paramName);
        switch (paramText.toLowerCase()){
            case "true": case "yes": case "1":
                return true;
            default:
                return false;
        }
    }

    protected boolean[] getTestParamAsBoolArray(String paramName){
        String[] textArray = getTestParamAsStringArray(paramName);
        boolean[] boolArray = new boolean[textArray.length];
        for(int i = 0; i < textArray.length; i++){
            switch (textArray[i]){
                case "true": case "yes": case "1":
                    boolArray[i] = true;
                default:
                    boolArray[i] = false;
            }
        }
        return boolArray;
    }

    protected int getTestParamAsInt(String paramName){
        String paramText = getTestParam(paramName);
        int paramInt;
        try {
            paramInt = Integer.valueOf(paramText);
        } catch (Exception e){
            paramInt = 0;
        }
        return paramInt;
    }

    protected void login(){
        try {
            // Any actions which should happen prior to the actual test area go here
            //      (i.e. login, get to the right area, etc)
            // Standard login code; for tests where the login process is the thing being tested, move to the testMethod
            LoginPage testPage;
            testPage = new LoginPage(driver, true);
            String user = getTestParam("username");
            String pass = getTestParam("password");
            int loginResult = testPage.loginWith(user, pass, true);
            switch (loginResult){
                case 0:
                    break;
                case 1:
                    throw new IllegalArgumentException(
                            "Login failed - Validation (user: [" + user + "]; password: [" + pass + "])");
                case 2:
                    throw new IllegalArgumentException(
                            "Login failed - Other Active Session (user: [" + user + "]; password: [" + pass + "])");
                case 3:
                    // First login - Click First Login is handled by the LoginPage.loginWith()
                    //testPage = new LoginPage(driver, false);
                    //driver.findElement(testPage.FIRST_LOGIN_PASSWORD_KEEP).click();
                    //testPage.waitForLoginResult(false);
            }
            if(loginResult>0){
                throw new IllegalArgumentException(
                        "Analytics Login failed (user: [" + user + "]; password: [" + pass + "])");
            }
        } catch (Exception e) {
            // Exception while logging in
            throw e;
        }
    }

    // VALIDATION CHECKING
    /**
     *
     * @param object
     * @return  {@code true} if the validation state matched expectations
     *          {@code false} if the page unexpectedly failed validation at this point
     *          throws an {@link AssertionError} if validation was expected but did not match expectations
     */
    protected boolean checkForExpectedValidationFail(AbstractAnalyticsObject object){
        List<WebElement> valMessageElements = object.getValidationMessages();
        List<String> actualMessages = Lists.newArrayList("");
        for (WebElement valElement : valMessageElements){
            actualMessages.add(valElement.getText());
        }
        actualMessages.remove(actualMessages.get(0));

        if (valExpectedPage == currentPage){
            this.checkForPageValidationFail(actualMessages);
            // Success! The page was expected to fail validation and did so with the expected messages
            return true;
        }
        if (actualMessages.size()==0){
            // Success! The input was expected to pass validation and did
            return true;
        }
        // Whoops! The page unexpectedly failed validation
        return false;
    }

    protected void checkForPageValidationFail(List<String> actualMessages){

        List<String> expectedMessages = Lists.newArrayList(
                this.getTestParamAsStringArray("val-messages"));

        assertThat(
                "The validation message count is incorrect.",
                actualMessages.size(),
                is(expectedMessages.size()));

        for (String expMsg : expectedMessages) {
            assertThat("Validation message \"" + expMsg + "\" was not found in displayed messages",
                    actualMessages,
                    hasItem(expMsg));
        }
    }

    // DEBUGGING STUFF
    protected String saveScreenshots() {

        String filenameStub = testNGCaseName+"_"+currentPage+"_"+System.currentTimeMillis();

        // Only the viewport is captured, so to ensure we have a record of the current URL add it
        // as text to the actual web page; at the same time scroll back to the top of the page and
        // also calculate and return the number of pixels required to scroll the page down in
        // increments of 90% of the visible height
        String js = "var pinTag = document.querySelector('body>*:not(script)');" +
                "var url = document.createElement('p');" +
                "url.setAttribute('style', 'top: 10px; margin: 2px; margin-left:75px; position: static; text-transform: none;');" +
                "url.innerHTML = '" + driver.getCurrentUrl() + "';" +
                "pinTag.appendChild(document.createElement('p'));" +
                "pinTag.appendChild(url);" +
                "window.scrollTo(0, 0);" +
                "return Math.round(window.innerHeight * 0.9);";

/*
        Object obj = driver.executeScript(js);
        String stepStr = obj.toString();

*/
        int scrollStep = ((Long) driver.executeScript(js)).intValue();

        int prevScrollPos_Y = -1;
        int currScrollPos_Y = 0;

        js = "window.scrollBy(0,"+scrollStep+"); return window.pageYOffset;";
        int snapCount = 1;
        while(prevScrollPos_Y < currScrollPos_Y){
            prevScrollPos_Y = currScrollPos_Y;
            this.saveScreenshot(filenameStub+"("+snapCount+").png");
            snapCount++;
            currScrollPos_Y = ((Long) driver.executeScript(js)).intValue();
        }


        return "";
    }

    protected String saveScreenshot(String filename){
        String base64Screenshot = driver.getScreenshotAs(OutputType.BASE64);
        byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot.getBytes());

        String dirPath = getTestParam("log-folder")+ File.separator + "screenshots";
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

        return filename;
    }

}

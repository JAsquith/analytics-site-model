package analytics.legacy.tests;

import analytics.AnalyticsDriver;
import analytics.utils.HtmlLogger;
import analytics.utils.enums.LogLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SISRATest {

    // Some Default timeout constants
    protected final int DEFAULT_TIMEOUT = 20;
    protected final int DEFAULT_SHORT_TIMEOUT = 10;
    protected final int DEFAULT_LONG_TIMEOUT = 30;

    //SISRATest Class Variables
    protected static long suiteStart;
    protected static long suiteEnd;
    protected static long suiteDuration;

    // SISRATest Instance Variables
    public AnalyticsDriver driver;
    public Map<String, String> testParams;
    public long testStart;
    public HtmlLogger htmlLog;
    public String logDir = null;
    public int logIndex = 0;
    public String logPrefix;
    public final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public final LogLevel logLevel = LogLevel.INFO;
    public String currWinName = "LIVE";

    public SISRATest(){
        currWinName = "Main";
    }

    /***
     * Simply sets the suite start time (suiteStart). <br>
     * TestNG Annotations: @BeforeSuite.
     */
    @BeforeSuite
    public void setupSuite(){suiteStart = System.currentTimeMillis();}

    /**
     * Simply logs that the suite has completed.
     * TestNG Annotations: @AfterSuite
     */
    @AfterSuite
    public void tearDownSuite() {
        suiteEnd = System.currentTimeMillis();
        suiteDuration = suiteEnd - suiteStart;
        return;
    }

    public String findLocalIPAddresses(){

        try {
            String addresses = new String();
            driver.get("https://diafygi.github.io/webrtc-ips/");
            List<WebElement> allAddressLIs = driver.findElements(By.cssSelector("body>ul>li"));
            if (allAddressLIs.size() == 0) return "Local Address Not Found";
            String anAddress;
            for (WebElement address : allAddressLIs) {
                anAddress = address.getText();
                if (anAddress.startsWith("192.168"))
                    addresses = anAddress + "; " + addresses;
            }
            return addresses;
        } catch (Exception e) {
            return "Exception Finding Local IP Address";
        }
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
                case DEBUG:htmlLog.debug(logIndex, currWinName, test, msg);break;
                case INFO:htmlLog.info(logIndex, currWinName, test, msg);break;
                case WARN:
                    htmlLog.warn(logIndex, currWinName, test, msg);
                    flushLog();
                    break;
                case ERROR:
                    htmlLog.error(logIndex, currWinName, test, msg);
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
        log(level, msg);
        String test = (logPrefix==null)?"Suite":logPrefix;
        if (!scrShotName.equals("")) {
            scrShotName = logIndex + "_" + level.label + "_" + currWinName + "_" + scrShotName;
            scrShotName = scrShotName.replace('*','^').replace("/","-");
            htmlLog.takeScreenshot(driver, logIndex, currWinName, level, test, scrShotName);
        }
    }

    public void logWithCSVFile(HtmlLogger.Level level, String msg, String csvFileName, String reportData){
        log(level, msg);
        String test = (logPrefix==null)?"Suite":logPrefix;
        if (!csvFileName.equals("")) {
            csvFileName = logIndex + "_" + level.label + "_" + currWinName + "_" + csvFileName;
            csvFileName = csvFileName.replace('*','^').replace("/","-");
            htmlLog.writeCSVFile(logIndex, currWinName, level, test, csvFileName, reportData);
        }
    }

    public void flushLog(){
        try {htmlLog.updateFile();}
        catch (IOException io){
            io.printStackTrace();
        }
    }

    protected String getTestParameter(String paramName){
        String paramValue = null;
        if (testParams.containsKey(paramName)){
            paramValue = testParams.get(paramName);
        }
        return (paramValue == null) ? "" : paramValue;
    }


}

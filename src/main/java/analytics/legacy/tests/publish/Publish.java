package analytics.legacy.tests.publish;

import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import analytics.legacy.pages.DataArea;
import analytics.legacy.pages.DataArea_DataTab;
import analytics.legacy.pages.DataTab_PublishPage;
import analytics.legacy.pages.Login;
import analytics.utils.HtmlLogger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Contains methods to publish multiple cohorts. <br>
 * TODO: Add test config details
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-19
 */
public class Publish extends SISRATest {

    // Suite Information
    private static String[] keystageIDs;
    private static long suiteStart;
    private static int suitePublished;

    // SISRATest Information
    private String testID = "";
    private long testStart;

    // Formats
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");


    @Parameters({ "keystages" })
    @BeforeSuite
    public void setupSuite(String keystages) {
        suiteStart = System.currentTimeMillis();
        keystageIDs = keystages.split(",");
    }

    @Parameters({ "test-id", "username", "password" })
    @BeforeTest(alwaysRun = true)
    public void setupTest(String test_id, String username, String password,
                          ITestContext testContext) throws Exception {

        /**********************************************************************
         * Log the start of this test, get the test settings and a driver
         *********************************************************************/
        SimpleDateFormat logDate = new SimpleDateFormat("hh-mm-ss-a_");

        htmlLog = new HtmlLogger("", logLevel, logDate.format(new Date()) + testContext.getName());
        logPrefix = test_id;
        testID = test_id;
        testStart = System.currentTimeMillis();
        testContext.setAttribute("logDirectory", htmlLog.getLogDirectory());

        // Get a driver
        DesiredCapabilities caps = AnalyticsDriver.getCapabilities(testContext);
        URL gridURL = AnalyticsDriver.getGridURL(testContext);

        log("Getting RemoteWebDriver with: <br>"
                + caps.toString()
                + "<br>From hub at: " + gridURL.toString());
        this.driver = new AnalyticsDriver(testContext);

        try {driver.manage().window().maximize();}
        catch (Exception e) {System.out.println(e.getMessage());}

        testContext.setAttribute("session-id", driver.getSessionId().toString());
        log("Started RemoteWebDriver with session id: " + driver.getSessionId().toString()
                + " on machine with local address(es): " + findLocalIPAddresses());


        // Use a Login page object to get the login page and submit credentials
        Login loginPage = new Login(driver);
        loginPage.visit("HOME");
        log ("Logging in as " + username);
        loginPage.with(username, password, true);
        if (!loginPage.waitForLoginSuccess(DEFAULT_SHORT_TIMEOUT)){
            failWith("Login failed (username:" + username + ", password:" + password + ")");
        }

        flushLog();
    }

    @Parameters({ "oldest-cohort", "newest-cohort"})
    @Test
    public void loadQueueTest(String oldest_cohort, String newest_cohort,
                              ITestContext testContext)
            throws Exception  {

        String d = dateFormat.format(new Date());

        DataArea_DataTab dataTab = new DataArea_DataTab(this);
        dataTab.gotoData();

        int testPublished = 0;
        int firstCohort = Integer.parseInt(oldest_cohort);
        int lastCohort = Integer.parseInt(newest_cohort);

        // Loop thru' keystages listed in suite parameters
        log ("Checking " + keystageIDs.length + " Key Stages");
        for (String ks: keystageIDs){
            log ("KS " + ks);
            if (dataTab.selectKeystageAndWait(ks, DEFAULT_TIMEOUT)) {

                // Publish each available cohort
                log ("Checking " + ((lastCohort - firstCohort) + 1) + " cohorts");
                for (int j = firstCohort; j <= lastCohort; j++) {
                    String cohort = Integer.toString(j);

                    if (dataTab.selectCohortAndWait(Integer.toString(j), DEFAULT_TIMEOUT)) {

                        try {
                            dataTab.find(DataArea.PUBLISH_REPORTS);
                        } catch (Exception e) {
                            // If the Publish Reports link is not available it should be because it is greyed out
                            logWithScreenshot(HtmlLogger.Level.WARN, "Could not access Publish Reports page for cohort " + (j-1) + "/" + cohort, "NoAccess");
                            break;      // Break to the next cohort
                        }

                        // Open the Publish Reports page and instantiate an appropriate page object
                        dataTab.click(dataTab.PUBLISH_REPORTS);
                        DataTab_PublishPage publishPage = new DataTab_PublishPage(this);

                        //

                        if (dataTab.publishCurrentCohort(true, "Random")) {
                            log("Published cohort " + (j-1) + "/" + cohort);
                            testPublished++;
                        } else {
                            logWithScreenshot(HtmlLogger.Level.WARN, "Exams Warning Icons? for cohort " + (j-1) + "/" + cohort, "ExamsWarningIcons");
                        }

                    } else {
                        log("Cohort not selectable: " + (j-1) + "/" + cohort);
                    }
                    flushLog();
                }
            }
            flushLog();
        }

        // Go back to the admin page so we can see the Publish Queue for the selfie
        dataTab.gotoData();
        dataTab.waitForReload();
        dataTab.selectKeystageAndWait("", DEFAULT_SHORT_TIMEOUT);

        // Take a screenshot of the Publishing Queue & log out
        htmlLog.takeScreenshot(driver, testID, "PublishQueue");
        dataTab.logOut();

        testContext.setAttribute("published", Integer.toString(testPublished));
        suitePublished = suitePublished + testPublished;
        log("Published "+testPublished+" cohorts");
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() throws Exception {
        log("Quitting RemoteWebDriver");
        driver.quit();

        long diff = (System.currentTimeMillis() - testStart);
        //log("Test completed in " + (diff/1000) + " secs");
        String strDiff = String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(diff),
                TimeUnit.MILLISECONDS.toSeconds(diff) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        );
        log("Test completed in " + strDiff);
        log("|==============================|");
        try{htmlLog.closeAndAddToZip();}
        catch (IOException |URISyntaxException e){e.printStackTrace();}

    }

    @AfterSuite
    public void summary(ITestContext context) {
        String cohortsItem = "Cohorts published by all tests: " + Integer.toString(suitePublished);

        long diff = (System.currentTimeMillis() - suiteStart);
        String durationItem = String.format("Time Taken: %02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(diff),
                TimeUnit.MILLISECONDS.toSeconds(diff) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        );

        System.out.println(cohortsItem);
        System.out.println(durationItem);

        String suiteSummary = "<li>" + cohortsItem + "</li>";
        suiteSummary += "<li>" + durationItem + "</li>";
        context.setAttribute("SuiteSummary", suiteSummary);
    }

}
package analytics.legacy.tests.upload;

import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import analytics.legacy.pages.DataArea;
import analytics.legacy.pages.DataArea_UploadsTab;
import analytics.legacy.pages.Login;
import analytics.utils.FileManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.log4testng.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains methods to create collections and upload Student and grades files for
 * a given keystage and cohort. <br>
 * A TestNG.xml file invoking this class should contain the standard grid and browser
 * specification parameters, and the following test specific parameters:<ul>
 *     <li>username - a valid username for the target school</li>
 *     <li>password - to match the username</li>
 *     <li>keystage - the Keystage in which Collections should be created.
 *                    Must match a keystage in the target school.</li>
 *     <li>cohort - the Cohort in which Collections should be created.
 *                  "15" = Year 11 in 14/15. Must match a cohort in the
 *                  target school.</li>
 * </ul>
 * Logs in to Analytics &amp; Navigates to the Student Data Uploads page of
 * the given Keystage &amp; cohort; Searches for "*Student*.csv" files in the
 * Uploads\[keystage]\[cohort]\Students folder of the resources directory;
 * Creates a collection for each student file found &amp; uploads the student
 * file; Navigates to the Assessment Grades Uploads page; Searches for
 * "*Assessment*.csv" files in the Uploads\[keystage]\[cohort]\Assessment
 * folder of the resources directory, &amp; "*Banked*.csv" files in the
 * Uploads\[keystage]\[cohort]\Banked folder; Uploads each assessment file
 * to a different created collection, &amp; each banked grades file to every
 * created collection.
 *
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-19
 */
public class UploadCollections extends SISRATest {

    // WebDriver
    private AnalyticsDriver driver;
    private static Logger TEST_LOGGER;
    private DataArea_UploadsTab dataArea;
    private static short DEFAULT_TIMEOUT = 10;

    // Suite/SISRATest Information
    private static long suiteStart;
    private long testStart;
    private String logPrefix;
    private short collectionsCreated;
    private short studentFilesUploaded;
    private short gradeFilesUploaded;

    //
    private String[] collectionNames;

    /**
     * Simply logs that the suite is starting. <br>
     * TestNG Annotations: @BeforeSuite.
     */
    @BeforeSuite
    public void setupSuite() {
        TEST_LOGGER = Logger.getLogger(this.getClass());
        log("===== Starting Test Suite =====");
        log("Upload Collection Data Tests");
        suiteStart = System.currentTimeMillis();
    }

    /**
     * Logs that the test is being setup, and creates new {@link AnalyticsDriver},
     * {@link Login}, and {@link DataArea} objects for use in the @Test methods. <br>
     * TestNG Annotations:<ul>
     *     <li>@Parameters ({ "test-id" })</li>
     *     <li>@BeforeTest (alwaysRun = true)</li>
     * </ul>
     *
     * @param test_id       The testng.xml parameter "test-id", passed in as a String by
     *                      the TestNG test runner.
     * @param usr A username
     * @param pwd A password
     * @param testContext   An object which implements the ITestContext Interface and
     *                      represents the context of the current test.
     * @throws MalformedURLException if {@link AnalyticsDriver#getGridURL(ITestContext)}
     *                               returns a bad link.
     */
    @Parameters({ "test-id", "username", "password" })
    @BeforeTest(alwaysRun = true)
    public void setupTest(String test_id, String usr, String pwd, ITestContext testContext)
            throws MalformedURLException {

        // Logging and reporting (the "test_id" attribute is used by the TestListener to update Sauce Labs)
        log("Starting Test");
        this.testStart = System.currentTimeMillis();
        logPrefix = testContext.getName() + " (" + test_id + ") - ";
        testContext.setAttribute("test_id", test_id);

        // Get a driver
        DesiredCapabilities caps = AnalyticsDriver.getCapabilities(testContext);
        URL gridURL = AnalyticsDriver.getGridURL(testContext);
        this.driver = new AnalyticsDriver(testContext);

        // Use a Login page object to get the login page and submit credentials
        Login loginPage = new Login(driver);
        loginPage.visit("HOME");
        log("Logging in");
        loginPage.with(usr, pwd, true);
        loginPage.waitForLoginSuccess(DEFAULT_TIMEOUT);

        // Open the DATA area and get a page object handle...then we're ready to test
        dataArea = new DataArea_UploadsTab(this);
        dataArea.gotoData();
        dataArea.waitForReload();
   }

    /**
     * Logs in to Analytics, creates collections and uploads student files
     * for the cohort specified in the "keystage" and "cohort" parameters of
     * the TestNG test. <br>
     * Fails if: No files matching "*Student*.csv" are found in the
     * Uploads\[keystage]\[cohort]\Students folder of the resources
     * directory.<br>
     * TestNG Annotations:<ul>
     *     <li>@Parameters({ "username", "password", "keystage", "cohort" })</li>
     *     <li>@SISRATest</li>
     * </ul>
     *
     * @param ks A keystage ("3", "4" or "5")
     * @param cohort A cohort ID (Year 11 in 13/14 = "14")
     */
    @Parameters({ "keystage", "cohort" })
    @Test
    public void createCollectionsTest(String ks, String cohort) {
        log("Selecting KS & Cohort");
        dataArea.selectKSAndCohort(ks, cohort);
        dataArea.waitForReload();

        // Switch to the uploads tab & open the Student Data page
        log("Navigating to Student Uploads");
        dataArea.openStudentDataUploads();

        // Get an array of student files for this KS & cohort and
        // Use the count to create empty arrays of names and dates
        log("Finding Student Files");
        FileManager fm = new FileManager();
        File[] studentFiles = new File[0];
        try {
            studentFiles = fm.getUploadFiles(ks, cohort, "Students");
        } catch (URISyntaxException e) {
            Assert.fail("Failed to find Student files for KS" + ks + " > Cohort " + cohort, e);
        }
        String[] collectionDates = new String[studentFiles.length];
        collectionNames = new String[studentFiles.length];

        // Iterate over the List of student files creating and populating a collection for each
        // Name the collections the same as the student files (without the ".csv")
        // Date the collections starting 2n months ago, where n is the number of student files
        // Separate collections by one month
        log("Enumerating Collections from Student Files");
        Calendar collectionDate = Calendar.getInstance();
        collectionDate.add(Calendar.MONTH, (studentFiles.length * -2));
        for (int i = 0; i < studentFiles.length; i++) {
            String fileName = studentFiles[i].getName();
            collectionNames[i] = fileName.substring(0, fileName.lastIndexOf("."));
            collectionDates[i] = new SimpleDateFormat("dd/MM/yyyy").format(collectionDate.getTime());

            log("Creating a Collection");
            if (dataArea.createEmptyCollection(collectionDates[i], collectionNames[i]))
                collectionsCreated++;
            log("Uploading a Student File");
            if (dataArea.uploadStudentFile(studentFiles[i], collectionNames[i]))
                studentFilesUploaded++;

            collectionDate.roll(Calendar.MONTH, true);
        }
        dataArea.waitForReload();
        log("Completed Creating Collections");
    }

    /**
     * Uploads assessment and banked grades files for the cohort specified
     * in the "keystage" and "cohort" parameters of the TestNG test. <br>
     * Fails if: No files matching "*Assessment*.csv" are found in the
     * Uploads\[keystage]\[cohort]\Assessment folder of the resources directory.<br>
     * TestNG Annotations:<ul>
     *     <li>@Parameters({ "keystage", "cohort" })</li>
     *     <li>@SISRATest</li>
     * </ul>
     *
     * @param ks A keystage ("3", "4" or "5")
     * @param cohort A cohort ID (Year 11 in 13/14 = "14")
     */
    @Parameters({ "keystage", "cohort" })
    @Test(dependsOnMethods = {"createCollectionsTest"})
     public void uploadGradeFilesTest(String ks, String cohort) {

        log("Selecting KS & Cohort");
        dataArea.selectKSAndCohort(ks, cohort);
        // Switch to the uploads tab & open the Assessment Grades page
        log("Navigating to Assessment Grades Uploads");
        dataArea.openAssessmentGradesUploads();
        dataArea.waitForReload();

        // Get a List of all the assessment grades files
        log("Finding Assessment Grades Files");
        FileManager fm = new FileManager();
        File[] assessmentFiles = new File[0];
        try {
            assessmentFiles = fm.getUploadFiles(ks, cohort, "Assessment");
        } catch (URISyntaxException e) {
            Assert.fail("Failed to find Assessment Grade files for KS"+ks+" > Cohort "+cohort, e);
        }
        // Get a list of all the banked grade files
        log("Finding Banked Grades Files");
        File[] bankedFiles = new File[0];
        try {
            bankedFiles = fm.getUploadFiles(ks, cohort, "Banked");
        } catch (URISyntaxException e) {
            //Do nothing - there don't have to be any Banked Grades.
            //Assert.fail("Failed to find Banked Grade files for KS"+ks+" > Cohort "+cohort, e);
        }

        // Iterate over the Collections uploading assessment and banked grades
        // If there are more collections than assessment files
        // We need to choose the collections to populate, so...
        log("Enumerating Assessment Grades Files");
        int assessmentsToUpload = assessmentFiles.length;
        int collectionsToPopulate = collectionNames.length;
        for (String collectionName : collectionNames) {
            int probabilityOfUpload = ThreadLocalRandom.current().nextInt(0, 101);
            int currentAssessment = assessmentFiles.length - assessmentsToUpload;

            if (probabilityOfUpload >= 100 - ((assessmentsToUpload / collectionsToPopulate) * 100)) {
                log("Uploading Assessment Grades");
                if (dataArea.uploadGradesFile(assessmentFiles[currentAssessment], collectionName))
                    gradeFilesUploaded++;
                for (File bankedFile : bankedFiles) {
                    if (dataArea.uploadGradesFile(bankedFile, collectionName))
                        gradeFilesUploaded++;
                }
                log("Uploading Banked Grades");
                assessmentsToUpload--;
            }
            collectionsToPopulate--;
        }
    }

    /**
     * If an {@link AnalyticsDriver} was successfully started for the test,
     * logs ut of Analytics and quits the driver.&nbsp; Logs test completion.
     * TestNG Annotations: @AfterTest
     * Logs out of Analytics and quits the RemoteWebDriver
     */
    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            dataArea.logOut();
            log("Closing RemoteWebDriver session: " + driver.getSessionId().toString());
            driver.quit();
        }

        long diff = (System.currentTimeMillis() - testStart)/1000;
        log("Test completed in "+Long.toString(diff)+" secs");
    }

    /**
     * Simply logs that the suite has completed.
     * TestNG Annotations: @AfterSuite
     */
    @AfterSuite
    public void summary() {
        logPrefix = null;
        long difference = (System.currentTimeMillis() - suiteStart)/1000;
        log("Created " + Short.toString(collectionsCreated) + " Collections.");
        log("Uploaded " + Short.toString(studentFilesUploaded) + " Student Files");
        log("Uploaded " + Short.toString(gradeFilesUploaded) + " Grades Files");
        log("Suite Timed At: " + Long.toString(difference)+" secs");
    }

}
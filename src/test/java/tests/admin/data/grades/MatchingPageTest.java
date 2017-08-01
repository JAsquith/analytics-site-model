package tests.admin.data.grades;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import pages.data.grades.MatchingPage;
import pages.data.grades.components.MatchingQualRow;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Add Javadoc comments here!
 */

public class MatchingPageTest extends SISRATest {

    private String[] rowKeys;
    private String[] qualNames;
    private String[] includes;
    private String[] currents;
    private String[] eaps;
    private String[] measures;
    private String[] specials;
    private String[] faculties;
    private String[] predecessors;
    private String[] barCodes;

    @Test
    public void updateMatching() {
        try {
            this.login();                   // In SISRATest

            this.gotoPage1();               // Gets us to the Matching Page

            if(!this.page1Actions()){       // Sets fields on the row and saves changes
                return;                     // Found expected validation messages
            }

            this.successCriteria_ValidationPassed();    // Per-test Asserts in here


        } catch (AssertionError assertFail) {
            this.saveScreenshots();
            MatchingPage page = new MatchingPage(driver);
            page.clickCancel();
            throw assertFail;

        } catch (Exception other) {
            this.saveScreenshots();
            throw other;

        }
    }

    private void gotoPage1(){
        new AnalyticsPage(driver).waitForLoadingWrapper(30);
        DataHome dataPage = new DataHome(driver, true);
        DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
        modeAndCohort.selectMode("EAP");

        String cohort = getTestParam("eap-cohort");
        if (cohort.length() > 2){
            cohort = cohort.substring(cohort.length()-2);
        }

        DataSideMenu sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        if (!sideMenu.isTabSelected("GRADES")){
            sideMenu.clickTab("GRADES");
        }
        sideMenu.clickMenuOption("Matching");

        currentPage++;
    }

    private boolean page1Actions(){
        MatchingPage page = new MatchingPage(driver);

        this.initLists();
        page.initQualsMap();

        for(int i = 0; i < rowKeys.length; i++){
            MatchingQualRow row = page.getQualRowByQualName(rowKeys[i]);

            row.setQualName(qualNames[i]);
            row.selectInclude(includes[i]);
            row.selectCurrent(currents[i]);
            row.selectEAP(eaps[i]);
            row.selectMeasure(measures[i]);
            row.selectSpecial(specials[i]);
            if(!faculties[i].equals("N/A"))
                row.selectFaculty(faculties[i]);
            row.selectPredecessor(predecessors[i]);
            row.setBarCode(barCodes[i]);

        }

        page.clickSaveChanges();

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            return false;
        }

        return true;
    }

    private void initLists(){
        rowKeys = getTestParamAsStringArray("row-keys");
        qualNames = getTestParamAsStringArray("qual-name");
        includes = getTestParamAsStringArray("include");
        currents = getTestParamAsStringArray("current");
        eaps = getTestParamAsStringArray("eap");
        measures = getTestParamAsStringArray("measure");
        specials = getTestParamAsStringArray("special");
        faculties = getTestParamAsStringArray("faculty");
        predecessors = getTestParamAsStringArray("predecessor");
        barCodes = getTestParamAsStringArray("bar-code");
    }

    private void successCriteria_ValidationPassed() {
        // Qualification matching successfully saved
        MatchingPage detailPage = new MatchingPage(driver);

        assertThat("Success banner displayed",
                detailPage.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                detailPage.getSuccessBannerText(),
                is("Qualification matching successfully saved"));


    }

    @BeforeTest
    public void setup(ITestContext testContext) {
        // The initialise method of the SISRATest superclass:
        //      - Copies the test parameters from the context to the Map<String, String> field testParams
        //      - Creates a new AnalyticsDriver (starting a browser session)
        try {
            this.initialise(testContext);
        } catch (MalformedURLException e) {
            return;
        }
    }

    @AfterTest
    public void tearDown() {
        try {
            if (driver.getSessionId() != null) {
                AnalyticsPage page = new AnalyticsPage(driver);
                page.waitForLoadingWrapper();
                if (page.getMenuOptions().size() > 0) {
                    page.clickMenuHome();
                    page.waitForLoadingWrapper();
                    page.clickMenuLogout();
                    page.waitForLoadingWrapper();
                }
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package admin.data.basedata;

import analytics.SISRATest;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.DataHome;
import analytics.pages.data.basedata.BaselinesList;
import analytics.pages.data.basedata.components.BaselinesListRow;
import analytics.pages.data.components.DataAdminSelect;
import analytics.pages.data.components.DataSideMenu;
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

public class EditBaselinesTest extends SISRATest {

    @Test
    public void editBaselineSubjects() {
        try {
            this.login();

            this.gotoPage1();

            if(!this.page1Actions()){
                return;          // Expected validation messages
            }

            this.successCriteria_ValidationPassed();

        } catch (AssertionError assertFail) {
            this.saveScreenshots();
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
        if (!sideMenu.isTabSelected("KS2 / EAP")){
            sideMenu.clickTab("KS2 / EAP");
        }
        sideMenu.clickMenuOption("KS2 / Baseline");

        currentPage++;
    }

    public boolean page1Actions(){
        BaselinesList page = new BaselinesList(driver);
        page.clickEditButton();
        String[] subjectKeys = getTestParam("subject-keys").split("\\|");
        String[] subjectNames = getTestParam("subject-names").split("\\|");
        String[] subjectGradeTypes = getTestParam("grade-types").split("\\|");
        String[] subjectCores = getTestParam("core-noms").split("\\|");

        int index = 0;
        for (String subject : subjectKeys){
            BaselinesListRow row = new BaselinesListRow(driver, subject);
            if (subjectNames.length>index) {
                if (!subjectNames[index].equals("")) {
                    row.updateSubjectName(subjectNames[index]);
                }
            }
            row.updateGradeType(subjectGradeTypes[index]);
            if (subjectCores.length>index) {
                row.updateCoreNom(subjectCores[index]);
            }
            index++;
        }

        page.clickSaveButton();

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            return false;
        }

        currentPage++;
        return true;

    }

    protected void successCriteria_ValidationPassed() {
        BaselinesList listPage = new BaselinesList(driver);
        assertThat("Success banner displayed",
                listPage.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                listPage.getSuccessBannerText(),
                is("Qualification(s) successfully updated"));
    }

    @BeforeTest
    public void setup(ITestContext testContext) {
        // The initialise method of the SISRATest superclass:
        //      - Copies the test parameters from the context to the Map<String, String> field testParams
        //      - Creates a new AnalyticsDriver
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
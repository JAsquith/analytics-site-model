package tests.admin.data.basedata;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.basedata.CreateEAP;
import pages.data.basedata.EAPDetail;
import pages.data.basedata.EAPList;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Add Javadoc comments here!
 */

public class CreateEAPTest extends SISRATest {

    private String eapName = null;

    @Test
    public void createEAP() {
        try {
            this.login();

            this.gotoPage1();

            CreateEAP testPage = this.page1Actions();

            if(!this.page2Actions(testPage)){
                return;                         // Expected validation messages
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
        try {
            // Go to the Student Imports page for the eap-cohort parameter
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
            sideMenu.clickMenuOption("EAP");

            currentPage++;
        } catch (Exception other) {
            // The default behaviour for any type of Exception is to throw it up the call stack so
            // the TestNG invoker knows the test has failed, but we may also need to do some
            // tidying up before that to avoid leaving the school in an unpredictable state
            System.out.println(testNGCaseName + ": non-AssertionError Exception (" + other.getMessage() + ")");
            throw other;
        }
    }

    private CreateEAP page1Actions(){
        EAPList listPage = new EAPList(driver, false);
        CreateEAP nextPage = listPage.clickCreateEAP();
        currentPage++;
        return nextPage;
    }

    private boolean page2Actions(CreateEAP page) {
        this.doEAPLevelActions(page);
        if (page.getCreateOrCopySetting().equals("Create")){
            this.setAssessmentMethods(page);
        } else {
            this.setCopyFromExistingEAPFields(page);
        }
        page.clickCreate();

        if (!this.checkForExpectedValidationFail(page)) {
            // Page was not expected to fail validation at this point!
            throw new IllegalStateException("Page validation failed unexpectedly");
        }
        if (valExpectedPage == currentPage) {
            // Validation failed as expected, clean up and return so the test can end cleanly
            page.clickCancel();
            return false;
        }

        currentPage++;
        return true;
    }

    private void doEAPLevelActions(CreateEAP page){

        eapName = getTestParam("name");

        page
                .setEAPName(eapName)
                .setEAPDescription(getTestParam("description"));
        page
                .selectKS2BaselineType(getTestParam("baseline-type"))
                .selectKS2Baseline_Create(getTestParam("baseline"));

        String createOrCopy = getTestParam("create-or-copy").toLowerCase();
        boolean isFirstEAP = page.isFirstEAP();

        if (isFirstEAP && createOrCopy.equals("copy")){
            throw new IllegalArgumentException("The first EAP in a school can't be a copy.");
        }

        switch (createOrCopy) {
            case "create":
                if(!isFirstEAP) {
                    page.chooseCreateNewEAP();
                }
                break;
            case "copy":
                page.chooseCopyExistingEAP();
                break;
            default:
                throw new IllegalArgumentException("The create-or-copy test parameter must be one of: " +
                        "\"Create\", \"create\", \"Copy\" and \"copy\"");
        }
    }

    private void setAssessmentMethods(CreateEAP page){
        boolean[] byTermFlags = getTestParamAsBoolArray("by-term-flags");
        for (int yearIndex = 0; yearIndex < byTermFlags.length; yearIndex++){
            int academicYearIndex = yearIndex + 7;

            String[] assessMethodsByYear = getTestParamAsStringArray("assess-methods");
            if (byTermFlags[yearIndex]){
                // Get the y_-assess-methods param and set the three method fields for this year
                String[] assessMethodsByTerm = getTestParamAsStringArray("y"+academicYearIndex+"-assess-methods");
                for (int termIndex = 1; termIndex < 4; termIndex++){
                    page.selectAssessmentMethod(academicYearIndex, termIndex, assessMethodsByTerm[termIndex-1]);
                }
            } else {
                page.selectAssessmentMethod(academicYearIndex, 0, assessMethodsByYear[yearIndex]);
            }
        }
        page.selectFinalExamMethod(getTestParam("exam-assess-method"));
    }

    private void setCopyFromExistingEAPFields(CreateEAP page){
        page.selectCohortToCopyByCohortNum(getTestParam("copy-from-cohort"));
        page.selectEAPToCopy(getTestParam("copy-from-eap"));
    }

    protected void successCriteria_ValidationPassed() {
        EAPDetail detailPage = new EAPDetail(driver);

        assertThat("Success banner displayed",
                detailPage.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                detailPage.getSuccessBannerText(),
                is("EAP successfully created"));

        EAPList listPage = detailPage.clickBackToListLink();
        List<WebElement> links = listPage.getEAPDetailLinksMatching(eapName);
        assertThat("Number of links to new EAP is on EAP List page",
                links.size(),
                is(1));
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
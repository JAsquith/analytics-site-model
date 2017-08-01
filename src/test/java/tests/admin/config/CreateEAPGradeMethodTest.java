package tests.admin.config;

import tests.SISRATest;
import pages.AnalyticsPage;
import pages.config.*;
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

public class CreateEAPGradeMethodTest extends SISRATest {

    private String methodName;
    private boolean methodHasPoints = true;
    private boolean isSubGradeMethod = false;

    @Test
    public void createEAPGradeMethod() {

        try {
            this.login();

            // Goto the test page/area
            GradeMethodsList_EAP listPage = this.gotoTestPage();

            // List Page - click the create EAP button
            GradeMethodStep1 page1 = this.listPageActions(listPage);

            // Step 1 of 6 - Set up Grades Method
            if (!this.page1Actions(page1)){
                return;                     // Expected validation messages
            }

            // Step 2 of 6 - Define Whole Grades
            GradeMethodStep2 page2 = new GradeMethodStep2(driver);
            if(!this.page2Actions(page2)){
                return;                     // Expected validation messages
            }

            if (isSubGradeMethod) {
                // Step 3 of 6 - Define Sub Grades
                GradeMethodStep3 page3 = new GradeMethodStep3(driver);
                if(!this.page3Actions(page3)){
                    return;                 // Expected validation messages
                }

                // Step 4 of 6 - Define Master Grades
                GradeMethodStep4 page4 = new GradeMethodStep4(driver);
                if (!this.page4Actions(page4)){
                    return;                 // Expected validation messages
                }

                // Step 5 of 6 - Define Sub Grade Equivalencies
                GradeMethodStep5 page5 = new GradeMethodStep5(driver);
                if(!this.page5Actions(page5)){
                    return;                 // Expected validation messages
                }
            } else {currentPage += 3;}

            // Step 6 of 6 - Confirmation
            GradeMethodStep6 page6 = new GradeMethodStep6(driver);
            page6.clickFinish();
            currentPage++;

            this.successCriteria_ValidationPassed(page6);

        } catch (AssertionError assertFail){
            this.saveScreenshots();
            throw assertFail;

        } catch (Exception other){
            this.saveScreenshots();
            GradeMethodEdit page = new GradeMethodEdit(driver);
            List<WebElement> cancelButtons = driver.findElements(page.CANCEL_BUTTON);
            if(cancelButtons.size()>0){
                page.clickCancel();
            }
            throw other;

        }
    }

    private GradeMethodsList_EAP gotoTestPage(){
        GradeMethodsList_EAP page = new GradeMethodsList_EAP(driver, true);
        currentPage++;
        return page;
    }

    private GradeMethodStep1 listPageActions(GradeMethodsList_EAP page){
        GradeMethodStep1 nextPage = page.createNewGradeMethod();
        currentPage++;
        return nextPage;
    }

    private boolean page1Actions(GradeMethodStep1 page){
        methodName = getTestParam("method-name");
        page.setMethodName(methodName);
        boolean canBeSplit = page.setPointsType(getTestParam("points-type"));
        if (!isSubGradeMethod){
            page.setSubGrades(false);
        }
        if(canBeSplit && getTestParamAsBool("is-split-double")){
            page.setSplitGrades(true);
        }

        page.clickNext();
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

    private boolean page2Actions(GradeMethodStep2 page){
        page.closeSplitGradesModal();
        String[] grades = getTestParam("whole-grades").split("\\|");
        String[] points = getTestParam("grade-points").split("\\|");
        String[] entries = getTestParam("grade-entries").split("\\|");

        for (int i = 0; i < grades.length; i++){
            page.setWholeGradeText (i, grades[i]);
            if (methodHasPoints) {
                page.setWholeGradePoints(i, points[i]);
            }
            page.setWholeGradeEntries (i, entries[i]);
        }

        page.clickNext();
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

    private boolean page3Actions(GradeMethodStep3 page){
        String[] subGrades = getTestParam("sub-grades").split("\\|");
        for (int i = 0; i < subGrades.length; i++){
            page.setSubGradeText(i, subGrades[i]);
        }

        page.clickNext();

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

    private boolean page4Actions(GradeMethodStep4 page){
        String[] validMasterGrades = getTestParam("valid-master-grades").split("\\|");
        switch (validMasterGrades[0]){
            case "All":
                page.selectAll();
                break;
            case "None":
                page.selectNone();
                break;
            default:
                page.selectNone();
                // Should in one of two formats:
                //  "Ra" where a is a row number
                //  "RaCb" where a is a row number and b is a column number
                for (String validOption : validMasterGrades){
                    // Strip off the leading "R"
                    String rowColText = validOption.substring(1);
                    int indexOfC = validOption.indexOf('C');
                    if(indexOfC==-1){
                        // No column specified, select the entire row
                        int row = Integer.valueOf(rowColText);
                        page.selectRow(row);
                    } else {
                        String rowText = rowColText.substring(0,indexOfC-1);
                        String colText = rowColText.substring(indexOfC);
                        int row = Integer.valueOf(rowText);
                        int col = Integer.valueOf(colText);
                        page.selectSubGrade(row, col);
                    }
                }
        }

        page.clickNext();

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

    private boolean page5Actions(GradeMethodStep5 page){
        String[] gradeEquivs = getTestParam("whole-grade-equiv").split("\\|");
        for (int i = 0; i < gradeEquivs.length; i++){
           page.setSubGradeEquivalent (i, gradeEquivs[i]);
        }
        page.clickNext();

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

    protected void successCriteria_ValidationPassed(GradeMethodEdit page) {
        assertThat("Success Banner Displayed",
                page.expectSuccessBanner(false),
                is(true));

        assertThat("Success banner text",
                page.getSuccessBannerText(),
                is("Grades Method successfully created"));

        GradeMethodsList_EAP listPage = new GradeMethodsList_EAP(driver, true);
        assertThat("New Grade Method shown on List page",
                listPage.findGradeMethodFor(methodName),
                is(true));

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

        String pointsType = getTestParam("points-type").toLowerCase();
        if (pointsType.equals("none")){
            methodHasPoints = false;
        }
        isSubGradeMethod = getTestParamAsBool("has-sub-grades");
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
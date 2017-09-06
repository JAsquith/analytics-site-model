package tests.admin.data;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import pages.data.basedata.PublishBaseData;
import pages.data.grades.PublishGrades;
import pages.data.students.PublishStudents;
import tests.BaseTest;

import static org.testng.Assert.fail;

@Epic( "EAP Publishing" )
public class EAPPublishTest extends BaseTest{

    @BeforeTest()
    @Step( "Login, select the specified EAP Cohort, and open the specified Publish Page" )
    @Parameters( { "username", "password", "cohort", "publish-type" })
    public void setup(ITestContext testContext, String user, String pass, String cohort, String pubType){

        // Checks this test is in the run list, creates the browser session and initialises some global test properties
        String initResult = super.initialise(testContext);
        if (!initResult.equals("")){
            fail(initResult);
        }

        try {
            // Login
            login(user, pass, true);

            // Open the a publish page of the specified type
            openPublishPage(cohort, pubType);

        } catch (Exception e){
            saveScreenshot(context.getName()+"_SetupFail.png");
            if (driver!=null){
                driver.quit();
            }
            e.printStackTrace();
            fail("Test Setup Failed! Exception: "+e.getMessage());
        }
    }

    @Step( "Select EAP cohort '{cohort}' and open the '{pubType}' publish page" )
    private void openPublishPage(String cohort, String pubType){
        switch (pubType.toLowerCase()){
            case "students":
                PublishStudents studentsPub = new PublishStudents(driver).
                        load(cohort, true);
                break;
            case "ks2/eap":
                PublishBaseData basedataPub = new PublishBaseData(driver).
                        load(cohort, true);
                break;
            case "grades":
                PublishGrades gradesPub = new PublishGrades(driver).
                        load(cohort, true);
                break;
            default:
                throw new IllegalArgumentException("pubType (" + pubType + ") must be one of " +
                        "{'Students', 'KS2/EAP', 'grades'}");

        }
    }

}

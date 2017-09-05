package tests.admin.data.grades;

import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import pages.data.grades.PublishGrades;
import tests.BaseTest;

/**
 * Add Javadoc comments here!
 */

public class PublishGradesTest extends BaseTest {

    @BeforeTest
    public void setup(ITestContext testContext, String cohort){
        PublishGrades publishGrades = new PublishGrades(driver);
        publishGrades.load(cohort, true);
    }

}
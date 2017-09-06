package tests.admin.data.students;

import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.data.students.PublishStudents;
import tests.admin.data.EAPPublishTest;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;

/**
 * Add Javadoc comments here!
 */

@Story( "Publishing EAP Student Data" )
public class PublishStudentsTest extends EAPPublishTest {

    @Test( description = "Publish Student Data" )
    @Step( "Publish Student Data for {cohort}>{year}" )
    public void publishStudents(String cohort, String year){
        PublishStudents publishPage = new PublishStudents(driver);

        String publishInfo = publishPage.
                clickPublishAndWait().
                closeModal().
                getLastPublishedInfo();

        assertWithScreenshot("Last Publish Info does not end with 'Failed'", publishInfo, not(endsWith("Failed")));
    }

}
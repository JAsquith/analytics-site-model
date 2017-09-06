package tests.admin.data.basedata;

import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.data.basedata.PublishBaseData;
import tests.BaseTest;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;

/**
 * Add Javadoc comments here!
 */

@Story( "Publishing EAP Basedata Data" )
public class PublishBasedataTest extends BaseTest {

    @Test( description = "Publish EAP Basedata" )
    @Step( "Publish KS2/EAP Data for {cohort}>{year}" )
    public void publishBasedata(String cohort, String year){
        PublishBaseData publishPage = new PublishBaseData(driver);

        String publishInfo = publishPage.
                clickPublishAndWait().
                closeModal().
                getLastPublishedInfo();

        assertWithScreenshot("Last Publish Info does not end with 'Failed'", publishInfo, not(endsWith("Failed")));
    }

}
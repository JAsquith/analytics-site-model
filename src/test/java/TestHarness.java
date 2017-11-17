import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.TestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This class is used to verify bits of code within the test project
 */
public class TestHarness {

    @Test(dataProvider = "testDates")
    public void testDateParsing(String pubDate, Boolean expected){

        String cutoffString = "Friday 17th November 2017 at 09:57";
        LocalDateTime cutoffDT = TestUtils.parseLastPublishedString(cutoffString);

        String assertText = String.format("%s is before %s", pubDate, cutoffString);

        try {
            LocalDateTime pubDT = TestUtils.parseLastPublishedString(pubDate);

            boolean testResult = (pubDT.isBefore(cutoffDT) || pubDT.isEqual(cutoffDT));
            assertThat(assertText, testResult, is(expected));
        } catch (Exception e){
            System.out.println("Test Exception!");
        }
    }

    @DataProvider(name = "testDates")
    public Iterator<Object[]> createTestDates(){
        List<Object []> testCases = new ArrayList<>();

        String[] testValues = {
                "Today at 09:56",
                "Today at 09:57",
                "Today at 09:58",
                "Yesterday at 09:56",
                "Yesterday at 09:57",
                "Yesterday at 09:58",
                "Thursday 16th November 2017 at 09:56",
                "Thursday 16th November 2017 at 09:57",
                "Thursday 16th November 2017 at 09:58",
                "Friday 17th November 2017 at 09:56",
                "Friday 17th November 2017 at 09:57",
                "Friday 17th November 2017 at 09:58"
        };

        Boolean[] testResults = {
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                false
        };
        int i = 0;
        for(String testValue : testValues){
            testCases.add(new Object[]{testValue, testResults[i++]});
        }
        return testCases.iterator();
    }


}

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelFile;
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
    public void testDateParsing(String pubDate, String deployDate, String expOutcome)
    {

        LocalDateTime cutoffDT = TestUtils.parseDeployDateString(deployDate);
        LocalDateTime pubDT = TestUtils.parseLastPubDateString(pubDate);

        boolean testResult = (pubDT.isBefore(cutoffDT) || pubDT.isEqual(cutoffDT));

        String actOutcome = testResult ? "earlier than or equal to" : "later than";

        String assertText = String.format("%1$tD at %1$tR is %2$s %3$tD at %3$tR", pubDT, expOutcome, cutoffDT);
        assertThat(assertText, actOutcome, is(expOutcome));
    }

    @DataProvider(name = "testDates")
    public Iterator<Object[]> createTestDates(){
        List<Object []> testCases = new ArrayList<>();

        ExcelFile xlFile = null;
        int casesCount = 0;
        try
        {
            xlFile = new ExcelFile("PublishDateVerificationData.xlsx");
            casesCount = xlFile.getRowCount("CaseData") - 1;
        } catch (Exception e)
        {
            System.out.println("File Not Found: PublishDateVerificationData.xlsx");
            e.printStackTrace();
        }

        if (xlFile == null)
        {
            return testCases.iterator();
        }

        XSSFSheet xlSheet = xlFile.getXlWorkbook().getSheet("CaseData");

        for (int caseIndex = 1; caseIndex <= casesCount; caseIndex++)
        {
            XSSFRow xlRow = xlSheet.getRow(caseIndex);
            String pubDate = xlRow.getCell(0).getStringCellValue();
            String deployDate = xlRow.getCell(1).getStringCellValue();
            String expectedOutcome = xlRow.getCell(2).getStringCellValue();
            testCases.add(new Object[]{pubDate, deployDate, expectedOutcome});
        }
        return testCases.iterator();
    }

}

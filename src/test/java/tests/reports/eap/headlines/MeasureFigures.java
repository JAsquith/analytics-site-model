package tests.reports.eap.headlines;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPHeadlineView;
import tests.reports.eap.ReportTest;
import utils.ViewDataFileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.is;

@Epic( "EAP Reports" )
@Feature( "Headlines Summary Figures" )
@Story( "Check a Section of the Headlines Summary Report" )
public class MeasureFigures extends ReportTest{

    private String actualFiguresFilePath;
    private String expectedFiguresFilePath;
    private String section;

    @BeforeTest
    @Parameters( { "username", "password" })
    @Override
    public void setup(ITestContext testContext, String user, String pass){
        // Check against list
        // fail("Skipped test - " + testContext.getName() + " your name's not down you're not coming in")
        insertHeadlinesSummaryParams(testContext);
        super.setup(testContext, user, pass);
    }

    @Test( description = "1. Read the Report Section" )
    @Severity( SeverityLevel.CRITICAL )
    @Parameters( {"section-name", "expected-report-figures-file"} )
    @Step ( "Saving Name and {sectionName} values to actual/{expectedFiguresFileName}" )
    public void extractReportFigures(String sectionName, String expectedFiguresFileName){

        section = sectionName;

        try {
            ViewDataFileManager fileMgr = new ViewDataFileManager();
            expectedFiguresFilePath = fileMgr.getFullTableDataPath("expected" + File.separator + expectedFiguresFileName);
            actualFiguresFilePath = fileMgr.getFullTableDataPath("actual" + File.separator + expectedFiguresFileName);

            // Extract the actual report figures to a csv file in the same folder as the expected report figures
            EAPHeadlineView reportPage = new EAPHeadlineView(driver);
            fileMgr.createTableDataFileWithData("actual", expectedFiguresFileName, reportPage.readSectionData(sectionName));
        } catch (Exception e){
            String failMsg = "Error setting up table data files for: "+ getSectionDescriptor()+"..." + System.lineSeparator();
            assertWithScreenshot(failMsg, e.getMessage(), is(""));
        }
    }

    @Test( description = "2. Check Number of Rows",
            dependsOnMethods = "extractReportFigures")
    @Severity( SeverityLevel.CRITICAL )
    @Parameters( {"expected-report-figures-file"} )
    @Step ( "actual/{expectedFiguresFileName} & expected/{expectedFiguresFileName} have the same number of lines" )
    public void checkFileLengths(String expectedFiguresFileName){
        int expectedLines = 0;
        int actualLines = -1;
        try {
            expectedLines = ViewDataFileManager.countLines(expectedFiguresFilePath);
        } catch (Exception e){
            Assert.fail("Exception while counting lines in file '"+ expectedFiguresFilePath +"'", e);
        }
        try {
            actualLines = ViewDataFileManager.countLines(actualFiguresFilePath);
        } catch (Exception e){
            Assert.fail("Exception while counting lines in file '"+ actualFiguresFilePath +"'", e);
        }
        assertWithScreenshot("Number of report rows exported",
                actualLines, is(expectedLines));
    }

    @Test( description = "3. Check Row Details",
            dependsOnMethods = {"extractReportFigures"}, dataProvider = "reportFigures")
    @Severity( SeverityLevel.CRITICAL )
    @Step( "Row {lineNum}: ({actual}) matches ({expected})" )
    public void checkReportFigures(String lineNum, String expected, String actual){
        assertWithScreenshot("Row "+ lineNum + " in "+ getSectionDescriptor(), actual, is(expected));
    }

    /**
     *  Provides a line at a time from each of the expected and actual report figures files created by the extractReportFigures method
     * @return an @link{}Iterator<Object[]>} allowing the test method access to one pair of lines at a time
     */
    @DataProvider(name = "reportFigures")
    public Iterator<Object[]> createData1() throws IOException {
        List<Object []> testCases = new ArrayList<>();
        String[] data={""};

        BufferedReader expectedBr = new BufferedReader(new FileReader(expectedFiguresFilePath));
        BufferedReader actualBr = new BufferedReader(new FileReader(actualFiguresFilePath));
        String expectedLine; String actualLine;
        int lineNum = 0;

        while ((expectedLine = expectedBr.readLine()) != null &&
                (actualLine = actualBr.readLine()) != null){
            lineNum++;
            data = (lineNum+"~"+expectedLine+"~"+actualLine).split("~");
            testCases.add(data);
        }
        return testCases.iterator();
    }

    private String getSectionDescriptor(){
        return getStringParam("report-area")+
                ">" + getStringParam("report-view")+
                ">" + getStringParam("report-level")+
                ">" + section;
    }

    @Step( "Insert Report area/view/level parameters for Headlines Summary" )
    private void insertHeadlinesSummaryParams(ITestContext testContext){
        testContext.getCurrentXmlTest().addParameter("report-area", "Headlines");
        testContext.getCurrentXmlTest().addParameter("report-view", "Summary");
        testContext.getCurrentXmlTest().addParameter("report-level", "Whole School");
    }

}

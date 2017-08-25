package tests.reports.eap.headlines;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPHeadlineView;
import tests.reports.ReportTest;
import utils.FileManager;
import utils.TableDataFileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Epic( "EAP Reports" )
@Feature( "Headlines Summary Figures" )
@Story( "Check a Section of the Headlines Summary Report" )
public class MeasureFigures extends ReportTest{

    private String actualFiguresFile;
    private String expectedFiguresFile;
    private String section;

    @Test( description = "1. Read the Report Section" )
    @Severity( SeverityLevel.CRITICAL )
    @Parameters( {"section-name", "expected-report-figures-file"} )
    @Step ( "Saving Name and {sectionName} values to actual/{expectedFiguresFileName}" )
    public void extractReportFigures(String sectionName, String expectedFiguresFileName){

        section = sectionName;

        try {
            TableDataFileManager fileMgr = new TableDataFileManager();
            expectedFiguresFile = fileMgr.getFullPath("expected" + File.separator + expectedFiguresFileName);
            actualFiguresFile = fileMgr.getFullPath("actual" + File.separator + expectedFiguresFileName);

            // Extract the actual report figures to a csv file in the same folder as the expected report figures
            EAPHeadlineView reportPage = new EAPHeadlineView(driver);
            fileMgr.createFileWithData(actualFiguresFile, reportPage.readSectionData(sectionName));
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
            expectedLines = FileManager.countLines(expectedFiguresFile);
        } catch (Exception e){
            Assert.fail("Exception while counting lines in file '"+expectedFiguresFile+"'", e);
        }
        try {
            actualLines = FileManager.countLines(actualFiguresFile);
        } catch (Exception e){
            Assert.fail("Exception while counting lines in file '"+actualFiguresFile+"'", e);
        }
        assertWithScreenshot("Number of report rows exported",
                actualLines, is(expectedLines));
    }

    @Test( description = "3. Check Row Details",
            dependsOnMethods = {"extractReportFigures"}, dataProvider = "reportFigures")
    @Severity( SeverityLevel.CRITICAL )
    @Step( "Row {lineNum}: ({actual}) matches ({expected})" )
    public void checkReportFigures(String lineNum, String expected, String actual){
        try {
            assertThat("Row in "+ getSectionDescriptor(), actual, is(expected));
        } catch (Throwable t){
            saveScreenshot(context.getName()+".png");
            throw t;
        }
    }

    /**
     *  Provides a line at a time from each of the expected and actual report figures files created by the extractReportFigures method
     * @return an @link{}Iterator<Object[]>} allowing the test method access to one pair of lines at a time
     */
    @DataProvider(name = "reportFigures")
    public Iterator<Object[]> createData1() throws IOException {
        List<Object []> testCases = new ArrayList<>();
        String[] data={""};

        BufferedReader expectedBr = new BufferedReader(new FileReader(expectedFiguresFile));
        BufferedReader actualBr = new BufferedReader(new FileReader(actualFiguresFile));
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

}

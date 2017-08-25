package tests.reports.eap.general;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPListView;
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

@Epic( "EAP Reports - Column Figures" )
@Feature( "Figures in a specific view and column should be calculated correctly" )
public class ListViewFigures extends ReportTest{

    private String actualFiguresFile;
    private String expectedFiguresFile;
    private String column;
    private String table;

    @Test
    @Severity( SeverityLevel.CRITICAL )
    @Parameters( {"column-name", "expected-report-figures-file"} )
    @Story( "Extract column data to text file" )
    @Step ( "Saving Name and {columnName} values to actual/{expectedFiguresFileName}" )
    public void extractReportFigures(String columnName, String expectedFiguresFileName){

        column = columnName;
        table = getStringParam("table-name");

        try {
            TableDataFileManager fileMgr = new TableDataFileManager();
            expectedFiguresFile = fileMgr.getFullPath("expected" + File.separator + expectedFiguresFileName);
            actualFiguresFile = fileMgr.getFullPath("actual" + File.separator + expectedFiguresFileName);

            // Extract the actual report figures to a csv file in the same folder as the expected report figures
            EAPListView reportPage = new EAPListView(driver);
            fileMgr.createFileWithData(actualFiguresFile, reportPage.readColumnData(table, columnName));
        } catch (Exception e){
            String failMsg = "Error setting up table data files for: "+getColumnDescriptor()+"..." + System.lineSeparator();
            assertWithScreenshot(failMsg, e.getMessage(), is(""));
        }
    }

    @Test( dependsOnMethods = "extractReportFigures")
    @Severity( SeverityLevel.CRITICAL )
    @Parameters( {"expected-report-figures-file"} )
    @Story( "View contained the expected number of rows" )
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

    @Test( dependsOnMethods = {"extractReportFigures"}, dataProvider = "createData")
    @Story( "Values match expected data for one row" )
    @Step( "{viewColDesc} row ({actual}) matches expected ({expected})" )
    public void checkReportFigures(ITestContext testContext, String viewColDesc, String expected, String actual){
        try {
            assertThat("Row data from "+viewColDesc, actual, is(expected));
        } catch (Throwable t){
            saveScreenshot(testContext.getName()+".png");
            throw t;
        }
    }

    /**
     *  Provides a line at a time from each of the expected and actual report figures files
     *  created by the extractReportFigures method
     * @return an @link{}Iterator<Object[]>} allowing the test method access to one pair of lines at a time
     */
    @DataProvider(name = "reportFigures")
    public Iterator<Object[]> createData() throws IOException {
        List<Object []> testCases = new ArrayList<>();
        String[] data;

        BufferedReader expectedBr = new BufferedReader(new FileReader(expectedFiguresFile));
        BufferedReader actualBr = new BufferedReader(new FileReader(actualFiguresFile));
        String expectedLine; String actualLine;

        while ((expectedLine = expectedBr.readLine()) != null &&
                (actualLine = actualBr.readLine()) != null){

            data = (getColumnDescriptor()+"~"+expectedLine+"~"+actualLine).split("~");
            testCases.add(data);
        }
        return testCases.iterator();
    }

    private String getColumnDescriptor(){
        return getStringParam("report-area")+
                ">" + getStringParam("report-view")+
                ">" + getStringParam("report-level")+
                " (" + table+
                "[" + column+"])";
    }

}

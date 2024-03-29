package tests.reports.eap.general;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.EAPListView;
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
@Feature( "List View Column Figures" )
@Story( "Check a Column in a List View Report" )
public class ListViewFigures extends ReportTest{

    private String actualFiguresFilePath;
    private String expectedFiguresFilePath;
    private String column;
    private String table;

    @Test ( description = "1. Check Report Column Exported" )
    @Parameters( {"column-name", "expected-report-figures-file"} )
    @Step ( "Saving Name and {columnName} values to actual/{expectedFiguresFileName}" )
    public void extractReportFigures(String columnName, String expectedFiguresFileName){

        column = columnName;
        table = getStringParam("table-name");

        try {
            ViewDataFileManager fileMgr = new ViewDataFileManager();
            expectedFiguresFilePath = fileMgr.getFullTableDataPath("expected" + File.separator + expectedFiguresFileName);
            actualFiguresFilePath = fileMgr.getFullTableDataPath("actual" + File.separator + expectedFiguresFileName);

            // Extract the actual report figures to a csv file in the same folder as the expected report figures
            EAPListView reportPage = new EAPListView(driver);
            fileMgr.createTableDataFileWithData("actual", expectedFiguresFileName, reportPage.readColumnData(table, columnName));
        } catch (Exception e){
            String failMsg = "Error setting up table data files for: "+getColumnDescriptor()+"..." + System.lineSeparator();
            assertWithScreenshot(failMsg, e.getMessage(), is(""));
        }
    }

    @Test( description = "2. Check Report Export Row Count",
            dependsOnMethods = "extractReportFigures")
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
    @Step( "Row {lineNum}; ({actual}) matches ({expected})" )
    public void checkReportFigures(String lineNum, String expected, String actual){
        assertWithScreenshot("Row "+ lineNum + " in "+ getColumnDescriptor(), actual, is(expected));
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

        BufferedReader expectedBr = new BufferedReader(new FileReader(expectedFiguresFilePath));
        BufferedReader actualBr = new BufferedReader(new FileReader(actualFiguresFilePath));
        String expectedLine; String actualLine;
        int rowNum = 0;
        while ((expectedLine = expectedBr.readLine()) != null &&
                (actualLine = actualBr.readLine()) != null){
            rowNum++;
            data = (rowNum+"~"+expectedLine+"~"+actualLine).split("~");
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

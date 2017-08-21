package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsPage;
import pages.reports.components.Report_DatasetOptions;
import pages.reports.components.Report_FilterTabs;
import pages.reports.components.Report_GradeFilters;
import pages.reports.components.Report_ViewOptions;

import java.util.List;

/**
 * Represents the contents and interactive elements common to any KS3/4 Report page
 */
public class EAPReport extends AnalyticsPage {

    // Locators for report navigation
    public static final By AREAS = By.className("area");
    public static final By AREA_BUTTONS = By.cssSelector(".area .rept");
    public static final By REPORT_NAV_AREA_LINKS = By.cssSelector(".list-grid td>a");
    public static final By REPORT_NAV_VIEW_LABELS = By.cssSelector("td.title-y:nth-of-type(2)");
    public static final By REPORT_NAV_LEVEL_LABELS = By.cssSelector("td.title-x");
    public static final By LIST_GRID_ROWS = By.cssSelector("tr.btn");
    public static final By LIST_GRID_COLS = By.cssSelector("tr:nth-of-type(3)>td");
    private static final String EXTRACT_COLUMN_JS = "oldDiv = document.querySelector('#se-table-data');" +
            "if (oldDiv != null) {  oldDiv.parentNode.removeChild(oldDiv);}" +
            "locator = 'table.rpt.stickyHead:nth-of-type(' + tableIndex + ')';" +
            "tableText = '';colIndexes = [];" +
            "tableRowElements = document.querySelectorAll(locator + ' tr');" +
            "titles = tableRowElements[0].querySelectorAll('th');" +
            "if (titles[0].textContent.trim() == '') {" +
            "  colIndexes.push(0);" +
            "  tableText = ','" +
            "}" +
            "for (i = 0; i < titles.length; i++) {" +
            "  colTitle = titles[i].textContent.trim();" +
            "  colTitle = colTitle.replace(/(\\?\\s+)/, '').trim();" +
            "  if (colTitle == 'Name' || colTitle == columnName) {" +
            "    colIndexes.push(i);" +
            "    if (tableText != '' && tableText != ',') {tableText += ',';}" +
            "    tableText += colTitle;" +
            "  }" +
            "}" +
            "tableText += '<br>';" +
            "for (i = 1; (i < tableRowElements.length); i++) {" +
            "  rowText = '';" +
            "  rowCellElements = tableRowElements[i].querySelectorAll('td,th');" +
            "  if (rowCellElements.length > 0) {" +
            "    for (j = 0; j < colIndexes.length; j++) {" +
            "      if (j != 0) { rowText += ',';}" +
            "      rowText += rowCellElements[colIndexes[j]].textContent.trim();" +
            "    }" +
            "  }" +
            "  tableText += rowText + '<br>';" +
            "}" +
            "newDiv = document.createElement('DIV');" +
            "newDiv.setAttribute('id', 'se-table-data');" +
            "newDiv.innerHTML = tableText;" +
            "document.querySelector('body>*:not(script)').appendChild(newDiv);";

    // Locators for dataset DDLs are public in the Report_DatasetOptions class
    public Report_DatasetOptions dsOptions;

    // Locators for Filters/Measures/Residual Exclusions tabs (and the buttons within them)
    // are public in the Report_FilterTabs class
    public Report_FilterTabs filterTabs;

    // Locators for Grade filters (On Track, Faculty, Class, Grade Type, etc)
    // are public in the Report_GradeFilters class
    public Report_GradeFilters gradeFilters;
    
    // Locators for View Options (Column Sort, Count/Percent, Standard/Cumulative, Breakdown, StuInfo, A8 Basket, Sub/Whole)
    public Report_ViewOptions viewOptions;

// CONSTRUCTORS
    public EAPReport(RemoteWebDriver aDriver){
        super(aDriver);
        filterTabs = new Report_FilterTabs(driver);
        gradeFilters = new Report_GradeFilters(driver);
        viewOptions = new Report_ViewOptions(driver);
        dsOptions = new Report_DatasetOptions(driver);
        waitMedium.until(ExpectedConditions.elementToBeClickable(dsOptions.DATASET_DDL));
    }

// METHODS
    //  - CHANGING THE STATE OF THE PAGE
    public EAPReport openView(String areaName, String reportName, String levelName){

        // Show the table of links to views within the given areaName
        WebElement area = expandAreaGrid(areaName);

        // Find the link to the given level of the given report
        WebElement button = getViewLinkFromListGrid(area, reportName, levelName);
        if (button.getText().trim().equals("Active")){
            // The requested view is already active, close the area grid and exit
            area.click();
            return this;
        }
        // The requested view is not currently active, click the button and wait for the reload
        button.click();
        waitForLoadingWrapper();
        return this;
    }

    // QUERYING THE CURRENT PAGE STATE
    public String readTableData(String tableName) {
        int tableIndex = this.findNamedTableIndex(tableName);
        switch (tableIndex){
            case -1:
                return "Table [" + tableName + "] not found";
            case -2:
                return "No tables found - check Report Options";
            default:
                return this.extractTableData(tableIndex);
        }
    }

    public String readTableData(){
        return this.extractMultiTableData();
    }

    public String readColumnData(String tableName, String columnName){
        int tableIndex = this.findNamedTableIndex(tableName);
        switch (tableIndex) {
            case -1:
                return "Table [" + tableName + "] not found";
            case -2:
                return "No tables found - check Report Options";
            default:
                return this.extractColumnData(tableIndex, columnName);
        }
    }
    public String readColumnData(String columnName){
        return this.extractColumnData(columnName);
    }

    // PRIVATE HELPER METHODS FOR THE ABOVE PUBLIC METHODS
    private WebElement getViewLinkFromListGrid(WebElement area, String reportName, String levelName){

        List<WebElement> rowLabels = area.findElements(REPORT_NAV_VIEW_LABELS);
        //int rowIndex = area.findElements(LIST_GRID_ROWS).size() - rowLabels.size();
        int rowIndex = 2;

        for (WebElement label: rowLabels){
            if (label.getText().trim().equals(reportName)){
                rowIndex += rowLabels.indexOf(label) + 1;
                break;
            }
        }

        List<WebElement> colLabels = area.findElements(REPORT_NAV_LEVEL_LABELS);
        int colIndex = area.findElements(LIST_GRID_COLS).size() - colLabels.size();

        for (WebElement label: colLabels){
            if (label.getText().trim().equals(levelName)){
                colIndex += colLabels.indexOf(label) + 1;
                break;
            }
        }
        String cssString = "tr:nth-of-type(" + rowIndex + ")>td:nth-of-type(" + colIndex + ")>a";
        WebElement link;
        try {
            link = area.findElement(By.cssSelector(cssString));
        } catch (NoSuchElementException nsee) {
            // Temporary logging for updated grid layout
            String activeArea = area.findElement(By.tagName("span")).getText();
            System.out.println("Active Area: " + activeArea);
            System.out.println("CSS Selector: " + cssString);
            throw new IllegalArgumentException("A link to report '" + reportName +
                    "' at level '" + levelName + "' could not be found.");
        }
        return link;
    }

    private WebElement expandAreaGrid(String areaName){
        // Given an area name (like "Grades", "Matrix", "Student Detail", etc) click the button which
        // expands the 'list-grid' of views in that area
        List<WebElement> areas = driver.findElements(AREAS);
        List<WebElement> areaButtons = driver.findElements(AREA_BUTTONS);
        for (WebElement button: areaButtons){
            if (button.getText().trim().toUpperCase().equals(areaName.toUpperCase())){
                WebElement area = areas.get(areaButtons.indexOf(button));
                button.click();
                waitForListGridDisplay(area);
                return area;
            }
        }
        throw new IllegalArgumentException("A Report Area named '" + areaName + "' was not found");
    }

    private List<WebElement> waitForListGridDisplay(WebElement area){
        return waitMedium.until(ExpectedConditions.visibilityOfAllElements(area.findElements(REPORT_NAV_AREA_LINKS)));
    }

    private int findNamedTableIndex(String tableName){

        List<WebElement> tableTitleElements = driver.findElements(By.className("tableTitle"));
        if (tableTitleElements.size()==0){
            return -2;
        }

        int tableIndex = -1;
        for (WebElement titleElement: tableTitleElements){
            String title = titleElement.getText();
            List<WebElement> subTitles = titleElement.findElements(By.className("smallInfo"));
            if (subTitles.size()>0){
                if (title.trim().equals(tableName)) {
                    tableIndex = tableTitleElements.indexOf(titleElement) + 1;
                    break;
                }
                title = title.replace(titleElement.findElement(By.className("smallInfo")).getText(),"");
            }
            if (title.trim().equals(tableName)) {
                tableIndex = tableTitleElements.indexOf(titleElement) + 1;
                break;
            }
        }

        return tableIndex;
    }

    private String extractColumnData(int tableIndex, String columnTitle){
        String csvText;
        String js = "tableIndex = '" + tableIndex + "';"
                + "columnName = '" + columnTitle + "';"
                + EXTRACT_COLUMN_JS;
        try {
            driver.executeScript(js);
        } catch (JavascriptException jse){
            throw new AssertionError("Javascript Execution failure ("
                    + jse.getMessage() + ". Script: [" + js + "]");
        }
        csvText = driver.findElement(By.id("se-table-data")).getText();
        js = "var elem = document.getElementById('se-table-data');" +
                "elem.parentNode.removeChild(elem);";
        try {
            driver.executeScript(js);
        } catch (JavascriptException jse){
            /* we've got what we want, and any action/page refresh will get rid, so we don't really care! */
        }
        return csvText;
    }

    private String extractColumnData(String columnTitle){
        String csvText="";
        List<WebElement> tableHeadings = driver.findElements(By.cssSelector(".tableTitle"));
        for (WebElement tabHeading : tableHeadings){
            if(!csvText.equals("")){
                csvText += System.lineSeparator();
            }
            int tableIndex = findNamedTableIndex(tabHeading.getText().trim());
            String js = "tableIndex = '" + tableIndex + "';"
                    + "columnName = '" + columnTitle + "';"
                    + EXTRACT_COLUMN_JS;
            try {
                driver.executeScript(js);
            } catch (JavascriptException jse){
                throw new AssertionError("Javascript Execution failure ("
                        + jse.getMessage() + ". Script: [" + js + "]");
            }
            csvText += driver.findElement(By.id("se-table-data")).getText();
            js = "var elem = document.getElementById('se-table-data');" +
                    "elem.parentNode.removeChild(elem);";
            try {
                driver.executeScript(js);
            } catch (JavascriptException jse){
            /* we've got what we want, and any action/page refresh will get rid, so we don't really care! */
            }
        }
        return csvText;
    }

    private String extractTableData(int tableIndex){

        String tableLocator = "table.rpt.stickyHead:nth-of-type(" + tableIndex + ")";

        String js = "var tableText = '';" +
                "var tableRowElements = document.querySelectorAll('" + tableLocator + " tr');" +
                "for (var i=0; i < tableRowElements.length; i++){" +
                "var rowCellElements = tableRowElements[i].querySelectorAll('td,th');" +
                "if (rowCellElements.length > 0) {" +
                "var rowText = rowCellElements[0].textContent.trim();" +
                "for (var j=1; j < rowCellElements.length; j++){" +
                "rowText += ',' + rowCellElements[j].textContent.trim();}" +
                "tableText += rowText + '<br>';}}" +
                "var newDiv = document.createElement('DIV');" +
                "newDiv.setAttribute ('id', 'se-table-data');" +
                "newDiv.innerHTML = tableText;" +
                "document.querySelector('body>*:not(script)').appendChild(newDiv);";

        String tableData;
        try {
            driver.executeScript(js);
            tableData = driver.findElement(By.id("se-table-data")).getText();

            js = "var elem = document.getElementById('se-table-data');" +
                    "elem.parentNode.removeChild(elem);";
            driver.executeScript(js);
        }catch(JavascriptException jse){
            throw new AssertionError("Named Table - Javascript Execution failure ("
                    + jse.getMessage() + ". Script: [" + js + "]");
        }

        return tableData;
    }

    private String extractMultiTableData(){

        String js = "var allTitles = document.querySelectorAll('.tableTitle');" +
                "var allTables = document.querySelectorAll('table.rpt.stickyHead');" +
                "var tableCount = allTitles.length;" +
                "var allText = '';" +
                "for (var t=0; t < tableCount; t++){" +
                "var tableText = allTitles[t].textContent.trim()+'<br>';" +
                "var tableRowElements = allTables[t].querySelectorAll('tr');" +
                "for (var i=0; i < tableRowElements.length; i++){" +
                "var rowCellElements = tableRowElements[i].querySelectorAll('td,th');" +
                "if (rowCellElements.length > 0) {" +
                "var rowText = rowCellElements[0].textContent.trim();" +
                "for (var j=1; j < rowCellElements.length; j++){" +
                "rowText += ',' + rowCellElements[j].textContent.trim();}" +
                "tableText += rowText + '<br>';}}" +
                "allText += tableText}" +
                "var newDiv = document.createElement('DIV');" +
                "newDiv.setAttribute ('id', 'se-table-data');" +
                "newDiv.innerHTML = allText;" +
                "document.querySelector('body>*:not(script)').appendChild(newDiv);";

        String tableData;
        try {
            driver.executeScript(js);
            tableData = driver.findElement(By.id("se-table-data")).getText();

            js = "var elem = document.getElementById('se-table-data');" +
                    "elem.parentNode.removeChild(elem);";
            driver.executeScript(js);
        }catch(JavascriptException jse){
            throw new AssertionError("Multi Table - Javascript Execution failure ("
                    + jse.getMessage() + ". Script: [" + js + "]");
        }

        return tableData;
    }

}

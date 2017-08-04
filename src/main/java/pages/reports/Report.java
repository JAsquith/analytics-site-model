package pages.reports;

import io.qameta.allure.Step;
import pages.AnalyticsPage;
import pages.reports.components.Report_AddStudentFilters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import pages.reports.components.Report_FilterTabs;
import pages.reports.components.Report_GradeFilters;
import pages.reports.components.Report_ViewOptions;

import java.util.List;

/**
 * Represents the contents and interactive elements common to any KS3/4 Report page
 */
public class Report extends AnalyticsPage {

    // Locators for report navigation
    public static final By AREAS = By.className("area");
    public static final By AREA_BUTTONS = By.cssSelector(".area .rept");
    public static final By AREA_LIST_GRID_LINKS = By.cssSelector(".list-grid td>a");
    public static final By LIST_GRID_ROW_LABELS = By.cssSelector("td.title-y:nth-of-type(2)");
    public static final By LIST_GRID_COL_LABELS = By.cssSelector("td.title-x");
    public static final By LIST_GRID_ROWS = By.cssSelector("tr.btn");
    public static final By LIST_GRID_COLS = By.cssSelector("tr:nth-of-type(3)>td");

    // Locators for dataset DDLs
    public static final By DATASET_DDL = By.id("actualSelect");
    public static final By COMPARE_WITH_DDL = By.id("compareSelect");

    // Locators for Filters/Measures/Residual Exclusions tabs (and the buttons within them)
    // are public in the Report_FilterTabs class
    public Report_FilterTabs filterTabs;

    // Locators for Grade filters (On Track, Faculty, Class, Grade Type, etc)
    // are public in the Report_GradeFilters class
    public Report_GradeFilters gradeFilters;
    
    // Locators for View Options (Column Sort, Count/Percent, Standard/Cumulative, Breakdown, StuInfo, A8 Basket, Sub/Whole)
    public Report_ViewOptions viewOptions;

// CONSTRUCTORS
    public Report(RemoteWebDriver aDriver){
        super(aDriver);
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DDL));
        filterTabs = new Report_FilterTabs(driver);
        gradeFilters = new Report_GradeFilters(driver);
        viewOptions = new Report_ViewOptions(driver);
    }

// METHODS
    /*
     * List of Methods to code:
     * ToDo - Possibly a method for addOrRemoveFilters; is this any different from the openStudentFiltersModal method?
     */
    // QUERYING THE CURRENT PAGE STATE

    //  - CHANGING THE STATE OF THE PAGE
    @Step( "Open View: ${0} > ${1} > ${2}" )
    public Report openView(String areaName, String reportName, String levelName){

        // Show the table of links to views within the given areaName
        WebElement area = expandAreaGrid(areaName);

        // Find the link to the given level of the given report
        getViewLinkFromListGrid(area, reportName, levelName).click();
        waitForLoadingWrapper();
        return this;
    }
    public Report selectDataset(String optionText){

        WebElement select = driver.findElement(DATASET_DDL);
        select.click();

        List<WebElement> allOptions = select.findElements(By.tagName("option"));
        WebElement choice = select.findElement(By.xpath("option[contains(text(),'"+optionText+"')]"));
        int choiceIndex = -1;
        for(WebElement option : allOptions){
            if (option.getText().equals(choice.getText())){
                choiceIndex = allOptions.indexOf(option);
                break;
            }
        }
        if (choiceIndex == -1){
            throw new IllegalArgumentException("Could not match '"+optionText+"' to a visible Data Set option");
        }

        new Select(select).selectByIndex(choiceIndex);
        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DDL));
        return this;
    }

    /**
     * Convenience method which passes through to the filterTabs property (which is a @link{Report_FilterTabs} object)
     * @return a @link{Report_AddStudentFiltersModal} object
     */
    public Report_AddStudentFilters openFiltersModal(){
        return filterTabs.openStudentFiltersModal();
    }

    public Report selectCompareWith(String optionText){
        List<WebElement> compWithDDLs = driver.findElements(COMPARE_WITH_DDL);
        if (compWithDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't compare with '" + optionText + "' because Compare With is not available");
            }
            return this; // The Compare With DDL is not currently available
        }
        WebElement select = driver.findElement(COMPARE_WITH_DDL);
        select.click();
        List<WebElement> allOptions = select.findElements(By.tagName("option"));
        WebElement choice = select.findElement(By.xpath("option[contains(text(),'"+optionText+"')]"));
        int choiceIndex = -1;
        for(WebElement option : allOptions){
            if (option.getText().equals(choice.getText())){
                choiceIndex = allOptions.indexOf(option);
                break;
            }
        }
        if (choiceIndex == -1){
            throw new IllegalArgumentException("Could not match '"+optionText+"' to a visible Compare With option");
        }

        new Select(select).selectByIndex(choiceIndex);
        waitForLoadingWrapper();
        return this;
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param trackStatus   a @link{String} identifying the type of track filtering to apply
     * @return  a (refreshed) @link{Report}
     */
    public Report filterByTrack(String trackStatus){
        return gradeFilters.filterByTrack(trackStatus);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the faculty to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectFaculty(String optionText){
        return gradeFilters.selectFaculty(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the qualification to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectQualification(String optionText){
        return gradeFilters.selectQualification(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the class to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectClass(String optionText){
        return gradeFilters.selectClass(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the grade method to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectGradeType(String optionText){
        return gradeFilters.selectGradeType(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String}, either "GCSE" or "Non-GCSE"
     * @return  a (refreshed) @link{Report}
     */
    public Report selectAwardClass(String optionText){
        return gradeFilters.selectAwardClass(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the KS2Core to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectKS2Core(String optionText){
        return gradeFilters.selectKS2Core(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText one of: "less than", "equal to", "greater or equal" or "greater than"
     * @return  a (refreshed) @link{Report}
     */
    public Report selectGradeFilterType(String optionText){
        return gradeFilters.selectGradeFilterType(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the whole grade to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectGradeFilterWhole(String optionText){
        return gradeFilters.selectGradeFilterWhole(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a @link{Report_GradeFilters} object)
     * @param optionText    a @link{String} identifying the sub grade to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectGradeFilterSub(String optionText){
        return gradeFilters.selectGradeFilterSub(optionText);
    }

    /**
     * Convenience method which passes through to the viewOptions property (which is a @link{Report_ViewOptions} object)
     * @param optionText    a @link{String} identifying the column to sort by
     * @return  a (refreshed) @link{Report}
     */
    public Report selectColSort(String optionText){
        return viewOptions.selectColSort(optionText);
    }

    /**
     * Convenience method which passes through to the viewOptions property (which is a @link{Report_ViewOptions} object)
     * @return  a (refreshed) @link{Report}
     */
    public Report toggleColSortDirection(){
        return viewOptions.toggleColSortDirection();
    }

    /**
     * Convenience method which passes through to the viewOptions property (which is a @link{Report_ViewOptions} object)
     * @param optionText    a @link{String} identifying the filter to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectBreakdown(String optionText){
        return viewOptions.selectBreakdown(optionText);
    }

    /**
     * Convenience method which passes through to the viewOptions property (which is a @link{Report_ViewOptions} object)
     * @param figType one of: "Percentage", "%", "Count", or "#"
     * @return  a (refreshed) @link{Report}
     */
    public Report setFigType(String figType){
        return viewOptions.setFigType(figType);
    }

    /**
     * Convenience method which passes through to the viewOptions property (which is a @link{Report_ViewOptions} object)
     * @param calcType one of: "Standard", "Std", "Cumulative", or "Cum"
     * @return  a (refreshed) @link{Report}
     */
    public Report setCalcType(String calcType){
        return viewOptions.setCalcType(calcType);
    }

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


    private WebElement getViewLinkFromListGrid(WebElement area, String reportName, String levelName){

        List<WebElement> rowLabels = area.findElements(LIST_GRID_ROW_LABELS);
        //int rowIndex = area.findElements(LIST_GRID_ROWS).size() - rowLabels.size();
        int rowIndex = 2;

        for (WebElement label: rowLabels){
            if (label.getText().trim().equals(reportName)){
                rowIndex += rowLabels.indexOf(label) + 1;
                break;
            }
        }

        List<WebElement> colLabels = area.findElements(LIST_GRID_COL_LABELS);
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
        return waitMedium.until(ExpectedConditions.visibilityOfAllElements(area.findElements(AREA_LIST_GRID_LINKS)));
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

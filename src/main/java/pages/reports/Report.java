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
    Report_FilterTabs filterTabs;

    // Locators for Grade filters (On Track, Faculty, Class, Grade Type, etc)
    // are public in the Report_GradeFilters class
    Report_GradeFilters gradeFilters;
    public static final By ON_TRACK_MENU = By.cssSelector(".onTrack.active");
    public static final By FACULTY_DDL = By.cssSelector("select#ReportOptions_Faculty_ID");
    public static final By QUALIFICATION_DDL = By.cssSelector("select#ReportOptions_Qual_ID");
    public static final By CLASS_DDL = By.cssSelector("select#ReportOptions_TchGrp_ID");
    public static final By GRADE_TYPE_DDL = By.cssSelector("select#ReportOptions_EAPGradesMethod_ID");
    public static final By AWARD_CLASS_DDL = By.cssSelector("select#ReportOptions_RPTQualType_ID");
    public static final By KS2_CORE_DDL = By.cssSelector("select#ReportOptions_KS2Baseline_ID");
    public static final By GRADE_FILTER_TYPE_DDL = By.cssSelector("select#ReportOptions_Grade_RPTOperand_ID");
    public static final By GRADE_FILTER_WHOLE_DDL = By.cssSelector("select#ReportOptions_EAPWholeGrade_ID");
    public static final By GRADE_FILTER_SUB_DDL = By.cssSelector("select#ReportOptions_EAPSubGrade_Order");

    // Locators for View Options (Column Sort, Count/Percent, Standard/Cumulative, Breakdown, StuInfo, A8 Basket, Sub/Whole)
    public static final By COL_SORT_DDL = By.cssSelector("select#ReportOptions_RPTColSort_ColName");
    public static final By COL_SORT_DIRECTION_TOGGLE = By.cssSelector("label[for='RPTColSort_Desc']");
    public static final By BREAKDOWN_DDL = By.cssSelector("select#ReportOptions_Filter_ID");
    public static final By FIG_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(2)");
    public static final By CALC_TYPE_TOGGLE = By.cssSelector("#sortWrapper>div:nth-of-type(3)");


// CONSTRUCTORS
    public Report(RemoteWebDriver aDriver){
        super(aDriver);
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DDL));
        filterTabs = new Report_FilterTabs(driver);
        gradeFilters = new Report_GradeFilters(driver);
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
     * Convenience method which passes through to the filterTabs property (which is a Report_FilterTabs object)
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
     * Convenience method which passes through to the gradeFilters property (which is a Report_GradeFilters object)
     * @param trackStatus   a @link{String} identifying the type of track filtering to apply
     * @return  a (refreshed) @link{Report}
     */
    public Report filterByTrack(String trackStatus){
        return gradeFilters.filterByTrack(trackStatus);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a Report_GradeFilters object)
     * @param optionText    a @link{String} identifying the faculty to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectFaculty(String optionText){
        return gradeFilters.selectFaculty(optionText);
    }

    /**
     * Convenience method which passes through to the gradeFilters property (which is a Report_GradeFilters object)
     * @param optionText    a @link{String} identifying the qualification to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectQualification(String optionText){
        return gradeFilters.selectQualification(optionText);
    }


    /**
     * Convenience method which passes through to the gradeFilters property (which is a Report_GradeFilters object)
     * @param optionText    a @link{String} identifying the class to select
     * @return  a (refreshed) @link{Report}
     */
    public Report selectClass(String optionText){
        return gradeFilters.selectClass(optionText);
    }
    public Report selectGradeType(String optionText){
        // NB. if field is 'Locked', the option will still be selected and the form submitted, but
        //      when the page is reloaded the previous option will be the active one
        List<WebElement> gradeTypeDDLs = driver.findElements(GRADE_TYPE_DDL);
        if (gradeTypeDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Type is not available");
            }
            return this; // The Grade Type DDL is not currently available
        }
        new Select(gradeTypeDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report selectAwardClass(String optionText){
        List<WebElement> awardClassDDLs = driver.findElements(AWARD_CLASS_DDL);
        if (awardClassDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because GCSE/Non-GCSE is not available");
            }
            return this; // The GCSE/Non-GCSE DDL is not currently available
        }
        new Select(awardClassDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report selectKS2Core(String optionText){
        List<WebElement> ks2CoreDDLs = driver.findElements(KS2_CORE_DDL);
        if (ks2CoreDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because KS2 Core is not available");
            }
            return this; // The KS2 Core DDL is not currently available
        }
        new Select(ks2CoreDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report selectGradeFilterType(String optionText){

        switch (optionText.toLowerCase()){
            case "":
                return this;
            case "less than":
                optionText = "<";
                break;
            case "equal to":
                optionText = "=";
                break;
            case "greater or equal":
                optionText = ">=";
                break;
            case "greater than":
                optionText = ">";
                break;
            default:
                throw new IllegalArgumentException("Unknown Grade Filter Type '" + optionText +
                        "'. Expected 'less than', 'equal to', 'greater or equal' or 'greater than'.");
        }

        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_TYPE_DDL);
        if (targetDDLs.size() == 0) {
            throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
        }

        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report selectGradeFilterWhole(String optionText){
        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_WHOLE_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
            }
            return this; // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report selectGradeFilterSub(String optionText){
        List<WebElement> targetDDLs = driver.findElements(GRADE_FILTER_SUB_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Grade Filters are not available");
            }
            return this; // The Grade Filter DDLs are not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report selectColSort(String optionText){
        List<WebElement> targetDDLs = driver.findElements(COL_SORT_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Column Sort is not available");
            }
            return this; // The Column Sort DDL is not currently available
        }
        if (!optionText.equals("")) {
            new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
            waitForLoadingWrapper();
        }
        return this;
    }
    public Report toggleColSortDirection(){
        driver.findElement(COL_SORT_DIRECTION_TOGGLE).click();
        return this;
    }
    public Report selectBreakdown(String optionText){
        List<WebElement> targetDDLs = driver.findElements(BREAKDOWN_DDL);
        if (targetDDLs.size() == 0) {
            if (!optionText.equals("")){
                throw new IllegalStateException("Can't select '" + optionText + "' because Breakdown is not available");
            }
            return this; // The Breakdown DDL is not currently available
        }
        new Select(targetDDLs.get(0)).selectByVisibleText(optionText);
        waitForLoadingWrapper();
        return this;
    }
    public Report setFigType(String figType){
        // checks current availability and setting, clicks label if req.
        if (figType.equals("")){
            return this;
        }
        WebElement figToggle = driver.findElement(FIG_TYPE_TOGGLE);
        if (figToggle.getAttribute("class").contains("disabled")){
            throw new IllegalStateException("The Figure Type options are disabled");
        }
        String oldType = figToggle.findElement(By.tagName("div")).getText().trim();
        String newType;
        switch (figType){
            case "%":case "Percentage":
                newType = "Percentage";
                break;
            case "#":case "Count":
                newType = "Count";
                break;
            default:
                throw new IllegalArgumentException("Unexpected Figure Type '" + figType +
                        "'. Expected '#', '%', 'Count' or 'Percentage'");
        }
        if (oldType.startsWith(newType)){
            return this;
        }
        figToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return this;
    }
    public Report setCalcType(String calcType){
        // checks current availability and setting, clicks label if req.
        if (calcType.equals("")){
            return this;
        }
        WebElement calcToggle = driver.findElement(CALC_TYPE_TOGGLE);
        if (calcToggle.getAttribute("class").contains("disabled")){
            throw new IllegalStateException("The Calculation Type options are disabled");
        }
        String oldType = calcToggle.findElement(By.tagName("div")).getText().trim();
        String newType;
        switch (calcType){
            case "Std":case "Standard":
                newType = "Standard";
                break;
            case "Cum":case "Cumulative":
                newType = "Cumulative";
                break;
            default:
                throw new IllegalArgumentException("Unexpected Calculation Type '" + calcType +
                        "'. Expected 'Std', 'Cum', 'Standard' or 'Cumulative'");
        }
        if (oldType.startsWith(newType)){
            return this;
        }
        calcToggle.findElement(By.cssSelector("label[title='" + newType + "']")).click();
        waitForLoadingWrapper();
        return this;
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

package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import analytics.utils.HtmlLogger.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

public class ReportView extends BaseWithMenu {

    public static final By VIEW_LEGEND = By.cssSelector(".fieldset>.legend");

    public static final By NAVI_BUT_HEADLINES = By.id("naviBut1");
    public static final By NAVI_BUT_QUALIFICATIONS = By.id("naviBut2");
    public static final By NAVI_BUT_STUDENTS = By.id("naviBut3");
    public static final By NAVI_BUT_SUBJECTS = By.id("naviBut4");

    public static final By ACTIVE_VIEW_MENU = By.cssSelector(".colStatTabs>.act");

    public static final By VIEWS_DDL_TABLE = By.id("btnz001");
    public static final By VIEWS_DDL_CHART = By.id("btnz002");
    public static final By VIEWS_DDL_TRACKER_TABLE = By.id("btnz003");
    public static final By VIEWS_DDL_TRACKER_CHART = By.id("btnz004");
    public static final By VIEWS_DDL_TREND_TABLE = By.id("btnz005");

    public static final By VIEW_NAMES_TABLE = By.cssSelector("#buts001>ol>li");
    public static final By VIEW_NAMES_CHART = By.cssSelector("#buts002>ol>li");
    public static final By VIEW_NAMES_TRACKER_TABLE = By.cssSelector("#buts003>ol>li");
    public static final By VIEW_NAMES_TRACKER_CHART = By.cssSelector("#buts004>ol>li");
    public static final By VIEW_NAMES_TREND_TABLE = By.cssSelector("#buts005>ol>li");

    public static final String NO_TABLE_FOUND = "No Table";
    public static final String NO_CHART_FOUND = "No Chart";

    public static final By CHARTS_LOADING_MSG = By.id("chartMsg");

    public static final By VIEW_DATA = By.cssSelector(".fieldset");
    public static final By VIEW_TABLE = By.cssSelector(".fieldset table.rpt");
    public static final By REPORT_CHART_LABELS = By.cssSelector("g.hc-data-labels");
    public static final By SORTABLE_COL_HEADERS = By.cssSelector("table.rpt tr.sortable>th");
    public static final By REPORT_TABLE_ROWS = By.cssSelector(".fieldset .rpt>tbody>tr:not(.tabHead)");
    public static final By REPORT_TABLE_LINKS = By.cssSelector("table.rpt a");
    public static final By REPORT_CHART_LINKS = By.cssSelector(".hc-container a");

    public static final By FILTERS_DDL_BUTTON = By.cssSelector("#menuSide>div.r_fTitDDL");
    public static final By ADD_OR_EDIT_FILTERS_LINK = By.cssSelector("#FilterDDL>ul>li>a.psnLink");
    public static final By RESET_FILTERS_LINK = By.linkText("Reset All");
    public static final By VIEW_MENU_DDL_TABS = By.cssSelector("div.colStatTabs>div");
    public static final String VIEW_LINKS_WITHIN_MENU_CSS = ">ol>li:not(.dis)>a";
    public static final By INVISI_MODAL = By.id("invisiModal");
    public static final By NOTIFICATION_MESSAGE = By.cssSelector("div.notif.Info.Information");

    private int currentQualIndex = -1;

    public ReportView(AnalyticsDriver aDriver){
        super(aDriver);
    }
    public ReportView(SISRATest aTest){
        super(aTest);
    }

    public int switchToViewArea(String newArea) {
        return switchToViewArea(newArea, -1);
    }

    public int switchToViewArea(String newArea, int linkIndex){

        int result = 0;

        // Do nothing if the desired area is already selected
        String currentArea = getCurrentArea();
        if (currentArea.equals(newArea)) {
            test.log(Level.DEBUG, newArea + " is already active");
            return 0;
        }

        // Headlines, Qualifications, and Students is simple: Click the appropriate naviBut.
        WebElement naviBut = null;

        switch (newArea) {
            case "Headlines":
                naviBut = find(NAVI_BUT_HEADLINES);
                break;
            case "Qualifications":
                naviBut = find(NAVI_BUT_QUALIFICATIONS);
                break;
            case "Subjects":
                naviBut = find(NAVI_BUT_SUBJECTS);
                break;
            case "Students":
                naviBut = find(NAVI_BUT_STUDENTS);
                break;
        }

        if (naviBut != null) {
            test.log(Level.DEBUG, "Opening " + newArea + " using " + naviBut.toString());
            click(naviBut);
            waitForReload();
            return result;
        }


        // There are no direct links to Classes or Student Detail
        switch (newArea) {
            case "Classes":
                // Open the Qualifications area so we can drill down to a class list
                test.log(Level.DEBUG, "Opening Qualifications to drill down to Classes");
                try {
                    click(NAVI_BUT_QUALIFICATIONS);
                } catch (NoSuchElementException e) {
                    click(NAVI_BUT_SUBJECTS);
                }
                waitForReload();
                break;
            case "Student Detail":
                // Open the "Students" area so we can drill down to a student detail
                test.log(Level.DEBUG, "Opening Students to drill down to Student Detail");
                click(NAVI_BUT_STUDENTS);
                waitForReload();
                break;
        }
        currentArea = getCurrentArea();

        // Click a qualification or student name to open a class or student detail report
        List<WebElement> links = findAll(REPORT_TABLE_LINKS);
        if (links.size() == 0) {
            test.log(Level.ERROR, "***** Opening " + newArea + " - No links available *****");
			return -1;
		}

        // If called with a linkIndex of -1, we're in Live so choose a qual/student at random
        // If not, we're in Dev so use the same linkIndex as we did in Live
        result = linkIndex == -1 ? new Random().nextInt(links.size()) : linkIndex;
        test.log(Level.DEBUG, "Opening " + newArea + " via link " + result + " in " + currentArea);

        click(links.get(result));
        waitForReload();

        return result;
    }

    public String openView(String viewType, String viewName){
        try {
            String result;
            if (viewType.equals("ANY")) {
                result = this.openRandomView();
            } else {
                result = this.openNamedView(viewType, viewName);
            }
            return result;
        } catch (Exception e) {
            test.logWithScreenshot(Level.WARN, "Exception opening view '" + viewType + " > " + viewName + "'",
                    "OpenViewError");
            throw e;
        }
    }

    protected String openRandomView(){
        // Choose a view menu  (Table, Chart, Tracker Table, etc)
        Random rnd = new Random(System.currentTimeMillis());
        List<WebElement> menuTabs = findAll(VIEW_MENU_DDL_TABS);
        int tabIndex = rnd.nextInt(menuTabs.size());
        WebElement menuTab = menuTabs.get(tabIndex);

        // Store & log the chosen menu's name
        String viewType = menuTab.getText().trim();
        test.log(Level.DEBUG, "Selecting a view from the " + viewType + " menu");

        // Ensure the chosen menu is expanded
        if (!menuTab.getAttribute("class").contains("act8")){
            test.log(Level.DEBUG, "Expanding drop down menu " + viewType);
            click(menuTab);
        } else {
            test.log(Level.DEBUG, viewType + " menu is already expanded");
        }

        // Store the id of the chosen menu's options container
        String menuID = menuTab.getAttribute("id").replace("btnz", "buts");

        // Get a list of the view links within the chosen menu that are not disabled (.dis)
        By viewLinksLocator = By.cssSelector("#" + menuID + VIEW_LINKS_WITHIN_MENU_CSS);
        test.log(Level.DEBUG, "Choosing from non-disabled view links identified by "
                + viewLinksLocator.toString());
        List<WebElement> viewLinks = findAll(viewLinksLocator);

        // Choose one of the available links
        int linkIndex = rnd.nextInt(viewLinks.size());
        WebElement viewLink = viewLinks.get(linkIndex);

        // Store & log the chosen view's name
        viewLink = waitForClickabilityOf(viewLink);
        String viewName = viewLink.getText().trim();
        test.log(Level.DEBUG, "Opening view '" + viewName + "'");

        // If the chosen view is already selected
        if (viewLink.getAttribute("class").contains("act")) {
            // ...click outside the menu so it collapses again
            // the Base method clickAt utilises the Actions library
            // which is not yet fully implemented in the geckodriver.
            // A page refresh should achieve the same effect with a performance cost (reload time)
            //clickAt(INVISI_MODAL, 50, 50);
            driver.get(driver.getCurrentUrl());
        }
        else {
            // Otherwise, click the view link and wait for the page to reload
            click(viewLink);
            waitForReload();
        }

        if (isDisplayed(INVISI_MODAL)) {
            // the Base method clickAt utilises the Actions library
            // which is not yet fully implemented in the geckodriver.
            // A page refresh should achieve the same effect with a performance cost (reload time)
            //clickAt(INVISI_MODAL, 50, 50);
            driver.get(driver.getCurrentUrl());
        }

        // Check if the chosen view requires a qualification to be selected
        this.selectQualifictionIfNeeded();

        return viewType + ">" + viewName;
    }

    protected String openNamedView(String viewType, String viewName){

        By viewMenuLocator = null;
        By viewLinksLocator = null;

        // Set locators for the menu and for the view options within that menu
        switch (viewType) {
            case "Table":
                viewMenuLocator = VIEWS_DDL_TABLE;
                viewLinksLocator = VIEW_NAMES_TABLE;
                break;
            case "Chart":
                viewMenuLocator = VIEWS_DDL_CHART;
                viewLinksLocator = VIEW_NAMES_CHART;
                break;
            case "Tracker Table":
                viewMenuLocator = VIEWS_DDL_TRACKER_TABLE;
                viewLinksLocator = VIEW_NAMES_TRACKER_TABLE;
                break;
            case "Tracker Chart":
                viewMenuLocator = VIEWS_DDL_TRACKER_CHART;
                viewLinksLocator = VIEW_NAMES_TRACKER_CHART;
                break;
            case "Trend Table":
                viewMenuLocator = VIEWS_DDL_TREND_TABLE;
                viewLinksLocator = VIEW_NAMES_TREND_TABLE;
                break;
        }

        // Expand the Drop Down Menu to show the list of views
        WebElement viewTypeTab = find(viewMenuLocator);
        waitForClickabilityOf(viewTypeTab);
        test.log(Level.DEBUG, "Expanding drop down menu " + viewTypeTab.getText());
        click(viewTypeTab);

        // Wait for the link to the view we are looking for to be visible
        WebElement viewListItem = waitForViewLinkDisplayed(viewLinksLocator, viewName, DEFAULT_SHORT_TIMEOUT);

        // If the view is already selected
        String listItemClass = viewListItem.getAttribute("class");
        if (listItemClass.contains("act")) {
            // ...click outside the menu so it collapses again
            // the Base method clickAt utilises the Actions library
            // which is not yet fully implemented in the geckodriver.
            // A page refresh should achieve the same effect with a performance cost (reload time)
            //clickAt(INVISI_MODAL, 100, 100);
            driver.get(driver.getCurrentUrl());
        } else {
            // Otherwise, click the view link and wait for the page to reload
            test.log(Level.DEBUG, "Opening view: " + viewListItem.getText());
            click(viewListItem);
            waitForReload();
        }

        // Check if the chosen view requires a qualification to be selected
        this.selectQualifictionIfNeeded();

        return viewType+">"+viewName;
    }

    public String getCurrentArea(){
        String subHeading = find(PAGE_SUB_TITLE).getText();
        int firstSpace = subHeading.trim().indexOf(' ');
        return subHeading.substring(firstSpace+1);
    }

    public String getCurrentViewType(){
        if (getCurrentArea().equals("Student Detail"))
            return "Table";

        return find(ACTIVE_VIEW_MENU).getText();
    }

    public WebElement waitForViewLinkDisplayed(By viewNamesLocator, String viewName, int timeout){
        long deadline = System.currentTimeMillis()+(1000 * timeout);
        List<WebElement> viewLinks;
        while (System.currentTimeMillis() < deadline){
            viewLinks = findAll(viewNamesLocator);
            for (WebElement element: viewLinks) {
                String linkText = element.getText().trim();
                if (viewName.equals("_DEFAULT_") && linkText.length() > 0)
                    return element;

                if (linkText.equals(viewName))
                    return element;

            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
                e.printStackTrace();
                return null;
            }
        }

        // If we're here, the view name was not found!
        viewLinks = findAll(viewNamesLocator);
        test.log(Level.WARN, "Failed to find '" + viewName +"' using '" + viewNamesLocator.toString() + "' in...");
        for (WebElement element: viewLinks) {
            test.log(Level.WARN, " - '" + element.getText().trim() + "'");
            if (element.getText().trim().equals(viewName)){
                test.log(Level.WARN, "...Oops - there it is!");
                return element;
            }
        }

        return null;
    }

    public boolean tableIsSortable(){
        try {
            find(SORTABLE_COL_HEADERS);
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public String getViewName(){
        String stuName = "";
        if (getCurrentArea().equals("Student Detail")){
            stuName = " (" + find(By.className("StuName")).getText() + ")";
        }
        return find(VIEW_LEGEND).getText() + stuName;
    }

    public String getViewDataRowByRow(String area, String viewType){
        // Code to build a string representing the data displayed by the view
        // For Headline, Qualification, and Student Tables, simply using
        // getText() on the table element should work.
        if ("|Headlines|Qualifications|Classes|Students|".contains("|"+area+"|")
                && viewType.endsWith("Table")){
            List<WebElement> rptTableRows = findAll(REPORT_TABLE_ROWS);
            if (rptTableRows.size() == 0)
                return "[NO TABLE]";

            StringBuilder sb = new StringBuilder();
            for (WebElement row: rptTableRows)
                sb.append(row.getText());
            return sb.toString();
        }

        if (viewType.contains("Charts")){
            waitForInvisibilityOf(CHARTS_LOADING_MSG,DEFAULT_LONG_TIMEOUT);
            List<WebElement> chartLabels = findAll(REPORT_CHART_LABELS);
            if (chartLabels.size() == 0)
                return "[NO CHARTS]";

            StringBuilder sb = new StringBuilder();
            for (WebElement label : chartLabels)
                sb.append(label.getText());
            return sb.toString();
        }

        if (area.equals("Student Detail")){
            List<WebElement> rptTableRows = findAll(REPORT_TABLE_ROWS);
            if (rptTableRows.size() == 0)
                return "[NO TABLE DATA]";

            StringBuilder sb = new StringBuilder();
            for (WebElement row: rptTableRows)
                sb.append(row.getText());
            return sb.toString();
        }

        return "HUH!?";
    }

    public String getViewData(){

        String viewType = getCurrentViewType();

        if (this.getCurrentArea().equals("Student Detail")){
            List<WebElement> rptTables = findAll(VIEW_TABLE);
            if (rptTables.size() == 0)
                return NO_TABLE_FOUND;

            StringBuilder sb = new StringBuilder();
            for (WebElement table: rptTables)
                sb.append(table.getText());
            return sb.toString();
        }

        if (viewType.contains("Chart")){
            waitForInvisibilityOf(CHARTS_LOADING_MSG,DEFAULT_LONG_TIMEOUT);
            List<WebElement> chartLabels = findAll(REPORT_CHART_LABELS);
            if (chartLabels.size() == 0)
                return NO_CHART_FOUND;

            String js = "var chartText = '';";
            js += "var chartTextElements = document.querySelectorAll('.hc-data-labels text, #hc-0 text');";
            js +="for (var i=0; i<chartTextElements.length; i++){";
            js +="  chartText += chartTextElements[i].textContent + '<br>';";
            js +="}";
            js +="var newDiv = document.createElement('DIV');";
            js +="newDiv.setAttribute ('id', 'se-chart-data');";
            js +="newDiv.innerHTML = chartText;";
            js +="document.querySelector('.fieldset').appendChild(newDiv);";

            driver.executeScript(js);
            return find(By.id("se-chart-data")).getText();
            //return find(VIEW_DATA).getText();
        }

        try {

            String js = "var tableText = '';";
            js += "var tableRowElements = document.querySelectorAll('.fieldset table.rpt tr');";
            js +="for (var i=0; i < tableRowElements.length; i++){";

            js +="  var rowCellElements = tableRowElements[i].querySelectorAll('td');";
            js +="  if (rowCellElements.length > 0) {";
            js +="    var rowText = rowCellElements[0].textContent.trim();";
            js +="    for (var j=1; j < rowCellElements.length; j++){";
            js +="      rowText += ',' + rowCellElements[j].textContent.trim();";
            js +="    }";
            js +="    tableText += rowText + ' <br>';";
            js +="  }";

            js +="}";

            js +="var newDiv = document.createElement('DIV');";
            js +="newDiv.setAttribute ('id', 'se-table-data');";
            js +="newDiv.innerHTML = tableText;";
            js +="document.querySelector('.fieldset').appendChild(newDiv);";

            driver.executeScript(js);
            return find(By.id("se-table-data")).getText();


/*
            WebElement rptTable = find(VIEW_TABLE);
            return rptTable.getText();
*/
        } catch (NoSuchElementException e){
            return NO_TABLE_FOUND;
        }
    }

    public void selectQualifictionIfNeeded(){
        try {
            String notifMsg = find(NOTIFICATION_MESSAGE).getText().trim();
            if (notifMsg.equals("Please select a qualification from the dropdown list above")){
                test.log(Level.DEBUG, getViewName() + " requires selection of a Qualification");
                Select qualSelect = new Select(find(ReportViewOptions.QUAL_SELECT));
                int index;
                if (currentQualIndex == -1){
                    index = new Random().nextInt(qualSelect.getOptions().size());
                    index = (index == 0) ? 1 : index;
                    currentQualIndex = index;
                } else {
                    index = currentQualIndex;
                    currentQualIndex = -1;
                }
                qualSelect.selectByIndex(index);
                waitForReload();
                test.log(Level.INFO, "Selected a qualification so view would not be blank");
            }
        } catch (NoSuchElementException e) {
            //test.log(Level.DEBUG, getCurrentArea() + ">" + getViewName());
        }
    }

    public void clearQualificationIfPresent(){
        if (isDisplayed(ReportViewOptions.QUAL_SELECT)){
            new Select(find(ReportViewOptions.QUAL_SELECT)).selectByIndex(0);
            waitForReload();
        }
    }

}

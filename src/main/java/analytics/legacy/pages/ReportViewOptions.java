package analytics.legacy.pages;


import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import analytics.legacy.pages.helpers.ViewUpdate;
import analytics.utils.HtmlLogger.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class ReportViewOptions extends ReportView {

	public static final By VIEW_OPTIONS_FORM = By.id("fm_RptOptions");

    public static final By UNCHECK_ALL_FILTERS = By.id("checkClear");
    public static final By SELECTED_CHILD_OPTION = By.cssSelector("[selected='selected']");

    public static final String FILTER_GROUP_BY_CSS_STUB = "#fm_RptFlt>.fkfieldset>ul";
    public static final By FILTER_GROUPS = By.cssSelector(FILTER_GROUP_BY_CSS_STUB);
    public static final By FILTER_GROUP_TITLE = By.className("title");
    public static final By FILTER_GROUP_VALUES = By.className("checkWrap");
    public static final By ADD_OR_EDIT_FILTERS_FORM = By.id("fm_RptFlt");
    /**************************************************************************
     * Check box fields and their labels. The labels 'intercept' mouse events
     * for the fields so we have to target the label for a click. But there is
     * no change to the label markup if the field is checked, so use the  _CB
     * locator with .getAttribute("checked").equals("checked") to...well, to check.
     *************************************************************************/
    public final By SHOW_CORE_CB = By.id("co_Tog");
    public final By SHOW_CORE_LBL =
            By.cssSelector("label.ui_lab[for='co_Tog']:not(.grey)");
    public final By USE_KS2_SUB_LEVELS_CB = By.id("pr_Tog");
    public final By USE_KS2_SUB_LEVELS_LBL =
            By.cssSelector("label.ui_lab[for='pr_Tog']:not(.grey)");
    public final By INC_NATIONAL_DATA_CB = By.id("na_Tog");
    public final By INC_NATIONAL_DATA_LBL =
            By.cssSelector("label.ui_lab[for='na_Tog']:not(.grey)");
    public final By SEPARATE_COMP_WITH_COLS_CB = By.id("ta_Tog");
    public final By SEPARATE_COMP_WITH_COLS_LBL =
            By.cssSelector("label.ui_lab[for='ta_Tog']:not(.grey)");

    /**************************************************************************
     * Select fields and whether they have a 'null' or 'All' state they can be
     * reset to. If the SELECT locator is listed in the 'Resettable' array the field
     * will be changed back to its null/All state by the 'resetViewOptions' method.
     *************************************************************************/
    public final By COMP_WITH_SELECT = By.id("CompPublishedReport_ID");
    public final By FILTER_BREAKDOWN_SELECT = By.id("ReportOptions_FilterBreak_ID");
    public final By FACULTY_SELECT = By.id("ReportOptions_Faculty_ID");
    public static final By QUAL_SELECT = By.id("ReportOptions_Qual_ID");
    public final By CLASS_SELECT = By.id("ReportOptions_TchGrp_ID");
    public final By STUDENT_SELECT = By.id("stu_goto");
    public final By GRADE_RANGE_SELECT = By.id("ReportOptions_RPTGradeRange_ID");
    public final By WHOLE_GRADE_SELECT = By.id("ReportOptions_RCGradeComboWhole_ID");
    public final By SUB_GRADE_SELECT = By.id("ReportOptions_RCGradeComboSub_ID");
    public final By KS2_LEVEL_SELECT = By.id("ReportOptions_RCProgGrade_ID");
    public final By KS2_LOP_SELECT = By.id("ReportOptions_LOPCount");

    public final By GRADE_FOCUS_SELECT = By.id("ReportOptions_RCGradeRange_ID");
    public final By EXPECTED_LOP_SELECT = By.id("ReportOptions_RCGradeRangeProg_ID");

    private ArrayList<By> allCheckBoxes;
    private ArrayList<By> allCBLabels;

    public ReportViewOptions(AnalyticsDriver aDriver){
        super(aDriver);
        init();
    }
    public ReportViewOptions(SISRATest aTest){
        super(aTest);
        init();
    }

    protected void init(){
        allCheckBoxes = new ArrayList<>(4);
        allCheckBoxes.add(SHOW_CORE_CB);
        allCheckBoxes.add(USE_KS2_SUB_LEVELS_CB);
        allCheckBoxes.add(INC_NATIONAL_DATA_CB);
        allCheckBoxes.add(SEPARATE_COMP_WITH_COLS_CB);

        allCBLabels = new ArrayList<>(4);
        allCBLabels.add(SHOW_CORE_LBL);
        allCBLabels.add(USE_KS2_SUB_LEVELS_LBL);
        allCBLabels.add(INC_NATIONAL_DATA_LBL);
        allCBLabels.add(SEPARATE_COMP_WITH_COLS_LBL);
    }

    public String getRandomUpdateType(){

        String currentArea = this.getCurrentArea();
        boolean sortable = this.tableIsSortable();

        test.log(Level.DEBUG, "Getting random Update Type for '" + currentArea + "' (sortable = " + sortable +")");
        ArrayList<String> options = new ArrayList<>();
        options.add("View Option");     // View Options should be available in every view

        if ((findAll(REPORT_TABLE_LINKS).size() + findAll(REPORT_CHART_LINKS).size()) > 0)
            options.add("Drill Down");      // If there are links we can drill down

        if (!currentArea.equals("Student Detail"))
            options.add("Add Filter");      // If we are not in a Student Detail report we can add filters

        if (sortable)
            options.add("Column Sort");


        int optIndex = new Random().nextInt(options.size());
        return options.get(optIndex);
    }

    public ViewUpdate updateView(String type){
        ViewUpdate update = new ViewUpdate();
        update.updateType = type;
        switch (type){
            case "Drill Down":
                update.oldValue1 = driver.getCurrentUrl();
                int linkIndex = this.drillDown();
                update.newValue1 = Integer.toString(linkIndex);
                break;
            case "View Option":
                update = this.changeRandomViewOption();
                break;
            case "Column Sort":
                int colIndex = this.sortByRandomColumn();
                update.newValue1 = Integer.toString(colIndex);
                break;
            case "Add Filter":
                update = this.clickRandomFilter();
                break;
            default:
                throw new IllegalArgumentException("'type' (" + type + ") must be one of 'Drill Down', 'View Option',"
                        + "'Column Sort', or 'Add Filter'");
        }

        return update;
    }

    public void updateView(ViewUpdate update){
        removeOverlay();
        switch (update.updateType){
            case "Drill Down":
                int linkIndex = Integer.parseInt(update.newValue1);
                update.oldValue2 = driver.getCurrentUrl();
                this.matchDrillDown(linkIndex);
                break;
            case "View Option":
                this.matchSelectedViewOption(update.firstLocator, update.newValue1);
                break;
            case "Column Sort":
                int colIndex = Integer.parseInt(update.newValue1);
                this.matchSortByColumnHeader(colIndex);
                break;
            case "Add Filter":
                this.matchClickFilter(update);
        }
    }

    public void rollbackUpdates(ArrayList<ViewUpdate> history, boolean deleteHistory){
        test.logWithScreenshot(Level.DEBUG, "View before rollbacks", "BeforeRollbacks");
        ListIterator<ViewUpdate> updates = history.listIterator(history.size());
        while (updates.hasPrevious()){
            ViewUpdate update = updates.previous();

            if (update.updateType.equals("Column Sort"))
                test.log(Level.INFO, "Skipping rollback of Column Sort update");
            else {
                test.log(Level.INFO, "Rolling back: " + update.toString());
                switch (update.updateType){
                    case "Drill Down":
                        if (test.currWinName.equals("LIVE"))
                            visit(update.oldValue1+"&rst=true");
                        else
                            visit(update.oldValue2+"&rst=true");

                        waitForReload();
                        break;
                    case "View Option":
                        matchSelectedViewOption(update.firstLocator, update.oldValue1);
                        break;
                    case "Add Filter":
                        matchClickFilter(update);
                        break;
                    default:
                        // throw exception?
                }
            }

            if (deleteHistory)
                updates.remove();
        }
        test.logWithScreenshot(Level.DEBUG, "View after rollbacks", "AfterRollbacks");
    }

    private int drillDown(){
        List<WebElement> links = findAll(REPORT_TABLE_LINKS);
        if (links.size() == 0)
            links = findAll(REPORT_CHART_LINKS);

        int linkIndex = new Random(System.currentTimeMillis()).nextInt(links.size());
        test.logWithScreenshot(Level.DEBUG, "Drilling Down using link " + linkIndex, "Drilldown");
        click(links.get(linkIndex));
        waitForReload();
        return linkIndex;
    }

    private void matchDrillDown(int linkIndex){
        test.logWithScreenshot(Level.DEBUG, "Drilling Down using link " + linkIndex, "MatchDrilldown");
        List<WebElement> links = findAll(REPORT_TABLE_LINKS);
        if (links.size() == 0)
            links = findAll(REPORT_CHART_LINKS);

        click(links.get(linkIndex));
        waitForReload();
    }

    private ViewUpdate changeRandomViewOption(){
        ViewUpdate update = new ViewUpdate();
        update.updateType = "View Option";
        // First discover which fields are available to be changed

        // Build a list of id locators for the currently editable select elements
        ArrayList<By> displayedSelects = new ArrayList<>();
        List<WebElement> visibleSelects = findAll(By.cssSelector("#fm_RptOptions select:not(.grey)"));
        for (WebElement select: visibleSelects){
            By locator = By.id(select.getAttribute("id"));
            displayedSelects.add(locator);
        }

        // Build a list of locators for the currently editable check boxes and their labels
        ArrayList<By> displayedCBLabels = new ArrayList<>(4);
        ArrayList<By> displayedCheckBoxes = new ArrayList<>(4);
        allCBLabels.stream().filter((locator) -> isDisplayed(locator)).forEach(labelBy -> {
            displayedCBLabels.add(labelBy);
            displayedCheckBoxes.add(allCheckBoxes.get(allCBLabels.indexOf(labelBy)));
        });

        // Choose a field to change...
        Random random = new Random();
        int selectsCount = displayedSelects.size();
        int labelsCount = displayedCBLabels.size();
        int fieldIndex = random.nextInt(selectsCount+labelsCount);

        // ...and finally update the chosen field
        if (fieldIndex >= displayedSelects.size()){
            // A Check box has been chosen, store the current details in the update
            fieldIndex = fieldIndex - selectsCount;
            test.logWithScreenshot(Level.DEBUG, "Toggling Checkbox using locator: " + displayedCBLabels.get(fieldIndex), "Check Box");
            update.firstLocator = displayedCBLabels.get(fieldIndex);
            update.secondLocator = displayedCheckBoxes.get(fieldIndex);
            update.oldValue1 = find(displayedCheckBoxes.get(fieldIndex)).getAttribute("checked");

            // Click the label of the chosen check box
            click(displayedCBLabels.get(fieldIndex));
            update.newValue1 = find(displayedCheckBoxes.get(fieldIndex)).getAttribute("checked");

        } else {
            // A Select field has been chosen, find it and the available options within it
            WebElement field = find(displayedSelects.get(fieldIndex));
            Select select = new Select(field);
            List<WebElement> options = select.getOptions();

            // Ensure we have a field with options to choose from
            int infiniteLoopCheck = 0;
            while (options.size() == 1) {
                // No alternate options to choose from, choose another field...
                fieldIndex = random.nextInt(selectsCount);
                field = find(displayedSelects.get(fieldIndex));
                select = new Select(field);
                options = select.getOptions();
                if (infiniteLoopCheck++ > 25) {
                    test.log(Level.DEBUG, "******** Unable to find a DDL with options!");
                    test.flushLog();
                    return null;
                }
            }

            // Store the field's locator and current value
            List<WebElement> selectedOptions;
            update.firstLocator = displayedSelects.get(fieldIndex);
            selectedOptions = select.getAllSelectedOptions();
            update.oldValue1 = "";
            if (selectedOptions.size() > 0)
                update.oldValue1 = selectedOptions.get(0).getText();

            // Choose one of the other values
            int optionIndex = random.nextInt(options.size());
            update.newValue1 = options.get(optionIndex).getText();

            // Ensure we are not going to select an invalid option
            while (update.newValue1.startsWith("!! - ") && update.newValue1.endsWith(" - !!")){
                optionIndex = random.nextInt(options.size());
                update.newValue1 = options.get(optionIndex).getText();
            }

            test.logWithScreenshot(Level.DEBUG, "Selecting view option "+optionIndex
                    +" ("+update.newValue1 +") in field located by: "
                    + displayedSelects.get(fieldIndex),
                    "Select Option");
            select.selectByIndex(optionIndex);
        }

        // Wait for the page to reload and return the update
        waitForReload();
        return update;
    }

    private void matchSelectedViewOption(By locator, String newVal){
		waitForClickabilityOf(VIEW_OPTIONS_FORM, 15);
        switch(find(locator).getTagName().toLowerCase()){
            case "select":
                test.logWithScreenshot(Level.DEBUG, "Matching selection of view option '"
                        + newVal + "' in " + locator, "matchSelectedViewOption");
                try {new Select(find(locator)).selectByVisibleText(newVal);}

                catch (NoSuchElementException e) {
                    // Probably a select with "All" as the default rather than ""
                    if (newVal.equals(""))
                        new Select(find(locator)).selectByVisibleText("All");
                    else {
                        test.logWithScreenshot(Level.ERROR, "***** Error selecting view option in DDL *****", "SelectError");
                        throw e;
                    }
                }

                catch (UnexpectedTagNameException e){
                    test.log(Level.ERROR, "Expected locator '" + locator + "' to identify a &lt;select&gt; element");
                    test.logWithScreenshot(Level.ERROR, "Instead found &lt;" + find(locator).getTagName() + "&gt;",
                            "UnexpectedTagNameException");
                    throw e;}
                break;

            case "label":
                test.logWithScreenshot(Level.DEBUG, "Toggling Checkbox using locator: " + locator, "matchSelectedViewOption");
                click(locator);
        }
        waitForReload();
    }

    private int sortByRandomColumn(){
        List<WebElement> columnHeaders = findAll(SORTABLE_COL_HEADERS);
        Random random = new Random(System.currentTimeMillis());
        int colIndex = random.nextInt(columnHeaders.size());
        test.logWithScreenshot(Level.DEBUG, "Re-sorting view by column: " + colIndex, "Sort By Random Column");
        click(columnHeaders.get(colIndex));
        waitForReload();
        return colIndex;
    }

    private void matchSortByColumnHeader(int colIndex){
        removeOverlay();
        test.logWithScreenshot(Level.DEBUG, "Re-sorting view by column: " + colIndex, "Sort By Column Header");
        click(findAll(SORTABLE_COL_HEADERS).get(colIndex));
        waitForReload();
    }

    private ViewUpdate clickRandomFilter(){
        ViewUpdate update = new ViewUpdate();
        update.updateType = "Add Filter";

        // Open the Filters Modal...
        this.openFiltersModal();

        // Count the filter groups and pick one at random
        Random random = new Random(System.currentTimeMillis());
        List<WebElement> filterGroups = findAll(FILTER_GROUPS);
        int grpIndex = random.nextInt(filterGroups.size());
        WebElement filterGroup = filterGroups.get(grpIndex);
        update.newValue1 = find(filterGroup, FILTER_GROUP_TITLE).getText().trim();

        int grpCssIndex = grpIndex+1;
        String grpCssSuffix = ":nth-of-type(" + (grpCssIndex) + ")";
        update.firstLocator = By.cssSelector(FILTER_GROUP_BY_CSS_STUB + grpCssSuffix);

        // Count the values within the group and pick one at random
        List<WebElement> filterValues = findAll(filterGroup, FILTER_GROUP_VALUES);
        int valIndex = random.nextInt(filterValues.size());
        WebElement filterValue = filterValues.get(valIndex);
        update.newValue2 = filterValue.getText().trim();

        int valCssIndex = valIndex + 2;
        String valCssSuffix = ">li:nth-of-type(" + valCssIndex + ")";
        update.secondLocator = By.cssSelector(FILTER_GROUP_BY_CSS_STUB + grpCssSuffix + valCssSuffix);

        test.logWithScreenshot(Level.DEBUG, "Clicking Filter: " + update.newValue1 + " > " + update.newValue2, "Click Random Filter");

        WebElement filterValueCB = find(update.secondLocator);

        try {
            click(filterValueCB);
        } catch (WebDriverException e){
            test.logWithScreenshot(Level.DEBUG, "Screenshot before scroll", "BeforeScroll");
            int scrollSize = filterValueCB.getLocation().getY() - 100;
            driver.executeScript("window.scrollTo(0, " + scrollSize + ");");
            test.logWithScreenshot(Level.DEBUG, "Screenshot after scroll", "AfterScroll");
            try {
                click(filterValueCB);
            }catch(WebDriverException wdex){
                wdex.printStackTrace();
            }
        }

        submit(ADD_OR_EDIT_FILTERS_FORM);
        waitForReload();

        return update;
    }

    private void matchClickFilter(ViewUpdate update){
        // Open the Filters Modal...
        this.openFiltersModal();

        test.logWithScreenshot(Level.DEBUG, "Clicking Filter: " + update.newValue1 + " > " + update.newValue2, "Match Clicked Filter");
        WebElement filterValueCB = find(update.secondLocator);

        try {
            click(filterValueCB);
        }
        catch (Exception e){
            test.logWithScreenshot(Level.DEBUG, "Screenshot before scroll", "BeforeScroll");
            int scrollTo = filterValueCB.getLocation().getY() - 100;
            driver.executeScript("window.scrollTo(0, " + scrollTo + ");");
            test.logWithScreenshot(Level.DEBUG, "Screenshot after scroll", "AfterScroll");
            try {
                click(filterValueCB);
            } catch (WebDriverException wdex){
                wdex.printStackTrace();
            }

        }
        submit(ADD_OR_EDIT_FILTERS_FORM);
        waitForReload();
    }

    protected void resetFilters(){
        waitForReload();
        waitForModalFilp();

        try {
            if (!isDisplayed(ADD_OR_EDIT_FILTERS_LINK)) {
                click(FILTERS_DDL_BUTTON);
                waitForVisibilityOf(ADD_OR_EDIT_FILTERS_LINK);
            }
        }
        catch (WebDriverException e){
            test.log(Level.WARN, "*** WebDriverException finding Add Filters Link ***");
            test.log(Level.WARN, e.getMessage());
            test.log(Level.WARN, "*** Sleeping before trying again ***");

            try {Thread.sleep(1000);}
            catch (InterruptedException intE){}

            if (!isDisplayed(ADD_OR_EDIT_FILTERS_LINK)) {
                click(FILTERS_DDL_BUTTON);
                waitForVisibilityOf(ADD_OR_EDIT_FILTERS_LINK);
            }
        }

        try {
            click(RESET_FILTERS_LINK);
        }
        catch (NoSuchElementException e){
            test.log(Level.WARN, "*** NoSuchElementException clicking Reset Filters Link ***");
            // If the Reset All link is not present there ar eno Filters applied...
            // Which is what we want :)
        }

        waitForReload();
    }

    private void openFiltersModal(){
        // Open the Filters Modal...
        try {
            if (!isDisplayed(ADD_OR_EDIT_FILTERS_LINK)) {
                click(FILTERS_DDL_BUTTON);
                waitForVisibilityOf(ADD_OR_EDIT_FILTERS_LINK);
            }
        }
        catch (WebDriverException e){
            test.log(Level.WARN, "*** WebDriverException finding Add Filters Link ***");
            test.log(Level.WARN, e.getMessage());
            test.log(Level.WARN, "*** Sleeping before trying again ***");

            try {Thread.sleep(1000);}
            catch (InterruptedException intE){}

            if (!isDisplayed(ADD_OR_EDIT_FILTERS_LINK)) {
                click(FILTERS_DDL_BUTTON);
                waitForVisibilityOf(ADD_OR_EDIT_FILTERS_LINK);
            }
            test.log(Level.WARN, "*** Just a timing issue then?? ***");
        }

        click(ADD_OR_EDIT_FILTERS_LINK);
        waitForVisibilityOf(UNCHECK_ALL_FILTERS);
        waitForReload();
    }
}
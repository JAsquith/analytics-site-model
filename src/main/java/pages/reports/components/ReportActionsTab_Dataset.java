package pages.reports.components;

import enums.ReportAction;
import enums.ReportViewType;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import pages.reports.EAPListView;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActionsTab_Dataset extends ReportActionsTab implements IReportActionGroup {

    private static final String TAB_NAME = "dataset";
    private static final String TAB_CLASS = "datasets";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");

    private static final By VIEW_TRACK_PROJECT = By.cssSelector(".vtp");
    private static final By VTP_ALL_AVAIL = By.cssSelector(".avail");

    private static final By FOCUS_DROPDOWN = By.cssSelector(".tabcontent>span.datasetList");
    private static final By FOCUS_OPTIONS_DIV = By.cssSelector(".list.ds");

    private static final By COMPARE_TITLE = By.cssSelector(".dsListTitle.comp");
    private static final By COMPARE_DROPDOWN = By.cssSelector(".tabcontent>div span.datasetList");
    private static final By COMPARE_OPTIONS_DIV = By.cssSelector(".list.cds");

    private static final By DS_LIST_OPTIONS = By.tagName("li");
    private static final By DS_LIST_ITEM_DOTS_SPAN = By.cssSelector(".dsDots");

    private static final By TRACKER_COL_DDL = By.id("TrackerColDDL");
    private static final By VTP_OPTION_VIEW = By.cssSelector(".canView");
    private static final By VTP_OPTION_TRACK = By.cssSelector(".IsTracker");
    private static final By VTP_OPTION_PROJECT = By.cssSelector(".HasSBP");
    private static final By VTP_OPTION_ANY = By.cssSelector(".canView,.HasSBP,.IsTracker");

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Dataset(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    /* Querying the state of the page */
    public boolean compareRequired(){
        WebElement compareDDL = driver.findElement(COMPARE_TITLE);
        if (compareDDL.getAttribute("class").contains("error")){
            System.out.println("Compare Required");
            return true;
        }
        return false;
    }


    /* Actions available within this component
    * ToDo: Javadoc */
    public EAPView selectFocusCollection(String optionText) {
        return selectFocusCollection(optionText, "", false);
    }

    public EAPView selectFocusCollection(String optionText, String showAs, boolean slideOutVTP){

        By vtpOptionLocator;
        vtpOptionLocator = getDDLVTPLocatorByLabel(showAs);

        // Switch to the Dataset tab & show the Focus pseudo DDL Options
        selectAndExpandTab();
        expandFocusPsuedoSelect();

        for(WebElement option : getFocusPsuedoOptions()){
            if (option.getText().equals(optionText)){
                if (slideOutVTP){
                    option.click();
                    // todo: wait for the slide out to complete
                }
                try {
                    option.findElement(vtpOptionLocator).click();
                } catch (NoSuchElementException e){
                    String eMsg = String.format("'%s' is not available for collection '%s' in the current state", showAs, optionText);
                    throw new IllegalStateException(eMsg);
                }
                waitForLoadingWrapper();
                return new EAPView(driver);
            }
        }

        throw new IllegalArgumentException("Could not find '" + optionText + "' in the Focus dataset options");

    }

    public EAPView selectCompareCollection(String optionText){
        // Switch to the Dataset tab & expand it if required
        selectAndExpandTab();

        expandComparePsuedoSelect();

        boolean found = false;
        for(WebElement option : getComparePsuedoOptions()){
            String currentText = option.getText();
            if (currentText.equals(optionText) || currentText.equals(optionText+" (Assessments)")){
                option.click();
                found = true;
                break;
            }
        }

        if(!found){
            throw new IllegalArgumentException("Could not find an options for dataset '"+ optionText +"'");
        }

        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView showFocusDataAs(String reportType){
        // Switch to the Dataset tab & expand it if required
        selectAndExpandTab();

        WebElement vtp = getViewTrackProject();
        WebElement button = getVTPButton(vtp, reportType);
        if(button==null){
            if(reportType.equals("View") || reportType.equals("Tracker") || reportType.equals("Projection")){
                throw new IllegalStateException(reportType + " is not currently available for the Focus Data");
            } else {
                throw new IllegalArgumentException(reportType + " is not a valid option for the Focus Data");
            }
        }
        button.click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView showCompareDataAs(String reportType){
        // Switch to the Dataset tab & expand it if required
        selectAndExpandTab();

        WebElement vtp = getViewTrackProject(true);
        WebElement button = getVTPButton(vtp, reportType);
        if(button==null){
            if(reportType.equals("View") || reportType.equals("Projection")){
                throw new IllegalStateException(reportType + " is not currently available for the Compare Data");
            } else {
                throw new IllegalArgumentException(reportType + " is not a valid option for the Compare Data");
            }
        }
        button.click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPView selectTrackerColumn(String optionText){
        WebElement trackerElement = driver.findElement(TRACKER_COL_DDL);
        try {
            Select trackerDDL = (Select) trackerElement;
            trackerDDL.selectByVisibleText(optionText);
        } catch (Exception e){
            System.out.println("Exception while attempting to set tracker column!");
            if (trackerElement!=null){
                System.out.println("trackerElement class = '"+trackerElement.getClass().getName()+"'");
            }
            e.printStackTrace();
        }
        return new EAPView(driver);
    }

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */

    @Override
    public List<ReportAction> getValidActionsList() {
        selectAndExpandTab();

        List<ReportAction> actions = new ArrayList();

        // Focus Dataset options
        actions.add(ReportAction.CHANGE_FOCUS);
        WebElement vtp = getViewTrackProject();
        if (vtp.findElements(VTP_ALL_AVAIL).size()>0){
            actions.add(ReportAction.FOCUS_VTP);
        }

        // Tracker options
        List<WebElement> trackerColDDLs = driver.findElements(TRACKER_COL_DDL);
        if(trackerColDDLs.size()>0){
            if(trackerColDDLs.get(0).isDisplayed()){
                actions.add(ReportAction.TRACKER_COLUMN);
            }
        }

        // Compare dataset options
        if (driver.findElements(COMPARE_DROPDOWN).size()>0){
            actions.add(ReportAction.CHANGE_COMPARE);
            vtp = getViewTrackProject(true);
            if (vtp.findElements(VTP_ALL_AVAIL).size()>0){
                actions.add(ReportAction.COMPARE_VTP);
            }
        }

        // Reset option
        if (getTabButton().findElements(RESET_ICON).size()>0){
            actions.add(ReportAction.RESET_DATASETS);
        }

        return actions;
    }
    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        switch(action){
            case CHANGE_FOCUS:
                return getFocusCollectionNames();
            case CHANGE_COMPARE:
                return getCompareCollectionNames();
            case FOCUS_VTP:
                return getVTPOptions(false);
            case COMPARE_VTP:
                return getVTPOptions(true);
            case TRACKER_COLUMN:
                return getTrackerColumns();
            case RESET_DATASETS:
                List<String> options = new ArrayList<String>();
                options.add("Reset");
                return options;
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Datasets tab");
        }
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        switch(action){
            case CHANGE_FOCUS:
                return selectFocusCollection(option);
            case CHANGE_COMPARE:
                return selectCompareCollection(option);
            case FOCUS_VTP:
                return showFocusDataAs(option);
            case COMPARE_VTP:
                return showCompareDataAs(option);
            case TRACKER_COLUMN:
                selectTrackerColumn(option);
            case RESET_DATASETS:
                this.resetTab();
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Datasets tab");
        }
    }

    @Override
    public String getName() {
        return "datasetTab";
    }

    /* Actions/state queries used within more than one public method */

    private void expandFocusPsuedoSelect(){
        WebElement optionsDiv = driver.findElement(FOCUS_OPTIONS_DIV);
        if (optionsDiv.isDisplayed()){
            return;
        }
        WebElement select = driver.findElement(FOCUS_DROPDOWN);
        select.click();
        waitShort.until(psuedoOptionsDisplayed(FOCUS_OPTIONS_DIV));
    }
    private void collapseFocusPsuedoSelect(){
        WebElement optionsDiv = driver.findElement(FOCUS_OPTIONS_DIV);
        if (optionsDiv.isDisplayed()){
            WebElement select = driver.findElement(FOCUS_DROPDOWN);
            select.click();
            waitShort.until(psuedoOptionsHidden(FOCUS_OPTIONS_DIV));
        }
    }
    private List<WebElement> getFocusPsuedoOptions(){
        WebElement optionsDiv = driver.findElement(FOCUS_OPTIONS_DIV);
        return optionsDiv.findElements(DS_LIST_OPTIONS);
    }
    private void expandComparePsuedoSelect(){
        WebElement optionsDiv = driver.findElement(COMPARE_OPTIONS_DIV);
        if (optionsDiv.isDisplayed()){
            return;
        }
        WebElement select = driver.findElement(COMPARE_DROPDOWN);
        select.click();
        waitShort.until(psuedoOptionsDisplayed(COMPARE_OPTIONS_DIV));
    }

    private void collapseComparePsuedoSelect(){
        WebElement optionsDiv = driver.findElement(COMPARE_OPTIONS_DIV);
        if (optionsDiv.isDisplayed()){
            WebElement select = driver.findElement(COMPARE_DROPDOWN);
            select.click();
            waitShort.until(psuedoOptionsHidden(COMPARE_OPTIONS_DIV));
        }
    }
    private List<WebElement> getComparePsuedoOptions(){
        WebElement optionsDiv = driver.findElement(COMPARE_OPTIONS_DIV);
        return optionsDiv.findElements(DS_LIST_OPTIONS);
    }
    private WebElement getViewTrackProject() {
        return getViewTrackProject(false);
    }

    private WebElement getViewTrackProject(boolean forCompare) {
        selectAndExpandTab();

        int dsType = forCompare ? 1 : 0;
        return driver.findElements(VIEW_TRACK_PROJECT).get(dsType);
    }
    private WebElement getVTPButton(WebElement vtp, String buttonLabel){
        for(WebElement option : vtp.findElements(VTP_ALL_AVAIL)){
            if(option.getText().equals(buttonLabel)){
                return option;
            }
        }
        return null;
    }

    private By getDDLVTPLocatorByLabel(String showAs) {
        switch (showAs.toLowerCase()){
            case "current":
                // Set the vtpLocator based on how the current view is being shown
            case "view":
                return VTP_OPTION_VIEW;
            case "track":
                return VTP_OPTION_TRACK;
            case "project":
                return VTP_OPTION_PROJECT;
            case "":
                return VTP_OPTION_ANY;
            default:
                throw new IllegalArgumentException("'" + showAs + "' is not a valid View-Project-Track option");
        }
    }

    private List<String> getFocusCollectionNames(){
        expandFocusPsuedoSelect();
        List<String> optionNames = new ArrayList<String>();
        for(WebElement option : getFocusPsuedoOptions()){
            if(!option.getAttribute("class").contains("active")){
                String collName = option.getText();
                String dotsText = option.findElement(DS_LIST_ITEM_DOTS_SPAN).getText();
                collName = collName.replace(dotsText, "").trim();
                optionNames.add(collName);
            }
        }
        collapseFocusPsuedoSelect();
        return optionNames;
    }
    private List<String> getCompareCollectionNames(){
        expandComparePsuedoSelect();
        List<String> optionNames = new ArrayList<String>();
        for(WebElement option : getComparePsuedoOptions()){
            if(!option.getAttribute("class").contains("active")){
                optionNames.add(option.getText());
            }
        }
        collapseComparePsuedoSelect();
        return optionNames;
    }
    private List<String> getVTPOptions(boolean forCompare){
        List<String> vtpOptions = new ArrayList<String>();
        for(WebElement option : getViewTrackProject(forCompare).findElements(VTP_ALL_AVAIL)){
            vtpOptions.add(option.getText());
        }
        return vtpOptions;
    }
    private List<String> getTrackerColumns(){
        WebElement trackerDDL = driver.findElement(TRACKER_COL_DDL);
        List<String> optionNames = new ArrayList<String>();
        for (WebElement option : trackerDDL.findElements(By.tagName("option"))){
            optionNames.add(option.getText());
        }
        return optionNames;
    }

    private ReportViewType getFocusReportViewType() {
        return getFocusReportViewType(false);
    }

    private ReportViewType getFocusReportViewType(boolean compare){
        selectTab();
        WebElement span = getViewTrackProject(compare).findElement(By.tagName("span"));
        String classAttr = span.getAttribute("class");
        if (classAttr.contains("canView")) return ReportViewType.VIEW;
        if (classAttr.contains("HasSBP")) return ReportViewType.PROJECT;
        if (classAttr.contains("IsTracker")) return ReportViewType.TRACK;
        return null;
    }



    /* Expected conditions specific to this component */
    private ExpectedCondition<Boolean> psuedoOptionsDisplayed(By optionsLocator){
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement optionsDiv = driver.findElement(optionsLocator);
                    if (optionsDiv.getAttribute("style").contains("overflow")) return null;
                    if (optionsDiv.getAttribute("style").contains("display: none")) return null;
                    return true;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "Psuedo-options located by " + optionsLocator + " to be displayed";
            }
        };
    }
    private ExpectedCondition<Boolean> psuedoOptionsHidden(By optionsLocator){
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement optionsDiv = driver.findElement(optionsLocator);
                    if (optionsDiv.getAttribute("style").contains("overflow")) return null;
                    if (optionsDiv.getAttribute("style").contains("display: block")) return null;
                    return true;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "Psuedo-options located by " + optionsLocator + " to be hidden";
            }
        };
    }

}

package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    private static final By DATASET_DROPDOWN = By.cssSelector(".tabcontent>span.datasetList");
    private static final By DATASET_OPTIONS_DIV = By.cssSelector(".list.ds");
    private static final By DATASET_OPTIONS = By.tagName("li");

    private static final By COMPARE_DROPDOWN = By.cssSelector(".tabcontent>div>span.datasetList");
    private static final By COMPARE_OPTIONS_DIV = By.cssSelector(".list.cds");

    private static final By TRACKER_COL_DDL = By.id("TrackerColDDL");

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Dataset(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    /* Actions available within this component
    * ToDo: Javadoc */
    public EAPView selectFocusDataset(String optionText){

        // Switch to the Dataset tab & expand it if required
        selectAndExpandTab();

        expandFocusPsuedoSelect();
        boolean found = false;
        for(WebElement option : getFocusPsuedoOptions()){
            if (option.getText().equals(optionText)){
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

    public EAPView selectCompareDataset(String optionText){
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
        Select trackerDDL = (Select)driver.findElement(TRACKER_COL_DDL);
        trackerDDL.selectByVisibleText(optionText);
        return new EAPView(driver);
    }

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    public List<ReportAction> getValidActionsList() {
        selectAndExpandTab();

        List<ReportAction> actions = new ArrayList();
        actions.add(ReportAction.CHANGE_FOCUS);
        WebElement vtp = getViewTrackProject();
        if (vtp.findElements(VTP_ALL_AVAIL).size()>0){
            actions.add(ReportAction.FOCUS_VTP);
        }
        if (driver.findElements(COMPARE_DROPDOWN).size()>0){
            actions.add(ReportAction.CHANGE_COMPARE);
            vtp = getViewTrackProject(true);
            if (vtp.findElements(VTP_ALL_AVAIL).size()>0){
                actions.add(ReportAction.COMPARE_VTP);
            }
        }
        List<WebElement> trackerColDDLs = driver.findElements(TRACKER_COL_DDL);
        if(trackerColDDLs.size()>0){
            if(trackerColDDLs.get(0).isDisplayed()) actions.add(ReportAction.TRACKER_COLUMN);
        }
        return actions;
    }

    public List<String> getOptionsForAction(ReportAction action) {
        switch(action){
            case CHANGE_FOCUS:
                return getFocusDatasetNames();
            case CHANGE_COMPARE:
                return getCompareDatasetNames();
            case FOCUS_VTP:
                return getVTPOptions(false);
            case COMPARE_VTP:
                return getVTPOptions(true);
            case TRACKER_COLUMN:
                return getTrackerColumns();
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Datasets tab");
        }
    }

    public EAPView applyActionOption(ReportAction action, String option) {
        switch(action){
            case CHANGE_FOCUS:
                return selectFocusDataset(option);
            case CHANGE_COMPARE:
                return selectCompareDataset(option);
            case FOCUS_VTP:
                return showFocusDataAs(option);
            case COMPARE_VTP:
                return showCompareDataAs(option);
            case TRACKER_COLUMN:
                selectTrackerColumn(option);
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Datasets tab");
        }
    }

    /*Actions/state queries used within more than one public method */
    private void expandFocusPsuedoSelect(){
        WebElement optionsDiv = driver.findElement(DATASET_OPTIONS_DIV);
        if (optionsDiv.isDisplayed()){
            return;
        }
        WebElement select = driver.findElement(DATASET_DROPDOWN);
        select.click();
        waitShort.until(psuedoOptionsDisplayed(DATASET_OPTIONS_DIV));
    }
    private void collapseFocusPsuedoSelect(){
        WebElement optionsDiv = driver.findElement(DATASET_OPTIONS_DIV);
        if (optionsDiv.isDisplayed()){
            WebElement select = driver.findElement(DATASET_DROPDOWN);
            select.click();
            waitShort.until(psuedoOptionsHidden(DATASET_OPTIONS_DIV));
        }
    }
    private List<WebElement> getFocusPsuedoOptions(){
        WebElement optionsDiv = driver.findElement(DATASET_OPTIONS_DIV);
        return optionsDiv.findElements(DATASET_OPTIONS);
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
        return optionsDiv.findElements(DATASET_OPTIONS);
    }

    private WebElement getViewTrackProject() {
        return getViewTrackProject(false);
    }
    private WebElement getViewTrackProject(boolean forCompare) {
        selectAndExpandTab();

        int dsType = forCompare ? 1 : 0;
        return driver.findElements(VIEW_TRACK_PROJECT).get(dsType);
    }

    private WebElement getVTPButton(WebElement vtp, String reportType){
        for(WebElement option : vtp.findElements(VTP_ALL_AVAIL)){
            if(option.getText().equals(reportType)){
                return option;
            }
        }
        return null;
    }

    private List<String> getFocusDatasetNames(){
        expandFocusPsuedoSelect();
        List<String> optionNames = new ArrayList<String>();
        for(WebElement option : getFocusPsuedoOptions()){
            if(!option.getAttribute("class").contains("active")){
                optionNames.add(option.getText());
            }
        }
        collapseFocusPsuedoSelect();
        return optionNames;
    }
    private List<String> getCompareDatasetNames(){
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

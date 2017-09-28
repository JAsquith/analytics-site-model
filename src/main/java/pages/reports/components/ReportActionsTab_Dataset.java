package pages.reports.components;

import enums.DatasetAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.reports.EAPListView;

import java.util.ArrayList;
import java.util.List;

public class ReportActionsTab_Dataset extends ReportActionsTab {

    private static final String TAB_NAME = "dataset";
    private static final String TAB_CLASS = "datasets";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");

    private static final By VIEW_TRACK_PROJECT = By.cssSelector(".vtp");
    private static final By VTP_VIEW = By.cssSelector(".canView");
    private static final By VTP_TRACK = By.cssSelector(".IsTracker");
    private static final By VTP_PROJECT = By.cssSelector(".HasSBP");
    private static final By VTP_ALL_AVAIL = By.cssSelector(".avail");

    private final By TRACKER_TAB = By.cssSelector(".dsTabs>div:nth-of-type(2)");

    private final By DATASET_DROPDOWN = By.cssSelector(".tabcontent>span.datasetList");
    private final By DATASET_OPTIONS_DIV = By.cssSelector(".list.ds");
    private final By DATASET_OPTIONS = By.tagName("li");

    private final By COMPARE_DROPDOWN = By.cssSelector(".tabcontent>div>span.datasetList");
    private final By COMPARE_OPTIONS_DIV = By.cssSelector(".list.cds");

    public ReportActionsTab_Dataset(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    public EAPListView selectFocusDataset(String optionText){

        // Switch to the Dataset tab & expand it if required
        selectTab();
        expandTab();

        WebElement select = driver.findElement(DATASET_DROPDOWN);
        select.click();
        waitShort.until(datasetListDisplayed());
        WebElement optionsDiv = driver.findElement(DATASET_OPTIONS_DIV);
        List<WebElement> options = optionsDiv.findElements(DATASET_OPTIONS);
        boolean found = false;
        for(WebElement option : options){
            if (option.getText().equals(optionText)){
                option.click();
                found = true;
            }
        }

        if(!found){
            throw new IllegalArgumentException("Could not find an options for dataset '"+ optionText +"'");
        }

        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DROPDOWN));
        return new EAPListView(driver);
    }

    public EAPListView selectCompareDataset(String optionText){
        // Switch to the Dataset tab & expand it if required
        selectTab();
        expandTab();

        WebElement select = driver.findElement(COMPARE_DROPDOWN);
        select.click();
        waitShort.until(compareListDisplayed());
        WebElement optionsDiv = driver.findElement(COMPARE_OPTIONS_DIV);
        List<WebElement> options = optionsDiv.findElements(DATASET_OPTIONS);

        boolean found = false;
        for(WebElement option : options){
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
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DROPDOWN));
        return new EAPListView(driver);
    }

    public EAPListView showFocusDataAs(String reportType){
        // Switch to the Dataset tab & expand it if required
        selectTab();
        expandTab();

        WebElement vtp = getViewTrackProject();
        getVTPButton(vtp, reportType).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    public EAPListView showCompareDataAs(String reportType){
        // Switch to the Dataset tab & expand it if required
        selectTab();
        expandTab();

        WebElement vtp = driver.findElements(VIEW_TRACK_PROJECT).get(1);
        getVTPButton(vtp, reportType).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }

    private WebElement getViewTrackProject() {
        return getViewTrackProject(false);
    }

    private WebElement getViewTrackProject(boolean forCompare) {
        int dsType = forCompare ? 1 : 0;
        return driver.findElements(VIEW_TRACK_PROJECT).get(dsType);
    }

    public List<String> getRandomActionName() {
        selectTab();
        expandTab();
        List<String> actions = new ArrayList();
        actions.add(DatasetAction.CHANGE_FOCUS.name);
        WebElement vtp = getViewTrackProject();
        if (vtp.findElements(VTP_ALL_AVAIL).size()>0){
            actions.add(DatasetAction.FOCUS_VTP.name);
        }
        if (driver.findElements(COMPARE_DROPDOWN).size()>0){
            actions.add(DatasetAction.CHANGE_COMPARE.name);
            vtp = getViewTrackProject(true);
            if (vtp.findElements(VTP_ALL_AVAIL).size()>0){
                actions.add(DatasetAction.COMPARE_VTP.name);
            }
        }
        return actions;
    }

    public String getRandomOptionFor(String actionName) {
        return null;
    }

    public boolean applyActionOption(String actionName, String option) {
        return false;
    }

    private void selectAndExpand(){
        super.selectTab();
        if (super.canExpand()){
            super.expandTab();
        }

    }

    private WebElement getVTPButton(WebElement vtp, String reportType){
        WebElement button;
        switch(reportType){
            case "Tracker":
                button = vtp.findElement(VTP_TRACK);
                break;
            case "Projection":
                button = vtp.findElement(VTP_PROJECT);
            default:
                button = vtp.findElement(VTP_VIEW);
        }
        return button;
    }
    private ExpectedCondition<Boolean>datasetListDisplayed(){
        WebElement optionsDiv = driver.findElement(DATASET_OPTIONS_DIV);
        return ExpectedConditions.attributeContains(optionsDiv, "style", "display: block");
    }
    private ExpectedCondition<Boolean>compareListDisplayed(){
        WebElement optionsDiv = driver.findElement(COMPARE_OPTIONS_DIV);
        return ExpectedConditions.attributeToBe(optionsDiv, "style", "");
    }

}

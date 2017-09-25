package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.reports.EAPListView;

import java.util.List;

public class Report_DatasetOptions extends Report_OptionsTab {

    public static final By VIEW_TRACK_PROJECT = By.cssSelector(".vtp");
    public static final By VTP_VIEW = By.cssSelector(".canView");
    public static final By VTP_TRACK = By.cssSelector(".avail.IsTracker");
    public static final By VTP_PROJECT = By.cssSelector(".avail.HasSBP");

    private final By TRACKER_TAB = By.cssSelector(".dsTabs>div:nth-of-type(2)");

    private final By DATASET_DROPDOWN = By.cssSelector(".tabcontent>span.datasetList");
    private final By DATASET_OPTIONS_DIV = By.cssSelector(".list.ds");
    private final By DATASET_OPTIONS = By.tagName("li");

    private final By COMPARE_DROPDOWN = By.cssSelector(".tabcontent>div>span.datasetList");
    private final By COMPARE_OPTIONS_DIV = By.cssSelector(".list.cds");

    public Report_DatasetOptions(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = "Dataset";
    }

    public boolean isDisabled(String optionName){

        WebElement toTest;
        switch (optionName){
            case "Tracker":
                WebElement vtp = driver.findElements(VIEW_TRACK_PROJECT).get(0);
                toTest = vtp.findElement(VTP_TRACK);
                break;
            default:
                toTest = driver.findElement(DATASET_DROPDOWN);
        }

        return toTest.getAttribute("class").toLowerCase().contains("disabled");
    }

    public EAPListView selectMainFocus(String optionText){

        // Switch to the Dataset tab
        allTabs.selectTab(tabName);

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

    public EAPListView selectCompareWith(String optionText){
        // Switch to the Dataset tab
        allTabs.selectTab(tabName);

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
            }
        }

        if(!found){
            throw new IllegalArgumentException("Could not find an options for dataset '"+ optionText +"'");
        }

        waitForLoadingWrapper();
        waitMedium.until(ExpectedConditions.elementToBeClickable(DATASET_DROPDOWN));
        return new EAPListView(driver);
    }

    public EAPListView showFocusAs(String reportType){

        WebElement vtp = driver.findElements(VIEW_TRACK_PROJECT).get(0);
        getVTPButton(vtp, reportType).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
    }
    public EAPListView showCompareAs(String reportType){

        WebElement vtp = driver.findElements(VIEW_TRACK_PROJECT).get(1);
        getVTPButton(vtp, reportType).click();
        waitForLoadingWrapper();
        return new EAPListView(driver);
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

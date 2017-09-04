package pages.reports.components;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;
import pages.reports.EAPListView;

import java.util.List;

/**
 * Represents the contents and interactive elements within an 'accordion' for a given
 * Cohort and academic year on the KS3/4 Reports Home Page
 */
public class ReportsHome_YearAccordion extends AnalyticsComponent {

    public static final By REPORT_AREA_BUTTONS = By.cssSelector(".EAPRptBtn>a");

    public final By COHORT_HEADING = By.cssSelector(".rptHome>h2");
    public final By COMPONENT = By.className("eapYear");
    public final By TITLE_BAR = By.className("eapYearTitle");
    public final By PUBLISHED_REPORT = By.cssSelector(".eapPub");
    public final By REPORT_INFO = By.cssSelector(".eapInfo>em");

    private WebElement accordion;
    private WebElement titleBar;

    // CONSTRUCTOR
    public ReportsHome_YearAccordion(RemoteWebDriver aDriver, String yearNumber){
        super(aDriver);
        this.init(yearNumber, "");
    }

    public ReportsHome_YearAccordion(RemoteWebDriver aDriver, String yearNumber, String forTracker){
        super(aDriver);
        this.init(yearNumber, forTracker);
    }

    @Step ( "Access Year Accordion: {yearNumber} (forTracker = {forTracker})" )
    private void init(String yearNumber, String forTracker){
        waitMedium.until(ExpectedConditions.elementToBeClickable(COMPONENT));

        String cohortHeading = driver.findElement(COHORT_HEADING).getText();
        cohortHeading = cohortHeading.substring(cohortHeading.indexOf('(')+1, cohortHeading.indexOf(')'));

        String accTitle;

        if (forTracker != ""){
            accTitle = "Tracker DataSets for " + cohortHeading;
        }else {
            accTitle = "Year " + yearNumber + " data for " + cohortHeading;
        }

        List<WebElement> accordions = driver.findElements(COMPONENT);
        for (WebElement accordion : accordions) {
            if (accordion.findElement(By.tagName("em")).getText().equals(accTitle)) {
                this.accordion = accordion;
                titleBar = accordion.findElement(TITLE_BAR);
                break;
            }
        }
    }

    //  - CHANGING THE STATE OF THE PAGE
    @Step( "Expand Year Accordion" )
    public void expandYear(){
        List<WebElement> yearSubElements = accordion.findElements(PUBLISHED_REPORT);
        if (!yearSubElements.get(0).isDisplayed()){
            titleBar.click();
            waitForAccordionExpansion();
        }
    }

    @Step( "Expand Dataset Accordion: {datasetName}" )
    public WebElement expandPublishedReport(String datasetName){
        expandYear();
        List<WebElement> publishedReports = accordion.findElements(PUBLISHED_REPORT);
        for (WebElement pubReport: publishedReports) {
            WebElement repInfo = pubReport.findElement(REPORT_INFO);
            if (repInfo.getText().trim().equals(datasetName)){
                if (!pubReport.getAttribute("class").contains("active")){
                    pubReport.click();
                    waitForPublishedReportExpansion(pubReport.findElement(REPORT_AREA_BUTTONS));
                }
                return pubReport;
            }
        }
        throw new IllegalArgumentException(datasetName + " is not a dataset in the current cohort");
    }

    public EAPListView gotoPublishedReport(String datasetName, String forTracker) {
        return gotoPublishedReport(datasetName, forTracker, "Grades");
    }

    @Step( "Open {repArea} Report for: {datasetName} (forTracker = {forTracker})" )
    public EAPListView gotoPublishedReport(String datasetName, String forTracker, String repArea) {
        WebElement pubReport;
        WebElement button;
        if(!forTracker.equals("")){
            pubReport = expandPublishedReport("Tracker");
            button = getReportButtonFor(pubReport, repArea);
        }else{
            pubReport = expandPublishedReport(datasetName);
            button = getReportButtonFor(pubReport, repArea);
        }
        if (button == null){
            String msg = "A button to access the "+repArea+" Reports for the "+datasetName+" "+
                    ((forTracker.equals("")) ? "" : "("+forTracker+")") + " dataset could not be found";
            throw new IllegalArgumentException(msg);
        }
        button.click();

        waitForLoadingWrapper();
        EAPListView reportPage = new EAPListView(driver);
        if (!forTracker.equals("")){
            reportPage = reportPage.dsOptions.selectDataset(datasetName);
        }
        return reportPage;
    }

    public EAPListView gotoPublishedReport(String datasetName, boolean forTracker, String repCategory){
        String trackerCol;
        if (forTracker){
            trackerCol = "[default]";
        } else {
            trackerCol = "";
        }
        return gotoPublishedReport(datasetName, trackerCol, repCategory);
    }

    //  - COMPONENT SPECIFIC WAITS
    private List<WebElement> waitForAccordionExpansion(){
        return waitShort.until(ExpectedConditions.numberOfElementsToBeMoreThan(PUBLISHED_REPORT, 0));
    }
    private WebElement waitForPublishedReportExpansion(WebElement button){
        return waitMedium.until(ExpectedConditions.visibilityOf(button));
    }
    private WebElement getReportButtonFor(WebElement pubReport, String repArea){
        List<WebElement> repButtons = pubReport.findElements(REPORT_AREA_BUTTONS);
        if (repButtons.size()==0){
            return null;
        }
        for (WebElement button : repButtons){
            if(button.getText().toLowerCase().trim().equals(repArea.toLowerCase())){
                return button;
            }
        }
        return null;
    }

}

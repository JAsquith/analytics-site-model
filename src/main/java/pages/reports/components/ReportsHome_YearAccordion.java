package pages.reports.components;

import pages.AnalyticsComponent;
import pages.reports.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Represents the contents and interactive elements within an 'accordion' for a given
 * Cohort and academic year on the KS3/4 Reports Home Page
 */
public class ReportsHome_YearAccordion extends AnalyticsComponent {

    public final By COHORT_HEADING = By.cssSelector(".rptHome>h2");
    public final By COMPONENT = By.className("eapYear");
    public final By TITLE_BAR = By.className("eapYearTitle");
    public final By PUBLISHED_REPORT = By.cssSelector(".eapPub");
    public final By REPORT_INFO = By.cssSelector(".eapInfo>em");
    public final By GO_TO_REPORTS = By.cssSelector(".EAPRptBtn>a");

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
    public void expandYear(){
        List<WebElement> yearSubElements = accordion.findElements(PUBLISHED_REPORT);
        if (!yearSubElements.get(0).isDisplayed()){
            titleBar.click();
            waitForAccordionExpansion();
        }
    }

    public WebElement expandPublishedReport(String datasetName){
        expandYear();
        List<WebElement> publishedReports = accordion.findElements(PUBLISHED_REPORT);
        for (WebElement pubReport: publishedReports) {
            WebElement repInfo = pubReport.findElement(REPORT_INFO);
            if (repInfo.getText().trim().equals(datasetName)){
                if (!pubReport.getAttribute("class").contains("active")){
                    pubReport.click();
                    waitForPublishedReportExpansion(pubReport.findElement(GO_TO_REPORTS));
                }
                return pubReport;
            }
        }
        return null;
    }

    public Report gotoPublishedReport(String datasetName, String forTracker){

        if(!forTracker.equals("")){
            expandPublishedReport("Tracker").findElement(GO_TO_REPORTS).click();
        }else{
            expandPublishedReport(datasetName).findElement(GO_TO_REPORTS).click();
        }

        waitForLoadingWrapper();
        Report reportPage = new Report(driver);
        if (!forTracker.equals("")){
            reportPage = reportPage.selectDataset(datasetName);
        }
        return reportPage;
    }

    //  - COMPONENT SPECIFIC WAITS
    private List<WebElement> waitForAccordionExpansion(){
        return waitShort.until(ExpectedConditions.numberOfElementsToBeMoreThan(PUBLISHED_REPORT, 0));
    }
    private WebElement waitForPublishedReportExpansion(WebElement button){
        return waitMedium.until(ExpectedConditions.visibilityOf(button));
    }

}

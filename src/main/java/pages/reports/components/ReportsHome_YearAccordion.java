package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
    public static final By REPORT_STATUS_TEXT = By.cssSelector(".eapPub>.eapStat>span:not(.icon)");
    public static final By REPORT_SETS_WITHIN_YEAR = By.cssSelector(".eapPub");
    public static final By REPORT_NAME_FAKE_LINK = By.cssSelector("em.fakea");

    public final By COHORT_HEADING = By.cssSelector(".CohortFriendlyName");
    public final By COMPONENT = By.className("eapYear");
    public final By TITLE_BAR = By.className("eapYearTitle");
    public final By PUBLISHED_REPORT = By.cssSelector(".eapPub");
    public final By REPORT_INFO = By.cssSelector(".eapInfo>em");

    private By yearGroupSelector;
    private By pubGroupSelector;
    private WebElement yearGroup;
    private WebElement pubGroups;
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
        // Wait for one or more Year groups to be clickable
        waitMedium.until(ExpectedConditions.elementToBeClickable(COMPONENT));

        String selectorSuffix = (forTracker.equals("")) ? yearNumber : "trk";
        yearGroupSelector = By.cssSelector(".eapYear[data-year='" + selectorSuffix + "']");
        pubGroupSelector = By.cssSelector(".pubGrp_"+ selectorSuffix);

        refreshElements();
    }
    private void refreshElements(){
        this.yearGroup = driver.findElement(yearGroupSelector);
        this.pubGroups = driver.findElement(pubGroupSelector);
        titleBar = yearGroup.findElement(TITLE_BAR);
    }

    //  - CHANGING THE STATE OF THE PAGE
    public void expandYear(){
        if (yearGroup.getAttribute("class").contains("open")){
            return;
        }
        yearGroup.click();
        waitShort.until(pubGroupDisplayed());
        this.refreshElements();
    }

    public WebElement expandPublishedReport(String datasetName){
        expandYear();
        List<WebElement> reportSets = pubGroups.findElements(REPORT_SETS_WITHIN_YEAR);
        for(WebElement reportSet : reportSets){
            WebElement reportNameLink = reportSet.findElement(REPORT_NAME_FAKE_LINK);
            if (reportNameLink.getText().trim().equals(datasetName)){
                reportNameLink.click();
                waitForPublishedReportExpansion(pubGroups.findElement(REPORT_AREA_BUTTONS));
                return reportSet;
            }
        }
        throw new IllegalArgumentException("A Report named " + datasetName + " was not found!");
    }

    public EAPListView gotoPublishedReport(String datasetName, String forTracker) {
        return gotoPublishedReport(datasetName, forTracker, "Grades");
    }

    public EAPListView gotoPublishedReport(String datasetName, String forTracker, String repArea) {
        expandPublishedReport(datasetName);
        WebElement button = getReportButtonFor(repArea);
        if (button == null){
            String msg = "A button to access the "+repArea+" Reports for the "+datasetName+" "+
                    ((forTracker.equals("")) ? "" : "("+forTracker+")") + " dataset could not be found";
            throw new IllegalArgumentException(msg);
        }
        button.click();

        waitForLoadingWrapper();
        EAPListView reportPage = new EAPListView(driver);
        if (!forTracker.equals("")){
            reportPage = reportPage.dsOptions.selectMainFocus(datasetName);
        }
        return reportPage;
    }

    //  - COMPONENT SPECIFIC WAITS
    private WebElement waitForPublishedReportExpansion(WebElement button){
        return waitMedium.until(ExpectedConditions.elementToBeClickable(button));
    }
    private ExpectedCondition<Boolean> pubGroupDisplayed() {
        return ExpectedConditions.attributeToBe(pubGroupSelector, "style", "display: block");
    }

    private WebElement getReportButtonFor(String repArea){
        List<WebElement> repButtons = pubGroups.findElements(REPORT_AREA_BUTTONS);
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

package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsComponent;
import pages.reports.EAPView;

import java.util.List;

/**
 * Represents the contents and interactive elements within an 'accordion' for a given
 * Cohort and academic year on the KS3/4 Reports Home Page
 */
public class ReportsHome_EAPYearGroup extends AnalyticsComponent {

    public static final By REPORT_AREA_BUTTONS = By.cssSelector(".EAPRptBtn a");
    public static final By REPORT_STATUS_TEXT = By.cssSelector(".eapPub>.eapStat>span:not(.icon)");
    public static final By REPORT_SETS_WITHIN_YEAR = By.cssSelector(".eapPub");
    public static final By REPORT_NAME_FAKE_LINK = By.cssSelector("em.fakea");

    public final By COHORT_HEADING = By.cssSelector(".CohortFriendlyName");
    public static final By COMPONENT = By.className("eapYear");
    public final By TITLE_BAR = By.className("eapYearTitle");
    public final By PUBLISHED_REPORT = By.cssSelector(".eapPub");
    public final By REPORT_INFO = By.cssSelector(".eapInfo>em");

    private By yearGroupSelector;
    private By pubGroupSelector;
    private WebElement yearGroup;
    private WebElement pubGroups;
    private WebElement pubReportSet;
    private WebElement titleBar;

    public static List<WebElement> getEAPYearGroups(RemoteWebDriver driver){
        return driver.findElements(COMPONENT);
    }

    // CONSTRUCTOR
    public ReportsHome_EAPYearGroup(RemoteWebDriver aDriver, String dataYear){
        super(aDriver);
        this.init(dataYear);
    }

    private void init(String dataYear){
        // Wait for one or more Year groups to be clickable
        waitMedium.until(ExpectedConditions.elementToBeClickable(COMPONENT));

        yearGroupSelector = By.cssSelector(".eapYear[data-year='" + dataYear + "']");
        pubGroupSelector = By.cssSelector(".pubGrp_"+ dataYear);

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
        waitShort.until(pubGroupDisplayed(yearGroup));
        this.refreshElements();
    }

    public List<WebElement> getPublishedReportSets(){
        return pubGroups.findElements(REPORT_SETS_WITHIN_YEAR);
    }

    public WebElement expandPublishedReport(String datasetName){
        expandYear();
        List<WebElement> reportSets = getPublishedReportSets();
        for(WebElement reportSet : reportSets){
            WebElement reportNameLink = reportSet.findElement(REPORT_NAME_FAKE_LINK);
            if (reportNameLink.getText().trim().equals(datasetName)){
                reportNameLink.click();
                List<WebElement> buttons = reportSet.findElements(REPORT_AREA_BUTTONS);
                waitForPublishedReportExpansion(reportSet.findElement(REPORT_AREA_BUTTONS));
                pubReportSet = reportSet;
                return reportSet;
            }
        }
        throw new IllegalArgumentException("A Report named " + datasetName + " was not found!");
    }

    public EAPView gotoPublishedReport(String datasetName, String forTracker) {
        return gotoPublishedReport(datasetName, forTracker, "Grades");
    }

    public EAPView gotoPublishedReport(String datasetName, String forTracker, String repArea) {
        expandPublishedReport(datasetName);
        WebElement button = getReportButtonFor(repArea);
        if (button == null){
            String msg = "A button to access the "+repArea+" Reports for the "+datasetName+" "+
                    ((forTracker.equals("")) ? "" : "("+forTracker+")") + " dataset could not be found";
            throw new IllegalArgumentException(msg);
        }
        button.click();

        waitForLoadingWrapper();
        EAPView reportPage = new EAPView(driver);
        if (!forTracker.equals("")){
            reportPage = reportPage.datasetsTab.selectFocusDataset(datasetName);
        }
        return reportPage;
    }

    public String getReportSetName(WebElement reportSet){
        return reportSet.findElement(REPORT_NAME_FAKE_LINK).getText();
    }

    public List<WebElement> getReportButtons(WebElement reportSet){
        return reportSet.findElements(REPORT_AREA_BUTTONS);
    }

    //  - COMPONENT SPECIFIC WAITS
    private WebElement waitForPublishedReportExpansion(WebElement button){
        return waitMedium.until(ExpectedConditions.elementToBeClickable(button));
    }
    private ExpectedCondition<Boolean> pubGroupDisplayed(WebElement yearGroup) {
        return ExpectedConditions.attributeContains(yearGroup, "class", "open");
    }

    private WebElement getReportButtonFor(String repArea){
        List<WebElement> repButtons = pubReportSet.findElements(REPORT_AREA_BUTTONS);
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

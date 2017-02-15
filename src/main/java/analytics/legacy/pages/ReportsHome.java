package analytics.legacy.pages;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

/**
 * Provides Selenium locators (as static fields) used to locate
 * <a href='https://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/WebElement.html'>
 * WebElement</a>s found in the Reports&nbsp;area of
 * <a href='https://www.sisraanalytics.co.uk/'>SISRA Analytics</a>, and convenience methods for
 * many actions commonly performed in that area.
 *
 * @author Milton
 * @version 1.0
 * @since 2016-02-28.
 */
public class ReportsHome extends BaseWithMenu {

    /** Identifies the Keystage div elements on the Reports homepage.*/
    public static final By KEYSTAGE_DIVS = By.cssSelector(".ks");

    /** Identifies the Keystage links on the Reports homepage.*/
    public static final By KEYSTAGE_LINKS = By.cssSelector(".ks>a");

    /** Identifies the Cohort links on the Reports homepage.*/
    public static final By COHORT_DIVS = By.cssSelector(".year");

    /** Identifies the Cohort links on the Reports homepage.*/
    public static final By COHORT_LINKS = By.cssSelector(".year>a");

    /** Identifies the Published Reports on the Reports homepage.*/
    public static final By PUBLISHED_REPORTS = By.cssSelector(".l_pub");

    /** Identifies the Report Area buttons within a particular Published Report.*/
    public static final By AREA_BUTTONS = By.cssSelector(".shim>a.button");

    /** Identifies the Leavers drop down tab on the right of the Reports Home page */
    public static final By LEAVERS_TAB = By.cssSelector(".lvrBTN");

    /** Identifies the name (&lt;em&gt; element) within a particular Published Report */
    public static final By REPORT_NAME = By.cssSelector(".info>em");

    // Constructors
    /**
     * Creates a new ReportsHome instance with the given AnayticsDriver.
     *
     * @param aDriver An AnalyticsDriver
     */
    public ReportsHome(AnalyticsDriver aDriver) {super(aDriver);}
    public ReportsHome(AnalyticsDriver aDriver, String analyticsHome) {
        super(aDriver);
        visit(analyticsHome+"Reports");
    }

    public int countKeyStages(){return getKeystages().size();}
    public List<WebElement> getKeystages(){return findAll(KEYSTAGE_DIVS);}
    public int countCohorts(){return getCohorts().size();}
    public List<WebElement> getCohorts(){return findAll(COHORT_DIVS);}
    public int countReports(){return findAll(PUBLISHED_REPORTS).size();}
    public List<WebElement> getReports(){return findAll(PUBLISHED_REPORTS);}
    public List<WebElement> getAreaButtons(WebElement report){return report.findElements(AREA_BUTTONS);}

    public List<WebElement> waitForAreaButtonsDisplayed(WebElement report, int timeout)
            throws TimeoutException {
        List<WebElement> buttons = getAreaButtons(report);
        long deadline = System.currentTimeMillis()+(timeout * 1000);
        boolean allDisplayed = true;
        for (WebElement button : buttons)
            if (!button.isDisplayed())
                allDisplayed = false;

        while(!allDisplayed && System.currentTimeMillis() < deadline){
            try{
                Thread.sleep(200);
            } catch (InterruptedException e){Thread.interrupted();}
            allDisplayed = true;
            for (WebElement button : buttons)
                if (!button.isDisplayed())
                    allDisplayed = false;
        }

        if (!allDisplayed)
            throw new TimeoutException("Timed out waiting for Report Area buttons to be displayed");
        return buttons;
    }

    public String openReportAtRandom(){
        this.collapseLeaverCohorts();
        List<WebElement> reportLinks = this.getReports();
        int reportIndex = new Random().nextInt(reportLinks.size());
        WebElement report = reportLinks.get(reportIndex);
        String reportName = report.findElement(REPORT_NAME).getText();
        report.click();
        this.waitForAreaButtonsDisplayed(report,DEFAULT_SHORT_TIMEOUT).get(0).click();
        this.waitForReload();
        return reportName;
    }

    public boolean openReportByName(String reportName){
        List<WebElement> reportLinks = this.getReports();
        for (WebElement rep : reportLinks)
            if (find(rep, REPORT_NAME).getText().equals(reportName)) {
                rep.click();
                this.waitForAreaButtonsDisplayed(rep, DEFAULT_SHORT_TIMEOUT).get(0).click();
                this.waitForReload();
                return true;
            }

        return false;
    }

    public void expandLeaverCohorts(){
        try {
            WebElement leaversTab = find(LEAVERS_TAB);
            if (!leaversTab.getAttribute("class").contains("active4"))
                click(leaversTab);
        } catch (NoSuchElementException e) {
            // No problem, there's nothing hidden under something that isn't here!
        }
    }

    public void collapseLeaverCohorts(){
        try {
            WebElement leaversTab = find(LEAVERS_TAB);
            if (leaversTab.getAttribute("class").contains("active4"))
                click(leaversTab);
        } catch (NoSuchElementException e) {
            // No problem, there's nothing hidden under something that isn't here!
        }
    }
    public boolean isSelected(WebElement element){return element.getAttribute("class").contains("selected");}

    public boolean selectKSByName(String keystage){
        for (WebElement ksDiv : getKeystages())
            if (ksDiv.getText().equals(keystage)) {
                if (!isSelected(ksDiv)) {
                    ksDiv.findElement(By.tagName("a")).click();
                    waitForReload();
                }
                // The keystage was found (selected or not), return true
                return true;
            }

        // If the keystage was not found, return false
        return false;
    }
    public String selectKSAtRandom(){
        List<WebElement> ksDivs = getKeystages();
        int ksIndex = new Random().nextInt(ksDivs.size());
        WebElement ksDiv = ksDivs.get(ksIndex);
        String selectedKS = ksDiv.getText();

        if (!isSelected(ksDiv)) {
            ksDiv.findElement(By.tagName("a")).click();
            waitForReload();
        }

        return selectedKS;
    }

    public boolean selectCohortByName(String cohort){
        this.expandLeaverCohorts();
        List<WebElement> cohortDivs = this.getCohorts();
        for (WebElement cohortDiv : cohortDivs) {
            if (cohortDiv.getText().equals(cohort)) {
                if (!isSelected(cohortDiv)){
                    cohortDiv.findElement(By.tagName("a")).click();
                    waitForReload();
                }
                // The cohort was found (selected or not), return true
                return true;
            }
        }
        return false;
    }
    public String selectCohortAtRandom(){
        this.expandLeaverCohorts();
        List<WebElement> cohortDivs = this.getCohorts();
        if (cohortDivs.get(0).getText().equals("Leavers"))
            cohortDivs.remove(0);
        int cohortIndex = new Random().nextInt(cohortDivs.size());
        WebElement cohortDiv = cohortDivs.get(cohortIndex);
        String selectedCohort = cohortDiv.getText();

        if (!isSelected(cohortDiv)){
            cohortDiv.findElement(By.tagName("a")).click();
            waitForReload();
        }

        return selectedCohort;
    }

    // Any private methods...
}

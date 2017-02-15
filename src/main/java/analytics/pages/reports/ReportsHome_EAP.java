package analytics.pages.reports;


import analytics.AnalyticsDriver;
import analytics.pages.reports.components.ReportsHome_YearAccordion;
import org.openqa.selenium.By;

/**
 * Represents the contents and interactive elements on the KS3/4 Reports Home Page
 */
public class ReportsHome_EAP extends ReportsHome {

    private static final String PAGE_PATH = "/ReportsHome?selectedKS=6";
    protected static final By COHORT_TITLE = By.cssSelector("div.rptHome>h2");

    public ReportsHome_EAP(AnalyticsDriver aDriver, boolean loadByUrl){
        super(aDriver);
        if (loadByUrl){
            driver.get(driver.getDomainRoot() + PAGE_PATH);
            waitForLoadingWrapper();
        }
    }

    public String getSelectedCohortTitle(){
        return driver.findElement(COHORT_TITLE).getText();
    }

    public ReportsHome_YearAccordion getYearAccordion(String yearNumber) {
        return getYearAccordion(yearNumber, "");
    }

    public ReportsHome_YearAccordion getYearAccordion(String yearNumber, String forTracker){
        return new ReportsHome_YearAccordion(driver, yearNumber, forTracker);
    }


}

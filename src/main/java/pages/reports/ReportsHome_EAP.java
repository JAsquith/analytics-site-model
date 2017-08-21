package pages.reports;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.components.ReportsHome_YearAccordion;

/**
 * Represents the contents and interactive elements on the KS3/4 Reports Home Page
 */
public class ReportsHome_EAP extends ReportsHome {

    private static final String PAGE_PATH = "/ReportsHome";
    private static final By KS_BUTTON_SELECTED = By.cssSelector(".ksTabs .ks.selected");
    private static final By YEAR_BUTTON_SELECTED = By.cssSelector(".yearTabs .year.selected");
    private static final String PAGE_PATH_SELECT_KS = PAGE_PATH + "?selectedKS=6";
    private static final String PAGE_PATH_SELECT_COHORT = PAGE_PATH + "?selectedCohort=";
    protected static final By COHORT_TITLE = By.cssSelector("div.rptHome>h2");

    public ReportsHome_EAP(RemoteWebDriver aDriver, boolean loadByUrl){
        super(aDriver);
        if (loadByUrl){
            openIfNotAlready();
            selectKSIfNotAlready();
        }
    }

    public ReportsHome_EAP selectCohortByUrl(String cohort){
        openIfNotAlready();
        selectKSIfNotAlready();
        if(!cohort.equals(getCurrentCohort())) {
            driver.get(getCurrentDomain() + PAGE_PATH_SELECT_COHORT + cohort);
        }
        return new ReportsHome_EAP(driver, false);
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

    private void openIfNotAlready(){
        boolean pageAlreadyOpen = driver.getCurrentUrl().
                startsWith(getCurrentDomain()+PAGE_PATH);
        if (!pageAlreadyOpen){
            driver.get(getCurrentDomain() + PAGE_PATH_SELECT_KS);
            waitForLoadingWrapper();
        }
    }
    private void selectKSIfNotAlready(){
        boolean ksAlreadySelected = driver.findElement(KS_BUTTON_SELECTED).
                findElement(By.tagName("a")).getText().equals("KS 3/4");
        if (!ksAlreadySelected){
            driver.get(getCurrentDomain() + PAGE_PATH_SELECT_KS);
            waitForLoadingWrapper();
        }
    }

    private String getCurrentCohort(){
        WebElement yearTab = driver.findElement(YEAR_BUTTON_SELECTED);
        String linkURL = yearTab.findElement(By.tagName("a")).getAttribute("href");
        return linkURL.substring(linkURL.indexOf("=")+1);
    }
}

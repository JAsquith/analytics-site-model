package pages.reports;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.reports.components.ReportsHome_EAPYearGroup;

import java.util.List;

/**
 * Represents the contents and interactive elements on the KS3/4 Reports Home Page
 */
public class ReportsHome_EAP extends ReportsHome {

    private static final String PAGE_PATH = "/ReportsHome";
    private static final By KS_BUTTON_SELECTED = By.cssSelector(".ksTabs .ks.selected");
    private static final By YEAR_BUTTON_SELECTED = By.cssSelector(".yearTabs .year.selected:not(.lvrBTN)");
    private static final String PAGE_PATH_SELECT_KS = PAGE_PATH + "?selectedKS=6";
    private static final String PAGE_PATH_SELECT_COHORT = PAGE_PATH + "?selectedCohort=";
    private static final By COHORT_TITLE = By.cssSelector("CohortFriendlyName");

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
            driver.get(getSiteBaseUrl() + PAGE_PATH_SELECT_COHORT + cohort);
        }
        return new ReportsHome_EAP(driver, false);
    }

    public ReportsHome_EAP selectCohortByUserAction(String cohort){
        openIfNotAlready();
        selectKSIfNotAlready();

        WebElement selectedCohortTab = driver.findElement(YEAR_BUTTON_SELECTED);
        String linkURL = selectedCohortTab.findElement(By.tagName("a")).getAttribute("href");
        String currYearID = linkURL.substring(linkURL.indexOf("=")+1);
        if (currYearID.equals(cohort)){
            return this;
        }

        List<WebElement> yearButtons = driver.findElements(By.cssSelector(".year:not(.lvrBTN)"));

        for (WebElement yearButton : yearButtons){
            linkURL = yearButton.findElement(By.tagName("a")).getAttribute("href");
            currYearID = linkURL.substring(linkURL.indexOf("=")+1);
            if(currYearID.equals(cohort)){
                if (!yearButton.isDisplayed()){
                    driver.findElement(By.cssSelector(".lvrBTN")).click();
                    waitShort.until(ExpectedConditions.elementToBeClickable(yearButton));
                }
                yearButton.click();
                return new ReportsHome_EAP(driver, false);
            }
        }
        throw new IllegalArgumentException("'"+cohort+"' did not match any Year link on the EAP Reports home page!");
    }

    public String getSelectedCohortTitle(){
        return driver.findElement(COHORT_TITLE).getText();
    }

    public ReportsHome_EAPYearGroup getEAPYearGroup(String yearNumber) {
        return new ReportsHome_EAPYearGroup(driver, yearNumber);
    }

    public ReportsHome_EAPYearGroup getEAPYearGroup(String yearNumber, String forTracker){
        String dataYear = yearNumber;
        if (!forTracker.equals("")){
            dataYear = "trk";
        }
        return new ReportsHome_EAPYearGroup(driver, dataYear);
    }

    public List<WebElement> countEAPYearGroups(){
        return ReportsHome_EAPYearGroup.getEAPYearGroups(driver);
    }

    private void openIfNotAlready(){
        boolean pageAlreadyOpen = driver.getCurrentUrl().
                startsWith(getSiteBaseUrl()+PAGE_PATH);
        if (!pageAlreadyOpen){
            driver.get(getSiteBaseUrl() + PAGE_PATH_SELECT_KS);
            waitForLoadingWrapper();
        }
    }
    private void selectKSIfNotAlready(){
        boolean ksAlreadySelected = driver.findElement(KS_BUTTON_SELECTED).
                findElement(By.tagName("a")).getText().equals("KS 3/4");
        if (!ksAlreadySelected){
            driver.get(getSiteBaseUrl() + PAGE_PATH_SELECT_KS);
            waitForLoadingWrapper();
        }
    }

    private String getCurrentCohort(){
        WebElement yearTab = driver.findElement(YEAR_BUTTON_SELECTED);
        String linkURL = yearTab.findElement(By.tagName("a")).getAttribute("href");
        return linkURL.substring(linkURL.indexOf("=")+1);
    }
}

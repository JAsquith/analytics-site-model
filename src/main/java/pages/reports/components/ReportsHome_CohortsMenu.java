package pages.reports.components;

import io.qameta.allure.Step;
import pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;


/**
 * Represents the contents and interactive elements within Cohort menu of the KS3/4 Reports Home Page
 */
public class ReportsHome_CohortsMenu extends AnalyticsComponent {

    private final By COMPONENT = By.cssSelector(".yearTabs");
    protected static final By LEAVERS_DDL = By.cssSelector(".lvrBTN");
    protected static final By COHORT_BUTTONS = By.cssSelector(".year:not(.lvrBTN)>a");
    protected static final By LEAVER_COHORT_BUTTONS = By.cssSelector(".lvrDDL>.year>a");
    protected static final By SELECTED_COHORT_BUTTON = By.cssSelector(".year.selected>a");


// CONSTRUCTOR
    public ReportsHome_CohortsMenu(RemoteWebDriver aDriver){
        super(aDriver);
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        driverWait.until(ExpectedConditions.elementToBeClickable(COMPONENT));
    }


// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public List<WebElement> getAllCohortButtons(){
        expandLeaversDDL();
        List<WebElement> cohortButtons = driver.findElements(COHORT_BUTTONS);
        return cohortButtons;
    }
    public String getSelectedCohortID(){
        expandLeaversDDL();
        String temp = driver.findElement(SELECTED_COHORT_BUTTON).getAttribute("href");
        temp = temp.substring(temp.length()-2);
        if (temp.startsWith("=")){
            temp = temp.substring(1);   // This is in case 2008/09 has an ID of "9" rather than "09"
        }
        return temp;
    }

    public String getSelectedCohortLabel(){
        expandLeaversDDL();
        return driver.findElement(SELECTED_COHORT_BUTTON).getText();
    }


    //  - CHANGING THE STATE OF THE PAGE
    public void expandLeaversDDL(){
        List<WebElement> lvrBTNs = driver.findElements(LEAVERS_DDL);
        if (lvrBTNs.size()==0){
            // There's no Leavers DDL so there's nothing to expand
            return;
        }
        if (driver.findElements(LEAVER_COHORT_BUTTONS).size()==0){
            // The DDL is present but we can't see any leaver years so it must be collapsed
            lvrBTNs.get(0).click();
            waitForLeaversDropDown();
        }
    }
    public void collapseLeaversDDL(){
        List<WebElement> lvrBTNs = driver.findElements(LEAVERS_DDL);
        if (lvrBTNs.size()==0){
            // There's no Leavers DDL so there's nothing to collapse
            return;
        }
        if (driver.findElements(LEAVER_COHORT_BUTTONS).size()>0){
            // The DDL is present and we can see some Leaver years so it must be expanded
            lvrBTNs.get(0).click();
        }
    }

    @Step( "Select a random cohort" )
    public void selectRandomCohort(){
        List<WebElement> cohortButtons = getAllCohortButtons();
        cohortButtons.get(new Random().nextInt(cohortButtons.size())).click();
        waitForLoadingWrapper();
    }

    @Step( "Select Cohort (url hack): ${0}" )
    public void selectCohortByYearID(String cohortYear){
        driver.get(getCurrentDomain()+"/ReportsHome?selectedCohort="+cohortYear);
        waitForLoadingWrapper();
    }

    @Step( "Select Cohort (click label): ${0}" )
    public void selectCohortByLabel(String cohortLabel){
        expandLeaversDDL();
        driver.findElement(By.linkText(cohortLabel)).click();
        waitForLoadingWrapper();
    }



    //  - COMPONENT SPECIFIC WAITS
    private List<WebElement> waitForLeaversDropDown(){
        WebDriverWait wait = new WebDriverWait(driver, MEDIUM_WAIT);
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(LEAVER_COHORT_BUTTONS,0));
    }
}

package analytics.pages.reports;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import analytics.pages.reports.components.ReportsHome_CohortsMenu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

/**
 * Represents the contents and interactive elements on the Legacy Reports Home page
 */
public class ReportsHome extends AnalyticsPage {

    // Page details
    private static final String PAGE_PATH = "/ReportsHome";

    // Page components
    protected ReportsHome_CohortsMenu cohortsMenu;

    // Selectors for the Keystage buttons
    protected static final By KS_BUTTONS = By.cssSelector(".ks>a");
    protected static final By KS3_BUTTON = By.linkText("KS3");
    protected static final By KS4_BUTTON = By.linkText("KS4");
    protected static final By KS5_BUTTON = By.linkText("KS5");
    protected static final By KS6_BUTTON = By.linkText("KS 3/4");
    protected static final By SELECTED_KS_BUTTON = By.cssSelector(".ks.selected>a");

// CONSTRUCTORS
    public ReportsHome(AnalyticsDriver aDriver){
        super(aDriver);
    }
    public ReportsHome(AnalyticsDriver aDriver, boolean loadByUrl){
        super(aDriver);
        if (loadByUrl) {
            driver.get(driver.getDomainRoot() + PAGE_PATH);
            cohortsMenu = new ReportsHome_CohortsMenu(driver);
        }
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public ReportsHome_CohortsMenu getCohortsMenu(){
        cohortsMenu = new ReportsHome_CohortsMenu(driver);
        return cohortsMenu;
    }
    public String getSelectedKeyStageID(){
        String selectedKSUrl = driver.findElement(SELECTED_KS_BUTTON).getAttribute("href");
        return String.valueOf(selectedKSUrl.charAt(selectedKSUrl.length()-1));
    }
    public String getSelectedKeyStageLabel(){
        return driver.findElement(SELECTED_KS_BUTTON).getText();
    }

    //  - CHANGING THE STATE OF THE PAGE
    public void selectRandomKeyStage(){
        List<WebElement> ksButtons = driver.findElements(KS_BUTTONS);
        ksButtons.get(new Random().nextInt(ksButtons.size())).click();
        waitForLoadingWrapper();
    }
    public void selectKeyStageByID(String keyStageID){
        driver.get(driver.getDomainRoot()+"/ReportsHome?selectedKS="+keyStageID);
        waitForLoadingWrapper();
    }
    public void selectKeyStageByLabel(String label){
        By locator;
        switch (label) {
            case "KS3":
                locator = KS3_BUTTON; break;
            case "KS4":
                locator = KS4_BUTTON; break;
            case "KS5":
                locator = KS5_BUTTON; break;
            case "KS 3/4": case "KS 6":
                locator = KS6_BUTTON; break;
            default:
                throw new IllegalArgumentException("Unknown Key Stage '" + label + "'");
        }
        driver.findElement(locator).click();
        waitForLoadingWrapper();
    }
}

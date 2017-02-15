package analytics.pages.data.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;
import com.google.common.base.Function;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents the contents and interactive elements within the Admin Select area above the side menu
 * in the DATA area
 */
public class DataAdminSelect extends AnalyticsComponent {

// FIELDS
    public final By COMPONENT = By.id("fm_KSAdminYr");

    public final By MODE_BUTTONS = By.cssSelector(".checkWrap>label.styledr");
    public final By MODE_LEGACY = By.id("mode1");
    public final By MODE_LEGACY_LABEL = By.cssSelector("[for='mode1']");
    public final By MODE_EAP = By.id("mode2");
    public final By MODE_EAP_LABEL = By.cssSelector("[for='mode2']");

    public final By SELECT_KEYSTAGE = By.id("SelectedKS");
    public final By ADMIN_YEAR_SPAN = By.cssSelector(".dataAdminSel>span:nth-of-type(4)");
    public final By SELECT_ADMIN_YEAR = By.id("SelectedAdminYr");

    private final String MODE_LEGACY_URL = "/Admin";
    private final String MODE_EAP_URL = "/EAPAdmin";

// CONSTRUCTOR
    public DataAdminSelect(AnalyticsDriver aDriver){
        super(aDriver);
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        driverWait.until(ExpectedConditions.elementToBeClickable(COMPONENT));
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public List<WebElement> getModeRadioLabels(){
        return driver.findElements(MODE_BUTTONS);
    }
    public WebElement getSelectedModeCB(String mode){
        switch (mode) {
            case "EAP":
                return driver.findElement(MODE_EAP);
            case "Legacy":
                return driver.findElement(MODE_LEGACY);
            default:
                throw new IllegalArgumentException("Mode must be 'EAP' or 'Legacy'");
        }
    }
    public WebElement getKeystageElement(){
        return driver.findElement(SELECT_KEYSTAGE);
    }
    public Select getKeystageSelect(){
        return new Select(getKeystageElement());
    }
    public WebElement getAdminYearElement(){
        return driver.findElement(SELECT_ADMIN_YEAR);
    }
    public Select getAdminYearSelect(){
        return new Select(getAdminYearElement());
    }

    //  - ACCESSORS FOR SPECIFIC INFORMATION
    public String getCurrentMode(){
        if (getSelectedModeCB("EAP").isSelected()){
            return "EAP";
        }
        if (getSelectedModeCB("Legacy").isSelected()){
            return "Legacy";
        }
        return "unknown";
    }
    public boolean checkCurrentModeIs(String expected, boolean throwIfNotExpected){
        String currentMode = getCurrentMode();
        if (!currentMode.equals(expected)){
            if (throwIfNotExpected){
                throw new IllegalStateException("Unexpected Data Mode; Expected '"+expected+"; Found '"+currentMode+"'");
            } else {
                return false;
            }
        }
        return true;
    }
    public String getCurrentLegacyKeystage(boolean throwOnNonLegacyMode){
        checkCurrentModeIs("Legacy", throwOnNonLegacyMode);
        return getKeystageSelect().getFirstSelectedOption().getText();
    }
    public String getCurrentAdminYear(){
        WebElement adminYearSpan = driver.findElement(ADMIN_YEAR_SPAN);
        if (adminYearSpan.getAttribute("class").contains("fake")){
            return "";
        }
        return getAdminYearSelect().getFirstSelectedOption().getText();
    }

    // CHANGING THE STATE OF THE CURRENT PAGE
    public DataAdminSelect selectMode(String newMode){
        hideProfiler();
        switch (newMode){
            case "EAP":
                driver.findElement(MODE_EAP_LABEL).click();
                waitMedium.until(ExpectedConditions.urlContains(MODE_EAP_URL));
                break;
            case "Legacy":
                driver.findElement(MODE_LEGACY_LABEL).click();
                waitMedium.until(ExpectedConditions.urlContains(MODE_LEGACY_URL));
                break;
            default:
                throw new IllegalArgumentException("Unknown mode requested ("+newMode+")");
        }

        waitForLoadingWrapper();

        return new DataAdminSelect(driver);
    }

    public void selectLegacyKeystage(String newKS){
        hideProfiler();
        checkCurrentModeIs("Legacy", true);
        Select adminKS = new Select(driver.findElement(SELECT_KEYSTAGE));
        adminKS.selectByVisibleText(newKS);
    }
    public void selectLegacyAdminYear(String newAdminYear){
        hideProfiler();
        if (getCurrentLegacyKeystage(false).trim().equals("")){
            throw new IllegalStateException("In Legacy mode a keystage must be selected before choosing an Admin Year");
        }
        Select adminYear = getAdminYearSelect();
        adminYear.selectByVisibleText(newAdminYear);
    }
    public DataSideMenu selectEAPAdminYearByLabel(String newAdminYear){
        hideProfiler();
        checkCurrentModeIs("EAP", true);
        Select adminYear = getAdminYearSelect();
        adminYear.selectByVisibleText(newAdminYear);

        waitForLoadingWrapper(MEDIUM_WAIT);
        return new DataSideMenu(driver);
    }
    public DataSideMenu selectEAPAdminYearByCohortNum(String newAdminYear){
        hideProfiler();
        checkCurrentModeIs("EAP", true);

        int cohortYear = Integer.valueOf(newAdminYear);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 2000;

        String cohortLabel1 = (cohortYear - 1) + "/" + cohortYear;
        String cohortLabel2 = cohortLabel1;

        if (cohortYear < currentYear){
            selectEAPAdminYearByLabel(cohortLabel1 + " (Leavers)");
        } else {
            if (cohortYear == currentYear){
                cohortLabel1 = cohortLabel1 + " (Leavers)";
                cohortLabel2 = cohortLabel2 + " (Current Yr 11)";
            } else {
                int academicYear = 11 - (cohortYear - currentYear);
                cohortLabel1 = cohortLabel1 + " (Current Yr " + (academicYear - 1) + ")";
                cohortLabel2 = cohortLabel2 + " (Current Yr " + academicYear + ")";
            }

            try {
                selectEAPAdminYearByLabel(cohortLabel1);
            } catch (Exception e) {
                selectEAPAdminYearByLabel(cohortLabel2);
            }
        }

        waitForLoadingWrapper(MEDIUM_WAIT);
        return new DataSideMenu(driver);
    }

    private void hideProfiler(){
        List<WebElement> profiler = driver.findElements(By.className("profiler-results"));
        if (profiler.size() > 0){
            driver.executeScript(
                    "document.querySelector('.profiler-results').setAttribute('style', 'display: none');");
        }
    }
}
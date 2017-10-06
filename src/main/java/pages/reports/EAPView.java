package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsPage;
import pages.reports.components.*;

import java.util.List;
import java.util.NoSuchElementException;

public class EAPView extends AnalyticsPage{

    private static final By KEY_CHARACTERISTICS_ICON = By.cssSelector(".icoEAP.act_key");
    private static final By COHORT_COUNT_BADGE = By.cssSelector(".cohCount.badge");
    private static final By NOTIFICATION_BANNER = By.cssSelector(".notif.Perm.Information");
    private static final By RESET_ALL = By.cssSelector(".resetAll");
    protected static By RESET_YES = By.cssSelector(".resetPop a");
    protected static By RESET_NO = By.cssSelector(".resetPop em");

    private static final String NO_ENTRIES_BANNER_TEXT = "There are no entries to display for this selection";
    private static final String STUDENT_DETAIL_KEY_CHARS_TITLE = "Key Characteristics are not available on the Student Detail view.";

    // Components on an EAPView Report page
    public ReportActions_NavMenu navMenu;
    public ReportActionsTab_Dataset datasetsTab;
    public ReportActionsTab_Options optionsTab;
    public ReportActionsTab_Filters filtersTab;
    public ReportActionsTab_Measures measuresTab;
    public ReportActionsTab_Residuals residualsTab;
    public ReportActions_DisplayOptions viewOptions;
    public List<ReportActions_Table> reportTables;

    // CONSTRUCTORS
    public EAPView(RemoteWebDriver aDriver){
        super(aDriver);
        if (getErrorMessage()=="") {
            navMenu = new ReportActions_NavMenu(driver);
            datasetsTab = new ReportActionsTab_Dataset(driver);
            optionsTab = new ReportActionsTab_Options(driver);
            filtersTab = new ReportActionsTab_Filters(driver);
            measuresTab = new ReportActionsTab_Measures(driver);
            residualsTab = new ReportActionsTab_Residuals(driver);
            viewOptions = new ReportActions_DisplayOptions(driver);
            reportTables = ReportActions_Table.getAllReportTables(driver);
            try {
                waitMedium.until(ExpectedConditions.elementToBeClickable(KEY_CHARACTERISTICS_ICON));
            } catch (TimeoutException e) {
                throw new IllegalStateException("Timeout waiting for Key Characteristics icon to be clickable on EAPView");
            }
        }
    }

// METHODS
    //  - CHANGING THE STATE OF THE PAGE
    public EAPView openView(String areaName, String reportName, String levelName){

        // Show the table of links to views within the given areaName
        WebElement area = navMenu.selectArea(areaName);
        navMenu.selectReport(area, reportName).
                navMenu.selectGrouping(levelName);

        waitForLoadingWrapper();
        return new EAPView(driver);
    }

    public EAPView resetAllOptions(){
        WebElement resetAll = driver.findElement(RESET_ALL);
        resetAll.click();
        waitShort.until(resetPopupDisplayed(resetAll)).click();
        return new EAPView(driver);
    }
    private ExpectedCondition<WebElement> resetPopupDisplayed(WebElement resetButton) {

        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    return resetButton.findElement(RESET_YES);
                } catch (NoSuchElementException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "The Reset All confirmation buttons are displayed";
            }
        };
    }

    //  - QUERYING THE STATE OF THE PAGE
    public int getCohortCount(){
        List<WebElement> cohortCountBadges = driver.findElements(COHORT_COUNT_BADGE);
        if (cohortCountBadges.size()==0){
            return 0;
        }
        String cohortCount = cohortCountBadges.get(0).getText();
        return Integer.valueOf(cohortCount);
    }

    public String getNotificationText(){
        List<WebElement> notifBanners = driver.findElements(NOTIFICATION_BANNER);
        if (notifBanners.size()==0) return "";
        return notifBanners.get(0).getText().trim();
    }

    public boolean isZeroEntriesReport(){
        return getNotificationText().equals(NO_ENTRIES_BANNER_TEXT);
    }

    public String getKeyCharacteristicsTitle(){
        return driver.findElement(KEY_CHARACTERISTICS_ICON).getAttribute("title");
    }

    public boolean isStudentDetailReport(){
        return getKeyCharacteristicsTitle().equals(STUDENT_DETAIL_KEY_CHARS_TITLE);
    }

    protected int findNamedTableIndex(String tableName){

        List<WebElement> tableTitleElements = driver.findElements(By.className("tableTitle"));
        if (tableTitleElements.size()==0){
            // There are no report tables visible on the page
            return -2;
        }

        if (tableName.equals("")){
            // No report table was specified, assume we want the first one
            return 1;
        }

        int tableIndex = -1;
        for (WebElement titleElement: tableTitleElements){
            String title = titleElement.getText();
            List<WebElement> subTitles = titleElement.findElements(By.className("smallInfo"));
            if (subTitles.size()>0){
                if (title.trim().equals(tableName)) {
                    tableIndex = tableTitleElements.indexOf(titleElement) + 1;
                    break;
                }
                title = title.replace(titleElement.findElement(By.className("smallInfo")).getText(),"");
            }
            if (title.trim().equals(tableName)) {
                tableIndex = tableTitleElements.indexOf(titleElement) + 1;
                break;
            }
        }

        return tableIndex;
    }

    // PRIVATE HELPER METHODS FOR THE PUBLIC METHODS
}

package pages.data.grades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import pages.data.grades.components.PublishDatasetsRow;
import pages.data.grades.components.PublishGradesModal;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the components and actions on the Publish Grades page.
*/
public class PublishGrades extends AnalyticsPage {

    public enum MainTab {
        REPORTS(1), TRACKER(2), FLIGHT_PATHS(3);

        private final String parentCss = "#page>.pageTab";
        private final int tabIndex;

        MainTab(int tabIndex){
            this.tabIndex = tabIndex;
        }

        public String getName(){
            switch (tabIndex){
                case 1:
                    return "Reports";
                case 2:
                    return "Tracker";
                case 3:
                    return "Flight Paths";
            }
            return "";
        }
        public String getTitleText(){
            switch (tabIndex){
                case 1:
                    return "Publish Individual Reports";
                case 2:
                    return "Generate Tracker";
                case 3:
                    return "Generate Flight Paths";
            }
            return "";
        }

        public By getTabSelector(){
            return By.cssSelector(parentCss+">*:nth-child("+tabIndex+")");
        }
        public By getActiveTabSelector(){
            return By.cssSelector(parentCss+">span:nth-child("+tabIndex+")");
        }
        public By getInactiveTabSelector(){
            return By.cssSelector(parentCss+">a:nth-child("+tabIndex+")");
        }
    }

    private final String PAGE_URL = "/EAPAdmin/Publish/Grades";

    private final By PUB_ADMIN_TABLE = By.cssSelector(".pubAdminTable");
    private final By PUB_ADMIN_ROWS = By.cssSelector(".pubAdminTable tr");
    private final By DATA_SETS_TAB = By.cssSelector(".pageTabContainer>.pageTab>a,.pageTabContainer>.pageTab>span");
    private final By ASSESSMENTS_YEAR_TABS = By.cssSelector(".pageTab>div>span,.pageTab>div>a");

    private final By DATASET_NAME_CELLS = By.cssSelector(".pubAdminTable tr:not(:first-child) td:nth-of-type(2)");

/* Unused, but potentially useful, locators:

    private final By DATA_SETS_TAB_SELECTED = By.cssSelector(".pageTabContainer>.pageTab>span");
    private final By DATA_SETS_TAB_UNSELECTED = By.cssSelector(".pageTabContainer>.pageTab>a");


    public final By ASSESSMENTS_YEAR_TAB_SELECTED = By.cssSelector(".pageTab>div>span");
    public final By ASSESSMENTS_YEAR_TABS_UNSELECTED = By.cssSelector(".pageTab>div>a");

*/

    /**
     * Simple constructor
     * @param aDriver   The browser should be on (or opening) the Publish Grades page
     */
    public PublishGrades(RemoteWebDriver aDriver) {
        super(aDriver);
        waitForLoadingWrapper();
    }

    public PublishGrades load(String cohort, boolean loadByUrl){
        DataSideMenu sideMenu = null;

        if(!cohort.equals("")){
            DataHome dataPage = new DataHome(driver, true);
            DataAdminSelect modeAndCohort = dataPage.getDataAdminSelect();
            modeAndCohort.selectMode("EAP");

            if (cohort.length() > 2){
                cohort = cohort.substring(cohort.length()-2);
            }
            sideMenu = modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        }
        if (loadByUrl){
            String targetUrl = getSiteBaseUrl() + PAGE_URL;
            if (driver.getCurrentUrl().equals(targetUrl))
            {
                return this;
            }
            driver.get(targetUrl);
        } else {
            if (sideMenu == null){
                sideMenu = new DataSideMenu(driver);
            }
            if (!sideMenu.isTabSelected("GRADES")){
                sideMenu.clickTab("GRADES");
            }
            sideMenu.clickMenuOption("Publish Grades");
        }
        return this;
    }

    // Getting the current state of the page
    public List<String> getDatasetNames()
    {
        List<String> names = new ArrayList<>();
        selectDatasetsTab();
        List<WebElement> dsNameCells = driver.findElements(DATASET_NAME_CELLS);
        for (WebElement dsNameCell : dsNameCells)
        {
            names.add(dsNameCell.getText().trim());
        }
        return names;
    }

    public int publishActionsAvailable()
    {
        // If the pubAdminTable is present there must be something that is publishable on this tab
        int rowsCount = driver.findElements(PUB_ADMIN_ROWS).size();
        switch (rowsCount)
        {
            case 7:
                return 1;
            case 11:
                return 2;
            case 15:
                return 3;
            default:
                return 0;
        }
    }

    // Interacting with the page
    public PublishGrades clickMainTab(MainTab tabType){
        WebElement tab = driver.findElement(tabType.getTabSelector());
        if (tab.getTagName().equals("a")){
            tab.click();
            waitForLoadingWrapper();
        }
        return this;
    }

    public PublishGrades selectDatasetsTab(){
        WebElement tab = driver.findElement(DATA_SETS_TAB);
        if (tab.getTagName().equals("a")){
            tab.click();
            waitForLoadingWrapper();
        }
        return this;
    }

    public PublishGrades selectYearTab(int yearNum){
        WebElement tab = getYearTab(yearNum);
        if (tab.getTagName().equals("a")){
            tab.click();
            waitForLoadingWrapper();
        }
        return this;
    }

    public PublishGradesModal clickPublishFor(String datasetName)
    {
        selectDatasetsTab();
        PublishDatasetsRow row = new PublishDatasetsRow(driver, datasetName);
        return row.clickPublish();
    }

    private WebElement getYearTab(int yearNum){
        String tabLabel = "Year "+yearNum;
        List<WebElement> tabs = driver.findElements(ASSESSMENTS_YEAR_TABS);
        for(WebElement tab : tabs){
            if(tab.getText().equals(tabLabel))
                return tab;
        }
        return null;
    }
}
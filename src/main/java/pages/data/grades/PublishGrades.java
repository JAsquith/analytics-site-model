package pages.data.grades;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import pages.data.grades.components.PublishAssessmentsYearRow;
import pages.data.grades.components.PublishDatasetsRow;
import pages.data.grades.components.PublishGradesModal;

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

    public final String PAGE_URL = "/EAPAdmin/Publish/Grades";

    public final By DATA_SETS_TAB = By.cssSelector(".pageTabContainer>.pageTab>a,.pageTabContainer>.pageTab>span");
    public final By ASSESSMENTS_YEAR_TABS = By.cssSelector(".pageTab>div>span,.pageTab>div>a");

/* Unused, but potentially useful, locators:

    public final By REPORTS_OR_TRACKER_TABS = By.cssSelector(".pageTab>span,.pageTab>a");
    public final By REPORTS_OR_TRACKER_TAB_SELECTED = By.cssSelector(".pageTab>span");
    public final By REPORTS_OR_TRACKER_TAB_UNSELECTED = By.cssSelector(".pageTab>a");

    private final By DATA_SETS_TAB_SELECTED = By.cssSelector(".pageTabContainer>.pageTab>span");
    private final By DATA_SETS_TAB_UNSELECTED = By.cssSelector(".pageTabContainer>.pageTab>a");

    public final By DATASET_NAME_CELLS = By.cssSelector(".pubAdminTable tr:not(:first-child) td:nth-of-type(2)");

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
            driver.get(getCurrentDomain()+PAGE_URL);
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

    // Methods to select a specified Main Tab (i.e. Reports/Trackers/Flight Paths)
    public PublishGrades clickMainTab(MainTab tabType){
        WebElement tab = driver.findElement(tabType.getTabSelector());
        if (tab.getTagName().equals("a")){
            tab.click();
            waitForLoadingWrapper();
        }
        return this;
    }

    // Methods to show the Grades Publish Modal for a given set of grades
    public PublishGradesModal clickPublishFor(String datasetName){
        selectDatasetsTab();
        PublishDatasetsRow row = new PublishDatasetsRow(driver, datasetName);
        return row.clickPublish();
    }
    public PublishGradesModal clickPublishFor(int year, int term, int slot){
        selectYearTab(year);
        PublishAssessmentsYearRow row = new PublishAssessmentsYearRow(driver, term, slot);
        return row.publish();
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
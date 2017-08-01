package pages.data.grades;

import pages.AnalyticsPage;
import pages.data.DataHome;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;
import pages.data.grades.components.PublishAssessmentsYearRow;
import pages.data.grades.components.PublishDatasetsRow;
import pages.data.grades.components.PublishGradesModal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;


/**
 * Represents the components and actions on the Publish Grades page.
*/
public class PublishGrades extends AnalyticsPage {

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
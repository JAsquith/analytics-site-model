package pages.data;

import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsPage;
import pages.data.components.DataAdminSelect;
import pages.data.components.DataSideMenu;

public class DataPage extends AnalyticsPage {

    protected final String AREA_PATH = "/EAPAdmin";

    public DataPage(RemoteWebDriver aDriver){
        super(aDriver);
    }

    public DataAdminSelect getDataAdminSelect(){
        return new DataAdminSelect(driver);
    }

    public DataSideMenu getEAPSideMenu(){
        return new DataSideMenu(driver);
    }

    protected boolean currentPageIsWithinData()
    {
        return driver.getCurrentUrl().startsWith(getSiteBaseUrl() + AREA_PATH);
    }

    protected DataSideMenu openCohortInDataArea(String cohort)
    {
        if (!currentPageIsWithinData())
        {
            new DataHome(driver, true);
        }
        DataAdminSelect modeAndCohort = this.getDataAdminSelect().selectMode("EAP");
        if (cohort.length() > 2)
        {
            cohort = cohort.substring(cohort.length() - 2);
        }
        if (!modeAndCohort.getCurrentAdminYear().equals(cohort))
        {
            return modeAndCohort.selectEAPAdminYearByCohortNum(cohort);
        } else
        {
            return new DataSideMenu(driver);
        }
    }

}

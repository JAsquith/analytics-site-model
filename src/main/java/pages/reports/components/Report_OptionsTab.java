package pages.reports.components;

import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;

public class Report_OptionsTab extends AnalyticsComponent {

    protected String tabName;
    protected Report_Tabs allTabs;

    public Report_OptionsTab(RemoteWebDriver aDriver){
        super(aDriver);
        allTabs = new Report_Tabs(driver);
    }

}

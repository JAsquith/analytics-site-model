package pages.reports.components;

import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;
import pages.reports.EAPView;

public class ReportTab extends AnalyticsComponent {

    protected String tabName;
    protected ReportTabs_Other allTabs;

    public ReportTab(RemoteWebDriver aDriver){
        super(aDriver);
        allTabs = new ReportTabs_Other(driver);
    }

    protected EAPView selectMe(){
        return allTabs.selectTab(tabName);
    }

    protected void expandMe(){
        allTabs.expand(tabName);
    }

    protected void collapseMe(){
        allTabs.collapse(tabName);
    }
}

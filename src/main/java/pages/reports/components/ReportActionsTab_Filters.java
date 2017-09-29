package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.List;

public class ReportActionsTab_Filters extends ReportActionsTab implements IReportActionGroup{

    private static final String TAB_NAME = "filter";
    private static final String TAB_CLASS = "filters";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Filters(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }


    @Override
    public List<ReportAction> getValidActionsList() {
        return null;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        return null;
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        return null;
    }
}

package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import pages.AnalyticsComponent;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActions_Table extends AnalyticsComponent implements IReportActionGroup{

    private static final By ALL_REPORT_TABLES = By.cssSelector("table.rpt");
    private RemoteWebElement table;

    public static List<RemoteWebElement> getAllReportTables(RemoteWebDriver driver){
        List<RemoteWebElement> allTables = new ArrayList<RemoteWebElement>();

        for(WebElement table:driver.findElements(ALL_REPORT_TABLES)){
            allTables.add((RemoteWebElement)table);
        }

        return allTables;
    }

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActions_Table(RemoteWebDriver aDriver, RemoteWebElement table){
        super((RemoteWebDriver)table.getWrappedDriver());
        this.table = table;
    }

    /* Actions available within this component
    * ToDo: Javadoc */

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    @Override
    public boolean isEnabled(){
        return false;
    }

    @Override
    public List<ReportAction> getValidActionsList() {
        if(isEnabled()){
            List<ReportAction> actions = new ArrayList<ReportAction>();
            actions.add(ReportAction.DRILL_DOWN);
            return actions;
        }
        return null;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {

        List<String> options = new ArrayList<String>();

        return options;
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        return null;
    }

    @Override
    public String getName() {
        return "methodsTab";
    }

    /*Actions/state queries used within more than one public method */

    /* Expected conditions specific to this component */
}

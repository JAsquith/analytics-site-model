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

    private static final By ALL_REPORT_TABLES = By.cssSelector("table.rpt:not(#fakeTable)");
    private static final By ALL_TABLE_TITLES = By.cssSelector(".tableTitle");
    private RemoteWebElement table;
    private WebElement tableTitle;
    private List<WebElement> tableLinks;

    public static List<ReportActions_Table> getAllReportTables(RemoteWebDriver driver){
        List<ReportActions_Table> allTables = new ArrayList<ReportActions_Table>();
        List<WebElement> tableTitles = driver.findElements(ALL_TABLE_TITLES);
        int index = 0;
        for(WebElement table:driver.findElements(ALL_REPORT_TABLES)){
            allTables.add(new ReportActions_Table((RemoteWebElement)table, tableTitles.get(index)));
            index++;
        }

        return allTables;
    }

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActions_Table(RemoteWebElement table, WebElement title){
        super((RemoteWebDriver)table.getWrappedDriver());
        this.table = table;
        this.tableTitle = title;
    }

    /* Actions available within this component
    * ToDo: Javadoc */

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    @Override
    public boolean isEnabled(){
        this.tableLinks = getTableLinks();
        return tableLinks.size()>0;
    }

    private List<WebElement> getTableLinks() {
        if (tableLinks==null) {
            return table.findElements(By.tagName("a"));
        }
        return tableLinks;
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
        for (WebElement link:getTableLinks()){
            options.add("Link "+tableLinks.indexOf(link)+" ["+link.getText()+"]");
        }
        return options;
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        String linkIndex = option.substring(6,option.indexOf("[")-1);
        tableLinks.get(Integer.valueOf(linkIndex)).click();
        return new EAPView(driver);
    }

    @Override
    public String getName() {
        return "Report Table ("+tableTitle.getText().trim()+")";
    }

    /*Actions/state queries used within more than one public method */

    /* Expected conditions specific to this component */
}

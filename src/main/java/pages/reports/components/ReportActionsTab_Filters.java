package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;

import java.util.ArrayList;
import java.util.List;

public class ReportActionsTab_Filters extends ReportActionsTab implements IReportActionGroup{

    private static final String TAB_NAME = "filter";
    private static final String TAB_CLASS = "filters";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");
    private static final By ADD_FILTER_BUTTON = By.cssSelector("."+TAB_CLASS+" .button.noIcon");

    private WebElement addFilterButton;

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Filters(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    public ReportViewModal_Filters openModal(){
        getAddFilterButton().click();
        return new ReportViewModal_Filters(driver);
    }

    /* Actions available within this component
    * ToDo: Javadoc */

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();
        actions.add(ReportAction.TOGGLE_FILTER);
        return actions;
    }

    public List<String> getOptionsForAction(ReportAction action) {
        selectAndExpandTab();

        // Todo: assign the return value of openModel to the IReportModal interface (once the class implements it)
        openModal();

        List<String> options = new ArrayList<String>();

        return options;
    }

    public EAPView applyActionOption(ReportAction action, String option) {
        return null;
    }

    /*Actions/state queries used within more than one public method */
    WebElement getAddFilterButton(){
        try{
            addFilterButton.isDisplayed();
        }
        catch (StaleElementReferenceException | NullPointerException e){
            addFilterButton = driver.findElement(ADD_FILTER_BUTTON);
        }
        return addFilterButton;
    }

    /* Expected conditions specific to this component */
}

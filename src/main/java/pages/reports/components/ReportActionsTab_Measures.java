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

public class ReportActionsTab_Measures extends ReportActionsTab implements IReportActionGroup{

    private static final String TAB_NAME = "measure";
    private static final String TAB_CLASS = "measures";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");
    private static final By ADD_MEASURE_BUTTON = By.cssSelector("."+TAB_CLASS+" .button.noIcon");

    private WebElement addMeasureButton;

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Measures(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    /* Actions available within this component
    * ToDo: Javadoc */
    public ReportViewModal_Measures openModal(){
        getAddMeasureButton().click();
        return new ReportViewModal_Measures(driver);
    }

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();
        actions.add(ReportAction.TOGGLE_MEASURE);
        return actions;
    }

    public List<String> getOptionsForAction(ReportAction action) {
        List<String> options = new ArrayList<String>();

        // Todo: make all ReportViewModal_xxx classes implement IReportModal & change from using the Class to the Interface
        openModal();

        return options;
    }

    public EAPView applyActionOption(ReportAction action, String option) {
        return null;
    }

    /*Actions/state queries used within more than one public method */
    WebElement getAddMeasureButton(){
        try{
            addMeasureButton.isDisplayed();
        }
        catch (StaleElementReferenceException | NullPointerException e){
            addMeasureButton =  driver.findElement(ADD_MEASURE_BUTTON);
        }
        return addMeasureButton;
    }

    /* Expected conditions specific to this component */
}

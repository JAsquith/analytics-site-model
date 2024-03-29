package pages.reports.components;

import enums.ReportAction;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.reports.EAPView;
import pages.reports.interfaces.IReportActionGroup;
import pages.reports.interfaces.IReportModal;

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
        selectTab();
        getAddFilterButton().click();
        return new ReportViewModal_Filters(driver);
    }

    /* Actions available within this component
    * ToDo: Javadoc */

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    @Override
    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();
        actions.add(ReportAction.TOGGLE_FILTER);

        // Reset option
        if (getTabButton().findElements(RESET_ICON).size()>0){
            actions.add(ReportAction.RESET_FILTERS);
        }

        return actions;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        List<String> options = new ArrayList<String>();

        switch (action){
            case TOGGLE_FILTER:
                selectAndExpandTab();
                IReportModal modal = openModal();

                for (String optionGroup : modal.getGroupsList()){
                    List<String> optionGroupValues = modal.getValuesForGroup(optionGroup);
                    for(String value : optionGroupValues){
                        options.add(optionGroup+"["+value+"]");
                    }
                }
                break;
            case RESET_FILTERS:
                options.add("Reset");
                break;
            default:
                throw new IllegalArgumentException(action.toString()+" is not a valid ReportAction for the Filters tab");
        }
        return options;
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        switch (action){
            case TOGGLE_FILTER:
                IReportModal modal;
                if (ReportViewModal.isModalOpen(driver))
                    modal = new ReportViewModal_Filters(driver);
                else
                    modal = openModal();

                String[] splitOption = option.split("\\[");
                String modalLabel = splitOption[0];
                String modalOption = splitOption[1].substring(0,splitOption[1].length()-1);
                modal.toggleOption(modalLabel, modalOption);
                return modal.applyChanges();

            case RESET_FILTERS:
                this.resetTab();
                return new EAPView(driver);

            default:
                throw new IllegalArgumentException("Unexpected ReportAction ("+action+") on Filters tab");
        }
    }

    @Override
    public String getName() {
        return "filtersTab";
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

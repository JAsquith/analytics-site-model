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

public class ReportActionsTab_Residuals extends ReportActionsTab implements IReportActionGroup{

    private static final String TAB_NAME = "residual";
    private static final String TAB_CLASS = "residuals";

    private static final By TAB_BUTTON = By.cssSelector(".tabbutton[data-tab='"+ TAB_NAME +"']");
    private static final By CONTENTS_DIV = By.cssSelector("."+TAB_CLASS+".pan");
    private static final By ADD_EXCLUSION_BUTTON = By.cssSelector("."+TAB_CLASS+" .button.noIcon");

    private WebElement addExclusionButton;

    /* Constructor method
    * ToDo: Javadoc */
    public ReportActionsTab_Residuals(RemoteWebDriver aDriver){
        super(aDriver);
        tabName = TAB_NAME;
        tabClass = TAB_CLASS;
        tabButtonBy = TAB_BUTTON;
        tabContentsBy = CONTENTS_DIV;
    }

    /* Actions available within this component
    * ToDo: Javadoc */
    public ReportViewModal_Residuals openModal(){
        selectTab();
        getAddExclusionButton().click();
        return new ReportViewModal_Residuals(driver);
    }

    /* These component actions implement the IReportActionGroup interface
    * ToDo: Javadoc */
    @Override
    public List<ReportAction> getValidActionsList() {
        List<ReportAction> actions = new ArrayList<ReportAction>();
        actions.add(ReportAction.TOGGLE_EXCLUSION);
        return actions;
    }

    @Override
    public List<String> getOptionsForAction(ReportAction action) {
        selectAndExpandTab();

        List<String> options = new ArrayList<String>();

        IReportModal modal = openModal();

        for (String optionGroup : modal.getGroupsList()){
            List<String> optionGroupValues = modal.getValuesForGroup(optionGroup);
            for(String value : optionGroupValues){
                options.add(optionGroup+"["+value+"]");
            }
        }

        return options;
    }

    @Override
    public EAPView applyActionOption(ReportAction action, String option) {
        if(action == ReportAction.TOGGLE_EXCLUSION){
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
        } else {
            throw new IllegalArgumentException("Unexpected ReportAction ("+action+") on Residuals tab");
        }
    }

    @Override
    public String getName() {
        return "residualsTab";
    }

    /*Actions/state queries used within more than one public method */
    WebElement getAddExclusionButton(){
        try{
            addExclusionButton.isDisplayed();
        }
        catch (StaleElementReferenceException | NullPointerException e){
            addExclusionButton = driver.findElement(ADD_EXCLUSION_BUTTON);
        }
        return addExclusionButton;
    }

    /* Expected conditions specific to this component */
}

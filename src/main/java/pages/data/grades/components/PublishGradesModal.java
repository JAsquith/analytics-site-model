package pages.data.grades.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AnalyticsComponent;
import pages.data.grades.PublishGrades;

/**
 * A component object representing the content and actions within a Publish Grades modal
 */
public class PublishGradesModal extends AnalyticsComponent{

    private final By MODAL_WINDOW = By.className("window");

    private final By PUB_COLL_SELECT = By.id("CurrentPublishItem_Coll_ID");
    private final By WITH_KS4_LOGIC_RADIO = By.id("chk_ks4_logic");
    private final By WITH_KS4_LOGIC_LABEL = By.cssSelector("label[for='chk_ks4_logic']");
    private final By WITHOUT_KS4_LOGIC_RADIO = By.id("chk_ks4_no_logic");
    private final By WITHOUT_KS4_LOGIC_LABEL = By.cssSelector("label[for='chk_ks4_no_logic']");
    private final By LOGIC_SELECT = By.id("logicDDL");
    private final By A8_ESTIMATES_SELECT = By.id("a8EstimatesDDL");
    private final By REPORT_NOTE_FIELD = By.id("CurrentPublishItem_PublishedReport_Note");
    private final By REPORT_STATUS_SELECT = By.id("CurrentPublishItem_Stat_ID");
    private final By CANCEL_BUTTON = By.cssSelector(".cancelChanges.modalClose");
    private final By PUBLISH_BUTTON = By.id("smtBtQue");
    private final By LOCAL_PUBLISH_BUTTON = By.id("smtBt");
    private final By CLOSE_PUB_PROG_MODAL_BUTTON = By.cssSelector(".pubFinished");

    /**
     * Creates a Component object for the Publish Grades modal and waits for the modal window to be clickable.
     * @param aDriver   Browser should be on the Publish Grades page and
     *                  a Publish button should have been clicked
     */
    public PublishGradesModal(RemoteWebDriver aDriver) {
        super(aDriver);
        initModal(MODAL_WINDOW);
    }

    public PublishGradesModal selectCollection(String optionText){
        new Select(modal.findElement(PUB_COLL_SELECT)).selectByVisibleText(optionText);
        return this;
    }

    public PublishGradesModal selectKS4RulesType(String ks4RulesType){
        switch (ks4RulesType.toLowerCase()){
            case "with":
                modal.findElement(WITH_KS4_LOGIC_LABEL).click();
                break;
            case "without":
                modal.findElement(WITHOUT_KS4_LOGIC_LABEL).click();
                break;
            default:
                //IllegalArgumentException??
        }
        return this;
    }

    public PublishGradesModal selectLogicYear(String optionText){
        new Select(modal.findElement(LOGIC_SELECT)).selectByVisibleText(optionText);
        return this;
    }

    public PublishGradesModal selectA8Estimates(String optionText){
        new Select(modal.findElement(A8_ESTIMATES_SELECT)).selectByVisibleText(optionText);
        return this;
    }

    public PublishGradesModal setReportNote(String text){
        WebElement field = modal.findElement(REPORT_NOTE_FIELD);
        field.clear();
        field.sendKeys(text);
        return this;
    }

    public PublishGradesModal selectReportStatus(String optionText){
        new Select(modal.findElement(REPORT_STATUS_SELECT)).selectByVisibleText(optionText);
        return this;
    }

    public void clickCancel(){
        modal.findElement(CANCEL_BUTTON).click();
    }
    public PublishGrades clickPublish(){
        return clickPublish(0);
    }
    public PublishGrades clickLocal(){
        return clickPublish(1);
    }

    public PublishGrades clickPublish(int publishTypeID){
        // Uses a switch to future-proof for other publishing methods
        switch (publishTypeID){
            case 0:
                driver.findElement(PUBLISH_BUTTON).click();
                waitForLoadingWrapper(LONG_WAIT);
                WebDriverWait wait = new WebDriverWait(driver, PUBLISH_WAIT);
                wait.until(ExpectedConditions.elementToBeClickable(CLOSE_PUB_PROG_MODAL_BUTTON)).click();
                return new PublishGrades(driver).loaded();
            case 1:
                driver.findElement(LOCAL_PUBLISH_BUTTON).click();
                waitForLoadingWrapper(PUBLISH_WAIT);
                return new PublishGrades(driver).loaded();
            default:
                throw new IllegalArgumentException("publishTypeID ("+publishTypeID+") must be 0 or 1");
        }
    }

}

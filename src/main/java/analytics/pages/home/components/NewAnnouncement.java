package analytics.pages.home.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Represents the contents and interactive elements of a new Announcement on the Analytics Home Page
 */
public class NewAnnouncement extends AnalyticsComponent {

// FIELDS
    WebElement newMessageForm;
    WebElement title = null;
    WebElement msgDetail = null;
    List<WebElement> bbIcons = null;

    protected static final By COMPONENT = By.className("message_new");

    protected static final By MSG_TITLE = By.id("SchMsgCreate_SchMsg_Title");

    protected static final By MSG_DETAIL = By.id("newmsg");
    protected static final By MSG_CANCEL = By.cssSelector("#fm_crtMsg>.new_footer>span");
    protected static final By MSG_SUBMIT = By.cssSelector("#fm_crtMsg>.new_footer>button");
    protected static final By MSG_VAL_MSGS = By.cssSelector("#fm_crtMsg>.val_sum>.field-validation-error");

    protected static final By MSG_BB_ICONS = By.cssSelector(".bbedit-toolbar>span");
    protected static final By MSG_BBHELP = By.className("BBHelpWindow");
    protected static final By MSG_BBHELP_CLOSE = By.cssSelector(".BBHelpWindow>.bbclose");

// CONSTRUCTOR
    public NewAnnouncement(AnalyticsDriver aDriver){
        super(aDriver);
        WebDriverWait driverWait = new WebDriverWait(driver, SHORT_WAIT);
        driverWait.until(ExpectedConditions.elementToBeClickable(COMPONENT));
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public List<WebElement> getValidationErrors(){
    return newMessageForm.findElements(MSG_VAL_MSGS);
}
    public List<WebElement> getAllBBIcons(){
        if (bbIcons==null)
            bbIcons = newMessageForm.findElements(MSG_BB_ICONS);
        return bbIcons;
    }
    public WebElement getBBIcon(String title){
        getAllBBIcons();
        for (int i = 0; i< bbIcons.size(); i++){
            if (bbIcons.get(i).getAttribute("title").equals(title))
                return bbIcons.get(i);
        }
        throw new IllegalArgumentException("No BB Icons with a title of '" + title + "' was found.");
    }
    public WebElement showBBHelp(){
        getBBIcon("help").click();
        return new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(MSG_BBHELP));
    }
    public void closeBBHelp(){
        if (newMessageForm.findElements(MSG_BBHELP).size() == 0)
            return;
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(MSG_BBHELP_CLOSE)).click();
    }

    // CHANGING THE STATE OF THE CURRENT PAGE
    public void setTitle(String newTitle){
        if (title == null)
            title = newMessageForm.findElement(MSG_TITLE);
        title.clear();
        title.sendKeys(newTitle);
    }
    public void appendTitle(String newTitle){
        if (title == null)
            title = newMessageForm.findElement(MSG_TITLE);
        title.sendKeys(newTitle);
    }
    public void setMessageDetail(String newDetail){
        if (msgDetail == null)
            msgDetail = newMessageForm.findElement(MSG_DETAIL);
        msgDetail.clear();
        msgDetail.sendKeys(newDetail);
    }
    public void appendMessageDetail(String newDetail){
        if (msgDetail == null)
            msgDetail = newMessageForm.findElement(MSG_DETAIL);
        msgDetail.sendKeys(newDetail);
    }
    public void cancelNewMessage(){
        newMessageForm.findElement(MSG_CANCEL).click();
    }
    public void submitNewMessage(){
        newMessageForm.findElement(MSG_SUBMIT).click();
    }

}

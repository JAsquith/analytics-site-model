package analytics.pages.home;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import analytics.pages.home.components.Announcement;
import analytics.pages.home.components.NewAnnouncement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


/**
 * Extends the AnalyticsPage class to model components and elements on the Analytics Home Page
 */
public class HomePage extends AnalyticsPage {

// FIELDS
    private static final String PAGE_PATH = "/";

    // Selectors for the message groups
    protected static final By SCHOOL_MESSAGES_GROUP = By.cssSelector(".sis_msg:nth-of-type(2)");
    protected static final By SISRA_MSG_BLOCK = By.cssSelector(".sis_msg:nth-of-type(1)");

    // Selectors directly to individual elements within the School messages group
    protected static final By CREATE_SCHOOL_MSG_BUTTON = By.cssSelector(".sis_msg:nth-of-type(2) .button.newbtn");
    protected static final By NEW_SCHOOL_MSG_FORM = By.id("fm_crtMsg");

    // Revision History Selectors
    protected static final By REV_HISTORY = By.cssSelector(".relHist");
    protected static final By REV_HISTORY_HEADER_ROW = By.cssSelector(".relHist tr:first-child");
    protected static final By REV_HISTORY_DETAIL_ROWS = By.cssSelector(".relHist tr:not(:first-child)");

    // Sub-elements common to Message and Revision History blocks
    protected static final By GROUP_HEADING = By.className("title");
    protected static final By GROUP_SUB_HEADING = By.className("co_name");
    protected static final By GROUP_VIEW_MORE = By.linkText("View More");

// CONSTRUCTOR
    public HomePage(AnalyticsDriver aDriver, boolean loadByUrl){
        super(aDriver);
        if (loadByUrl)
            driver.get(driver.getDomainRoot()+PAGE_PATH);
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    protected WebElement getMessagesGroup(String groupType){
        switch (groupType){
            case "SISRA":
                return driver.findElement(SISRA_MSG_BLOCK);
            case "School":
                return driver.findElement(SCHOOL_MESSAGES_GROUP);
            case "History":
                return driver.findElement(REV_HISTORY);
            default:
                throw new IllegalArgumentException
                        ("Unexpected group type (expected SISRA, School or History): " + groupType);
        }
    }
    protected WebElement getSchoolMsgsGroup(){
        return driver.findElement(SCHOOL_MESSAGES_GROUP);
    }
    protected WebElement getSISRAMsgsGroup(){
        return driver.findElement(SISRA_MSG_BLOCK);
    }
    public WebElement getMsgsGroup(String type){
        switch (type){
            case "SISRA": return getSISRAMsgsGroup();
            case "School": return getSchoolMsgsGroup();
            default: throw new IllegalArgumentException("Unknown Announcement Type: " + type);
        }
    }
    public Announcement getNthSchoolAnnouncement(int index){
        return new Announcement(driver, getMsgsGroup("School"), index);
    }
    public Announcement getNthSISRAAnnouncement(int index){
        return new Announcement(driver, getMsgsGroup("SISRA"), index);
    }
    public Announcement getNthAnnouncement(String type, int index){
        switch (type){
            case "SISRA": return getNthSISRAAnnouncement(index);
            case "School": return getNthSchoolAnnouncement(index);
            default: throw new IllegalArgumentException("Unknown Announcement Type: " + type);
        }
    }

    //  - ACCESSORS FOR SPECIFIC INFORMATION
    public String getMessageGroupHeading(String groupType){
        WebElement block = getMessagesGroup(groupType);
        return block.findElement(GROUP_HEADING).getText();
    }
    public String getMessageGroupSubHeading(String groupType){
        WebElement block = getMessagesGroup(groupType);
        return block.findElement(GROUP_SUB_HEADING).getText();
    }

    // CHANGING THE STATE OF THE CURRENT PAGE
    public void clickMessageGroupViewMore(String groupType){
        WebElement block = getMessagesGroup(groupType);
        block.findElement(GROUP_VIEW_MORE).click();
    }
    public NewAnnouncement clickNewAnnouncemennt(){
        driver.findElement(CREATE_SCHOOL_MSG_BUTTON).click();
        return new NewAnnouncement(driver);
    }

}

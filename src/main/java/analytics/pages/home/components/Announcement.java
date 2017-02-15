package analytics.pages.home.components;


import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents the contents and interactive elements of a given Announcement on the Analytics Home Page
 */
public class Announcement extends AnalyticsComponent{
    private WebElement msgGroup;
    private int msgIndex;
    private WebElement title = null;
    private WebElement message = null;
    private WebElement titleBlock = null;
    private WebElement deleteIcon = null;

    // Selectors for elements of the announcement - Applicable to School and SISRA announcements
    protected static final By GROUP_MSG_TITLE_BLOCKS = By.cssSelector(".post_title");
    protected static final By GROUP_MSG_TITLES = By.cssSelector(".post_title>.ttl");
    protected static final By GROUP_MSG_DETAIL = By.cssSelector(".post");

    // Selectors specific to School announcements
    protected static final By MSG_DELETE_ICONS = By.cssSelector(".post_title a");

    public Announcement(AnalyticsDriver aDriver, WebElement group, int index){
        super(aDriver);
        msgGroup = group;
        msgIndex = index;
    }

    public String getTitle(){
        if (title == null)
            title = msgGroup.findElements(GROUP_MSG_TITLES).get(msgIndex);
        return title.getText();
    }

    public String getMessage(){
        if (message == null)
            message = msgGroup.findElements(GROUP_MSG_DETAIL).get(msgIndex);
        return message.getText();
    }

    public boolean isNew(){
        if (titleBlock == null) {
            titleBlock = msgGroup.findElements(GROUP_MSG_TITLE_BLOCKS).get(msgIndex);
        }
        return titleBlock.getAttribute("class").contains("Active");
    }

    public void clickDelete(){
        if (deleteIcon == null) {
            deleteIcon = msgGroup.findElements(MSG_DELETE_ICONS).get(msgIndex);
        }
    }
}

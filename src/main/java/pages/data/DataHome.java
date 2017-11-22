package pages.data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.data.components.DataAdminSelect;

/**
 * Extends the AnalyticsPage class to model components and elements on the Data Home Page
 */
public class DataHome extends DataPage {

//FIELDS
    private final String PAGE_PATH = "/EAPAdmin";
    private final By SISRA_MSG_BLOCK = By.cssSelector(".sis_msg:nth-of-type(1)");
    private final By MESSAGES_TITLE = By.className("title");
    private final By MESSAGES_SUB_TITLE = By.className("co_name");
    private final By MESSAGES_VIEW_MORE = By.linkText("View More");

// CONSTRUCTORS
    public DataHome(RemoteWebDriver aDriver){
        super(aDriver);
    }
    public DataHome(RemoteWebDriver aDriver, boolean loadByUrl){
        super(aDriver);
        waitForLoadingWrapper();
        if (loadByUrl) {
            openByUrl();
        }
        if (new DataAdminSelect(driver).getCurrentAdminYear().equals(""))
        {
            waitMedium.until(ExpectedConditions.or(
                    ExpectedConditions.textToBe(PAGE_TITLE, "Data"),
                    ExpectedConditions.textToBe(PAGE_TITLE, "Grades")
            ));
        }
        this.waitForLoadingWrapper();
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    protected WebElement getSISRAMsgsGroup(){
    return driver.findElement(SISRA_MSG_BLOCK);
}

    //  - ACCESSORS FOR SPECIFIC INFORMATION
    public String getMessageGroupHeading(String groupType){
        WebElement block = getSISRAMsgsGroup();
        return block.findElement(MESSAGES_TITLE).getText();
    }
    public String getMessageGroupSubHeading(String groupType){
        WebElement block = getSISRAMsgsGroup();
        return block.findElement(MESSAGES_SUB_TITLE).getText();
    }

    // ACTIONS TO AFFECT THE STATE
    public void clickMessageGroupViewMore(String groupType){
        WebElement block = getSISRAMsgsGroup();
        block.findElement(MESSAGES_VIEW_MORE).click();
    }

    private void openByUrl(){
        boolean pageAlreadyOpen = driver.getCurrentUrl().
                startsWith(getSiteBaseUrl()+PAGE_PATH);
        if (!pageAlreadyOpen){
            driver.get(getSiteBaseUrl() + PAGE_PATH);
            waitForLoadingWrapper();
        }
    }

}

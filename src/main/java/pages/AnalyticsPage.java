package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.account.LoginPage;
import pages.components.AuthorityDetailsModal;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * Page Object class modelling components and elements common to all pages when logged in to Analytics<br>
 * Every page should have the following components (and sub components):<br>
 * &nbsp; Header;<br>
 * &nbsp; &nbsp; Logo;<br>
 * &nbsp; &nbsp; MainMenu (with a minimum of Home, Report &amp; Log out buttons);<br>
 * &nbsp; Help Centre button (** SPECIAL CASE ** Not visible on Reports Home Page!);<br>
 * &nbsp; &nbsp; Videos &amp; Guides Link;<br>
 * &nbsp; &nbsp; Email Support Link;<br>
 * &nbsp; &nbsp; Live Chat Link;<br>
 * &nbsp; &nbsp; Hot Topics list;<br>
 * &nbsp; Page;<br>
 * &nbsp; &nbsp; Page Header/Title;<br>
 * &nbsp; &nbsp; User &amp; School Info;<br>
 * &nbsp; &nbsp; UserAccountDDL (with links to change username, password, etc);<br>
 * &nbsp; Footer (text containging Version &amp; Build info);<br>
 * &nbsp; &nbsp; Legal notices links;<br>
 * &nbsp; &nbsp; Social media links;<br>
 * <br>
 * Loading overlay (normally invisible);<br>
 * Modal overlay (normally invisible);<br>
 */
public class AnalyticsPage extends AbstractAnalyticsObject {

// FIELDS
    public final By LOGO = By.cssSelector("#logo>img");

    public final By MAIN_MENU_BUTTONS = By.cssSelector("#menuMain li");
    public final By MAIN_MENU_LINKS = By.cssSelector("#tabsLeft li a");
    public final By MAIN_MENU_HOME = By.xpath("//img[@alt='Home']/../../..");
    public final By MAIN_MENU_DATA = By.xpath("//img[@alt='Data']/../../..");
    public final By MAIN_MENU_CONFIG = By.xpath("//img[@alt='Config.']/../../..");
    public final By MAIN_MENU_USERS = By.xpath("//img[@alt='Users']/../../..");
    public final By MAIN_MENU_REPORTS = By.xpath("//img[@alt='Reports']/../../..");
    public final By MAIN_MENU_LOGOUT = By.cssSelector(".logOutTab");

    public final By HELP_CENTRE_BUTTON = By.cssSelector("#menuSide .helpCentre");
    public final By HELP_CENTRE_FLYOUT = By.id("supportFlyOut");
    public final By HELP_VIDEOS_AND_GUIDES = By.cssSelector("#supportFlyOut>.hlp_Guides");
    public final By HELP_EMAIL = By.cssSelector("#supportFlyOut>.hlp_Email");
    public final By HELP_CHAT = By.cssSelector("#supportFlyOut>.lz_cbl");
    public final By HELP_HOT_TOPICS = By.cssSelector("#supportFlyOut>.hlp_Admin");
    public final By HELP_HOT_TOPICS_ITEMS = By.cssSelector("#supportFlyOut>.hlp_Admin>h4");

    public final By NOTIF_BANNER_SUCCESS = By.cssSelector(".notif.Temp.Success");

    public final By ACC_INFO = By.id("nameSchool");
    public final By ACC_UPDATE_DDL = By.id("UserAccountDDL");
    public final By ACC_CHANGE_USERNAME = By.linkText("Change Username");
    public final By ACC_CHANGE_PASSWORD = By.linkText("Change Password");
    public final By ACC_CHANGE_SETTINGS = By.linkText("Change Settings");
    public final By ACC_VIEW_AUTH = By.linkText("View Authority Details");

    public final By SORRY_ERROR_MESSAGE = By.cssSelector("#page>h2");

    public final By FOOTER = By.id("footer");

    public final By FOOTER_NOTICE_PRIVACY = By.linkText("Privacy Policy");
    public final By FOOTER_NOTICE_DATA_PROTECTION = By.linkText("Data Protection");
    public final By FOOTER_NOTICE_COOKIES = By.linkText("Cookies");

    public final By FOOTER_SM_LINK_TWITTER = By.cssSelector("#footer img[title='Twitter']");
    public final By FOOTER_SM_LINK_FACEBOOK = By.cssSelector("#footer img[title='Facebook']");

    public final By OVERLAY_LOADING_WRAPPER = By.id("loadingWrapper");
    public final By UNDERLAY_MODAL = By.cssSelector("#page .modal");
    public final By UNDERLAY_NON_MODAL = By.id("invisiModal");

// CONSTRUCTOR
    public AnalyticsPage(RemoteWebDriver aDriver){
        driver = aDriver;
        waitTiny = new WebDriverWait(driver, TINY_WAIT);
        waitShort = new WebDriverWait(driver, SHORT_WAIT);
        waitMedium = new WebDriverWait(driver, MEDIUM_WAIT);
        waitLong = new WebDriverWait(driver, LONG_WAIT);
        waitForLoadingWrapper(LONG_WAIT);
    }

// METHODS
    // QUERYING THE CURRENT PAGE STATE
    //  - ACCESSORS FOR ELEMENTS/COMPONENTS
    public WebElement getAnalyticsLogo(){
    return driver.findElement(LOGO);
}
    public List<WebElement> getMenuOptions(){
        return driver.findElements(MAIN_MENU_BUTTONS);
    }
    public List<WebElement> getMenuOptionLinks(){
        return driver.findElements(MAIN_MENU_LINKS);
    }
    public WebElement getHelpCentreFlyout(){
        if (!driver.findElement(HELP_CENTRE_FLYOUT).isDisplayed())
            clickHelpCentre();
        return new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(HELP_CENTRE_FLYOUT));
    }
    public List<WebElement> getHelpHotTopics(){
        if (!driver.findElement(HELP_HOT_TOPICS).isDisplayed())
            clickHelpCentre();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(HELP_HOT_TOPICS));
        return driver.findElements(HELP_HOT_TOPICS_ITEMS);
    }
    public WebElement getFooterPrivacyLink(){
        return driver.findElement(FOOTER_NOTICE_PRIVACY);
    }
    public WebElement getFooterDataProtectionLink(){
        return driver.findElement(FOOTER_NOTICE_DATA_PROTECTION);
    }
    public WebElement getFooterCookiesLink(){
        return driver.findElement(FOOTER_NOTICE_COOKIES);
    }
    public WebElement getFooterTwitterLink(){
        return driver.findElement(FOOTER_SM_LINK_TWITTER);
    }
    public WebElement getFooterFacebookLink(){
        return driver.findElement(FOOTER_SM_LINK_FACEBOOK);
    }

    //  - ACCESSORS FOR SPECIFIC INFORMATION
    public boolean isLoading(){
        return driver.findElement(OVERLAY_LOADING_WRAPPER).isDisplayed();
    }
    public boolean isUnderlayModalActive(){
        return driver.findElement(UNDERLAY_MODAL).isDisplayed();
    }
    public boolean isUnderlayNonModalActive(){
        return driver.findElement(UNDERLAY_NON_MODAL).isDisplayed();
    }
    public String getErrorMessage(){
        List<WebElement> pageErrors = driver.findElements(SORRY_ERROR_MESSAGE);
        if (pageErrors.size()>0){
            return pageErrors.get(0).getText().trim();
        }
        return "";
    }
    public String getPageTitleText(){
        return driver.findElement(PAGE_TITLE).getText();
    }
    public String getUserAndSchoolInfo(){return driver.findElement(ACC_INFO).getText();}
    public String getFullFooterText(){
        return driver.findElement(FOOTER).getText();
    }

    // CHANGING THE STATE OF THE CURRENT PAGE
    public void removeUnderlayForNonModals(){
        WebElement overlay = driver.findElement(UNDERLAY_NON_MODAL);
        if (overlay.isDisplayed())
            new Actions(driver).moveToElement(overlay, 100, 20).click().build().perform();
    }
    public void clickHelpCentre(){
        driver.findElement(HELP_CENTRE_BUTTON).click();
    }
    public void clickAccountInfoDDL(){
        driver.findElement(ACC_INFO).click();
        waitShort.until(accountDropDownDisplayed());
    }
    public void clickAccChangeUsername(){
        if (!driver.findElement(ACC_CHANGE_USERNAME).isDisplayed())
            clickAccountInfoDDL();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(ACC_CHANGE_USERNAME)).click();
    }
    public void clickAccChangePassword(){
        if (!driver.findElement(ACC_CHANGE_PASSWORD).isDisplayed())
            clickAccountInfoDDL();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(ACC_CHANGE_PASSWORD)).click();
    }
    public void clickAccChangeSettings(){
        if (!driver.findElement(ACC_CHANGE_SETTINGS).isDisplayed())
            clickAccountInfoDDL();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(ACC_CHANGE_SETTINGS)).click();
    }
    public AuthorityDetailsModal clickAccViewAuthority(){
        if (!driver.findElement(ACC_VIEW_AUTH).isDisplayed())
            clickAccountInfoDDL();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(ACC_VIEW_AUTH)).click();
        return new AuthorityDetailsModal(driver);
    }

    public boolean expectSuccessBanner(boolean throwOnFailure){
        try {
            waitMedium.until(ExpectedConditions.visibilityOfElementLocated(NOTIF_BANNER_SUCCESS));
        } catch (TimeoutException e){
            if (throwOnFailure){
                throw e;
            }
            return false;
        }
        return true;
    }

    public String getSuccessBannerText(){
        return driver.findElement(NOTIF_BANNER_SUCCESS).getText().trim();
    }


    // NAVIGATING TO ANOTHER PAGE
    public void clickMenuOption(String imgAltText){
        By optionLocator = By.xpath("//img[@alt='"+imgAltText+"']/../../..");
        driver.findElement(optionLocator).click();
        waitForLoadingWrapper();
    }
    public void clickMenuHome(){
        driver.findElement(MAIN_MENU_HOME).click();
    }
    public void clickMenuData(){
        driver.findElement(MAIN_MENU_DATA).click();
    }
    public void clickMenuConfig(){
        driver.findElement(MAIN_MENU_CONFIG).click();
    }
    public void clickMenuUsers(){
        driver.findElement(MAIN_MENU_USERS).click();
    }
    public void clickMenuReports(){
        driver.findElement(MAIN_MENU_REPORTS).click();
    }
    public void clickMenuLogout(){
        clickAccountInfoDDL();
        WebElement logoutButton = driver.findElement(MAIN_MENU_LOGOUT);
        logoutButton.click();
        new LoginPage(driver, false);
    }

    public void clickHelpVideosAndGuides(){
        if (!driver.findElement(HELP_VIDEOS_AND_GUIDES).isDisplayed())
            clickHelpCentre();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(HELP_VIDEOS_AND_GUIDES)).click();
    }
    public void clickHelpEmail(){
        if (!driver.findElement(HELP_EMAIL).isDisplayed())
            clickHelpCentre();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(HELP_EMAIL)).click();
    }
    public void clickHelpChat(){
        if (!driver.findElement(HELP_CHAT).isDisplayed())
            clickHelpCentre();
        new WebDriverWait(driver, SHORT_WAIT).until(elementToBeClickable(HELP_CHAT)).click();
    }

    private ExpectedCondition<Boolean> accountDropDownDisplayed(){
        return ExpectedConditions.attributeContains(ACC_UPDATE_DDL,"style","display: block");
    }
}

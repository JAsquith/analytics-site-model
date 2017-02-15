package analytics.legacy.pages;


import analytics.AnalyticsDriver;
import analytics.legacy.tests.SISRATest;
import org.openqa.selenium.By;

public class BaseWithMenu extends Base {

    public static final By MENU_HOME = By.cssSelector("img[alt='Home']");
    public static final By MENU_DATA = By.cssSelector("img[alt='Data']");
    public static final By MENU_CONFIG = By.cssSelector("img[alt='Config.']");
    public static final By MENU_USERS = By.cssSelector("img[alt='Users']");
    public static final By MENU_REPORTS = By.cssSelector("img[alt='Reports']");

    public static final By MENU_LOGOUT = By.cssSelector("img[alt='Logout']");

    public BaseWithMenu (AnalyticsDriver aDriver){super(aDriver);}
    public BaseWithMenu (SISRATest aTest){super(aTest);}

    public void gotoArea(String mainMenuOption){
        switch (mainMenuOption.toLowerCase()){
            case "home": gotoHome();break;
            case "data": gotoData();;break;
            case "config":case "config.": gotoConfig();break;
            case "users": gotoUsers();break;
            case "reports": gotoReports();break;
        }
    }

    public void gotoHome(){click(MENU_HOME);}
    public void gotoData(){click(MENU_DATA);}
    public void gotoConfig(){click(MENU_CONFIG);}
    public void gotoUsers(){click(MENU_USERS);}
    public void gotoReports(){click(MENU_REPORTS);}

    public void logOut(){click(MENU_LOGOUT);}

}

package pages.reports.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;

public class DDLOption extends AnalyticsComponent {

    private static final By ALL_OPTION_ROWS = By.cssSelector("li[data-value]");

    private WebElement parent;
    private WebElement child;

    private int index;
    private String name;

    public DDLOption(RemoteWebDriver aDriver, WebElement parentDDL, int childIndex) {
        super(aDriver);
        parent = parentDDL;
        index = childIndex;
    }

    public DDLOption(RemoteWebDriver aDriver, WebElement parentDDL, String childName){
        super(aDriver);
        parent = parentDDL;
        name = childName;
    }

    private WebElement getOptionElement(){

        String js = "";
        driver.executeScript(js, parent);
        return null;
    }

}
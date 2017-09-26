package pages.data.basedata;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.AnalyticsPage;

import java.util.List;

public class EAPList extends AnalyticsPage{

    public final String PAGE_URL = "/EAPAdmin/EAP";
    public final String PAGE_TITLE_TEXT = "EAP List";

    public final By CREATE_EAP_BUTTON = By.linkText("Create EAP");

    public final By EAPS_TABLE = By.cssSelector(".fieldset>table");

    public EAPList(RemoteWebDriver aDriver, boolean loadByUrl){
        super(aDriver);
        if(loadByUrl){
            driver.get(getSiteBaseUrl()+PAGE_URL);
        }
        waitMedium.until(ExpectedConditions.and(
                ExpectedConditions.urlContains(PAGE_URL),
                ExpectedConditions.textToBePresentInElementLocated(PAGE_TITLE,PAGE_TITLE_TEXT),
                ExpectedConditions.elementToBeClickable(CREATE_EAP_BUTTON)
        ));
        waitForLoadingWrapper();
    }

    public CreateEAP clickCreateEAP(){
        driver.findElement(CREATE_EAP_BUTTON).click();
        return new CreateEAP(driver);
    }

    public List<WebElement> getEAPDetailLinksMatching(String eapName){
        // Should only ever be one, but this lets us check for presence without throwing an error
        WebElement table = driver.findElement(EAPS_TABLE);
        return table.findElements(By.linkText(eapName));
    }

    public EAPDetail openEAP(String eapName){
        this.getEAPDetailLinksMatching(eapName).get(0).click();
        return new EAPDetail(driver);
    }

}
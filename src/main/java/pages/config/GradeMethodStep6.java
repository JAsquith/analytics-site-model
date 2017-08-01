package pages.config;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class GradeMethodStep6 extends GradeMethodEdit {

    public final By FINSIH_BUTTON = By.cssSelector(".submitButtons>.button.green");

    public final String EXPECTED_PAGE_LEGEND = "Step 6 of 6 - Confirmation";
    public final String PAGE_URL = "Config/EAPGradesMethod/ConfirmCreate";

    public GradeMethodStep6(RemoteWebDriver aDriver){
        super(aDriver);
        waitMedium.until(ExpectedConditions.textToBe(PAGE_LEGEND,EXPECTED_PAGE_LEGEND));
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(FINSIH_BUTTON));
    }

    public void clickFinish(){
        driver.findElement(FINSIH_BUTTON).click();
    }

}
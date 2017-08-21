package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import tests.reports.ReportTest;

public class DevTest extends ReportTest {

    @Test
    public void devTest(){
        WebElement sortIcon = driver.findElement(By.cssSelector("label.icon.sortDir"));
        String cssValue = sortIcon.getCssValue("background-position");
        System.out.println(cssValue);
    }

}

package tests;

import org.testng.annotations.Test;
import tests.reports.ReportTest;

public class DevTest extends ReportTest {

    @Test
    public void devTest(){
        saveScreenshot("devtest.png");
        System.out.println(report.getPageTitleText());
    }

}

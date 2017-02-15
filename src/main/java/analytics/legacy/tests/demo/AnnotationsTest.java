package analytics.legacy.tests.demo;

import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains methods to test the <a href="https://www.sisraanalytics.co.uk">SISRA Analytics</a>
 * login page.
 *
 * @author Milton Asquith
 * @version 1.0
 * @since 2016-02-19
 */
public class AnnotationsTest {

    private int methodCounter = 0;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " beforeSuite");
    }

    @BeforeTest(groups = { "GroupA" })
    public void beforeTest(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " beforeTest");
    }

    @BeforeClass(groups = { "GroupA" })
    public void beforeClass(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " beforeClass");
    }

    @BeforeGroups(groups = { "GroupA" })
    public void beforeGroupsA(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " beforeGroups (Group A)");
    }

    @BeforeGroups(groups = { "GroupB" })
    public void beforeGroupsB(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " beforeGroups (Group B)");
    }

    @BeforeMethod(groups = { "GroupA" })
    public void beforeMethod(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " beforeMethod");
    }

    @Test(groups = { "GroupA" })
    public void testMethodA(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " TEST (testMethodA)");
    }

    @Test(groups = { "GroupA", "GroupB" })
    public void testMethodB(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " TEST (testMethod B)");
    }

    @AfterMethod(groups = { "GroupA" })
    public void afterMethod(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " afterMethod");
    }

    @AfterGroups(groups = { "GroupA" })
    public void afterGroupsA(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " afterGroups (Group A)");
    }

    @AfterGroups(groups = { "GroupB" })
    public void afterGroupsB(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " afterGroups (Group B)");
    }

    @AfterClass(groups = { "GroupA" })
    public void afterClass(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " afterClass");
    }

    @AfterTest(groups = { "GroupA" })
    public void afterTest(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " afterTest");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext){
        log(methodCounter++ + " - " + testContext.getName() + " afterSuite");
    }

    private void log(String msg){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.S");
        Date d = new Date();
        System.out.println(timeFormat.format(d) + " - " + msg);
    }
}

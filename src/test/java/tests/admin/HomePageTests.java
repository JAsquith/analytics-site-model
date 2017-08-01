package tests.admin;

import tests.SISRATest;
import pages.account.LoginPage;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.*;

/**
 * Add Javadoc comments here!
 */

public class HomePageTests extends SISRATest {

    LoginPage testPage;

   protected void successCriteria_ValidationPassed() {
        // Todo
    }

    @BeforeTest
    public void setup(ITestContext testContext) throws MalformedURLException {

        try {
            System.out.println("Test running on IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException while retrieving local IP with InetAddress.getLocalHost()");
        }

        initialise(testContext);

        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        testPage = new LoginPage(driver, true);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
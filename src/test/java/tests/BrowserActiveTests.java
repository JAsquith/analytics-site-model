package tests;

import io.qameta.allure.*;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;

@Epic( "This is an Epic!" )
@Feature ( "This is a Feature" )
public class BrowserActiveTests extends BaseTest {

    @BeforeTest()
    @Step( "Login" )
    @Parameters( { "username", "password" })
    public void setupMethod(ITestContext testContext, String user, String pass) throws MalformedURLException {
        super.initialise(testContext);

        try {
            // Login, Go to reports, Open the dataset containing the test data
            login(user, pass, true);

        } catch (Exception e){
            if (driver!=null){
                driver.quit();
                fail("Test Setup Failed!");
            }
        }
    }

    @Test( description = "This is a description which overrides the method name" )
    @Description ( "This is a Description" )
    @Owner ( "Owner" )
    @Severity ( SeverityLevel.TRIVIAL )
    @Step( "The main Test method can be a Step (including parameters like username='{username}'" )
    @Story ( "Are you sitting comfortably? This is a Story" )
    @Parameters( { "username" })
    public void mainTestMethod1(String username){
        System.out.println("This is a test");
        testStep("value", username);
    }

    @Test( description = "This is Test method 2" )
    @Description ( "Story: Nothing happens. The End." )
    @Owner ( "Another Owner" )
    @Severity ( SeverityLevel.MINOR )
    @Story ( "Once upon a time..." )
    public void mainTestMethod2(){
        System.out.println("Nothing much happens here");
        attachmentAndStep();
    }

    @Test( description = "Test without @Story or @Description" )
    @Severity ( SeverityLevel.MINOR )
    public void mainTestMethod3(){
        System.out.println("Nothing much happens here");
    }

    @Test
    @Story ( "A test fails if one of its assertions is not true " )
    @Description ( "Every test should contain at least (and ideally only) one assertion" )
    public void testMethodWithNoDescription(){
        assertThat("This is an assertion: \"actual\" is the same as \"expected\"",
                12, is(13));
    }

    @Step( "Test step where param = '{param}' and a screenshot called {username} is attached" )
    private void testStep(String param, String username){
        saveScreenshot(username+".png");
        System.out.println("This is a test step with a parameter ("+param+")");
    }

    @Step ( "A step that attaches a text file" )
    @Attachment
    private String attachmentAndStep(){
        String text = "This step returns a string.\n";
        text += "Because the step method is annotated with '@Attachment', the string is used...\n";
        text += "to create a text file which is attached to this step in the Allure report\n\n";
        text += "This has been: " + context.getName();
        return text;
    }

    @Test(  dependsOnMethods = "testMethodWithNoDescription",
            description = "Description of a skipped test")
    @Description ("This is the Allure description, which is slightly different")
    @Story ( "A test method that depends on another test method is skipped if the method it depends on fails" )
    public void callThisWhatever(){
        for (int x=0; x<100; x++) {
            System.out.println(x + ". Since this code is never run I don't know why I'm writing this!");
        }
    }

    @Feature ( "Different Feature within the same class" )
    @Test( description = "A Test that produces a 'Test Defect'" )
    @Story ( "If a test method errors before an assertion is false, it is a 'Test Defect'" )
    public void testDefect(){
        throw new IllegalArgumentException ("Your code is bad and you should feel bad!");
    }
}

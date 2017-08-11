package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Epic( "Allure Test Reports" )
@Feature( "Demonstrate Features" )
public class ReportDemosFailing extends BaseTest {

    @BeforeTest
    public void setup(ITestContext testContext) throws MalformedURLException {
        initialise(testContext);
        saveScreenshot("setup.png");
    }

    @Story( "Attaching a screenshots to the report" )
    @Test
    public void productDefectWithScreenshot(){
        doStuff();
        doSomeStuffWithArguments("arg0", 5);
        doSomethingElse();
        assertWithScreenshot("Result of 2 + 2", 5, is(4));
    }

    @Story( "Test Defects are bad test code (not Product Defects)" )
    @Test
    public void testDefect(){
        doSomethingExceptional();
        assertThat("Test should be broken", true, is(true));
    }

    @Step( "Do some stuff" )
    private void doStuff(){

    }

    @Step( "Do some other stuff" )
    private void doSomethingElse(){
        saveScreenshot("otherStuff.png");
    }

    @Step( "Do stuff with {aString} and {anInteger}")
    private void doSomeStuffWithArguments(String aString, int anInteger){

    }

    @Step( "Do something to throw an exception" )
    private void doSomethingExceptional(){
        String[] strArray = {""};
        strArray[5] = "IndexOutOfBounds";
    }

}

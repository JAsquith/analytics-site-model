package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;

@Epic( "This is an Epic!" )
@Feature ( "Same Epic, Different Feature" )
public class TestClass2 {


    @BeforeTest()
    @Step( "Intentionally Fail Setup in order to show a different skipped test" )
    public void setupMethod(){
        fail("Test Setup Failed - let me tell you why...");
    }





    @Test ( description = "A test that is skipped due to setup failure")
    @Story ( "Maths Is Fun!" )
    public void testMethod(){
        assertThat("This is a true assertion: 2+2=4", (2*2), is(4));
    }


}

package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Epic( "Mathematical Axioms" )
@Feature ( "Addition and Subtraction" )
public class NonBrowserTests {

    @Story ( "Addition is commutative" )
    @Test ( description = "Check that A+B = B+A", dataProvider = "randomTinyInts" )
    public void addingTestMethod (int a, int b){
        int a_plus_b = add(a, b);
        int b_plus_a = add(b, a);
        assertThat("a+b == b+a (where a="+a+"; and b=+"+b+")" , (a_plus_b), is(b_plus_a));
    }

    @Story ( "Subtraction is commutative" )
    @Test ( description = "Check that A-B = B-A", dataProvider = "randomTinyInts" )
    public void subtractingTestMethod (int a, int b){
        int a_minus_b = subtract(a, b);
        int b_minus_a = subtract(b, a);
        assertThat("a-b == b-a (where a="+a+"; and b="+b+")" , (a_minus_b), is(b_minus_a));
    }

    @Step( "Calculate {a}+{b}")
    private int add(int a, int b){
        return a + b;
    }

    @Step( "Calculate {a}-{b}")
    private int subtract(int a, int b){
        return a - b;
    }

    @DataProvider( name = "randomSmallInts")
    Iterator<Object[]> randomSmallInts(){
        List<Object []> testCases = new ArrayList<>();
        Random rnd = new Random();
        int cases = 15*15;//rnd.nextInt(11)+5;

        for (int i=0; i<cases; i++){
            int first = rnd.nextInt(15)+1;
            int second = rnd.nextInt(15)+1;

            testCases.add(new Integer[]{first, second});
        }
        return testCases.iterator();
    }

    @DataProvider( name = "randomTinyInts")
    Iterator<Object[]> randomTinyInts(){
        List<Object []> testCases = new ArrayList<>();
        Random rnd = new Random();
        int cases = 4*4;//rnd.nextInt(11)+5;

        for (int i=0; i<cases; i++){
            int first = rnd.nextInt(3)+1;
            int second = rnd.nextInt(3)+1;

            testCases.add(new Integer[]{first, second});
        }
        return testCases.iterator();
    }

}

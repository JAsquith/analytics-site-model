package analytics.pages.data.students.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents the contents and interactive elements on the Students &gt; Assign Quals page
 */
public class AssignQuals extends AnalyticsPage {

    private final By SETS_OF_CLASSES_NAMES = By.cssSelector("td:nth-of-type(1)");

    // CONSTRUCTOR
    public AssignQuals(AnalyticsDriver aDriver){
        super(aDriver);
    }

    // METHODS

    /**
     * Finds a named set of classes in the Sets of Classes Name column and returns the index of that row.
     *
     * @param setOfClassesName      The name of a Set of Classes listed on the page
     * @return  The index of the row containing the Set of Classes; or 0 if there is no match;
     *          The returned row indices start at 1 rather than 0 for use in css nth-of-type psuedo-classes,
     *          however, {@code 1} should never be returned as that would indicate the header row.
     *          So valid returns are {@code 0} (no match) or {@code >=2} (match)
     */
    public int rowForSetOfClasses(String setOfClassesName){
        List<WebElement> setsOfClassesList = driver.findElements(SETS_OF_CLASSES_NAMES);

        for (WebElement setOfClassesTD : setsOfClassesList){
            if (setOfClassesTD.getText().equals(setOfClassesName)){
                return setsOfClassesList.indexOf(setOfClassesTD)+2;
            }
        }

        return 0;
    }

    /**
     * Clicks the link to open the Class Assignments modal for the specified set of classes and
     * returns a page component object representing that modal
     *
     * @param setOfClassesName  The name of the set of classes; should match a set of classes listed on the current page
     */
    public void openClassAssignmentsModal(String setOfClassesName){
        // @todo return a component representing the modal which has been opened
        int rowIndex = rowForSetOfClasses(setOfClassesName);
        String cssText = "tr:nth-of-type("+rowIndex+">td:nth-of-type(4)>a";
        driver.findElement(By.cssSelector(cssText)).click();
    }
}

package analytics.pages.data.grades;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import analytics.pages.data.grades.components.MatchingQualRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.List;

/**
 * Represents the components and actions on the Matching page.
 * Use an object of this class to access a {@link analytics.pages.data.grades.components.MatchingQualRow}
 * component object for any particular qualification row
*/
public class MatchingPage extends AnalyticsPage {

    private HashMap<String, Integer> qualsMap;
    public final By QUAL_NAME_FIELDS = By.cssSelector(".colQual>input");

    public final By CANCEL_BUTTON = By.linkText("Cancel");
    public final By SAVE_CHANGES_BUTTON = By.cssSelector(".submitButtons>button.green");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on (or opening) the Matching page
     */
    public MatchingPage(AnalyticsDriver aDriver) {
        super(aDriver);
        waitForLoadingWrapper(LONG_WAIT);
    }

    public void initQualsMap(){
        int qualIndex = 0;

        List<WebElement> qualNameFields = driver.findElements(QUAL_NAME_FIELDS);
        qualsMap = new HashMap<String, Integer>();
        for(WebElement nameField : qualNameFields){
            qualsMap.put(nameField.getAttribute("value"), qualIndex);
            qualIndex++;
        }
    }

    /**
     * Allows access to a {@link MatchingQualRow} object representing the fields on one row
     * @param qualName  The name of the qualification
     * @return  A {@link MatchingQualRow} object
     */
    public MatchingQualRow getQualRowByQualName(String qualName) throws IllegalArgumentException {
        int qualIndex = qualsMap.get(qualName);
        return new MatchingQualRow(driver, qualIndex);
    }

    /**
     * Allows access to a {@link MatchingQualRow} object representing the fields on one row
     * @param qualIndex  The index of the qualification on the page (1 represents the first qualification)
     * @return  A {@link MatchingQualRow} object
     */
    public MatchingQualRow getQualRowByQualIndex(int qualIndex) throws IllegalArgumentException{
        return new MatchingQualRow(driver, qualIndex);
    }

    /**
     * Clicks the Cancel button
     * @return  The current Matching page object
     */
    public MatchingPage clickCancel(){
        driver.findElement(CANCEL_BUTTON).click();
        waitShort.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        waitForLoadingWrapper(LONG_WAIT);
        return this;
    }

    public MatchingPage clickSaveChanges(){
        driver.findElement(SAVE_CHANGES_BUTTON).click();
        waitForLoadingWrapper(LONG_WAIT);
        return this;
    }
}
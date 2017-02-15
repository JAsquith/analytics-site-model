package analytics.pages.data.grades.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import analytics.pages.data.GradesFileUpload;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * A component object representing the content and actions within one accordion on the
 * Uploads For Assessments page
 */
public class CollectionAccordion extends AnalyticsComponent{

    private WebElement accHeader;
    private WebElement accContent;

    // Locators for all accordion elements on the page
    private final By ACC_HEADERS = By.className("ui-accordion-header");
    private final By ACC_CONTENTS = By.className("ui-accordion-content");

    // Locators for elements within the accordion header element of the accordion this object represents
    private final By COLL_DATE = By.className("floatR");

    // Locators for elements within the accordion content element of the accordion this object represents
    private final By UPLOAD_GRADE_FILE_BUTTON = By.linkText("Upload Grade File");
    private final By FILE_TITLE_CELLS = By.cssSelector("td:nth-of-type(4)");

    /**
     * Creates a Component object for the accordion identified by the collDate param.
     * @param aDriver   Browser should be on the Uploads For Assessment page
     * @param collDate   Used to identify the accordion which should be represented by this object
     */
    public CollectionAccordion(AnalyticsDriver aDriver, String collDate) {
        super(aDriver);

        int accIndex = -1;
        List<WebElement> accHeaders = driver.findElements(ACC_HEADERS);
        for(WebElement header : accHeaders){
            accIndex++;
            accHeader = accHeaders.get(accIndex);

            if(accHeader.findElement(COLL_DATE).getText().equals(collDate)) {
                accContent = driver.findElements(ACC_CONTENTS).get(accIndex);
                if (!driver.findElements(ACC_CONTENTS).get(accIndex).isDisplayed()){
                    accHeader.click();
                    waitForLoadingWrapper();
                    waitMedium.until(ExpectedConditions.visibilityOf(accContent));
                }
                break;
            }
        }

        // if we get this far without setting the accContent field and returning, then the collDate was not found
        if (accIndex == -1){
            throw new IllegalArgumentException("An Assessment Collection dated '"+collDate+"' could not be found!");
        }
    }

    /**
     * Clicks the Upload Grade File button
     * @return  An {@link GradesFileUpload} page object
     */
    public GradesFileUpload clickUploadGradeFile(){
        accContent.findElement(UPLOAD_GRADE_FILE_BUTTON).click();
        return new GradesFileUpload(driver);
    }

    public boolean hasFile(String fileTitle){
        List<WebElement> titleCells = accContent.findElements(FILE_TITLE_CELLS);
        for(WebElement titleCell : titleCells){
            if(titleCell.getText().equals(fileTitle)){
                return true;
            }
        }
        return false;
    }

}

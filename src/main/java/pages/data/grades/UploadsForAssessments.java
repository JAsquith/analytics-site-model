package pages.data.grades;

import pages.AnalyticsPage;
import pages.data.grades.components.CollectionAccordion;
import pages.data.grades.components.CreateCollectionModal;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Represents the components and actions on an Uploads For Assessments page
 */
public class UploadsForAssessments extends AnalyticsPage {

    public final By CREATE_ASSESSMENT_COLLECTION_BUTTON = By.linkText("Create Assessment Collection");

    /**
     * Simple constructor
     * @param aDriver   The browser should be on (or opening) the Uploads For Assessments page
     */
    public UploadsForAssessments(RemoteWebDriver aDriver) {
        super(aDriver);
        waitShort.until(ExpectedConditions.elementToBeClickable(CREATE_ASSESSMENT_COLLECTION_BUTTON));
        waitForLoadingWrapper(LONG_WAIT);
    }

    /**
     * Clicks the Create Assessment Collection button
     * @return  A {@link CreateCollectionModal} object representing the elements of the Create Collection modal
     */
    public CreateCollectionModal clickCreateAssessmentCollection(){
        driver.findElement(CREATE_ASSESSMENT_COLLECTION_BUTTON).click();
        return new CreateCollectionModal(driver);
    }

    /**
     * Gives access to one of the Assessment Collection Accordions as a {@link CollectionAccordion} page object
     * @param collectionDate  Uniquely identifies the Collection Accordion to be represented
     *                  by the returned page component object
     * @return  A {@link CollectionAccordion} page object representing the accordion (if found!)
     */
    public CollectionAccordion getAccordion(String collectionDate){
        return new CollectionAccordion(driver, collectionDate);
    }

    public boolean collectionExists(String collectionDate){
        try{
            this.getAccordion(collectionDate);
        } catch (Exception e){
            return false;
        }
        return true;
    }

}
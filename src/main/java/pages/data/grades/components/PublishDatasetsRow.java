package pages.data.grades.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;
import pages.data.grades.interfaces.IPublishGradesRow;

import java.util.List;

/**
 * A component object representing the content and actions within one row of the Publish Data Sets table
 */
public class PublishDatasetsRow
        extends AnalyticsComponent
        implements IPublishGradesRow {

    private final By DATASET_NAME_CELLS = By.cssSelector(".pubAdminTable tr:not(:first-child) td:nth-of-type(2)");
    private WebElement mainDetailsRow;
    private final By PUBLISH_BUTTON = By.linkText("Publish");
    private static final By PUB_INFO_SPAN = By.cssSelector(".smallInfo>span");
    private WebElement pubInfoRow;

    /**
     * Creates a Component object for a row of the Publish Datasets table identified by the dataset param.
     * @param aDriver   Browser should be on the Publish Grades page
     * @param dataset   Used to identify the row which should be represented by this object
     */
    public PublishDatasetsRow(RemoteWebDriver aDriver, String dataset){
        super(aDriver);
        initByDSName(dataset);
    }

    // Initialisation code abstracted out in case we want to add a 'switchRow' method
    private void initByDSName(String dataset) {
        int dsIndex = 0;

        List<WebElement> datasetNameCells = driver.findElements(DATASET_NAME_CELLS);
        for(WebElement datasetNameCell : datasetNameCells){
            dsIndex++;
            if(datasetNameCell.getText().equals(dataset)){
                break;
            }
        }
        if(dsIndex == 0){
            // The specified file title was not found
            // Todo - deal with this properly, for now just throw an IllegalArgumentException
            throw new IllegalArgumentException("The specified dataset name was not found in the table ("+dataset+")");
        }

        initByIndex(dsIndex);
    }

    public String getLastPublishedInfo()
    {
        return pubInfoRow.findElement(PUB_INFO_SPAN).getText();
    }

    public PublishGradesModal clickPublish(){
        mainDetailsRow.findElement(PUBLISH_BUTTON).click();
        waitForLoadingWrapper();
        return new PublishGradesModal(driver);
    }

    private void initByIndex(int dsIndex){
        int mainRowCssIndex = (dsIndex * 2) + 1;
        int pubInfoRowCssIndex = mainRowCssIndex + 1;
        mainDetailsRow = driver.findElement(By.cssSelector(".pubAdminTable tr:nth-of-type(" + mainRowCssIndex + ")"));
        pubInfoRow = driver.findElement(By.cssSelector(".pubAdminTable tr:nth-of-type(" + pubInfoRowCssIndex + ")"));
    }

}

package pages.data.grades.components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.AnalyticsComponent;
import pages.data.grades.interfaces.IPublishGradesRow;

import java.util.List;

/**
 * A component object representing the content and actions within one row of the Publish Assessments table
 */
public class PublishAssessmentsYearRow
        extends AnalyticsComponent
        implements IPublishGradesRow {

    public final By PUBLISH_BUTTON = By.linkText("Publish");
    public final By TERM_NAME_CELLS = By.cssSelector(".pubAdminTable tr td[rowspan='4']");
    private static final By PUB_INFO_SPAN = By.cssSelector(".smallInfo>span");

    private WebElement mainDetailRow;
    private WebElement pubInfoRow;

    /**
     * Creates a Component object for a row of the Publish Assessments table identified by the term and slot params.
     * The term is matched to the Term column rather than assuming there are grades uploade for earlier terms
     * (e.g. term = 3 will return the first main detail row if there are no grades for terms 1 & 2)
     * @param aDriver   Browser should be on the Publish Grades page & a "Year n" tab should be selected
     * @param term      1, 2, or 3
     * @param slot      1 or 2
     */
    public PublishAssessmentsYearRow(RemoteWebDriver aDriver, int term, int slot){
        super(aDriver);
        List<WebElement> nameCells = driver.findElements(TERM_NAME_CELLS);
        for (WebElement nameCell : nameCells)
        {
            if (nameCell.getText().endsWith("Term " + term))
            {
                initByIndex((nameCells.indexOf(nameCell) * 2) + slot);
                return;
            }
        }
        throw new IllegalArgumentException("Could not find a row for Term '" + term + "'");
    }

    /**
     * Creates a Component object for a row of the Publish Assessments table identified by the slot index.
     * The nth available slot is returned regardless of which term that relates to
     *
     * @param aDriver   Browser should be on the Publish Grades page & a "Year n" tab should be selected
     * @param slotIndex an integer between 1 and 6
     */
    public PublishAssessmentsYearRow(RemoteWebDriver aDriver, int slotIndex)
    {
        super(aDriver);
        initByIndex(slotIndex);
    }

    private void initByIndex(int slotIndex)
    {
        int mainRowCssIndex = ((slotIndex) * 2) + 1;
        int infoRowCSSIndex = mainRowCssIndex + 1;
        try
        {
            mainDetailRow = driver.findElement(By.cssSelector(".pubAdminTable tr:nth-of-type(" + mainRowCssIndex + ")"));
            pubInfoRow = driver.findElement(By.cssSelector(".pubAdminTable tr:nth-of-type(" + infoRowCSSIndex + ")"));
        } catch (NoSuchElementException e)
        {
            String msg = "Could not find a row for slot with an index of '" + slotIndex + "'";
            throw new IllegalArgumentException(msg += System.lineSeparator() + e.getMessage());
        }
    }

    public PublishGradesModal clickPublish()
    {
        mainDetailRow.findElement(PUBLISH_BUTTON).click();
        waitForLoadingWrapper();
        return new PublishGradesModal(driver);
    }

    public String getLastPublishedInfo()
    {
        return pubInfoRow.findElement(PUB_INFO_SPAN).getText();
    }

    public boolean publishAvailable()
    {
        if (getLastPublishedInfo().startsWith("Not"))
        {
            return false;
        }
        try
        {
            mainDetailRow.findElement(PUBLISH_BUTTON);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

}

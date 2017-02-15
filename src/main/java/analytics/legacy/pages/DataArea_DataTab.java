package analytics.legacy.pages;

import analytics.legacy.tests.SISRATest;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataArea_DataTab extends DataArea {

    // Elements on individual pages in Data
    /**
     * Identifies the 'Save Changes And Add To Publish Queue' button (&lt;div&gt; element)
     * on the Publish Reports page. <br><br>
     * Not supported when: The Publish Reports page is not displayed.
     */
    public static final By PUBLISH_QUEUE_SUBMIT = By.id("smtBtQue");
    public static final By PUBLISH_DATASET_ROWS = By.cssSelector(".pubAdminTable tr:nth-child(2n+0)");
    public static final By PUBLISH_DATASET_CB__ROW_RELATIVE = By.cssSelector("input.includeActive");
    public static final By PUBLISH_DATASET_NAME__ROW_RELATIVE = By.cssSelector("td:nth-of-type(2)");
    public static final By PUBLISH_DATASET_LOGIC__ROW_RELATIVE = By.cssSelector("td:nth-of-type(3) > select");
    public static final By PUBLISH_DATASET_VA__ROW_RELATIVE = By.cssSelector("td:nth-of-type(4) > select");
    public static final By PUBLISH_DATASET_A8__ROW_RELATIVE = By.cssSelector("td:nth-of-type(5) > select");
    public static final By PUBLISH_DATASET_STATUS__ROW_RELATIVE = By.cssSelector("td:nth-of-type(6) > select");
    public static final By PUBLISH_DATASET_NOTE__ROW_RELATIVE = By.cssSelector("td:nth-of-type(7) > input");

    // Constructors
    public DataArea_DataTab(SISRATest aTest) {super(aTest);}

    /**
     * Clicks the {@link #PUBLISH_REPORTS} link in the Data side menu, clicks the
     * {@link #PUBLISH_QUEUE_SUBMIT} button (if it is present), and accepts the
     * resulting javascript alert. <br>
     * Throws a {link #NotFoundException} if the Data tab of the side menu is not
     * displayed, or if the 'Publish Reports' link is not active. For example
     * if the Uploads tab is active, no grades are uploaded, or no qualifications
     * have been matched.
     *
     * @return  {@code true} if the Reports were added to the queue.<br>
     *          {@code false} if the '...Add to Queue' button was not found (e.g.
     *          if there are exam grades with warning icons).
     */
    public boolean publishCurrentCohort() {
        return publishCurrentCohort(false, "");
    }

    /**
     * Clicks the {@link #PUBLISH_REPORTS} link in the Data side menu, clicks the
     * {@link #PUBLISH_QUEUE_SUBMIT} button (if it is present), and accepts the
     * resulting javascript alert. <br>
     * Throws a {link #NotFoundException} if the Data tab of the side menu is not
     * displayed, or if the 'Publish Reports' link is not active. For example
     * if the Uploads tab is active, no grades are uploaded, or no qualifications
     * have been matched.
     *
     * @return  {@code true} if the Reports were added to the queue.<br>
     *          {@code false} if the '...Add to Queue' button was not found (e.g.
     *          if there are exam grades with warning icons).
     * @param singleDataSet
     * @param singleDataSetName
     */
    public boolean publishCurrentCohort(boolean singleDataSet, String singleDataSetName) {

        if (singleDataSet && singleDataSetName.equals(""))
            throw new IllegalArgumentException("A valid Data Set Name must be supplied if the singleDataSet flag is set.");

        waitForClickabilityOf(PUBLISH_REPORTS).click();
        this.waitForReload();

        if (singleDataSet)
            selectSingleDataSet(singleDataSetName);
        else

        try {
            find(PUBLISH_QUEUE_SUBMIT).click();
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (NotFoundException e) {
            return false;
        }

        this.waitForReload();
        return true;
    }

    public void selectSingleDataSet(String dataSetName){
        List<WebElement> dataSetRows = findAll(PUBLISH_DATASET_ROWS);
        int publishRowIndex = -1;

        if (dataSetName.toLowerCase().equals("random")) {
            if (dataSetRows.size() > 1)
                publishRowIndex = ThreadLocalRandom.current().nextInt(1, dataSetRows.size());
            else
                publishRowIndex = 0;
        }

        for (int rowIndex = 0; rowIndex < dataSetRows.size()-1; rowIndex++){
            WebElement row = dataSetRows.get(rowIndex);
            row.isSelected();
            String rowDataSetName = find(row, PUBLISH_DATASET_NAME__ROW_RELATIVE).getText().trim();

            if (publishRowIndex == -1 && rowDataSetName.equals(dataSetName) || rowIndex == publishRowIndex) {
                publishRowIndex = rowIndex;
                setDatasetOptions(row, true);
            } else {
                setDatasetOptions(row, false);
            }
        }
        if (publishRowIndex == -1) {
            throw new IllegalArgumentException("A valid Data Set Name must be supplied if the singleDataSet flag is set.");
        }
    }

    public void setDatasetOptions (WebElement row) {
        setDatasetOptions(row, true);
    }

    public void setDatasetOptions(WebElement row, boolean publish) {
        setDatasetOptions(row, publish, "", "", "", "");
    }

    public void setDatasetOptions
            (WebElement row, boolean publish, String logic, String vaMethod, String a8Estimates, String status) {

        WebElement dataSetCB = find(row, PUBLISH_DATASET_CB__ROW_RELATIVE);

        if (publish) {
            if (!dataSetCB.isSelected())
                click(dataSetCB);

            if (!logic.equals(""))
                new Select(find(row,PUBLISH_DATASET_LOGIC__ROW_RELATIVE)).selectByVisibleText(logic);
            if (!vaMethod.equals(""))
                new Select(find(row,PUBLISH_DATASET_VA__ROW_RELATIVE)).selectByVisibleText(vaMethod);
            if (!a8Estimates.equals(""))
                new Select(find(row,PUBLISH_DATASET_A8__ROW_RELATIVE)).selectByVisibleText(a8Estimates);

            // Status is set individually for each Assessment Point in the Tracker
            if (!status.equals(""))
                if (findAll(row, PUBLISH_DATASET_STATUS__ROW_RELATIVE).size() > 0)
                    new Select(find(row,PUBLISH_DATASET_STATUS__ROW_RELATIVE)).selectByVisibleText(status);

            Date now = new Date();
            overType("Updated by TestRunner at " + timeFormat.format(now)
                    + " on " + dateFormat.format(now), find(row, PUBLISH_DATASET_NOTE__ROW_RELATIVE));
        } else {
            if (dataSetCB.isSelected())
                click(dataSetCB);
        }
    }
}

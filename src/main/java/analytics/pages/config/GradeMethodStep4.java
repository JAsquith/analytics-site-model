package analytics.pages.config;

import analytics.AnalyticsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Represents the page shown as Step 4 of the Grade Method creation process/wizard
 */
public class GradeMethodStep4 extends GradeMethodEdit {

    public final By SELECT_ALL_BUTTON = By.id("checkAll");
    public final By SELECT_NONE_BUTTON = By.id("checkClear");

    public final By MASTER_GRADE_CHECKBOXES = By.cssSelector("input[type='checkbox']");

    public final String EXPECTED_PAGE_LEGEND = "Step 4 of 6 - Define Master Grades";

    /**
     * The AnalyticsDriver param must represent a browser open at step 4 of the EAP Grade Method edit process.
     * @param aDriver   An AnalyticsDriver object logged in to a school in the given domain
     */
    public GradeMethodStep4(AnalyticsDriver aDriver) {
        super(aDriver);
        waitShort.until(ExpectedConditions.textToBe(PAGE_LEGEND,EXPECTED_PAGE_LEGEND));
        waitForLoadingWrapper();
        waitShort.until(ExpectedConditions.elementToBeClickable(SELECT_NONE_BUTTON));
    }

    /**
     * Clicks the "Select All" button
     */
    public void selectAll(){
        driver.findElement(SELECT_ALL_BUTTON).click();
    }

    /**
     * Clicks the "Select None" button
     */
    public void selectNone(){
        driver.findElement(SELECT_NONE_BUTTON).click();
        waitMedium.until(ExpectedConditions
                .elementSelectionStateToBe(MASTER_GRADE_CHECKBOXES,false));
    }

    /**
     * Ensures each of the check boxes on the given row is selected
     * @param rowNum    The row on which to select all sub grades; starts at <code>1</code> for first whole grade
     */
    public void selectRow(int rowNum){
        // rowNum represents a data row, the table has a row containing th (header) elements, so...
        // increment rowNum by 1 to find the correct tr:nth-of-type index
        rowNum++;

        By rowCheckBoxes = By.cssSelector("#page tr:nth-of-type("+rowNum+") input[type='checkbox']");
        List<WebElement> subGradeCheckBoxes = driver.findElements(rowCheckBoxes);
        for(WebElement cb: subGradeCheckBoxes){
            if(cb.isSelected()==false){
                cb.click();
            }
        }
    }

    /**
     * Selects (if it is not already) the nth (given by {@code colNum}) sub grade of
     * the nth (given by {@code rowNum})whole grade
     * @param rowNum    The row on which to find the sub grade; starts at {@code 1} for first whole grade
     * @param colNum    The column within which to ensure the check box is selected;
     *                  starts at {@code 1} for the first sub grade
     */
    public void selectSubGrade(int rowNum, int colNum){
        // rowNum represents a data row, the table has a row containing th (header) elements, so...
        // increment rowNum by 1 to find the correct tr:nth-of-type index
        rowNum++;
        // colNum represents a data column, the table has a col containing whole grade text (and no check boxes), so...
        // increment colNum by 1 to find the correct td:nth-of-type index
        colNum++;

        By subGradeCB = By.cssSelector(
                "#page tr:nth-of-type("+rowNum+")>td:nth-of-type("+colNum+") input[type='checkbox']");
        WebElement cb = driver.findElement(subGradeCB);
        if (cb.isSelected()==false){
            cb.click();
        }
    }
}

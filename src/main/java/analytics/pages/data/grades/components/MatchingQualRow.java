package analytics.pages.data.grades.components;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * A component object representing the content and fields within one row of the Matching page
 * !! At some point it may be necessary to subclass this for Current and Predecessor quals !!
 */
public class MatchingQualRow extends AnalyticsComponent{

    private int cssIndex;
    public final By QUAL_NAME_FIELDS = By.cssSelector(".colQual>input");

    public final By QUAL_NAME_FIELD = By.cssSelector(".colQual>input");
    public final By QUAL_INC_SELECT = By.cssSelector(".colInc>select");
    public final By QUAL_CURRENT_SELECT = By.cssSelector(".hi_3>select");
    public final By QUAL_EAP_SELECT = By.cssSelector(".hi_4>select");
    public final By QUAL_MEASURE_SELECT = By.cssSelector(".hi_5>select");
    public final By QUAL_SPECIAL_SELECT = By.cssSelector(".hi_6>select");
    public final By QUAL_FACULTY_SELECT = By.cssSelector(".hi_7>select");
    public final By QUAL_PREDECESSOR_SELECT = By.cssSelector(".hi_8>select");
    public final By QUAL_BAR_CODE_FIELD = By.cssSelector(".hi_9>input");


    /**
     * Creates a Component object for a row of a the Matching page identified by the qualName param.
     * @param aDriver       Browser should be on an Matching page
     * @param qualName     Used to identify the row which should be represented by this object
     */
    public MatchingQualRow(AnalyticsDriver aDriver, String qualName) throws IllegalArgumentException {
        super(aDriver);
        initByName(qualName);
    }

    /**
     * Creates a Component object for a row of the Matching Page identified by the qualIndex param.
     * @param aDriver       Browser should be on the Matching page
     * @param qualIndex     Used to identify the row which should be represented by this object
     */
    public MatchingQualRow(AnalyticsDriver aDriver, int qualIndex) throws IllegalArgumentException {
        super(aDriver);
        initByIndex(qualIndex);
    }

    /**
     * Overwrites the text in the QUAL NAME field of the current row with the text passed in
     * @param text  The new value for the field
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow setQualName(String text){
        WebElement field = tableRow.findElement(QUAL_NAME_FIELD);
        field.clear();
        field.sendKeys(text);
        return this;
    }
    /**
     * Overwrites the text in the BAR CODE field of the current row with the text passed in
     * @param text  The new value for the field
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow setBarCode(String text){
        WebElement field = tableRow.findElement(QUAL_BAR_CODE_FIELD);
        field.clear();
        field.sendKeys(text);
        return this;
    }

    /**
     * Selects an option in the INCLUDE drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectInclude(String optionText){
        new Select(tableRow.findElement(QUAL_INC_SELECT)).selectByVisibleText(optionText);
        return this;
    }
    /**
     * Selects an option in the CURRENT drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectCurrent(String optionText){
        new Select(tableRow.findElement(QUAL_CURRENT_SELECT)).selectByVisibleText(optionText);
        return this;
    }
    /**
     * Selects an option in the EAP drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectEAP(String optionText){
        new Select(tableRow.findElement(QUAL_EAP_SELECT)).selectByVisibleText(optionText);
        return this;
    }
    /**
     * Selects an option in the KS4 MEASURE drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectMeasure(String optionText){
        new Select(tableRow.findElement(QUAL_MEASURE_SELECT)).selectByVisibleText(optionText);
        return this;
    }
    /**
     * Selects an option in the SPECIAL drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectSpecial(String optionText){
        new Select(tableRow.findElement(QUAL_SPECIAL_SELECT)).selectByVisibleText(optionText);
        return this;
    }
    /**
     * Selects an option in the FACULTY drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectFaculty(String optionText){
        new Select(tableRow.findElement(QUAL_FACULTY_SELECT)).selectByVisibleText(optionText);
        return this;
    }
    /**
     * Selects an option in the PREDECESSOR drop down list
     * @param optionText    The visible text of the option to be selected
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow selectPredecessor(String optionText){
        new Select(tableRow.findElement(QUAL_PREDECESSOR_SELECT)).selectByVisibleText(optionText);
        return this;
    }

    /**
     * Convenience method to set all fields for a single qual in one go;
     * Zero length strings are ignored (so care should be taken with the BAR CODE field)
     * @param name      The new value for the QUAL NAME text field
     * @param include   The new value for the INCLUDE ddl
     * @param current   The new value for the CURRENT ddl
     * @param eap       The new value for the EAP ddl
     * @param measure   The new value for the KS4 MEASURE ddl
     * @param special   The new value for the SPECIAL ddl
     * @param faculty   The new value for the FACULTY ddl
     * @param predecessor   The new value for the PREDECESSOR ddl
     * @param barCode   The new value for the BAR CODE text field
     * @return  The current MatchingQualRow object
     */
    public MatchingQualRow setRowValues(String name,
                                        String include,
                                        String current,
                                        String eap,
                                        String measure,
                                        String special,
                                        String faculty,
                                        String predecessor,
                                        String barCode){
        if (name.equals("")==false){
            this.setQualName(name);
        }
        if (include.equals("")==false){
            this.selectInclude(include);
        }
        if (current.equals("")==false){
            this.selectCurrent(current);
        }
        if (eap.equals("")==false){
            this.selectEAP(eap);
        }
        if (measure.equals("")==false){
            this.selectMeasure(measure);
        }
        if (special.equals("")==false){
            this.selectSpecial(special);
        }
        if (faculty.equals("")==false){
            this.selectFaculty(faculty);
        }
        if (predecessor.equals("")==false){
            this.selectPredecessor(predecessor);
        }
        if (barCode.equals("")==false){
            this.setBarCode(barCode);
        }
        return this;
    }

    // Initialisation code abstracted out in case we want to add a 'switchQual' method
    private void initByName(String qualName) throws IllegalArgumentException {
        int qualIndex = 0;

        List<WebElement> qualNameFields = driver.findElements(QUAL_NAME_FIELDS);
        for(WebElement nameField : qualNameFields){
            if(nameField.getAttribute("value").equals(qualName)){
                break;
            }
            qualIndex++;
        }
        if(qualIndex == 0){
            // The qualName was not found
            // Todo - deal with this properly, for now just throw an IllegalArgumentException
            throw new IllegalArgumentException("The specified Qualification was not found on the page ("+qualName+")");
        }
        initByIndex(qualIndex);
    }

    private void initByIndex(int qualIndex){
        cssIndex = qualIndex+2;
        tableRow = driver.findElement(By.cssSelector(".matchItem:nth-of-type("+cssIndex+")"));
    }

}

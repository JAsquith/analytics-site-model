package analytics.legacy.pages.helpers;

import analytics.legacy.pages.ReportViewOptions;
import org.openqa.selenium.By;

import java.util.ArrayList;

public class ViewUpdate {

    public String updateType;
    public By firstLocator;
    public By secondLocator;
    public String oldValue1;
    public String oldValue2;
    public String newValue1;
    public String newValue2;

    public ViewUpdate(){
        updateType = null;
        firstLocator = null;
        secondLocator = null;
        oldValue1 = null;
        oldValue2 = null;
        newValue1 = null;
        newValue2 = null;
    }

    public String toString(){
        String result = "ViewUpdate: {type='" + updateType + "', ";
        switch (updateType){
            case "Drill Down":
                result += "priorURL_Live='" + oldValue1 + "', ";
                result += "priorURL_Dev='" + oldValue2 + "', ";
                result += "linkIndex='" + newValue1 + "'}";
                break;
            case "View Option":
                if (oldValue1 == null || newValue1 == null){
                    result += "labelLocator='" + firstLocator + "', ";
                    result += "checkBoxLocator='" + secondLocator + "', ";
                    if (oldValue1 == null)
                        result += "toggledTo='checked'}";
                    else
                        result += "toggledTo='unchecked'}";
                } else {
                    result += "selectLocator='" + firstLocator + "', ";
                    result += "from='" + oldValue1 + "', ";
                    result += "to='" + newValue1 + "'}";
                }
                break;
            case "Column Sort":
                result += "colIndex='" + newValue1 + "'}";
                break;
            case "Add Filter":
                result += "groupLocator='" + firstLocator + "', ";
                result += "valueLocator='" + secondLocator + "', ";
                result += "groupTitle='" + newValue1 + "', ";
                result += "valueLabel='" + newValue2 + "'}";
            default:
                // throw exception?
        }
        return result;
    }

    public void undo(ReportViewOptions viewOptions){
        ArrayList<ViewUpdate> history = new ArrayList<>(1);
        history.add(this);
        viewOptions.rollbackUpdates(history, false);
    }

}

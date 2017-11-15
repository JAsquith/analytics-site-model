package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Represents the contents and interactive elements common to any KS3/4 Report page
 */
public class EAPHeadlineView extends EAPView {

    // CONSTRUCTORS
    public EAPHeadlineView(RemoteWebDriver aDriver){
        super(aDriver);
    }

    // QUERYING THE CURRENT PAGE STATE
    public String readSectionData(String section){
        String csvText;
        String js = getExtractSectionJs(section);
        try {
            driver.executeScript(js);
        } catch (JavascriptException jse){
            throw new AssertionError("Javascript Execution failure ("
                    + jse.getMessage() + ". Script: [" + js + "]");
        }
        csvText = driver.findElement(By.id("se-table-data")).getText();
        js = "var elem = document.getElementById('se-table-data');" +
                "elem.parentNode.removeChild(elem);";
        try {
            driver.executeScript(js);
        } catch (JavascriptException jse){
            /* we've got what we want, and any action/page refresh will get rid, so we don't really care! */
        }
        return csvText;
    }

    private String getExtractSectionJs(String sectionName){
        String mainJS = "oldDiv = document.querySelector('#se-table-data');" +
                "if (oldDiv != null) {oldDiv.parentNode.removeChild(oldDiv);}" +
                "titlesLocator = '.tableTitle';tablesLocator = '.rpt.headlines';" +
                "titles = document.querySelectorAll(titlesLocator);" +
                "tables = document.querySelectorAll(tablesLocator);" +
                "var title; var table;" +
                "for (i=0; i<titles.length; i++){" +
                "if(titles[i].textContent.trim()==sectionName){" +
                "title = titles[i];table = tables[i];}}" +
                "tableText = '';" +
                "rows = table.querySelectorAll('tr:not(.fakeTR)');" +
                "for (i=0; i<rows.length; i++){" +
                " cells = rows[i].querySelectorAll('th,td');" +
                "  for (j=0; j<cells.length; j++){" +
                "  tableText += cells[j].textContent.trim()+',';}" +
                " tableText = tableText.substring(0, tableText.length-1)+'<br>';" +
                "}" +
                "newDiv = document.createElement('DIV');" +
                "newDiv.setAttribute('id', 'se-table-data');" +
                "newDiv.innerHTML = tableText;" +
                "document.querySelector('#layoutNotif').appendChild(newDiv);";

        return "sectionName = '" + sectionName + "';" +
                mainJS;

    }
}

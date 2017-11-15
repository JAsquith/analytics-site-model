package pages.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Represents the contents and interactive elements common to any KS3/4 Report page
 */
public class EAPListView extends EAPView {

    // CONSTRUCTORS
    public EAPListView(RemoteWebDriver aDriver){
        super(aDriver);
    }

    // QUERYING THE CURRENT PAGE STATE
    public String readTableData(String tableName) {
        int tableIndex = this.findNamedTableIndex(tableName);
        switch (tableIndex){
            case -1:
                return "Table [" + tableName + "] not found";
            case -2:
                return "No tables found - check Report Options";
            default:
                return this.extractTableData(tableIndex);
        }
    }

    public String readTableData(){
        return this.extractMultiTableData();
    }

    public String readColumnData(String tableName, String columnName){
        int tableIndex = this.findNamedTableIndex(tableName);
        switch (tableIndex) {
            case -1:
                return "Table [" + tableName + "] not found";
            case -2:
                return "No tables found - check Report Options";
            default:
                return this.extractColumnData(tableIndex, columnName);
        }
    }
    public String readColumnData(String columnName){
        return this.extractColumnData(1, columnName);
    }

    // PRIVATE HELPER METHODS FOR THE ABOVE PUBLIC METHODS
    private String extractColumnData(int tableIndex, String columnTitle){
        String csvText;
        String js = getExtractColumnJs(tableIndex, columnTitle);
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

    private String extractColumnData(String columnTitle){
        String csvText="";
        List<WebElement> tableHeadings = driver.findElements(By.cssSelector(".tableTitle"));
        for (WebElement tabHeading : tableHeadings){
            if(!csvText.equals("")){
                csvText += System.lineSeparator();
            }
            int tableIndex = findNamedTableIndex(tabHeading.getText().trim());
            String js = getExtractColumnJs(tableIndex, columnTitle);
            try {
                driver.executeScript(js);
            } catch (JavascriptException jse){
                throw new AssertionError("Javascript Execution failure ("
                        + jse.getMessage() + ". Script: [" + js + "]");
            }
            csvText += driver.findElement(By.id("se-table-data")).getText();
            js = "var elem = document.getElementById('se-table-data');" +
                    "elem.parentNode.removeChild(elem);";
            try {
                driver.executeScript(js);
            } catch (JavascriptException jse){
            /* we've got what we want, and any action/page refresh will get rid, so we don't really care! */
            }
        }
        return csvText;
    }

    private String extractTableData(int tableIndex){

        String tableLocator = "table.rpt.stickyHead:nth-of-type(" + tableIndex + ")";

        String js = "var tableText = '';" +
                "var tableRowElements = document.querySelectorAll('" + tableLocator + " tr');" +
                "for (var i=0; i < tableRowElements.length; i++){" +
                "var rowCellElements = tableRowElements[i].querySelectorAll('td,th');" +
                "if (rowCellElements.length > 0) {" +
                "var rowText = rowCellElements[0].textContent.trim();" +
                "for (var j=1; j < rowCellElements.length; j++){" +
                "rowText += ',' + rowCellElements[j].textContent.trim();}" +
                "tableText += rowText + '<br>';}}" +
                "var newDiv = document.createElement('DIV');" +
                "newDiv.setAttribute ('id', 'se-table-data');" +
                "newDiv.innerHTML = tableText;" +
                "document.querySelector('#layoutNotif').appendChild(newDiv);";

        String tableData;
        try {
            driver.executeScript(js);
            tableData = driver.findElement(By.id("se-table-data")).getText();

            js = "var elem = document.getElementById('se-table-data');" +
                    "elem.parentNode.removeChild(elem);";
            driver.executeScript(js);
        }catch(JavascriptException jse){
            throw new AssertionError("Named Table - Javascript Execution failure ("
                    + jse.getMessage() + ". Script: [" + js + "]");
        }

        return tableData;
    }

    private String extractMultiTableData(){

        String js = "var allTitles = document.querySelectorAll('.tableTitle');" +
                "var allTables = document.querySelectorAll('table.rpt.stickyHead');" +
                "var tableCount = allTitles.length;" +
                "var allText = '';" +
                "for (var t=0; t < tableCount; t++){" +
                "var tableText = allTitles[t].textContent.trim()+'<br>';" +
                "var tableRowElements = allTables[t].querySelectorAll('tr');" +
                "for (var i=0; i < tableRowElements.length; i++){" +
                "var rowCellElements = tableRowElements[i].querySelectorAll('td,th');" +
                "if (rowCellElements.length > 0) {" +
                "var rowText = rowCellElements[0].textContent.trim();" +
                "for (var j=1; j < rowCellElements.length; j++){" +
                "rowText += ',' + rowCellElements[j].textContent.trim();}" +
                "tableText += rowText + '<br>';}}" +
                "allText += tableText}" +
                "var newDiv = document.createElement('DIV');" +
                "newDiv.setAttribute ('id', 'se-table-data');" +
                "newDiv.innerHTML = allText;" +
                "document.querySelector('body>*:not(script)').appendChild(newDiv);";

        String tableData;
        try {
            driver.executeScript(js);
            tableData = driver.findElement(By.id("se-table-data")).getText();

            js = "var elem = document.getElementById('se-table-data');" +
                    "elem.parentNode.removeChild(elem);";
            driver.executeScript(js);
        }catch(JavascriptException jse){
            throw new AssertionError("Multi Table - Javascript Execution failure ("
                    + jse.getMessage() + ". Script: [" + js + "]");
        }

        return tableData;
    }

    private String getExtractColumnJs(int tableIndex, String columnTitle){

/*

oldDiv = document.querySelector('#se-table-data');
if (oldDiv != null) {  oldDiv.parentNode.removeChild(oldDiv);}
locator = 'table.rpt.stickyHead:nth-of-type(' + tableIndex + ')';
tableText = '';colIndexes = [];
tableRowElements = document.querySelectorAll(locator + ' tr');
titles = tableRowElements[0].querySelectorAll('th');
if (titles[0].textContent.trim() == '') {
  colIndexes.push(0);
  tableText = ','
}
for (i = 0; i < titles.length; i++) {
  colTitle = titles[i].textContent.trim();
  colTitle = colTitle.replace(/(\?\s+)/, '').trim();
  if (colTitle == 'Name' || colTitle == columnName || colTitle == 'Filter Value') {
    colIndexes.push(i);
    if (tableText != '' && tableText != ',') {tableText += ',';}
    tableText += colTitle;
  }
}
tableText += '<br>';
for (i = 1; (i < tableRowElements.length); i++) {
  rowText = '';
  rowCellElements = tableRowElements[i].querySelectorAll('td,th');
  if (rowCellElements.length > 0) {
    for (j = 0; j < colIndexes.length; j++) {
      if (j != 0) { rowText += ',';}
      rowText += rowCellElements[colIndexes[j]].textContent.trim();
    }
  }
  tableText += rowText + '<br>';
}
newDiv = document.createElement('DIV');
newDiv.setAttribute('id', 'se-table-data');
newDiv.innerHTML = tableText;
document.querySelector('body>*:not(script)').appendChild(newDiv);

*/

        String mainJS = "oldDiv = document.querySelector('#se-table-data');";
        mainJS += "if (oldDiv != null) {  oldDiv.parentNode.removeChild(oldDiv);}";
        mainJS += "locator = 'table.rpt.stickyHead:nth-of-type(' + tableIndex + ')';";
        mainJS += "tableText = '';colIndexes = [];";
        mainJS += "tableRowElements = document.querySelectorAll(locator + ' tr');";
        mainJS += "titles = tableRowElements[0].querySelectorAll('th');";
        mainJS += "if (titles[0].textContent.trim() == '') {";
        mainJS += "colIndexes.push(0);tableText = ','}";
        mainJS += "for (i = 0; i < titles.length; i++) {";
        mainJS += "colTitle = titles[i].textContent.trim();";
        mainJS += "colTitle = colTitle.replace(/(\\?\\s+)/, '').trim();";
        mainJS += "if (colTitle == 'Name' || colTitle == columnName || colTitle == 'Filter Value') {";
        mainJS += "colIndexes.push(i);";
        mainJS += "if (tableText != '' && tableText != ',') {tableText += ',';}";
        mainJS += "tableText += colTitle;}}";
        mainJS += "tableText += '<br>';";
        mainJS += "for (i = 1; (i < tableRowElements.length); i++) {";
        mainJS += "rowText = '';";
        mainJS += "rowCellElements = tableRowElements[i].querySelectorAll('td,th');";
        mainJS += "if (rowCellElements.length > 0) {";
        mainJS += "for (j = 0; j < colIndexes.length; j++) {";
        mainJS += "if (j != 0) { rowText += ',';}";
        mainJS += "rowText += rowCellElements[colIndexes[j]].textContent.trim();}}";
        mainJS += "tableText += rowText + '<br>';}";
        mainJS += "newDiv = document.createElement('DIV');";
        mainJS += "newDiv.setAttribute('id', 'se-table-data');";
        mainJS += "newDiv.innerHTML = tableText;";
        mainJS += "document.querySelector('body').appendChild(newDiv);";

        return "tableIndex = '" + tableIndex + "';" +
                "columnName = '" + columnTitle + "';" +
                mainJS;

    }
}

function extractNamedColumn_StandardEAPViews(tableIndex, columnName){
  var locator = 'table.rpt.stickyHead:nth-of-type(' + tableIndex + ')';
  var tableText = '';
  var tableRowElements = document.querySelectorAll(locator + ' tr');
  
  var titles = tableRowElements[0].querySelectorAll('th');
  // flag the columns to export:
  //    'Name' holds the Filter/Faculty/Qual/Class/Student name
  //    columnName is the one we want to check
  //    on Students level, we also want the unnamed first column
  
  for (var i=0; (i < tableRowElements.length && i < 25); i++){
    var rowCellElements = tableRowElements[i].querySelectorAll('td,th');
    if (rowCellElements.length > 0) {
      var rowText = rowCellElements[0].textContent.trim();
      for (var j=1; j < rowCellElements.length; j++){
        rowText += ',' + rowCellElements[j].textContent.trim();
      }
    }
    tableText += rowText + '<br>';
  }
  return tableText;
}

var tableText = extractColumn(1);
var newDiv = document.createElement('DIV');
newDiv.setAttribute ('id', 'se-table-data');
newDiv.innerHTML = tableText;
document.querySelector('body>*:not(script)').appendChild(newDiv);

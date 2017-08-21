function SelectText(element) {
    var doc = document
        , text = doc.getElementById(element)
        , range, selection
    ;    
    if (doc.body.createTextRange) {
        range = document.body.createTextRange();
        range.moveToElementText(text);
        range.select();
    } else if (window.getSelection) {
        selection = window.getSelection();        
        range = document.createRange();
        range.selectNodeContents(text);
        selection.removeAllRanges();
        selection.addRange(range);
    }
}

tableIndex = window.prompt('Enter the table index \n(Always 1 except on the Grades Overview report)', '1');
columnName = window.prompt('Enter the **exact** name of the column to be exported');

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
SelectText('se-table-data');
tableIndex = '1'; columnName='In A8 Basket';
var oldDiv = document.querySelector('#se-table-data');
if (oldDiv!=null){oldDiv.parentNode.removeChild(oldDiv);}

locator = 'table.rpt.stickyHead:nth-of-type(' + tableIndex + ')';
tableText = '';  rowText = ''; colIndexes = [];
tableRowElements = document.querySelectorAll(locator + ' tr');

titles = tableRowElements[0].querySelectorAll('th');
if (titles[0].textContent.trim()==''){colIndexes.push(0);tableText=','}
for (i=0; i<titles.length; i++){
  colTitle=titles[i].textContent.trim().replace(/(\?)/, '').trim();
  if(colTitle=='Name' || colTitle==columnName){
    colIndexes.push(i);
    if(tableText!=''&&tableText!=','){tableText+=',';}
    tableText += colTitle;
  }
}
tableText += rowText + '<br>';
for (i=1; (i < tableRowElements.length); i++){
  rowText = '';
  rowCellElements = tableRowElements[i].querySelectorAll('td,th');
  if (rowCellElements.length > 0) {
    for (j=0; j<colIndexes.length; j++){
      if(j!=0){rowText+=',';}
      rowText += rowCellElements[colIndexes[j]].textContent.trim();
    }
  }
  tableText += rowText + '<br>';
}

newDiv = document.createElement('DIV');
newDiv.setAttribute ('id', 'se-table-data');
newDiv.innerHTML = tableText;
document.querySelector('body>*:not(script)').appendChild(newDiv);

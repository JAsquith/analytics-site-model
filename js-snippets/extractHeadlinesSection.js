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

sectionName = window.prompt('Which section would you like to export?','Cohort Summary');

oldDiv = document.querySelector('#se-table-data');
if (oldDiv != null) {
  oldDiv.parentNode.removeChild(oldDiv);
}

titlesLocator = '.tableTitle';
tablesLocator = '.rpt.headlines';

titles = document.querySelectorAll(titlesLocator);
tables = document.querySelectorAll(tablesLocator);
var title; var table;

for (i=0; i<titles.length; i++){
  if(titles[i].textContent.trim()==sectionName){
    title = titles[i];
    table = tables[i];
  }
}

tableText = '';
rows = table.querySelectorAll('tr:not(.fakeTR)');
for (i=0; i<rows.length; i++){
  cells = rows[i].querySelectorAll('th,td');
  for (j=0; j<cells.length; j++){
    tableText += cells[j].textContent.trim()+','
  }
  tableText = tableText.substring(0, tableText.length-1)+'<br>';
}
newDiv = document.createElement('DIV');
newDiv.setAttribute('id', 'se-table-data');
newDiv.innerHTML = tableText;
document.querySelector('body>*:not(script)').appendChild(newDiv);
SelectText('se-table-data');
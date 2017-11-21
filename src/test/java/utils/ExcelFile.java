package utils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelFile {

    private String xlDir;
    private XSSFWorkbook xlWorkbook;

    public ExcelFile(String xlFilePath) throws IOException
    {
        String currDir = System.getProperty("user.dir");
        currDir = (currDir.endsWith(File.separator)) ? currDir : currDir + File.separator;
        xlDir = currDir + "test-resources" + File.separator + "Excel" + File.separator;

        FileInputStream fis = new FileInputStream(xlDir + xlFilePath);
        xlWorkbook = new XSSFWorkbook(fis);
        fis.close();
    }

    public XSSFWorkbook getXlWorkbook()
    {
        return xlWorkbook;
    }

    public int getRowCount(String sheetName)
    {
        return xlWorkbook.getSheet(sheetName).getLastRowNum() + 1;
    }

    public int getColumnCount(String sheetName)
    {
        return xlWorkbook.getSheet(sheetName).getRow(0).getLastCellNum();
    }


}

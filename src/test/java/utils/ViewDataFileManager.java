package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewDataFileManager extends FileManager {

    protected String dirTableData;

    public ViewDataFileManager(){
        super();
        dirTableData = dirProject + "test-resources" + File.separator + "table-data" + File.separator;
    }

    public String getTableDataDirectory() {
        return dirTableData;
    }
    public String getFullTableDataPath(String filename){
        return dirTableData + filename;
    }

    public Boolean createTableDataFileWithData(String folder, String fileName, String csvData) {
        String filePath = checkTableDataDirExists(folder) + fileName;
        return createFileWithData(filePath, csvData);
    }

    public int findFileDifference(String expectedFileName, String actSubDir, String diffSubDir){

        if(!actSubDir.endsWith(File.separator)){
            actSubDir += File.separator;
        }

        String actFileName = checkTableDataDirExists(actSubDir) + expectedFileName;

        BufferedReader brExpected;
        BufferedReader brActual;

        brExpected = openTableDataFileForReading(expectedFileName);
        brActual = openTableDataFileForReading(actFileName);

        FileOutputStream fosDifferences = null;

        int lineNum = 1;
        int diffLines = 0;

        String lineExpected = readNextLine(brExpected);
        String lineActual = readNextLine(brActual);
        while (lineExpected != null && lineActual != null){

            String difference = compareLines(lineNum, expectedFileName, actFileName, lineExpected, lineActual);

            if (!difference.equals("")){
                if (diffLines == 0) {
                    fosDifferences = createTableDataFileForOutput(diffSubDir, expectedFileName);
                    writeCompareHeader(fosDifferences, expectedFileName, actFileName);
                }
                diffLines++;
                updateDiffFile(fosDifferences, difference);
            }

            lineExpected = readNextLine(brExpected);
            lineActual = readNextLine(brActual);
            lineNum++;
        }

        if (lineExpected != null){
            if (diffLines == 0) {
                fosDifferences = createTableDataFileForOutput(diffSubDir, expectedFileName);
                writeCompareHeader(fosDifferences, expectedFileName, actFileName);
            }
            lineActual = "ERROR - Beyond Last Expected Row!";
            while (lineExpected != null){
                String difference = compareLines(lineNum, expectedFileName, actFileName, lineExpected, lineActual);
                diffLines++;
                updateDiffFile(fosDifferences, difference);
                lineExpected = readNextLine(brExpected);
            }
        }

        if (lineActual != null){
            if (diffLines == 0) {
                fosDifferences = createTableDataFileForOutput(diffSubDir, expectedFileName);
                writeCompareHeader(fosDifferences, expectedFileName, actFileName);
            }
            lineExpected = "ERROR - Beyond Last Actual Row!";
            while (lineActual != null){
                String difference = compareLines(lineNum, expectedFileName, actFileName, lineExpected, lineActual);
                diffLines++;
                updateDiffFile(fosDifferences, difference);
                lineActual = readNextLine(brActual);
            }
        }

        try {
            if (diffLines > 0) {
                fosDifferences.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diffLines;
    }

    protected BufferedReader openTableDataFileForReading(String fileName) {
        return openFileForReading(fileName, dirTableData);
    }

    protected FileOutputStream createTableDataFileForOutput(String folder, String fileName){
        if (!folder.endsWith(File.separator)){
            folder += File.separator;
        }
        if (!folder.startsWith(dirTableData)){
            folder = dirTableData+folder;
        }
        String filePath = checkTableDataDirExists(folder) + fileName;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(filePath));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return fos;
    }

    protected void updateDiffFile(FileOutputStream fosDifferences, String difference){
        try {
            fosDifferences.write(difference.getBytes());
            fosDifferences.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    protected String compareLines(int lineNum, String expFileName, String actFileName,
                                  String lineExpected, String lineActual){
        String difference = "";
        if (!lineExpected.equals(lineActual)){
            difference = lineNum + ",Expected (" + expFileName + ")," + lineExpected + NEW_LINE;
            difference += lineNum + ",Actual" + "," + lineActual + NEW_LINE;
        }
        return difference;
    }

    protected void writeCompareHeader(FileOutputStream out, String fileName_Expected, String fileName_Actual){
        String line = "Comparing Lines in Expected and Actual files." + NEW_LINE;
        line += "'=============================================" + NEW_LINE;
        line += "Expected: " + fileName_Expected + NEW_LINE;
        line += "Actual: " + fileName_Actual + NEW_LINE;
        line += "'=============================================" + NEW_LINE;
        line += "line,Expected/Actual,line data --->" + NEW_LINE;
        try {
            out.write(line.getBytes());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    protected String checkTableDataDirExists(String folder) {
        return checkDirExists(folder, dirTableData);
    }


}

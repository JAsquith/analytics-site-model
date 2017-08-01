package utils;

import java.io.*;

public class TableDataFileManager {

    static final String NEW_LINE = System.getProperty("line.separator");

    public String getDirTableData() {
        return dirTableData;
    }

    private String dirTableData;

    public TableDataFileManager(){
        String currDir = System.getProperty("user.dir");
        dirTableData = (currDir.endsWith(File.separator)) ? currDir : currDir + File.separator;
        dirTableData += "test-resources" + File.separator + "table-data" + File.separator;
    }

    public BufferedReader openFileForReading(String fileName) {

        BufferedReader br;
        try {
            if(!fileName.startsWith(dirTableData)){
                fileName = dirTableData + fileName;
            }
            br = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return br;
    }

    public String readNextLine(BufferedReader br){

        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return line;
    }

    public Boolean createFileWithData(String folder, String fileName, String csvData) {

        String filePath = checkDirExists(folder) + fileName;

        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            fos.write(csvData.getBytes());
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private FileOutputStream createFileForOutput(String folder, String fileName){
        String filePath = checkDirExists(folder) + fileName;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(filePath));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return fos;
    }

    public int findFileDifference(String expectedFileName, String actSubDir, String diffSubDir){

        if(!actSubDir.endsWith(File.separator)){
            actSubDir += File.separator;
        }

        String actFileName = checkDirExists(actSubDir) + expectedFileName;

        BufferedReader brExpected;
        BufferedReader brActual;

        brExpected = openFileForReading(expectedFileName);
        brActual = openFileForReading(actFileName);

        FileOutputStream fosDifferences = null;

        int lineNum = 1;
        int diffLines = 0;

        String lineExpected = readNextLine(brExpected);
        String lineActual = readNextLine(brActual);
        while (lineExpected != null && lineActual != null){

            String difference = compareLines(lineNum, expectedFileName, actFileName, lineExpected, lineActual);

            if (!difference.equals("")){
                if (diffLines == 0) {
                    fosDifferences = createFileForOutput(diffSubDir, expectedFileName);
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
                fosDifferences = createFileForOutput(diffSubDir, expectedFileName);
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
                fosDifferences = createFileForOutput(diffSubDir, expectedFileName);
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

    private void updateDiffFile(FileOutputStream fosDifferences, String difference){
        try {
            fosDifferences.write(difference.getBytes());
            fosDifferences.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private String compareLines(int lineNum, String expFileName, String actFileName,
                                String lineExpected, String lineActual){
        String difference = "";
        if (!lineExpected.equals(lineActual)){
            difference = lineNum + ",Expected (" + expFileName + ")," + lineExpected + NEW_LINE;
            difference += lineNum + ",Actual" + "," + lineActual + NEW_LINE;
        }
        return difference;
    }

    private void writeCompareHeader(FileOutputStream out, String fileName_Expected, String fileName_Actual){
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

    private String checkDirExists(String folder){
        String dirPath = dirTableData + folder;
        File directory = new File(String.valueOf(dirPath));
        directory.mkdir();

        if(dirPath.length()>0 && !dirPath.endsWith(File.separator)){
            dirPath += File.separator;
        }
        return dirPath;
    }
}
package utils;

import java.io.*;

public class FileManager {

    static final String NEW_LINE = System.getProperty("line.separator");

    private String dirProject;

    private String dirTableData;
    private String dirTarget;
    public FileManager(){
        String currDir = System.getProperty("user.dir");
        dirProject = (currDir.endsWith(File.separator)) ? currDir : currDir + File.separator;
        dirTarget = dirProject + File.separator + "target" + File.separator;
        dirTableData = dirProject + "test-resources" + File.separator + "table-data" + File.separator;
    }

    // Getters & Setters for private data members
    public String getTableDataDirectory() {
        return dirTableData;
    }
    public String getTargetDirectory(){
        return dirTarget;
    }


    // Used in new tests (classes that extend BaseTest)
    public String getFullTableDataPath(String filename){
        return dirTableData + filename;
    }
    public Boolean createTableDataFileWithData(String filePath, String csvData){
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
    public FileOutputStream createTargetFileForOutput(String folder, String fileName){
        return createFileForOutput(dirTarget+folder, fileName);
    }
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if(endsWithoutNewLine) {
                ++count;
            }
            return count;
        } finally {
            is.close();
        }
    }

    // Used in old tests (classes that extend SISRATest)
    public Boolean createTableDataFileWithData(String folder, String fileName, String csvData) {
        String filePath = checkTableDataDirExists(folder) + fileName;
        return createTableDataFileWithData(filePath, csvData);
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



    // Private Methods
    private FileOutputStream createFileForOutput(String folder, String fileName){
        if (!folder.endsWith(File.separator)){
            folder += File.separator;
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
    private String readNextLine(BufferedReader br){

        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return line;
    }

    private BufferedReader openFileForReading(String fileName, String parentDir) {

        BufferedReader br;
        try {
            if(!fileName.startsWith(parentDir)){
                fileName = parentDir + fileName;
            }
            br = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return br;
    }

    private BufferedReader openTableDataFileForReading(String fileName) {
        return openFileForReading(fileName, dirTableData);
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

    private String checkTableDataDirExists(String folder) {
        return checkDirExists(folder, dirTableData);
    }

    private String checkDirExists(String folder, String parentDir){
        String dirPath = parentDir + folder;
        File directory = new File(String.valueOf(dirPath));
        directory.mkdir();

        if(dirPath.length()>0 && !dirPath.endsWith(File.separator)){
            dirPath += File.separator;
        }
        return dirPath;
    }

}
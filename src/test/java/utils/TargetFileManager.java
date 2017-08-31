package utils;

import java.io.*;

public class TargetFileManager extends FileManager {

    protected String dirTarget;

    public TargetFileManager(){
        super();
        dirTarget = dirProject + "target" + File.separator;
    }

    public boolean fileExistsInTargetDir(String folder, String fileName){
        folder += folder.endsWith(File.separator) ? "" : File.separator;
        String fullPath = dirTarget + folder + fileName;
        return fileExists(fullPath);
    }

    public FileOutputStream openTargetFileForOutput(String folder, String fileName){
        return openFileForOutput(folder, fileName, dirTarget, true);
    }

    public boolean testPassed(String testId){
        String passesFilePath = dirTarget+"sisra-results"+File.separator+"All_Passes.csv";
        if (!fileExistsInTargetDir("sisra-results", "All_Passes.csv")){
            return false;
        }
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(passesFilePath));
            String line = br.readLine();
            while(line != null){
                if(line.equals(testId)){
                    return true;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.err.println("Exception reading All_Passes.csv");
            e.printStackTrace();
            return false;
        }

        return false;
    }

}

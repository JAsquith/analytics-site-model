package utils;

import java.io.File;
import java.io.FileOutputStream;

public class TargetFileManager extends FileManager {

    protected String dirTarget;

    public TargetFileManager(){
        super();
        dirTarget = dirProject + File.separator + "target" + File.separator;
    }

    public boolean fileExistsInTargetDir(String folder, String fileName){
        String fullPath = dirTarget + folder + fileName;
        return fileExists(fullPath);
    }

    public FileOutputStream openTargetFileForOutput(String folder, String fileName){
        return openFileForOutput(folder, fileName, dirTarget, true);
    }

    public FileOutputStream createTargetFileForOutput(String folder, String fileName){
        return openFileForOutput(folder, fileName, dirTarget);
    }

}

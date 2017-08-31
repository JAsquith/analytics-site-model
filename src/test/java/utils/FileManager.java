package utils;

import java.io.*;

public class FileManager {

    static final String NEW_LINE = System.getProperty("line.separator");

    protected String dirProject;

    public FileManager(){
        String currDir = System.getProperty("user.dir");
        dirProject = (currDir.endsWith(File.separator)) ? currDir : currDir + File.separator;
    }

    // Used in new tests (classes that extend BaseTest)
    public Boolean createFileWithData(String filePath, String data){
        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
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

    protected FileOutputStream openFileForOutput(String folder, String fileName, String parentDir, boolean append){
        String filePath = checkDirExists(folder, parentDir) + fileName;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(filePath), append);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return fos;
    }

    protected String readNextLine(BufferedReader br){

        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return line;
    }

    protected BufferedReader openFileForReading(String fileName, String parentDir) {

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

    protected String checkDirExists(String folder, String parentDir){
        String dirPath = parentDir + folder;
        File directory = new File(String.valueOf(dirPath));
        directory.mkdir();

        if(dirPath.length()>0 && !dirPath.endsWith(File.separator)){
            dirPath += File.separator;
        }
        return dirPath;
    }

    public boolean fileExists(String fullPath){
        File f = new File(fullPath);
        if(f.exists()) {
            return true;
        }
        return false;
    }

}
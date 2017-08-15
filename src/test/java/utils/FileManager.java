package utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A helper class for manipulating files uploaded/downloaded by Analytcs tests.
 */
public class FileManager implements FilenameFilter {

    private String dataType = "";

    public File[] getUploadFiles(String forKeyStage, String forAdminYear, String uploadType)
            throws URISyntaxException{
        dataType = uploadType.toLowerCase();
        String resourceDir = "Uploads/KS"+forKeyStage+"/Cohort"+forAdminYear+"/" + uploadType;
        URL uploadFileURL = this.getClass().getClassLoader().getResource(resourceDir);
        URI uploadFileURI = uploadFileURL.toURI();
        return new File(uploadFileURI).listFiles(this);
    }

    public String getViewListJSONFileAsString(String fileName){
        String resourceDir = "ViewLists/";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceDir+fileName);

        String result = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (dataType.equals("")) {
            return name.toLowerCase().endsWith(".csv");
        }
        return name.toLowerCase().contains(dataType) && name.toLowerCase().endsWith(".csv");
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

}

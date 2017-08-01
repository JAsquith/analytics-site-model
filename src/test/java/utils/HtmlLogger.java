package utils;

import utils.enums.LogLevel;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class HtmlLogger implements FilenameFilter {

    /**
     * @deprecated since this class is no longer a singleton, use {@code this} instead
     */
    @Deprecated
    private HtmlLogger logger;

    /**
     * Buffer for messages written to the log file
     */
    private BufferedWriter logWriter;

    /**
     * Location of the html log file
     */
    private String logDir;

    /**
     * Name set by the consumer
     * - it is their responsibility to ensure they do not create more
     * than one logger with the same name in the same namespace/directory
     */
    private String logName;

    /**
     * Simple name of the html log file within the logDir directory
     * Built using the logName plus ".html"
     */
    private String logFile;

    /**
     * Replicates the logName for use as a prefix to screenshot files.
     * This field should be phased out in favour of always using the logName
     */
    private String scrShotPrefix = null;

    /**
     * Defines the allowable Log Levels
     */
    public enum Level {DEBUG(0,"DEBUG"), INFO(10,"INFO"), WARN(20,"WARN"), ERROR(30,"ERROR");
        public int index; public String label;
        Level(int idx, String label){this.index = idx;this.label = label;}
    }

    /**
     * A valid Level
     * - messages whose Level.index returns a value lower than currentLevel.index
     * are not written to the file
     */
    private LogLevel currentLevel = LogLevel.INFO;
    /**
     * The format used to write entries to the time column
     */
    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     *  @param directory The directory to create the log file in. If null
     *                  or "" is passed in, the "/logs" sub-directory of
     *                  the current directory (user.dir) is used.
     * @param level     An initial value for what level of logging to perform
     * @param name      If specified, this value will be used in place of the
     *
     */
    public HtmlLogger(String directory, LogLevel level, String name){
        logName = name;
        scrShotPrefix = logName;
        currentLevel = level;
        logDir = "";
        if (directory != null)
            logDir = directory;
        logDir = (logDir.endsWith(File.separator)) ? logDir : logDir + File.separator;

        if (logDir.equals("")) {
            logDir = System.getProperty("user.dir");
            logDir = logDir + "logs" + File.separator;
        }
        File logDirFile = new File(String.valueOf(logDir));
        logDirFile.mkdir();

        logFile = logDir + logName + ".html";
        File file = new File(logFile);
        try {
            logWriter = new BufferedWriter(new FileWriter(file));
            logWriter.write("<!DOCTYPE HTML>");
            writeStyle();
            logWriter.write("<html><body><table id='headers'><tbody><tr>"
                    + "<th class='time'>Time</th>"
                    + "<th class='level'>Level</th>"
                    + "<th class='test'>Test ID</th>"
                    + "<th class='index'>Index</th>"
                    + "<th class='window'>Window</th>"
                    + "<th class='message'>Message</th>" +
                    "</tr></tbody></table><table id='messages'><tbody>"
            );
            logWriter.newLine();
            write(0, null, Level.INFO, "", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStyle() throws IOException {
        logWriter.write("<style style=\"text/css\">");
        logWriter.write("table {width:100%; border-collapse:collapse;}");
        logWriter.write("#headers {position: fixed;top:7;left:7;}");
        logWriter.write("th {background-color: #C0C0C0;}");
        logWriter.write("td, th{padding-left:7px; padding-top: 7px; border:#4e95f4 1px solid;}");
        logWriter.write("tr{background: #b8d1f3;}");
        logWriter.write(".warn {font-weight: bold;}");
        logWriter.write(".error {background-color: #CC0033;color: white;font-weight: bold;}");
        logWriter.write("tr:hover {background-color: #ffff99;color: black;}");
        logWriter.write(".time {width: 70px;} .index {width: 50px;} .window {width: 70px;}");
        logWriter.write(".level {width: 70px;}.test {width: 75px;}");
        logWriter.write("</style></head>");
    }

    public String getLogDirectory(){return logDir;}
    public String getLogFileName(){return logName+".html";}
    public String getLogName(){return logName;}
    public synchronized void setLevel(LogLevel newLevel){currentLevel = newLevel;}
    public LogLevel getLevel(){return currentLevel;}
    public int getLevelInt(){return currentLevel.index;}

//    @SISRATest
//    public static void testDir(){
//        System.out.println(System.getProperty("user.dir"));
//    }

    public void debug(String testId, String message) throws IOException {
        debug(1, "Main", testId, message);
    }

    public void debug(int index, String window, String testId, String message) throws IOException {
        if(currentLevel.index <= Level.DEBUG.index)
            write(index, window, Level.DEBUG, testId, message);
    }

    public void info(String testId, String message) throws IOException {
        info(1, "Main", testId, message);
    }

    public void info(int index, String window, String testId, String message) throws IOException {
        if(currentLevel.index <= Level.INFO.index)
            write(index, window, Level.INFO, testId, message);
    }

    public void warn(String testId, String message) throws IOException {
        warn(1, "Main", testId, message);
    }

    public void warn(int index, String window, String testId, String message) throws IOException {
        if(currentLevel.index <= Level.WARN.index)
            write(index, window, Level.WARN, testId, message);
    }

    public void error(String testId, String message) throws IOException {
        error(1, "Main", testId, message);
    }

    public void error(int index, String window, String testId, String message) throws IOException {
        if(currentLevel.index <= Level.ERROR.index)
            write(index, window, Level.ERROR, testId, message);
    }

    public void takeScreenshot(RemoteWebDriver driver, String testId, String fileName) {
        takeScreenshot(driver, 1, "Main", testId, fileName);
    }

    public void takeScreenshot(RemoteWebDriver driver, int index, String window, String testId, String fileName) {
        takeScreenshot(driver, index, window, Level.INFO, testId, fileName);
    }

    public void takeScreenshot(RemoteWebDriver driver, int index, String window,
                               Level level, String testId, String fileName) {

        // Skip if the current level is greater than the requested one
        // e.g. if current = INFO and requested is DEBUG, don't take a screenshot
        if (currentLevel.index > level.index)
            return;

        if (scrShotPrefix == null)
            scrShotPrefix = logName;

        try {
            // Get the screenshot data
            //WebDriver augmentedDriver = new Augmenter().augment(driver);
            //TakesScreenshot ss = (TakesScreenshot) augmentedDriver;
            String base64Screenshot = driver.getScreenshotAs(OutputType.BASE64);
            byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot.getBytes());

            // Create the file and write the data to it
            fileName = scrShotPrefix +"_"+fileName;
            if (!fileName.toLowerCase().endsWith(".png"))
                fileName = fileName+".png";
            File file = new File(logDir+fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedScreenshot);
            fos.close();

            // Write a log message with a link to the screenshot file
            write(index, window, level, testId,
                    "Screenshot saved to <a href='./"+fileName+"' target='_blank'>"+fileName+"</a>");

        } catch (Exception e) {
            System.out.println("An exception was encountered while taking a screenshot.");
            System.out.println("Please see the stack trace info below.");
            e.printStackTrace();
        }
    }


    public void writeCSVFile(int index, String window, Level level,
                             String testId, String fileName, String csvData){
        if (currentLevel.index > level.index)
            return;

        if (scrShotPrefix == null)
            scrShotPrefix = logName;

        try {
            // Create the file and write the data to it
            fileName = scrShotPrefix +"_"+fileName;
            if (!fileName.toLowerCase().endsWith(".csv"))
                fileName = fileName+".csv";
            File file = new File(logDir+fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(csvData.getBytes());
            fos.close();

            // Write a log message with a link to the screenshot file
            write(index, window, level, testId,
                    "Report Data saved to <a href='./"+fileName+"' target='_blank'>"+fileName+"</a>");

        } catch (IOException e) {
            System.out.println("An exception was encountered while saving report data to a csv file.");
            System.out.println("Please see the stack trace info below.");
            e.printStackTrace();
        }
    }

    protected synchronized void write(int index, String window, Level level, String testId, String message) throws IOException {
        String dateStr = timeFormat.format(new Date());
        logWriter.write("<tr class='"+level.label.toLowerCase()+"'>");
        logWriter.write("<td class='time'>"+dateStr+"</td>");
        logWriter.write("<td class='level'>"+level.label+"</td>");
        logWriter.write("<td class='test'>"+testId+"</td>");
        logWriter.write("<td class='index'>"+index+"</td>");
        logWriter.write("<td class='window'>"+window+"</td>");
        logWriter.write("<td class='message'>"+message+"</td></tr>");
        logWriter.newLine();
    }

    public synchronized void updateFile() throws IOException {
        logWriter.flush();
    }

    public synchronized void close() throws IOException {
        logWriter.write("</tbody></table></body></html>");
        logWriter.flush();
        logWriter.close();
        logger = null;
    }

    public String closeAndAddToZip() throws IOException, URISyntaxException {
        this.close();
        // Zip the log file and any screenshots
        String zipFilePath = zipUpLogFiles();
        System.out.println("Zip file created at " + zipFilePath);
        return zipFilePath;
        // send them to the address passed in.
    }

    private String zipUpLogFiles() throws IOException, URISyntaxException {
        File zipFile = Paths.get("logs" + File.separator + logName + ".zip").toFile();
        File[] filesToZip = new File(logDir).listFiles(this);

        try( ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFile)) ) {
            for (File aFilesToZip : filesToZip) {
                byte[] buffer = new byte[1024];
                FileInputStream fin = new FileInputStream(aFilesToZip);
                zipStream.putNextEntry(new ZipEntry(aFilesToZip.getName()));
                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zipStream.write(buffer, 0, length);
                }
                zipStream.closeEntry();
                //close the InputStream
                fin.close();
            }
        }

        // Tidy up by deleting the files
        for (File aFilesToZip : filesToZip) {
            aFilesToZip.delete();
        }

        return zipFile.getAbsolutePath();
    }

    @Override
    public boolean accept(File dir, String name) {
        if (name.equals(logName + ".html"))
            return true;

        return name.startsWith(logName) && (name.endsWith(".png") || name.endsWith(".csv"));
    }


}

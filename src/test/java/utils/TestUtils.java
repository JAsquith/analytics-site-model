package utils;

import com.opencsv.CSVReader;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.TestConstants.TESTS_TO_RUN_FILE_NAME;
import static utils.TestConstants.TEST_PROPERTIES_FILENAME;

public class TestUtils {
    private Map<String, String> params;

    private boolean testPropsRead;
    private Properties testProperties;
    private Set<String> testPropNames;

    private Properties sysProperties;
    private Set<String> sysPropNames;

    private boolean testListExists;
    public boolean runTest;
    public String testId;
    private String[] testListLine;

    public TestUtils (ITestContext test){
        // Read the Test Parameters from the TestNG.xml file(s) into memory
        params = test.getCurrentXmlTest().getAllParameters();

        // Read the System Properties into memory
        sysProperties = System.getProperties();
        sysPropNames = sysProperties.stringPropertyNames();

        // Read the Test Properties file into memory
        testPropsRead = false;
        File f_Props = new File(TEST_PROPERTIES_FILENAME);
        try {
            FileReader r_Props = new FileReader(f_Props);
            testProperties = new Properties();
            testProperties.load(r_Props);
        } catch (IOException e){
            return;
        }
        testPropNames = testProperties.stringPropertyNames();
        testPropsRead = true;

        // Read this test's line from the Test List file (if it exists)
        testId = getTestSetting("test-id");
        testListExists = readTestListCSV();
    }

    public boolean readTestListCSV(){
        runTest = false;
        CSVReader reader;
        try{
            reader = new CSVReader(new FileReader(TESTS_TO_RUN_FILE_NAME));
            String testId = getTestSetting("test-id");
            while ((testListLine = reader.readNext()) != null){
                if(testListLine[0].equals(testId)){
                    runTest = true;
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println(TESTS_TO_RUN_FILE_NAME + " does not exist");
            e.printStackTrace();
            runTest = true;
            return false;
        } catch (IOException e) {
            System.err.println(TESTS_TO_RUN_FILE_NAME + " may be corrupted");
            e.printStackTrace();
            runTest = false;
            return false;
        }
        return true;
    }

    public String getDependsOnTestId(){
        String dependsOnTestId = getTestSetting("depends-on-test-id","");
        if (!dependsOnTestId.equals("")){
            return dependsOnTestId;
        }
        if (testListExists){
            if (testListLine.length>1){
                return testListLine[1];
            }
        }
        return "";
    }


    public String getTestSetting(String setting) {
        return getTestSetting(setting, "");
    }
    public String getTestSetting(String setting, String defaultVal){
        if (params.containsKey(setting)) {
            return params.get(setting);
        }
        if (testPropsRead) {
            if (testPropNames.contains(setting)) {
                return testProperties.getProperty(setting);
            }
        }
        if (sysPropNames.contains(setting)){
            return System.getProperty(setting);
        }
        return defaultVal;
    }

    public boolean getTestSetting(String name, boolean boolDefault){
        String s_value = getTestSetting(name, String.valueOf(boolDefault));
        return Boolean.valueOf(s_value);
    }

    public int getTestSetting(String name, int intDefault){
        String s_value = getTestSetting(name, String.valueOf(intDefault));
        return Integer.parseInt(s_value);
    }

    public String[] getTestSettingAsArray(String name, String sep){
        String s_value = getTestSetting(name, "");
        return s_value.split(sep);
    }

    public URL getGridUrl() throws MalformedURLException {
        String gridType = getTestSetting("grid-type", "sisra");

        String domain = getTestSetting(gridType + ".Hub", "localhost:4444/wd/hub");
        String user = getTestSetting(gridType + ".User");
        String password = getTestSetting(gridType + ".Password");

        if(user.equals("") || password.equals("")){
            return new URL("http://"+domain);
        }
        return new URL("http://"+user+":"+password+"@"+domain);

    }

    public DesiredCapabilities getCapabilities() {
        DesiredCapabilities caps;
        String browser = getTestSetting("browser");

        switch (browser.toLowerCase()) {
            case "internetexplorer":case "ie" :case "msie" :case "internet explorer":
                caps = DesiredCapabilities.internetExplorer();break;
            case "chrome" :case "google" :case "google-chrome":case "opera":
                caps = DesiredCapabilities.chrome();break;
            case "safari" :
                caps = DesiredCapabilities.safari();break;
            case "htmlunit":case "headless":
                caps = DesiredCapabilities.htmlUnit();break;
            default:
                caps = DesiredCapabilities.firefox();
        }

        String version = getTestSetting("browser-ver",getTestSetting("version", "ANY"));
        if (!version.equals("") && (!version.equals("ANY"))) {
            caps.setVersion(version);
        }

        String platform = getTestSetting("platform","ANY");
        switch (platform.toLowerCase()){
            case "any" : caps.setPlatform(Platform.ANY); break;
            case "xp" : caps.setPlatform(Platform.XP); break;
            case "vista" : caps.setPlatform(Platform.VISTA); break;
            case "win8" : caps.setPlatform(Platform.WIN8); break;
            case "win8.1" : caps.setPlatform(Platform.WIN8_1); break;
            case "win10" : caps.setPlatform(Platform.WIN10); break;
            case "windows" : caps.setPlatform(Platform.WINDOWS); break;
            case "android" : caps.setPlatform(Platform.ANDROID); break;
            case "el-capitan" : caps.setPlatform(Platform.EL_CAPITAN); break;
            case "linux" : caps.setPlatform(Platform.LINUX); break;
            case "mavericks" : caps.setPlatform(Platform.MAVERICKS); break;
            case "mountain-lion" : caps.setPlatform(Platform.MOUNTAIN_LION); break;
            case "snow-leopard" : caps.setPlatform(Platform.SNOW_LEOPARD); break;
            case "mac" : caps.setPlatform(Platform.MAC); break;
            case "unix" : caps.setPlatform(Platform.UNIX); break;
            case "yosemite" : caps.setPlatform(Platform.YOSEMITE); break;
            //default: caps.setPlatform(Platform.WIN10);
        }

        return caps;
    }

    public static LocalDateTime parseDeployDateString(String s)
    {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(deleteOrdinal(s), dateFormatter);
    }

    public static LocalDateTime parseLastPubDateString(String s)
    {

        if (s.startsWith("Fail") || s.startsWith("Not"))
        {
            return LocalDateTime.of(LocalDate.MIN, LocalTime.MIN);
        }

        int atIndex = s.indexOf(" at ");
        String datePart = s.substring(0, atIndex).trim();
        String timePart = s.substring(atIndex + 4).trim();

        // Parse the date
        LocalDate pubDate = null;
        if(s.startsWith("Today")){
            pubDate = LocalDate.now();
        }
        if(s.startsWith("Yesterday")){
            pubDate = LocalDate.now().minusDays(1);
        }
        if (pubDate == null){
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy");
            pubDate = LocalDate.parse(deleteOrdinal(datePart), dateFormatter);
        }

        // Parse the time
        LocalDateTime pubDateTime = null;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        LocalTime pubTime = null;
        try{
            pubTime = LocalTime.parse(timePart, timeFormatter);
        } catch (Exception e){
            System.out.println(String.format("Exception parsing Time: '%s'", timePart));
            throw e;
        }

        return LocalDateTime.of(pubDate, pubTime);

    }

    public static final Pattern dateOrdinalPattern = Pattern.compile("([0-9]+)(st|nd|rd|th)");

    private static String deleteOrdinal(String dateString) {
        Matcher m = dateOrdinalPattern.matcher(dateString);
        while (m.find()) {
            dateString = dateString.replaceAll(Matcher.quoteReplacement(m.group(0)), m.group(1));
        }
        return dateString;

    }
}

package utils;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TestUtils {
    private Map<String, String> params;
    private Properties testProperties;
    private Set<String> testPropNames;
    private boolean testPropsRead;
    private Properties sysProperties;
    private Set<String> sysPropNames;
    private final String TEST_PROPERTIES_FILE = "test.properties";

    public TestUtils (ITestContext test){
        // Read the Test Parameters from the TestNG.xml file(s) into memory
        params = test.getCurrentXmlTest().getAllParameters();

        // Read the System Properties into memory
        sysProperties = System.getProperties();
        sysPropNames = sysProperties.stringPropertyNames();

        // Read the Test Properties file into memory
        testPropsRead = false;
        File f_Props = new File(TEST_PROPERTIES_FILE);
        try {
            FileReader r_Props = new FileReader(f_Props);
            testProperties = new Properties();
            testProperties.load(r_Props);
        } catch (IOException e){
            return;
        }
        testPropNames = testProperties.stringPropertyNames();
        testPropsRead = true;
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

    public int getTestSetting(String setting, int defaultVal){
        String s_value = getTestSetting(setting);
        return Integer.parseInt(s_value);
    }

    public String[] getTestSettingAsArray(String setting){
        return getTestSetting(setting).split("\\|");
    }

    public URL getGridUrl() throws MalformedURLException {
        String gridType = getTestSetting("grid-type", "sisra");

        String domain = getTestSetting(gridType + ".Hub", "localhost:4444/wd/hub");
        String user = getTestSetting(gridType + ".User");;
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
}

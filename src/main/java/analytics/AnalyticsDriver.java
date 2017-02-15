package analytics;

import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.Augmentable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * A class to extend RemoteWebDriver for use in testing <a href="https://www.sisraanalytics.co.uk">SISRA Analytics</a>
 * via the TestNG framework.
 * <br>AnalyticsDriver builds on RemoteWebDriver in two ways:<ul>
 *     <li>Todo - Provides getter methods for Page Objects in the analytics.pages package.</li>
 *     <li>Provides methods to connect to a Selenium Grid based on TestNG file properties
 *     (The grid may be hosted on the SISRA network, a cloud-based service (e.g. SauceLabs),
 *     or on the pc running this code (i.e. localhost).</li>
 * </ul>
 * <br><br>
 * The {@code grid-hub}, {@code grid-user}, and {@code grid-pass}, and the desired
 * {@code platform}, {@code browser}, and {@code browser-version} should be
 * specified in the TestNG xml file. However, if they are not, the following defaults are used:
 * <ul>
 *     <li>parameter name: "grid-hub"; default: "localhost"</li>
 *     <li>parameter name: "grid-user"; default: null</li>
 *     <li>parameter name: "grid-pass"; default: null</li>
 *
 *     <li>parameter name: "platform"; default: Windows 10</li>
 *     <li>parameter name: "browser"; default: Firefox</li>
 *     <li>parameter name: "browser-version"; default: 44</li>
 * </ul>
 */
@Augmentable
public class AnalyticsDriver extends RemoteWebDriver implements TakesScreenshot {

    protected String domainType = "dev";
    protected String domainRoot = "https://dev.sisraanalytics.co.uk";

    // Constructors
    /**
     * A Constructor which starts a new RemoteWebDriver session with the specified DesiredCapabilties
     * on the grid specified in the url parameter.
     * Expected usage<br>
     *     (where the grid-type, grid-user, grid-pass, platform, browser, and browser-version
     * are parameters of a TestNG xml file):<br><br>
     * {@code   DesiredCapabilities caps = AnalyticsDriver.getCapabilities(testContext);}<br>
     * {@code   String gridURL = AnalyticsDriver.getGridURLString(testContext);}<br>
     * {@code   driver = new AnalyticsDriver(new URL(gridURL), caps);}<br><br>
     *
     * @param url           A URL object pointing to the hub of the grid to invoke the tests on.
     * @param capabilities  A DesiredCapabilities object specifying the platform, browser, &amp; browser version
     *                      the tests should be run on.
     *
     * @deprecated  Migrate all tests to use {@link #AnalyticsDriver(ITestContext)}
     */
    public AnalyticsDriver(URL url, DesiredCapabilities capabilities){
        super(url,capabilities);
    }

    /**
     * A Constructor which starts a new RemoteWebDriver session based on parameters specified in
     * the given TestNG test context.
     * @param context   A TestNG ITestContext object containing grid and desired capabilities parameters.
     * @throws      MalformedURLException if the grid.properties file contains invalid
     *              [gridtype].Hub, [gridtype].User or [gridType].Password settings
     */
    public AnalyticsDriver(ITestContext context) throws MalformedURLException{
        super(AnalyticsDriver.getGridURL(context),AnalyticsDriver.getCapabilities(context));

        Map<String, String> params = context.getCurrentXmlTest().getAllParameters();
        String targetDomain = "live";
        if (params.containsKey("test-domain"))
            targetDomain = params.get("test-domain");
        setTestDomain(targetDomain);

    }

    // Instance methods
    public void setTestDomain(String subDomain){
        if (subDomain.toLowerCase().equals("live")){
            domainType = "www";
        } else {
            domainType = subDomain;
        }
        domainRoot = "https://" + domainType + ".sisraanalytics.co.uk";
    }
    public String getTestDomain(){
        return domainType;
    }
    public String getDomainRoot(){
        return domainRoot;
    }

    // Class methods
    /**
     * Expected usage:<br>
     * <code>new AnalyticsDriver(
     *              new URL(AnalyticsDriver.getGridURLString(context)),
     *              AnalyticsDriver.getCapabilities(context))</code><br>
     * ...or similar.
     *
     * @param testContext   The TestContext provided by TestNG to the invoking
     *                      SISRATest method/class.
     *
     * @return A url string either matching
     * "<code>http:[user]:[password]@[gridservice.com]/wd/hub</code>" or
     * the local hub "<code>localhost:4444/wd/hub</code>".
     */
    public static String getGridURLString(ITestContext testContext){
        String domain;
        String user;
        String password;
        Map<String, String> params = testContext.getCurrentXmlTest().getAllParameters();

        String gridType = "sisra";
        if (params.containsKey("grid-type"))
            gridType = params.get("grid-type");

        File gridConfig = new File("grid.properties");
        try {
            FileReader configReader = new FileReader(gridConfig);
            Properties gridProperties = new Properties();
            gridProperties.load(configReader);
            domain = gridProperties.getProperty(gridType + ".Hub", "");
            user = gridProperties.getProperty(gridType + ".User", "");
            password = gridProperties.getProperty(gridType + ".Password", "");
        } catch (IOException e){
            domain = "localhost:4444/wd/hub";
            user = "";
            password = "";
        }

        if(user.equals("") || password.equals("")){
            return "http://"+domain;
        }
        return "http://"+user+":"+password+"@"+domain;
    }

    /**
     * Expected usage:<br>
     * <code>new AnalyticsDriver(
     *              AnalyticsDriver.getGridURL(context),
     *              AnalyticsDriver.getCapabilities(context))</code><br>
     * ...or similar.
     *
     * @param testContext   The TestContext provided by TestNG to the invoking
     *                      SISRATest method/class.
     * @throws MalformedURLException if {@link #getGridURLString} returns a bad link.
     * @return  A URL where the address either matches
     * "<code>http:[user]:[password]@[gridservice.com]/wd/hub</code>" or
     * is the local hub "<code>localhost:4444/wd/hub</code>".
     */
    public static URL getGridURL(ITestContext testContext) throws MalformedURLException {
        String url = getGridURLString(testContext);
        return new URL(url);
    }

    /**
     * Uses the {@code "platform"}, {@code "browser"} , and {@code "browser-ver"} or {@code "version"}
     * test parameters (retrieved from the passed in ITestContext) to create and return a
     * DesiredCapabilities object to be used in instantiating a new instance of AnalyticsDriver.
     * <br><br>
     * The following defaults are used if a parameter is missing from the TestNG xml:
     * <ul>
     *     <li>platform: WIN10</li>
     *     <li>browser: firefox</li>
     * </ul>
     * Each default is only used if <em>that</em> parameter is missing, and there is
     * no compatibility check. So if you specify "InternetExplorer" as the browser, but
     * don't specify a browser-version, IE44 will be requested and an exception will be
     * thrown when trying to create a new AnalyticsDriver using the returned object.
     *
     * @param testContext   The ITestContext object passed into the invoking suite/test method
     *                      by the TestNG TestRunner.
     * @return  A DesiredCapabilitiies object with the requested platform, browser
     *          and browser-version.
     */
    public static DesiredCapabilities getCapabilities(ITestContext testContext) {
        DesiredCapabilities caps;
        String browser;
        String version;

        Map<String, String> params = testContext.getCurrentXmlTest().getAllParameters();

        if (params.containsKey("browser"))
            browser = params.get("browser");
        else
            browser = "";

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

        version = "";
        if (browser.equals("firefox")) {
            if (params.containsKey("browser-ver")) {
                version = params.get("browser-ver");
            } else {
                if (params.containsKey("version")) {
                    version = params.get("version");
                }
            }
            if (!version.equals("")) {
                caps.setVersion(version);
            }
        }

        if (params.containsKey("platform")){
            switch (params.get("platform").toLowerCase()){
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
        }

        if (params.containsKey("grid-type")) {
            String gridType = params.get("grid-type");
            if (gridType.equals("gridlastic")){
                caps.setCapability("video", "True");
                caps.setPlatform(Platform.WIN8_1);
            }
        }

        // With Sauce Labs and testing bot we can set some additional job info
        String gridType = "";
        if (params.containsKey("grid-type"))
            gridType = params.get("grid-type");

        if (gridType.equals("sauce")){
            caps.setCapability("name", testContext.getName());
            caps.setCapability("tags", params.get("test-tags"));
            caps.setCapability("build", params.get("build"));
            caps.setCapability("screenResolution", "1280x1024");
            caps.setCapability("recordVideo", true);
            caps.setCapability("maxDuration", 6000);
            caps.setVersion("45");
        }

        return caps;
    }

}

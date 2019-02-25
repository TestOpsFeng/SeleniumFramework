import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import page.BaiduPage;
import utils.PropertiesUtil;

import java.net.MalformedURLException;
import java.net.URL;
public abstract class BaseSetup {
    protected WebDriver driver;
    protected BaiduPage baiduPage;
    protected String browser;
    public static boolean isUseSSR = PropertiesUtil.getBool("isUseSSR");
    public static boolean isUseRemoteDriver =PropertiesUtil.getBool("isUseRemoteDriver");
    public static boolean isUseHeadless = PropertiesUtil.getBool("isUseHeadless");
    public static boolean isWindowsDriver = PropertiesUtil.getBool("isWindowsDriver");

    public BaseSetup() {
    }

    @BeforeClass
    @Parameters({"browser"})
    public void setupClass(String browser) {
        Reporter.log("Using browser: " + browser,true);
        initDriverExe();
        this.driver = initWebDriver(browser);
        setBrowserName(browser);
        //读取resource下的config文件
        this.driver.manage().window().maximize();
        this.driver.get(PropertiesUtil.get("baiduPageUrl"));
        this.initPage();
    }

    public void setBrowserName(String browser){
        this.browser = browser;
    }

    public void logBrowserName(){
        Reporter.log("Current browser name is: " + browser,true);
    }

    private WebDriver initWebDriver(String browser) {
        useGlobalSSR();
        if (isUseRemoteDriver) {
            return initRemoteDriver(browser);
        } else {
            switch (browser) {
                case "chrome":
                    return new ChromeDriver((ChromeOptions) initDriverOptions(browser));
                case "firefox":
                    return new FirefoxDriver((FirefoxOptions) initDriverOptions(browser));
                case "ie":
                    return new InternetExplorerDriver((InternetExplorerOptions) initDriverOptions(browser));
                case "edge":
                    return new EdgeDriver((EdgeOptions) initDriverOptions(browser));
            }
        }
        return null;
    }

    private void useGlobalSSR() {
        if (isUseSSR) {
            Reporter.log("Using ssr",true);
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "1080");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "1080");
        }else {
            Reporter.log("Don't use ssr",true);
        }
    }




    protected RemoteWebDriver initRemoteDriver(String browser) {
        try {
            switch (browser) {
                case "chrome":
                    Capabilities chromeCapabilities = DesiredCapabilities.chrome();
                    return new RemoteWebDriver(new URL("http://testops.top:4444/wd/hub"), chromeCapabilities);
                case "firefox":
                    Capabilities firefoxCapabilities = DesiredCapabilities.firefox();
                    return new RemoteWebDriver(new URL("http://testops.top:4444/wd/hub"), firefoxCapabilities);
            }
        }catch (MalformedURLException e){
            Reporter.log(e.getMessage(),true);
        }
        return null;
    }

    public void initPage() {
        this.baiduPage = new BaiduPage(this.driver);
    }

    @AfterClass
    public void tearDownClass() throws InterruptedException {
        Thread.sleep(5000L);
        Reporter.log("Close Browser",true);
        driver.close();
    }


    public MutableCapabilities initDriverOptions(String browser) {
        //在jenkins上跑脚本要设置driver路径
//        initDriverExe();
        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-gpu");
                if (isUseHeadless){
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("lang=zh_CN.UTF-8");
                return chromeOptions;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--no-sandbox");
                firefoxOptions.addArguments("--start-maximized");
                firefoxOptions.addArguments("--disable-gpu");
                if (isUseHeadless){
                    firefoxOptions.addArguments("--headless");
                }
                firefoxOptions.addArguments("--disable-extensions");
                return firefoxOptions;
            case "ie":
                InternetExplorerOptions IEOptions = new InternetExplorerOptions();
                IEOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                IEOptions.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
                IEOptions.setCapability("requireWindowFocus", true);
                return IEOptions;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                return edgeOptions;
        }
        return null;
    }

    private void initDriverExe() {
        String driverRoot = System.getProperty("user.dir") + "/src/main/java/driver/";
        Reporter.log("Driver path: " + driverRoot,true);
        if (isWindowsDriver){
            System.setProperty("webdriver.chrome.driver", driverRoot + "chromedriver.exe");
            System.setProperty("webdriver.gecko.driver", driverRoot + "geckodriver.exe");
            System.setProperty("webdriver.ie.driver", driverRoot + "IEDriverServer.exe");
            System.setProperty("webdriver.edge.driver",driverRoot + "MicrosoftWebDriver.exe");
        }else {
            System.setProperty("webdriver.chrome.driver", driverRoot + "chromedriver");
            System.setProperty("webdriver.gecko.driver", driverRoot + "geckodriver");
        }
    }
}

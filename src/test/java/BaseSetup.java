import listener.AnnotationTransformer;
import listener.ExtentTestNGIReporterListener;
import listener.MyITestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil.Test;
import org.apache.logging.log4j.spi.LoggerContext;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import page.BaiduPage;
import utils.PropertiesUtil;

import java.net.MalformedURLException;
import java.net.URL;

@Listeners({AnnotationTransformer.class, ExtentTestNGIReporterListener.class,MyITestListener.class})
public abstract class BaseSetup {
    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(Test.class.getName());
    protected BaiduPage baiduPage;

    public BaseSetup() {
    }

    @BeforeClass
    @Parameters({ "browser" })
    public void setupClass(String browser) {
       logger.info("Using browser: " + browser);
        this.driver = initRemoteDriver(browser);
        //读取resource下的config文件
        logger.info("Open Browser: " + PropertiesUtil.get("baiduPageUrl"));
        this.driver.get(PropertiesUtil.get("baiduPageUrl"));
        this.initPage();
    }
    protected RemoteWebDriver initRemoteDriver(String browser){
        if (browser.equals("chrome")){
            Capabilities chromeCapabilities = DesiredCapabilities.chrome();
            try {
                return new RemoteWebDriver(new URL("http://testops.top:4444/wd/hub"), chromeCapabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if (browser.equals("firfox")){
            Capabilities firefoxCapabilities = DesiredCapabilities.firefox();
            try {
                return new RemoteWebDriver(new URL("http://testops.top:4444/wd/hub"), firefoxCapabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void initPage() {
            this.baiduPage = new BaiduPage(this.driver);
        }

        @AfterClass
        public void tearDownClass() throws InterruptedException {
            Thread.sleep(5000L);
            logger.info("Close Browser");
            driver.close();
        }


        public ChromeOptions initChromOptions() {
            //在jenkins上跑脚本要设置driver路径
//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
            ChromeOptions chromeOptions = new ChromeOptions();
//            Proxy proxy = new Proxy();
//            proxy.setHttpProxy("127.0.0.1:1080");
//            chromeOptions.setCapability("proxy",proxy);
//        chromeOptions.addArguments("--disable-images");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--start-maximized");
            chromeOptions.addArguments("--disable-gpu");
//        chromeOptions.addArguments("--hide-scrollbars");
//        //chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        return chromeOptions;
    }
}

import java.util.Locale;
import java.util.ResourceBundle;
import listener.AnnotationTransformer;
import listener.ExtentTestNGIReporterListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import page.BaiduPage;

@Listeners({AnnotationTransformer.class, ExtentTestNGIReporterListener.class})
public abstract class BaseSetup {
    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(Test.class.getName());
    protected ResourceBundle config;
    protected BaiduPage baiduPage;

    public BaseSetup() {
    }

    @BeforeClass
    public void setupClass() {
        this.driver = new ChromeDriver(this.initChromOptions());
        //读取resource下的config文件
        this.config = ResourceBundle.getBundle("config", Locale.getDefault());
        logger.info("Open Browser: " + this.config.getString("url"));
        this.driver.get(this.config.getString("url"));
        initPage();
    }

    public void initPage() {
        baiduPage = new BaiduPage(driver);
    }

    @AfterClass
    public void tearDownClass() throws InterruptedException {
        Thread.sleep(5000L);
        logger.info("Close Browser");
        driver.close();
    }

    public ChromeOptions initChromOptions() {
        //在jenkins上跑脚本要设置driver路径
//        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
//        Proxy proxy = new Proxy();
//        proxy.setHttpProxy("127.0.0.1:1080");
//        chromeOptions.setCapability("proxy",proxy);
        //--headless为无界面模式
        chromeOptions.addArguments("--disable-images");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--hide-scrollbars");
        //chromeOptions.addArguments("blink-settings=imagesEnabled=false");
//        chromeOptions.addArguments("--headless");
//        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        return chromeOptions;
    }
}

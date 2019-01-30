package page;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * page的父类，封装了日志体系，等待元素体系
 */
public abstract class BasePage {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(Test.class.getName());

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * 打开页面，等待3秒是为了防止还没登陆获取token就跳转新页面
     *
     * @param url
     */
    public void openPage(String url) {
        this.sleep(3000L);
        logger.info(this.getClass().getSimpleName() + ": Navigate to: " + url);
        this.driver.navigate().to(url);
    }

    /**
     * select选择方法
     * @param by
     * @return
     */
    public String randomSelect(By by) {
        WebElement elementWithTimeout = findElementWithTimeout(by);
        Select select = new Select(elementWithTimeout);
        List<WebElement> options = select.getOptions();
        int i = new Random().nextInt(options.size() - 1);
        String text = options.get(i).getText();
        select.selectByVisibleText(text);
        return text;
    }

    /**
     * 针对公司编辑页面写的select方法
     * @param by
     * @return
     */
    public String myRandomSelect(By by){
        click(by);
        By select_subCon_first = By.xpath("//div[@style=\"position: absolute; top: 0px; left: 0px; width: 100%;\"]");
        List<WebElement> list = findElementsWithTimeout(select_subCon_first);
        int size = list.size();
        By select_subCon_second = By.xpath("//div[@style=\"position: absolute; top: 0px; left: 0px; width: 100%;\"]["+size+"]/div/div/div/ul/li");
        List<WebElement> li_list = findElementsWithTimeout(select_subCon_second);
        int randomSubCon = random(0, li_list.size());
        String text = li_list.get(randomSubCon).getText();
        logger.info(this.getClass().getSimpleName() + ": Select Text： " + text);
        li_list.get(randomSubCon).click();
        return text;
    }

    /**
     * 随机数，包含left，和right
     * @param left
     * @param right
     * @return
     */
    public int random(int left,int right){
        int i = new Random().nextInt(right - left) + left;
        logger.info(this.getClass().getSimpleName() + ": Random int: " + i);
        return i;
    }

    /**
     * 滑动到元素可见
     *
     * @param by
     */
    public void scrollToElement(By by) {
        WebElement element = this.findElementWithTimeout(by);
        JavascriptExecutor je = (JavascriptExecutor) this.driver;
        logger.info(this.getClass().getSimpleName() + ": Scroll to: " + by);
        je.executeScript("arguments[0].scrollIntoView(true);", new Object[]{element});
    }

    /**
     * 跳转到新页面，并关闭旧页面
     */
    public void switchHandle() {
        String currentHandle = this.driver.getWindowHandle();
        Iterator var2 = this.driver.getWindowHandles().iterator();

        while (var2.hasNext()) {
            String handles = (String) var2.next();
            if (handles.equals(currentHandle)) {
                this.driver.close();
            } else {
                logger.info(this.getClass().getSimpleName() + ": Switch to: " + handles);
                this.driver.switchTo().window(handles);
            }
        }

    }

    /**
     * 查找元素，默认等待10秒
     *
     * @param by
     * @return
     */
    public WebElement findElementWithTimeout(By by) {
        return this.findElementWithTimeout(by, 10L);
    }

    /**
     * 等待元素，找到元素后返回
     *
     * @param by
     * @param timeout
     * @return
     */
    public WebElement findElementWithTimeout(By by, long timeout) {
        WebElement webElement = null;

        try {
            logger.info(this.getClass().getSimpleName() + ": Find element：" + by.toString());
            webElement = (WebElement) (new WebDriverWait(this.driver, timeout)).until(ExpectedConditions.elementToBeClickable(by));
            return webElement;
        } catch (TimeoutException var6) {
            logger.error(this.getClass().getSimpleName() + ": Can not find element: " + by.toString());
            throw new TimeoutException("没有找到元素: " + by.toString());
        } catch (NullPointerException var7) {
            logger.error(this.getClass().getSimpleName() + ": Can not find element: " + by.toString());
            throw new NullPointerException("没有找到元素: " + by.toString());
        }
    }

    /**
     * 封装查找一个集合的元素
     * @param by
     * @return
     */
    public List<WebElement> findElementsWithTimeout(By by) {
        return findElementsWithTimeout(by, 10);
    }

    /**
     * 封装查找一个集合的元素
     * @param by
     * @param timeout
     * @return
     */
    public List<WebElement> findElementsWithTimeout(By by, long timeout) {
        List<WebElement> webElements = null;
        try {
            logger.info(this.getClass().getSimpleName() + ": Find elements：" + by.toString());
            new WebDriverWait(this.driver, timeout).until(ExpectedConditions.presenceOfElementLocated(by));
            webElements = driver.findElements(by);
            return webElements;
        } catch (TimeoutException var6) {
            logger.error(this.getClass().getSimpleName() + ": Can not find elements: " + by.toString());
            throw new TimeoutException("没有找到元素: " + by.toString());
        } catch (NullPointerException var7) {
            logger.error(this.getClass().getSimpleName() + ": Can not find elements: " + by.toString());
            throw new NullPointerException("没有找到元素: " + by.toString());
        }
    }

    /**
     * 封装sleep方法
     *
     * @param millis
     */
    public void sleep(long millis) {
        try {
            logger.info(this.getClass().getSimpleName() + ": Sleep in " + millis / 1000L + " seconds");
            Thread.sleep(millis);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        }

    }

    /**
     * 保存pagesource，用于调试
     */
    public void savePageSource() {
        String path = System.getProperty("user.dir") + "/src/java/report/PageSource.xml";

        try {
            FileWriter writer = new FileWriter(path);
            writer.write(this.driver.getPageSource());
            writer.flush();
            writer.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    /**
     * 等待loading，避免selenium行为被拦截
     *
     * @param by
     */
    public void waitForLoading(By by) {
        logger.info(this.getClass().getSimpleName() + ": Waiting for loading：" + by.toString());
        while (true) {
            try {
                new WebDriverWait(this.driver, 4).until(ExpectedConditions.elementToBeClickable(by));
                sleep(1500);
            } catch (TimeoutException e) {
                return;
            }
        }
    }

    /**
     * 点击元素
     *
     * @param by
     */
    public void click(By by) {
        WebElement webElement = this.findElementWithTimeout(by);
        logger.info(this.getClass().getSimpleName() + ": Click element: " + by.toString());
        webElement.click();
    }

    /**
     * 输入文字
     *
     * @param by
     * @param text
     */
    public void sendKeys(By by, String text) {
        WebElement webElement = this.findElementWithTimeout(by);
        logger.info(this.getClass().getSimpleName() + ": SendKeys to element: " + by.toString());
        webElement.sendKeys(new CharSequence[]{text});
    }

    /**
     * 清理input后，再输入text
     *
     * @param by
     * @param text
     */
    public void sendKeysAfterClear(By by, String text) {
        WebElement webElement = this.findElementWithTimeout(by);
        logger.info(this.getClass().getSimpleName() + ": Clear text for element: " + by.toString());
        webElement.clear();
        logger.info(this.getClass().getSimpleName() + ": SendKeys to element: " + by.toString());
        webElement.sendKeys(new CharSequence[]{text});
    }

    /**
     * 获取元素的文本text()
     *
     * @param by
     * @return
     */
    public String getText(By by) {
        WebElement webElement = this.findElementWithTimeout(by);
        logger.info(this.getClass().getSimpleName() + ": Get Text in element: " + by.toString());
        return webElement.getText();
    }
}

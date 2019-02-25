package page;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 * page的父类，封装了日志体系，等待元素体系
 */
public abstract class BasePage {
    private WebDriver driver;

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
        Reporter.log(this.getClass().getSimpleName() + ": Navigate to: " + url,true);
        this.driver.navigate().to(url);
    }

    /**
     * 针对select的随机选择方法
     *
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
        Reporter.log(this.getClass().getSimpleName() + ": Select Text： " + text,true);
        return text;
    }

    /**
     * 针对公司编辑页面写的select方法
     *
     * @param by
     * @return
     */
    public String myRandomSelect(By by) {
        click(by);
        By select_subCon_first = By.xpath("//div[@style=\"position: absolute; top: 0px; left: 0px; width: 100%;\"]");
        List<WebElement> list = findElementsWithTimeout(select_subCon_first);
        int size = list.size();
        By select_subCon_second = By.xpath("//div[@style=\"position: absolute; top: 0px; left: 0px; width: 100%;\"][" + size + "]/div/div/div/ul/li");
        List<WebElement> li_list = findElementsWithTimeout(select_subCon_second);
        int randomSubCon = random(0, li_list.size());
        String text = li_list.get(randomSubCon).getText();
        Reporter.log(this.getClass().getSimpleName() + ": Select Text： " + text,true);
        li_list.get(randomSubCon).click();
        return text;
    }

    /**
     * 随机获取element集合中的某一个
     *
     * @param by
     * @return
     */
    public WebElement getRandomElement(By by) {
        List<WebElement> list = findElementsWithTimeout(by);
        int size = list.size();
        int randomNum = random(0, size);
        WebElement element = list.get(randomNum);
        Reporter.log(this.getClass().getSimpleName() + ": Select Text： " + element.getText(),true);
        return element;
    }

    /**
     * 针对公司datepicker的方法
     *
     * @param by
     */
    public void selectDate(By by, String date) {
        click(by);
        By text_date = By.xpath("//a[text()='Today']");
        sleep(1500);
        click(text_date);
        //a[text()="Today"]
    }

    /**
     * 随机数，包含left，和right
     *
     * @param left
     * @param right
     * @return
     */
    public int random(int left, int right) {
        int i = new Random().nextInt(right - left) + left;
        Reporter.log(this.getClass().getSimpleName() + ": Random int: " + i,true);
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
        Reporter.log(this.getClass().getSimpleName() + ": Scroll to: " + element.getText(),true);
        je.executeScript("arguments[0].scrollIntoView(true);", new Object[]{element});
    }

    /**
     * 滑动到元素可见
     *
     * @param element
     */
    public void scrollToElement(WebElement element) {
        JavascriptExecutor je = (JavascriptExecutor) this.driver;
        Reporter.log(this.getClass().getSimpleName() + ": Scroll to: " + element.getText(),true);
        je.executeScript("arguments[0].scrollIntoView(true);", new Object[]{element});
    }

    /**
     * 使用actions点击
     *
     * @param by
     */
    public void clickAction(By by) {
        WebElement element = findElementWithTimeout(by);
        Actions action = new Actions(driver);
        Reporter.log(this.getClass().getSimpleName() + ": Click element: " + by.toString(),true);
        action.moveToElement(element).click().perform();
    }

    /**
     * 使用actions点击
     *
     * @param element
     */
    public void clickAction(WebElement element) {
        Actions action = new Actions(driver);
        Reporter.log(this.getClass().getSimpleName() + ": Click element: " + element.getText(),true);
        action.moveToElement(element).click().perform();
    }

    /**
     * 使用js点击
     *
     * @param element
     */
    public void clickByJS(WebElement element) {
        JavascriptExecutor je = (JavascriptExecutor) this.driver;
        je.executeScript("$(arguments[0]).click();", new Object[]{element});
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
                Reporter.log(this.getClass().getSimpleName() + ": Switch to: " + handles,true);
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
            Reporter.log(this.getClass().getSimpleName() + ": Find element：" + by.toString(),true);
            webElement = (WebElement) (new WebDriverWait(this.driver, timeout)).until(ExpectedConditions.elementToBeClickable(by));
            return webElement;
        } catch (TimeoutException var6) {
            Reporter.log(this.getClass().getSimpleName() + ": Can not find element: " + by.toString(),true);
            throw new TimeoutException("没有找到元素: " + by.toString());
        } catch (NullPointerException var7) {
            Reporter.log(this.getClass().getSimpleName() + ": Can not find element: " + by.toString(),true);
            throw new NullPointerException("没有找到元素: " + by.toString());
        }
    }

    /**
     * 封装查找一个集合的元素
     *
     * @param by
     * @return
     */
    public List<WebElement> findElementsWithTimeout(By by) {
        return findElementsWithTimeout(by, 10);
    }

    /**
     * 封装查找一个集合的元素
     *
     * @param by
     * @param timeout
     * @return
     */
    public List<WebElement> findElementsWithTimeout(By by, long timeout) {
        List<WebElement> webElements = null;
        try {
            Reporter.log(this.getClass().getSimpleName() + ": Find elements By：" + by.toString(),true);
            new WebDriverWait(this.driver, timeout).until(ExpectedConditions.presenceOfElementLocated(by));
            webElements = driver.findElements(by);
            return webElements;
        } catch (TimeoutException var6) {
            Reporter.log(this.getClass().getSimpleName() + ": Can not find elements: " + by.toString(),true);
            throw new TimeoutException("没有找到元素: " + by.toString());
        } catch (NullPointerException var7) {
            Reporter.log(this.getClass().getSimpleName() + ": Can not find elements: " + by.toString(),true);
            throw new NullPointerException("没有找到元素: " + by.toString());
        }
    }

    /**
     * 封装sleep方法
     *
     * @param millis 毫秒
     */
    public void sleep(long millis) {
        try {
            Reporter.log(this.getClass().getSimpleName() + ": Sleep in " + millis / 1000L + " seconds",true);
            Thread.sleep(millis);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        }

    }

    /**
     * 保存pagesource，用于调试
     */
    public void savePageSource() {
        sleep(1500);
        String path = System.getProperty("user.dir") + "\\src\\main\\java\\report\\PageSource.html";
        Reporter.log(this.getClass().getSimpleName() + ": Save PageSource in：" + path,true);
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
        Reporter.log(this.getClass().getSimpleName() + ": Waiting for loading：" + by.toString(),true);
        for (int i = 0; i < 20; i++) {
            try {
                new WebDriverWait(this.driver, 4).until(ExpectedConditions.elementToBeClickable(by));
                sleep(1500);
            } catch (TimeoutException e) {
                sleep(1000);
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
        Reporter.log(this.getClass().getSimpleName() + ": Click element: " + by.toString() + ". Element's Text is: "+webElement.getText(),true);
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
        Reporter.log(this.getClass().getSimpleName() + ": Input Text: " + text + " to element: " + by.toString(),true);
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
        Reporter.log(this.getClass().getSimpleName() + ": Clear text for element: " + by.toString(),true);
        webElement.clear();
        Reporter.log(this.getClass().getSimpleName() + ": Input Text: " + text + " to element: " + by.toString(),true);
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
        Reporter.log(this.getClass().getSimpleName() + ": Get Text: "+webElement.getText()+" in element: " + by.toString(),true);
        return webElement.getText();
    }

    public void ExecJs(String js) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript(js);
    }

    public String getAttr(By by, String attrKey) {
        WebElement webElement = this.findElementWithTimeout(by);
        String value = webElement.getAttribute("value");
        Reporter.log(this.getClass().getSimpleName() + ": Get Value:" + value + " by Key: " + attrKey + "in element: " +by.toString(),true);
        return value;
    }
}

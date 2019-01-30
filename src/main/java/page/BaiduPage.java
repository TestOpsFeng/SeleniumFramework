package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import page.BasePage;

/**
 * 子类，变量管理元素，方法封装行为
 */
public class BaiduPage extends BasePage {
    private By input_serch = By.id("kw");
    private By btn_search = By.xpath("//input[@value=\"百度一下\"]");
    private By first_search_result = By.xpath("//div[@id=\"1\"]/h3");
    public BaiduPage(WebDriver driver) {
        super(driver);
    }

    public void clickSearch(String searchText) {
        sendKeys(input_serch,searchText);
        click(btn_search);
    }
    public String getSearchResult(){
        return getText(first_search_result);
    }

}

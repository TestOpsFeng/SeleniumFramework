### 框架介绍
1. 在Windows平台，能多浏览器（Chrome、FireFox、Edge）并发执行用例
2. 有远程浏览器执行功能，需要在Linux上安装Selenium Grid Hub and Nodes
3. 当用例结果是TimeoutException时，用例自动重跑，最多重跑3次
4. 定制ExtendReport样式，作为测试报告
5. 使用properties，测试数据与测试代码分离
6. 继承了[binarywang](https://github.com/binarywang/java-testdata-generator)的随机数据生成器

### 使用
通过Maven运行：
```
mvn clean
mvn test
```
或：

使用idea，通过右键运行testng.xml。
testng.xml详解：
```xml
    <test verbose="2" preserve-order="true" name="firfox">
        <parameter name="browser" value="firefox"/>  <!--参数：name必须为browser，value有3个可选：chrome,firefox,edge，分别对应不同浏览器-->
        <classes>
            <class name="SearchTest">
                <methods>
                <include name="testSearch"/>
                </methods>
            </class>
        </classes>
    </test> <!-- Test -->
```
### 元素定位及方法封装
```java
public class BaiduPage extends BasePage {
    //成员变量控制元素
    private By input_serch = By.id("kw");
    private By btn_search = By.xpath("//input[@value=\"百度一下\"]");
    private By first_search_result = By.xpath("//div[@id=\"1\"]/h3");
    public BaiduPage(WebDriver driver) {
        super(driver);
    }

    //方法控制行为
    public void clickSearch(String searchText) {
        sendKeys(input_serch,searchText);
        click(btn_search);
    }
    public String getSearchResult(){
        return getText(first_search_result);
    }

}
```
### PropertiesUtil使用
```properties
searchText=Selenium
host = www.baidu.com
baiduPageUrl= https://${host}
```
以上是配置文件，通过以下语句读取：
```java
PropertiesUtil.get("baiduPageUrl");
```

### 报告样式
在Report中记录了操作过程，方便定位Exception：
![Alt text](../report.png "测试报告")
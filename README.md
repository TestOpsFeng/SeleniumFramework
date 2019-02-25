### 框架介绍
1. 基于Selenium+Maven+TestNG搭建
2. 本地多浏览器并行执行
3. 远程多浏览器并行执行
4. 对[ExtendReport](http://extentreports.com/)样式进行修改，把运行logs写入details中
5. 集成了[binarywang](https://github.com/binarywang/java-testdata-generator)的随机数据生成器


### 本地环境搭建
安装chrome、firefox、efge等浏览器即可。
### 远程环境搭建
要调用远程浏览器，需要配置Selenium Grid Hub and Nodes。[理解docker和selenium hub的概念看虫师的博客](https://www.cnblogs.com/fnng/p/8358326.html)。

这里介绍[SeleniumHQ官方项目](https://github.com/seleniumHQ/docker-selenium)中docker+docker-compose的搭建方法：
1. 在linux上安装docker和docker-conpose环境
2. 新建一个docker-selenium.yaml文件，写入：
```yaml
version: "3"
services:
  selenium-hub:
    image: selenium/hub:3.141.59-gold
    container_name: selenium-hub
    ports:
      - "4444:4444"
  chrome:
    image: selenium/node-chrome:3.141.59-gold
    volumes:
      - /dev/shm:/dev/shm
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
      - HUB_PORT=4444
  firefox:
    image: selenium/node-firefox:3.141.59-gold
    volumes:
      - /dev/shm:/dev/shm
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
      - HUB_PORT=4444
```
3. 命令行运行：`docker-compose -f docker-selenium.yaml up`
### 使用
随机数据生成器使用：
```$java
//英文名
String expectEngGeneratorName = EnglishNameGenerator.getInstance().generate();
//中文名
String expectChiGeneratorName = ChineseNameGenerator.getInstance().generate();
//手机号码
String expectPhoneNum = ChineseMobileNumberGenerator.getInstance().generate();
//地址
String expectPassportAddress = ChineseIDCardNumberGenerator.generateIssueOrg();
```
运行：
```$xml
mvn test
```


### 报告
![Alt text](../ptah_web_cloud/report.png "测试报告")

### 总结
- TestNG暴露的接口很实用，并发机制也很省事
- TestNG还能做到更多，例如数据驱动
- 日志很重要，好的日志体系可以迅速定位问题
- docker对于环境搭建很方便
- 感谢所有乐于分享的人
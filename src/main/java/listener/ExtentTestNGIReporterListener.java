package listener;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.http.util.TextUtils;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 继承IReporter生成报告
 */
public class ExtentTestNGIReporterListener implements IReporter {

    private static final String OUTPUT_FOLDER = System.getProperty("user.dir");
    private static final String FILE = "/src/main/java/report/";
    private static final String FILE_NAME = FILE + "extent.html";

    private ExtentReports extent;

    /**
     * 用于生成结果的，不用在这纠结
     */
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();
        for (ISuite suite : suites) {
            Map result = suite.getResults();
            for (Object obj : result.values()) {
                ISuiteResult r = (ISuiteResult) obj;
                ITestContext context = r.getTestContext();

                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);
                buildTestNodes(context.getPassedTests(), Status.PASS);
            }
        }

        for (String s : Reporter.getOutput()) {
            extent.setTestRunnerOutput(s);
        }

        extent.flush();
    }

    private void init() {
        System.out.println(OUTPUT_FOLDER + FILE_NAME);
        //报表路径
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
        //文档名称
        htmlReporter.config().setDocumentTitle("自动化测试");
        htmlReporter.config().setReportName("自动化测试");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        //设置主题
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
    }

    private void buildTestNodes(IResultMap tests, Status status) {
        ExtentTest test;
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                String description = result.getMethod().getDescription();
                if (!TextUtils.isEmpty(description)) {
                    //有@Test的description，则以description生成测试用例标题
                    test = extent.createTest(description);
                } else {
                    //没有以方法名生成测试用例标题
                    test = extent.createTest(result.getMethod().getMethodName());
                }
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
                List<String> outputs = Reporter.getOutput(result);
                for (String output : outputs) {
                    test.log(status, output);
                }
                if (result.getThrowable() != null) {
                    //失败则输出到报表
                    test.log(status, result.getThrowable());
                }
                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }


}
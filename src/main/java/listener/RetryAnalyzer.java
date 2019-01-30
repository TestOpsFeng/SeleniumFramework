package listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
/**
 * 用于设置失败重试
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    int counter = 0;
    //重试的次数
    int retryLimit = 2;

    @Override
    public boolean retry(ITestResult result) {
        if(counter < retryLimit)
        {
            counter++;
            return true;
        }
        return false;
    }
}
package listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.openqa.selenium.TimeoutException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
/**
 * 用于设置失败重试
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    int counter = 0;
    //重试的次数
    int retryLimit = 2;
    private static final Logger logger = LogManager.getLogger(ResolverUtil.Test.class.getName());

    public boolean retry(ITestResult result) {
        if(counter < retryLimit)
        {
            if (result.getThrowable() != null){
                String simpleName = result.getThrowable().getClass().getSimpleName();
                if (simpleName.equals("TimeoutException")){
                    logger.info("Retry Test for: " + simpleName);
                    counter++;
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
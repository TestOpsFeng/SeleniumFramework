package listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

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
            if (result.getThrowable() != null){
                String simpleName = result.getThrowable().getClass().getSimpleName();
                if (simpleName.equals("TimeoutException")){
                    Reporter.log("Retry Test: "+result.getName()+" for: " + simpleName,true);
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
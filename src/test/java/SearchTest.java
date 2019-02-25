import cn.binarywang.tools.generator.ChineseNameGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.BaiduPage;

public class SearchTest extends BaseSetup {

    @Test(description = "搜索是否正常")
    public void testSearch() {
        String generate = ChineseNameGenerator.getInstance().generate();
        baiduPage.clickSearch(generate);

        String searchResult = baiduPage.getSearchResult();
        Assert.assertEquals(searchResult,generate);
    }

    @Test(description = "搜索是否正常2")
    public void testSearch2() {
        String generate = ChineseNameGenerator.getInstance().generate();
        baiduPage.clickSearch(generate);

        String searchResult = baiduPage.getSearchResult();
        Assert.assertEquals(searchResult,generate);
    }

}

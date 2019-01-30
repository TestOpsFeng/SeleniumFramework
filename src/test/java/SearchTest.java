import org.testng.annotations.Test;
import page.BaiduPage;

public class SearchTest extends BaseSetup {

    @Test(description = "搜索是否正常")
    public void testSearch() {
        baiduPage.clickSearch(config.getString("searchText"));
        String searchResult = baiduPage.getSearchResult();
        assert searchResult.contains(config.getString("searchText"));
    }

}

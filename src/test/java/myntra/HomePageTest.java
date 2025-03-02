package myntra;

import com.ayrus.pages.myntra.HomePage;
import com.ayrus.playwright.BrowserUtil;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.dataprovider.BrowserDataProvider;

public class HomePageTest {
  private static final String MYNTRA_URL = "https://www.myntra.com/";
  private final ThreadLocal<Page> page = ThreadLocal.withInitial(() -> null);
  private final ThreadLocal<HomePage> homePage = ThreadLocal.withInitial(() -> null);

  @BeforeMethod
  public void loadPage(Object[] browserType) {
    BrowserContext context = BrowserUtil.getContext(browserType[0].toString());
    page.set(context.newPage());
    page.get().navigate(MYNTRA_URL);

    homePage.set(new HomePage(page.get()));
  }

  @Test(dataProviderClass = BrowserDataProvider.class, dataProvider = "browsers")
  public void logo(String browserType) {
    SoftAssertions softAssert = new SoftAssertions();

    softAssert.assertThat(homePage.get().getLogo().isVisible()).as("Logo is Visible").isTrue();
    softAssert.assertThat(homePage.get().getLogo().isEnabled()).as("Logo is Enabled").isTrue();

    homePage.get().getLogo().click();
    softAssert.assertThat(page.get().url()).as("Page Url").isEqualTo(MYNTRA_URL);

    softAssert.assertAll();
  }
}

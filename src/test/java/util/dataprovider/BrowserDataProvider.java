package util.dataprovider;

import com.ayrus.playwright.BrowserType;
import org.testng.annotations.DataProvider;

public class BrowserDataProvider {

  @DataProvider(name = "browsers", parallel = true)
  public static Object[][] browserProvider() {
    return BrowserType.getAllBrowserNames();
  }
}

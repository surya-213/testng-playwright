package com.ayrus.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Playwright;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class BrowserUtil {

  private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();

  public static BrowserContext getContext(String browserType) {
    return createContext(browserType, false);
  }

  public static BrowserContext getHeadlessContext(String browserType) {
    return createContext(browserType, true);
  }

  /**
   * Creates a new BrowserContext based on the specified browser type and headless mode.
   *
   * @param browserType The browser type (chromium, firefox, webkit, chrome, msedge, opera).
   * @param headless Whether the browser should run in headless mode.
   * @return A new BrowserContext instance.
   */
  public static BrowserContext createContext(String browserType, boolean headless) {
    Playwright pwServer = PlaywrightUtil.server();
    log.info("Launching browser: {} | Headless: {}", browserType, headless);
    LaunchOptions options =
        new LaunchOptions().setHeadless(headless).setArgs(List.of("--disable-http2"));

    BrowserType browserEnum = BrowserType.fromString(browserType);
    BROWSER.set(
        switch (browserEnum) {
          case FIREFOX -> pwServer.firefox().launch(options);
          case SAFARI -> pwServer.webkit().launch(options);
          case CHROME -> pwServer.chromium().launch(options.setChannel("chrome"));
          case MSEDGE -> pwServer.chromium().launch(options.setChannel("msedge"));
          case CHROMIUM -> pwServer.chromium().launch(options);
        });

    log.info("Browser launched successfully: {}", browserEnum);
    return BROWSER.get().newContext();
  }

  static {
    // Register the shutdown hook to close connections
    Runtime.getRuntime().addShutdownHook(new Thread(BrowserUtil::cleanup));
  }

  private static void cleanup() {
    Optional.ofNullable(BROWSER.get()).ifPresent(Browser::close);
  }
}

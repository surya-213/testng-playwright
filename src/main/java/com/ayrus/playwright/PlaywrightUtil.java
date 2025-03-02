package com.ayrus.playwright;

import com.microsoft.playwright.Playwright;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlaywrightUtil {

  private static Playwright playwright;

  public static Playwright server() {
    if (playwright == null) {
      playwright = Playwright.create();
    }
    return playwright;
  }

  static {
    // Register the shutdown hook to close Playwright on JVM exit
    Runtime.getRuntime().addShutdownHook(new Thread(PlaywrightUtil::cleanup));
  }

  private static void cleanup() {
    Optional.ofNullable(playwright).ifPresent(Playwright::close);
  }
}

package com.ayrus.playwright;

import lombok.Getter;

@Getter
public enum BrowserType {
  CHROME("chrome"),
  FIREFOX("firefox"),
  SAFARI("webkit"),
  CHROMIUM("chromium"),
  MSEDGE("msedge");

  private final String name;

  BrowserType(String name) {
    this.name = name;
  }

  public static BrowserType fromString(String name) {
    for (BrowserType browser : BrowserType.values()) {
      if (browser.getName().equalsIgnoreCase(name)) {
        return browser;
      }
    }
    throw new IllegalArgumentException("Invalid browser name: " + name);
  }

  // **New method to return all browser names for DataProvider**
  public static Object[][] getAllBrowserNames() {
    return java.util.Arrays.stream(BrowserType.values())
        .map(browser -> new Object[] {browser.getName()})
        .toArray(Object[][]::new);
  }
}

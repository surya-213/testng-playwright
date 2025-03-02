package com.ayrus.pages.myntra;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {

  Page page;
  String logoLocator = "a.myntraweb-sprite.desktop-logo.sprites-headerLogo";

  public HomePage(Page page) {
    this.page = page;
  }

  public Locator getLogo() {
    return page.locator(logoLocator);
  }
}

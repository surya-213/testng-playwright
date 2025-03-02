package com.ayrus.listeners;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.Reporter;

@Slf4j
public class AllureListener implements TestLifecycleListener {

  @Override
  public void beforeTestStop(TestResult result) {
    if (!Status.FAILED.equals(result.getStatus())) {
      return; // Exit if test did not fail
    }

    // Get the test instance from Allure context
    Optional<String> testId = Allure.getLifecycle().getCurrentTestCase();
    if (testId.isEmpty()) {
      log.error("Failed to capture screenshot: No test is running in Allure lifecycle.");
      return;
    }

    Object testClassInstance = getCurrentTestInstance();
    if (testClassInstance == null) {
      log.error("Failed to retrieve test instance.");
      return;
    }

    Page page = extractPageFromTestClass(testClassInstance);
    if (page != null) {
      captureScreenshot(page, result.getName());
    } else {
      log.error("Failed to capture screenshot: Page instance not found.");
    }
  }

  public void captureScreenshot(Page page, String testName) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
      String currentIstTime = sdf.format(new Date());

      // Capture screenshot as byte array (no file storage)
      byte[] screenshotBytes =
          page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));

      // Attach screenshot directly to Allure report
      Allure.addAttachment(
          "Failure Screenshot - " + testName + " [" + currentIstTime + "]",
          "image/png",
          new ByteArrayInputStream(screenshotBytes),
          "png");

    } catch (Exception e) {
      log.error("Failed to capture screenshot: {}", e.getMessage(), e);
    }
  }

  private Page extractPageFromTestClass(Object testClassInstance) {
    try {
      Field pageField = testClassInstance.getClass().getDeclaredField("page");
      pageField.setAccessible(true);
      Object pageObject = pageField.get(testClassInstance);

      if (pageObject instanceof ThreadLocal) {
        Object pageValue = ((ThreadLocal<?>) pageObject).get();
        if (pageValue instanceof Page page) {
          return page;
        }
      } else if (pageObject instanceof Page page) {
        return page;
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      log.error("Error retrieving Page instance: {}", e.getMessage(), e);
    }
    return null;
  }

  private Object getCurrentTestInstance() {
    try {
      ITestResult testResult = Reporter.getCurrentTestResult();
      return testResult.getInstance(); // Fetch test instance correctly
    } catch (Exception e) {
      log.error("Error retrieving current test instance: {}", e.getMessage());
      return null;
    }
  }
}

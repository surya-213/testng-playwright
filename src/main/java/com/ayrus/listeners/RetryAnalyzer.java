package com.ayrus.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

  private int count = 0;
  private static final int MAX_RETRY_COUNT = 2;

  @Override
  public boolean retry(ITestResult result) {
    if (count < MAX_RETRY_COUNT) {
      count++;
      return true;
    }
    return false;
  }
}

package com.vanniktech.lintrules.rxjava2;

import org.junit.Test;

import static com.vanniktech.lintrules.rxjava2.RxJava2Detector.COMPOSITE_DISPOSABLE_ADD_ALL;
import static com.vanniktech.lintrules.rxjava2.RxJava2Detector.COMPOSITE_DISPOSABLE_DISPOSE;
import static com.vanniktech.lintrules.rxjava2.RxJava2Detector.SUBSCRIBE_MISSING_ERROR_CONSUMER;
import static com.vanniktech.lintrules.rxjava2.RxJava2MethodCheckReturnValueDetector.ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE;
import static com.vanniktech.lintrules.rxjava2.RxJava2MissingCompositeDisposableClearDetector.ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR;
import static org.fest.assertions.api.Assertions.assertThat;

public class IssueRegistryTest {
  @Test public void getIssues() {
    assertThat(new IssueRegistry().getIssues()).containsExactly(
        COMPOSITE_DISPOSABLE_DISPOSE,
        COMPOSITE_DISPOSABLE_ADD_ALL,
        SUBSCRIBE_MISSING_ERROR_CONSUMER,
        ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE,
        ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR
    );
  }
}

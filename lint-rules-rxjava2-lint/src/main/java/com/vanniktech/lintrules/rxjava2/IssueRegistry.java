package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Issue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vanniktech.lintrules.rxjava2.RxJava2MethodCheckReturnValueDetector.ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  @Override public List<Issue> getIssues() {
    final List<Issue> issues = new ArrayList<>();
    Collections.addAll(issues, RxJava2Detector.getIssues());
    Collections.addAll(issues, ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE);
    return issues;
  }
}

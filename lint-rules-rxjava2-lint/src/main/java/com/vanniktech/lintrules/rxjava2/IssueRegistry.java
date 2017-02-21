package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Issue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  @Override public List<Issue> getIssues() {
    final List<Issue> issues = new ArrayList<>();
    Collections.addAll(issues, RxJava2Detector.getIssues());
    Collections.addAll(issues, RxJava2MethodCheckReturnValueDetector.getIssues());
    return issues;
  }
}

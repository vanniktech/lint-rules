package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Issue;
import java.util.List;

import static java.util.Arrays.asList;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  @Override public List<Issue> getIssues() {
    return asList(AndroidDetector.getIssues());
  }
}

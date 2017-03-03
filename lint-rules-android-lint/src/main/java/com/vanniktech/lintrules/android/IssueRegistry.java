package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Issue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vanniktech.lintrules.android.RawDimenDetector.ISSUE_RAW_DIMEN;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.ISSUE_WRONG_ID_FORMAT;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  @Override public List<Issue> getIssues() {
    final List<Issue> issues = new ArrayList<>();
    Collections.addAll(issues, AndroidDetector.getIssues());
    Collections.addAll(issues, ISSUE_WRONG_ID_FORMAT);
    Collections.addAll(issues, ISSUE_RAW_DIMEN);
    return issues;
  }
}

package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Issue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vanniktech.lintrules.android.RawColorDetector.ISSUE_RAW_COLOR;
import static com.vanniktech.lintrules.android.RawDimenDetector.ISSUE_RAW_DIMEN;
import static com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION;
import static com.vanniktech.lintrules.android.SuperfluousPaddingDeclarationDetector.SUPERFLUOUS_PADDING_DECLARATION;
import static com.vanniktech.lintrules.android.WrongMenuIdFormatDetector.ISSUE_WRONG_MENU_ID_FORMAT;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.ISSUE_WRONG_VIEW_ID_FORMAT;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  @Override public List<Issue> getIssues() {
    final List<Issue> issues = new ArrayList<>();
    Collections.addAll(issues, AndroidDetector.getIssues());
    Collections.addAll(issues, ISSUE_WRONG_VIEW_ID_FORMAT);
    Collections.addAll(issues, ISSUE_WRONG_MENU_ID_FORMAT);
    Collections.addAll(issues, ISSUE_RAW_DIMEN);
    Collections.addAll(issues, ISSUE_RAW_COLOR);
    Collections.addAll(issues, SUPERFLUOUS_MARGIN_DECLARATION);
    Collections.addAll(issues, SUPERFLUOUS_PADDING_DECLARATION);
    return issues;
  }
}

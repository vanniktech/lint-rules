package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import java.util.Collection;

import static com.android.SdkConstants.ATTR_PADDING_BOTTOM;
import static com.android.SdkConstants.ATTR_PADDING_END;
import static com.android.SdkConstants.ATTR_PADDING_START;
import static com.android.SdkConstants.ATTR_PADDING_TOP;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class SuperfluousPaddingDeclarationDetector extends SuperfluousDeclarationDetector {
  static final Issue ISSUE_SUPERFLUOUS_PADDING_DECLARATION = Issue.create("SuperfluousPaddingDeclaration", "Instead of using start, end, bottom and top padding can be used.",
      "Instead of using start, end, bottom and top padding can be used.", CORRECTNESS, 8, WARNING,
      new Implementation(SuperfluousPaddingDeclarationDetector.class, RESOURCE_FILE_SCOPE));

  @Override public Collection<String> getApplicableAttributes() {
    return asList(
        ATTR_PADDING_TOP,
        ATTR_PADDING_BOTTOM,
        ATTR_PADDING_START,
        ATTR_PADDING_END
    );
  }

  @Override public Issue issue() {
    return ISSUE_SUPERFLUOUS_PADDING_DECLARATION;
  }

  @Override public String message() {
    return "Should be using padding instead.";
  }
}

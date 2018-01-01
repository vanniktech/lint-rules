package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import java.util.Collection;

import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class SuperfluousMarginDeclarationDetector extends SuperfluousDeclarationDetector {
  static final Issue ISSUE_SUPERFLUOUS_MARGIN_DECLARATION = Issue.create("SuperfluousMarginDeclaration", "Instead of using start, end, bottom and top layout_margin can be used.",
      "Instead of using start, end, bottom and top layout_margin can be used.", CORRECTNESS, 8, WARNING,
      new Implementation(SuperfluousMarginDeclarationDetector.class, RESOURCE_FILE_SCOPE));

  @Override public Collection<String> getApplicableAttributes() {
    return asList(
        ATTR_LAYOUT_MARGIN_TOP,
        ATTR_LAYOUT_MARGIN_BOTTOM,
        ATTR_LAYOUT_MARGIN_START,
        ATTR_LAYOUT_MARGIN_END
    );
  }

  @Override public Issue issue() {
    return ISSUE_SUPERFLUOUS_MARGIN_DECLARATION;
  }

  @Override public String message() {
    return "Should be using layout_margin instead.";
  }
}

package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_TEXT_STYLE;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Collections.singletonList;

public final class DefaultLayoutAttributeDetector extends LayoutDetector {
  static final Issue ISSUE_DEFAULT_LAYOUT_ATTRIBUTE = Issue.create("DefaultLayoutAttribute", "Specifying a default value which is not needed.",
      "Specifying a default value which is not needed.", CORRECTNESS, 8, WARNING,
      new Implementation(DefaultLayoutAttributeDetector.class, RESOURCE_FILE_SCOPE));

  @Override public Collection<String> getApplicableAttributes() {
    return singletonList(ATTR_TEXT_STYLE);
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    if (ATTR_TEXT_STYLE.equals(attribute.getLocalName())) {
      final String value = attribute.getValue();
      final boolean isSuppressed = context.getDriver().isSuppressed(context, ISSUE_DEFAULT_LAYOUT_ATTRIBUTE, attribute);

      if ("normal".equals(value) && !isSuppressed) {
        context.report(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE, context.getValueLocation(attribute), "textStyle=\"normal\" is the default and hence you don't need to specify it.");
      }
    }
  }
}

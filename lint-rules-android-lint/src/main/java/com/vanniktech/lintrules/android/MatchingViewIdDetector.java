package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.LintUtils.stripIdPrefix;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Collections.singletonList;
import static java.util.Locale.US;

public final class MatchingViewIdDetector extends LayoutDetector {
  static final Issue ISSUE_MATCHING_VIEW_ID = Issue.create("MatchingViewId",
      "Flags view ids that don't match with the file name.",
      "When the layout file is named activity_home all of the containing ids should be prefixed with activityHome to avoid ambiguity between different layout files across different views.",
      CORRECTNESS, 8, WARNING,
      new Implementation(MatchingViewIdDetector.class, RESOURCE_FILE_SCOPE));

  @Override public Collection<String> getApplicableAttributes() {
    return singletonList(ATTR_ID);
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final String fileName = toLowerCamelCase(context.file.getName().replace(".xml", ""));
    final String id = stripIdPrefix(attribute.getValue());

    if (!id.startsWith(fileName)) {
      context.report(ISSUE_MATCHING_VIEW_ID, attribute, context.getValueLocation(attribute), "Id should start with: " + fileName);
    }
  }

  static String toLowerCamelCase(final String string) {
    final Pattern p = Pattern.compile("_(.)");
    final Matcher matcher = p.matcher(string);
    final StringBuffer sb = new StringBuffer();

    while (matcher.find()) {
      matcher.appendReplacement(sb, matcher.group(1).toUpperCase(US));
    }

    matcher.appendTail(sb);

    sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
    return sb.toString();
  }
}

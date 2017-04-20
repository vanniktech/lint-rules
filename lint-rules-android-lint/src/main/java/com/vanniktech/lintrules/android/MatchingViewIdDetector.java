package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_ID;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class MatchingViewIdDetector extends LayoutDetector {
  static final Issue ISSUE_MATCHING_VIEW_ID = Issue.create("MatchingViewId", "Ids should match in regards to the layout file.",
      "Ids should match in regards to the layout file.", CORRECTNESS, 8, WARNING,
      new Implementation(MatchingViewIdDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == LAYOUT;
  }

  @Override public Collection<String> getApplicableAttributes() {
    return Collections.singletonList(ATTR_ID);
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final boolean isSuppressed = context.getDriver().isSuppressed(context, ISSUE_MATCHING_VIEW_ID, attribute);

    if (ATTR_ID.equals(attribute.getLocalName()) && !isSuppressed) {
      final String fileName = toLowerCamelCase(context.file.getName().replace(".xml", ""));
      final String id = attribute.getValue().replace("@+id/", "");

      if (!id.startsWith(fileName)) {
        context.report(ISSUE_MATCHING_VIEW_ID, context.getValueLocation(attribute), "Id should start with: " + fileName);
      }
    }
  }

  private static String toLowerCamelCase(final String string) {
    final Pattern p = Pattern.compile("_(.)");
    final Matcher matcher = p.matcher(string);
    final StringBuffer sb = new StringBuffer();

    while (matcher.find()) {
      matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
    }

    matcher.appendTail(sb);

    return sb.toString();
  }
}

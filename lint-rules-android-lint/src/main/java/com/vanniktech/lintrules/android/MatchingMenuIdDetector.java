package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import java.util.EnumSet;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_ID;
import static com.android.resources.ResourceFolderType.MENU;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.LintUtils.stripIdPrefix;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static com.vanniktech.lintrules.android.MatchingViewIdDetector.toLowerCamelCase;
import static java.util.Collections.singletonList;

public final class MatchingMenuIdDetector extends ResourceXmlDetector {
  static final Issue ISSUE_MATCHING_MENU_ID = Issue.create("MatchingMenuId",
      "Flags menu ids that don't match with the file name.",
      "When the layout file is named menu_home all of the containing ids should be prefixed with menuHome to avoid ambiguity between different menu files across different menu items.",
      CORRECTNESS, 8, WARNING,
      new Implementation(MatchingMenuIdDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return EnumSet.of(MENU).contains(folderType);
  }

  @Override public Collection<String> getApplicableAttributes() {
    return singletonList(ATTR_ID);
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final String fileName = toLowerCamelCase(context.file.getName().replace(".xml", ""));
    final String id = stripIdPrefix(attribute.getValue());

    if (!id.startsWith(fileName)) {
      context.report(ISSUE_MATCHING_MENU_ID, attribute, context.getValueLocation(attribute), "Id should start with: " + fileName);
    }
  }
}

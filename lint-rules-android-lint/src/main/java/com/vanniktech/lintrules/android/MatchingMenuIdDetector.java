package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_ID;
import static com.android.resources.ResourceFolderType.MENU;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static com.vanniktech.lintrules.android.MatchingViewIdDetector.toLowerCamelCase;
import static java.util.Collections.singletonList;

public final class MatchingMenuIdDetector extends LayoutDetector {
  static final Issue ISSUE_MATCHING_MENU_ID = Issue.create("MatchingMenuId", "Ids should match in regards to the menu file.",
      "Ids should match in regards to the menu file.", CORRECTNESS, 8, WARNING,
      new Implementation(MatchingMenuIdDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == MENU;
  }

  @Override public Collection<String> getApplicableAttributes() {
    return singletonList(ATTR_ID);
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final boolean isSuppressed = context.getDriver().isSuppressed(context, ISSUE_MATCHING_MENU_ID, attribute);

    if (ATTR_ID.equals(attribute.getLocalName()) && !isSuppressed) {
      final String fileName = toLowerCamelCase(context.file.getName().replace(".xml", ""));
      final String id = attribute.getValue().replace("@+id/", "");

      if (!id.startsWith(fileName)) {
        context.report(ISSUE_MATCHING_MENU_ID, context.getValueLocation(attribute), "Id should start with: " + fileName);
      }
    }
  }
}

package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_ID;
import static com.android.resources.ResourceFolderType.MENU;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.isCamelCase;
import static java.util.Collections.singletonList;

public final class WrongMenuIdFormatDetector extends ResourceXmlDetector {
  static final Issue ISSUE_WRONG_MENU_ID_FORMAT = Issue.create("WrongMenuIdFormat", "Ids should be in lowerCamelCase Format.",
      "Ids should be in lowerCamelCase Format.", CORRECTNESS, 8, WARNING,
      new Implementation(WrongMenuIdFormatDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == MENU;
  }

  @Override public Collection<String> getApplicableAttributes() {
    return singletonList(ATTR_ID);
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    if (ATTR_ID.equals(attribute.getLocalName())) {
      final String id = attribute.getValue().replace("@+id/", "");

      if (!isCamelCase(id)) {
        context.report(ISSUE_WRONG_MENU_ID_FORMAT, attribute, context.getValueLocation(attribute), "Id is not in lowerCamelCaseFormat");
      }
    }
  }
}

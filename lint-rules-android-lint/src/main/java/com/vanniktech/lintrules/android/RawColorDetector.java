package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_BACKGROUND;
import static com.android.SdkConstants.ATTR_BACKGROUND_TINT;
import static com.android.SdkConstants.ATTR_CACHE_COLOR_HINT;
import static com.android.SdkConstants.ATTR_CARD_BACKGROUND_COLOR;
import static com.android.SdkConstants.ATTR_COLOR;
import static com.android.SdkConstants.ATTR_ITEM_TEXT_COLOR;
import static com.android.SdkConstants.ATTR_RIPPLE_COLOR;
import static com.android.SdkConstants.ATTR_TAB_INDICATOR_COLOR;
import static com.android.SdkConstants.ATTR_TAB_SELECTED_TEXT_COLOR;
import static com.android.SdkConstants.ATTR_TEXT_COLOR;
import static com.android.SdkConstants.ATTR_TEXT_COLOR_HINT;
import static com.android.resources.ResourceFolderType.DRAWABLE;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class RawColorDetector extends LayoutDetector {
  static final Issue ISSUE_RAW_COLOR = Issue.create("RawColor", "This value should be defined as a color.",
      "This value should be defined as a color.", CORRECTNESS, 8, WARNING,
      new Implementation(RawColorDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == LAYOUT || folderType == DRAWABLE;
  }

  @Override public Collection<String> getApplicableAttributes() {
    return asList(
        ATTR_COLOR,
        ATTR_TEXT_COLOR,
        ATTR_TEXT_COLOR_HINT,
        ATTR_RIPPLE_COLOR,
        ATTR_ITEM_TEXT_COLOR,
        ATTR_CACHE_COLOR_HINT,
        ATTR_CARD_BACKGROUND_COLOR,
        ATTR_TAB_INDICATOR_COLOR,
        ATTR_TAB_SELECTED_TEXT_COLOR,
        ATTR_BACKGROUND,
        ATTR_BACKGROUND_TINT
        );
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final String value = attribute.getValue();
    final boolean isToolsAttribute = "http://schemas.android.com/tools".equalsIgnoreCase(attribute.getNamespaceURI());

    if (value.matches("#[a-fA-F\\d]{3,8}") && !isToolsAttribute) {
      context.report(ISSUE_RAW_COLOR, context.getValueLocation(attribute), "Should be using color instead.");
    }
  }
}

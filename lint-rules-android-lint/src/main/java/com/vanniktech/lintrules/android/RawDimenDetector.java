package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Arrays;
import java.util.Collection;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.SdkConstants.ATTR_PADDING;
import static com.android.SdkConstants.ATTR_PADDING_BOTTOM;
import static com.android.SdkConstants.ATTR_PADDING_END;
import static com.android.SdkConstants.ATTR_PADDING_LEFT;
import static com.android.SdkConstants.ATTR_PADDING_RIGHT;
import static com.android.SdkConstants.ATTR_PADDING_START;
import static com.android.SdkConstants.ATTR_PADDING_TOP;
import static com.android.SdkConstants.ATTR_TEXT_SIZE;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class RawDimenDetector extends LayoutDetector {
  static final Issue ISSUE_RAW_DIMEN = Issue.create("RawDimen", "This value should be defined as a dimen.",
      "This value should be defined as a dimen.", CORRECTNESS, 8, WARNING,
      new Implementation(RawDimenDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == LAYOUT;
  }

  @Override public Collection<String> getApplicableAttributes() {
    return Arrays.asList(
        ATTR_LAYOUT_MARGIN,
        ATTR_LAYOUT_MARGIN_LEFT,
        ATTR_LAYOUT_MARGIN_TOP,
        ATTR_LAYOUT_MARGIN_RIGHT,
        ATTR_LAYOUT_MARGIN_BOTTOM,
        ATTR_LAYOUT_MARGIN_START,
        ATTR_LAYOUT_MARGIN_END,
        ATTR_PADDING,
        ATTR_PADDING_LEFT,
        ATTR_PADDING_TOP,
        ATTR_PADDING_RIGHT,
        ATTR_PADDING_BOTTOM,
        ATTR_PADDING_START,
        ATTR_PADDING_END,
        ATTR_TEXT_SIZE
    );
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final String value = attribute.getValue();
    final boolean isToolsAttribute = "http://schemas.android.com/tools".equalsIgnoreCase(attribute.getNamespaceURI());
    final boolean isSuppressed = context.getDriver().isSuppressed(context, ISSUE_RAW_DIMEN, attribute);

    if (value.matches("[\\d]+(sp|dp|dip)") && !isToolsAttribute && !isSuppressed) {
      context.report(ISSUE_RAW_DIMEN, context.getValueLocation(attribute), "Should be using dimen instead.");
    }
  }
}

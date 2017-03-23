package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Attr;

import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class SuperfluousMarginDeclarationDetector extends LayoutDetector {
  static final Issue SUPERFLUOUS_MARGIN_DECLARATION = Issue.create("SuperfluousMarginDeclaration", "Instead of using start, end, bottom and top layout_margin can be used.",
      "Instead of using start, end, bottom and top layout_margin can be used.", CORRECTNESS, 8, WARNING,
      new Implementation(SuperfluousMarginDeclarationDetector.class, RESOURCE_FILE_SCOPE));

  private final Map<String, String> margins = new HashMap<>();

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == LAYOUT;
  }

  @Override public Collection<String> getApplicableAttributes() {
    return Arrays.asList(
        ATTR_LAYOUT_MARGIN_TOP,
        ATTR_LAYOUT_MARGIN_BOTTOM,
        ATTR_LAYOUT_MARGIN_START,
        ATTR_LAYOUT_MARGIN_END
    );
  }

  @Override public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final boolean isToolsAttribute = "http://schemas.android.com/tools".equalsIgnoreCase(attribute.getNamespaceURI());
    final boolean isSuppressed = context.getDriver().isSuppressed(context, SUPERFLUOUS_MARGIN_DECLARATION, attribute);

    if (!isToolsAttribute && !isSuppressed) {
      margins.put(attribute.getName(), attribute.getValue());
    }

    if (margins.size() == 4) {
      final Set<String> values = new HashSet<>(margins.values());

      if (values.size() == 1) {
        context.report(SUPERFLUOUS_MARGIN_DECLARATION, context.getLocation(attribute.getOwnerElement()), "Should be using layout_margin instead.");
      }
    }
  }
}

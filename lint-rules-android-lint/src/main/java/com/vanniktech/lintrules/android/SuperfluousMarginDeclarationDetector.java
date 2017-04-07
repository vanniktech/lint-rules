package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

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

  private final Map<Element, List<String>> margins = new HashMap<>();

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
    final Element ownerElement = attribute.getOwnerElement();
    final boolean isToolsAttribute = "http://schemas.android.com/tools".equalsIgnoreCase(attribute.getNamespaceURI());
    final boolean isSuppressed = context.getDriver().isSuppressed(context, SUPERFLUOUS_MARGIN_DECLARATION, attribute);

    if (!isToolsAttribute && !isSuppressed) {
      final List<String> values = margins.get(ownerElement);

      if (values != null) {
        values.add(attribute.getValue());
      } else {
        final List<String> list = new ArrayList<>();
        list.add(attribute.getValue());
        margins.put(ownerElement, list);
      }
    }
  }

  @Override public void afterCheckFile(final Context context) {
    final XmlContext xmlContext = (XmlContext) context;

    final Set<Map.Entry<Element, List<String>>> entries = margins.entrySet();
    for (final Map.Entry<Element, List<String>> entry : entries) {
      final List<String> marginsForElement = entry.getValue();

      if (marginsForElement.size() == 4) {
        final Set<String> values = new HashSet<>(marginsForElement);

        if (values.size() == 1) {
          context.report(SUPERFLUOUS_MARGIN_DECLARATION, xmlContext.getLocation(entry.getKey()), "Should be using layout_margin instead.");
        }
      }
    }
  }
}

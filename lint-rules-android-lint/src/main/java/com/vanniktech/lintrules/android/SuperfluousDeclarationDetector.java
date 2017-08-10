package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public abstract class SuperfluousDeclarationDetector extends LayoutDetector {
  private Map<Element, List<String>> values = new HashMap<>();

  @Override public void beforeCheckFile(final Context context) {
    values = new HashMap<>();
  }

  @Override public final void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
    final Element ownerElement = attribute.getOwnerElement();
    final boolean isToolsAttribute = "http://schemas.android.com/tools".equalsIgnoreCase(attribute.getNamespaceURI());
    final boolean isSuppressed = context.getDriver().isSuppressed(context, issue(), attribute);

    if (!isToolsAttribute && !isSuppressed) {
      final List<String> valuesForElement = values.get(ownerElement);

      if (valuesForElement != null) {
        valuesForElement.add(attribute.getValue());
      } else {
        final List<String> list = new ArrayList<>();
        list.add(attribute.getValue());
        values.put(ownerElement, list);
      }
    }
  }

  @Override public final void afterCheckFile(final Context context) {
    final XmlContext xmlContext = (XmlContext) context;
    final Set<Map.Entry<Element, List<String>>> entries = values.entrySet();

    for (final Map.Entry<Element, List<String>> entry : entries) {
      final List<String> valuesForElement = entry.getValue();

      if (valuesForElement.size() == getApplicableAttributes().size()) {
        final Set<String> distinctValues = new HashSet<>(valuesForElement);

        if (distinctValues.size() == 1) {
          context.report(issue(), xmlContext.getLocation(entry.getKey()), message());
        }
      }
    }
  }

  public abstract Issue issue();

  public abstract String message();
}

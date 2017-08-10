package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.ERROR;

public class WrongConstraintLayoutUsageDetector extends LayoutDetector {
  static final Issue ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE = Issue.create("WrongConstraintLayoutUsage", "Wrong usage of the Constraint Layout.",
      "Wrong usage of the Constraint Layout.", CORRECTNESS, 8, ERROR,
      new Implementation(WrongConstraintLayoutUsageDetector.class, RESOURCE_FILE_SCOPE));

  @Override public Collection<String> getApplicableElements() {
    return ALL;
  }

  @Override public void visitElement(final XmlContext context, final Element element) {
    final NamedNodeMap attributes = element.getAttributes();

    for (int i = 0; i < attributes.getLength(); i++) {
      final Node item = attributes.item(i);

      final String localName = item.getLocalName();

      if (localName != null) {
        final String properLocalName = localName.replace("Left", "Start").replace("Right", "End");

        final boolean isSuppressed = context.getDriver().isSuppressed(context, ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE, item);
        final boolean isConstraint = localName.contains("layout_constraint");
        final boolean hasLeft = localName.contains("Left");
        final boolean hasRight = localName.contains("Right");

        final boolean isAnIssue = isConstraint && (hasLeft || hasRight);

        if (!isSuppressed && isAnIssue) {
          context.report(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE, context.getNameLocation(item), "This attribute won't work with RTL. Please use " + properLocalName + " instead.");
        }
      }
    }
  }
}

package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.android.SdkConstants.TAG_PLURALS;
import static com.android.SdkConstants.TAG_STRING;
import static com.android.SdkConstants.TAG_STRING_ARRAY;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public class InvalidStringDetector extends ResourceXmlDetector {
  static final Issue ISSUE_INVALID_STRING = Issue.create("InvalidString", "Strings should not contain new lines or trailing whitespace.",
      "Strings should not contain new lines or trailing whitespace.", CORRECTNESS, 8, WARNING,
      new Implementation(InvalidStringDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == ResourceFolderType.VALUES;
  }

  @Override
  public Collection<String> getApplicableElements() {
    return asList(
        TAG_STRING,
        TAG_STRING_ARRAY,
        TAG_PLURALS
    );
  }

  @Override public void visitElement(@NonNull final XmlContext context, @NonNull final Element element) {
    final NodeList childNodes = element.getChildNodes();
    final int childNodesLength = childNodes.getLength();

    for (int i = 0; i < childNodesLength; i++) {
      final Node child = childNodes.item(i);

      final short childNodeType = child.getNodeType();
      final Node parentNode = child.getParentNode();

      if (childNodeType == Node.TEXT_NODE && "item".equals(parentNode.getLocalName()) || "string".equals(parentNode.getLocalName())) {
        final String text = child.getNodeValue();
        checkText(context, element, child, text);
      } else if (childNodeType == Node.ELEMENT_NODE && (parentNode.getNodeName().equals(TAG_STRING_ARRAY) || parentNode.getNodeName().equals(TAG_PLURALS))) {
        // String array or plural item children.
        final NodeList items = child.getChildNodes();
        final int itemsLength = items.getLength();

        for (int j = 0; j < itemsLength; j++) {
          final Node item = items.item(j);

          if (item.getNodeType() == Node.TEXT_NODE) {
            final String text = item.getNodeValue();
            checkText(context, child, item, text);
          }
        }
      }
    }
  }

  static void checkText(final XmlContext context, final Node element, final Node textNode, final String text) {
    if (text.contains("\n")) {
      context.report(ISSUE_INVALID_STRING, element, context.getLocation(textNode), "Text contains new line.");
    } else if (text.length() != text.trim().length()) {
      context.report(ISSUE_INVALID_STRING, element, context.getLocation(textNode), "Text contains trailing whitespace.");
    }
  }
}

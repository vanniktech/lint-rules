package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static com.android.resources.ResourceFolderType.DRAWABLE;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class RawColorDetector extends ResourceXmlDetector {
  static final Issue ISSUE_RAW_COLOR = Issue.create("RawColor", "This value should be defined as a color.",
      "This value should be defined as a color.", CORRECTNESS, 8, WARNING,
      new Implementation(RawColorDetector.class, RESOURCE_FILE_SCOPE));

  @Override public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
    return folderType == LAYOUT || folderType == DRAWABLE;
  }

  @Override public Collection<String> getApplicableElements() {
    return ALL;
  }

  @Override public void visitElement(final XmlContext context, final Element element) {
    final NamedNodeMap attributes = element.getAttributes();

    for (int i = 0; i < attributes.getLength(); i++) {
      final Node item = attributes.item(i);
      final String value = item.getNodeValue();

      final boolean isToolsAttribute = "http://schemas.android.com/tools".equalsIgnoreCase(item.getNamespaceURI());
      final boolean isVectorGraphic = "vector".equals(element.getLocalName()) || "path".equals(element.getLocalName());

      if (!isToolsAttribute && !isVectorGraphic && value.matches("#[a-fA-F\\d]{3,8}")) {
        context.report(ISSUE_RAW_COLOR, item, context.getValueLocation((Attr) item), "Should be using color instead.");
      }
    }
  }
}

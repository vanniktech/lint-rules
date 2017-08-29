package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

@JvmField val ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR = Issue.create("ConstraintLayoutToolsEditorAttributeDetector",
    "Flags tools:layout_editor properties.", "Flags tools:layout_editor properties.",
    Category.CORRECTNESS, 5, WARNING,
    Implementation(ConstraintLayoutToolsEditorAttributeDetector::class.java, Scope.RESOURCE_FILE_SCOPE))

class ConstraintLayoutToolsEditorAttributeDetector : LayoutDetector() {
  override fun getApplicableAttributes() = ALL

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    val isLayoutEditorAttribute = attribute.localName?.startsWith("layout_editor_") ?: false

    if (isLayoutEditorAttribute && attribute.hasToolsNamespace()) {
      context.report(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR, context.getNameLocation(attribute), "Don't use ${attribute.name}")
    }
  }
}

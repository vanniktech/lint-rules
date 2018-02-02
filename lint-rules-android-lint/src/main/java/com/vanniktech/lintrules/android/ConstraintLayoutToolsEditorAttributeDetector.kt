package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

@Suppress("Detekt.VariableMaxLength") val ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR = Issue.create("ConstraintLayoutToolsEditorAttribute",
    "Flags tools:layout_editor xml properties.",
    "The tools:layout_editor xml properties are only used for previewing and won't be used in your APK hence they're unnecessary and just add overhead.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(ConstraintLayoutToolsEditorAttributeDetector::class.java, Scope.RESOURCE_FILE_SCOPE))

class ConstraintLayoutToolsEditorAttributeDetector : LayoutDetector() {
  override fun getApplicableAttributes() = ALL

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    val isLayoutEditorAttribute = attribute.localName?.startsWith("layout_editor_") ?: false

    if (isLayoutEditorAttribute && attribute.hasToolsNamespace()) {
      val fix = fix()
          .unset(attribute.namespaceURI, attribute.localName)
          .name("Remove")
          .build()

      context.report(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR, attribute, context.getNameLocation(attribute), "Don't use ${attribute.name}", fix)
    }
  }
}

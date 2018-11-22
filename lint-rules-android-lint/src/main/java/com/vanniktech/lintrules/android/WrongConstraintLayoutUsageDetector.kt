package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element

val ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE = Issue.create("WrongConstraintLayoutUsage", "Marks a wrong usage of the Constraint Layout.",
    "Instead of using left & right constraints start & right should be used.",
    CORRECTNESS, PRIORITY, ERROR,
    Implementation(WrongConstraintLayoutUsageDetector::class.java, RESOURCE_FILE_SCOPE))

class WrongConstraintLayoutUsageDetector : LayoutDetector() {
  override fun getApplicableElements() = ALL

  override fun visitElement(context: XmlContext, element: Element) {
    val attributes = element.attributes

    for (i in 0 until attributes.length) {
      val item = attributes.item(i)
      val localName = item.localName

      if (localName != null) {
        val properLocalName = localName.replace("Left", "Start")
            .replace("Right", "End")

        val isConstraint = localName.contains("layout_constraint")
        val hasLeft = localName.contains("Left")
        val hasRight = localName.contains("Right")

        val isAnIssue = isConstraint && (hasLeft || hasRight)

        if (isAnIssue) {
          val fix = fix()
            .name("Fix it")
            .replace()
            .text(localName)
            .with(properLocalName)
            .autoFix()
            .build()

          context.report(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE, item, context.getNameLocation(item), "This attribute won't work with RTL. Please use $properLocalName instead.", fix)
        }
      }
    }
  }
}

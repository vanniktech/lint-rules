@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants
import com.android.SdkConstants.ANDROID_URI
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element

val ISSUE_MISSING_SCROLLBARS = Issue.create(
  id = "MissingScrollbars",
  briefDescription = "Scroll views should declare a scrollbar.",
  explanation = "Every scroll view should explicitly define whether it has scrollbars (none | vertical | horizontal).",
  category = Category.CORRECTNESS,
  priority = PRIORITY,
  severity = WARNING,
  implementation = Implementation(MissingScrollbarsDetector::class.java, RESOURCE_FILE_SCOPE)
)

class MissingScrollbarsDetector : LayoutDetector() {
  override fun getApplicableElements() = ALL

  override fun visitElement(
    context: XmlContext,
    element: Element
  ) {
    val layoutName = element.layoutName()
    if (layoutName.endsWith("ScrollView", ignoreCase = true) || layoutName.endsWith("RecyclerView", ignoreCase = true)) {
      val hasScrollbar = element.attributes().any { it.layoutAttribute() == SdkConstants.ATTR_SCROLLBARS }

      if (!hasScrollbar) {
        val quickFixes = listOf(
          SdkConstants.VALUE_VERTICAL,
          SdkConstants.VALUE_HORIZONTAL,
          SdkConstants.VALUE_NONE
        ).mapIndexed { index, it ->
          fix()
            .set(ANDROID_URI, SdkConstants.ATTR_SCROLLBARS, it)
            .name("${index + 1}. Set scrollbars to $it")
            .build()
        }.toTypedArray()

        context.report(
          issue = ISSUE_MISSING_SCROLLBARS,
          scope = element,
          location = context.getLocation(element),
          message = "Missing scrollbars on $layoutName",
          quickfixData = fix().alternatives(*quickFixes)
        )
      }
    }
  }
}

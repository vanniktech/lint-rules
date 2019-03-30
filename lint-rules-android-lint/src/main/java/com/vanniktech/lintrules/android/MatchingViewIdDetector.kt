package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_ID
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import org.w3c.dom.Attr

val ISSUE_MATCHING_VIEW_ID = Issue.create("MatchingViewId", "Flags view ids that don't match with the file name.",
    "When the layout file is named activity_home all of the containing ids should be prefixed with activityHome to avoid ambiguity between different layout files across different views.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(MatchingViewIdDetector::class.java, RESOURCE_FILE_SCOPE))

class MatchingViewIdDetector : LayoutDetector() {
  override fun getApplicableAttributes() = listOf(ATTR_ID)

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    val fileName = context.file.name.replace(".xml", "").toLowerCamelCase()
    val id = stripIdPrefix(attribute.value)
    val isAndroidId = attribute.value.startsWith("@android:id/")

    if (!id.startsWith(fileName) && !isAndroidId) {
      val fix = fix()
          .replace()
          .text(id)
          .with(toBeReplaced(fileName, id))
          .autoFix()
          .build()

      context.report(ISSUE_MATCHING_VIEW_ID, attribute, context.getValueLocation(attribute), "Id should start with: $fileName", fix)
    }
  }

  private fun toBeReplaced(fileName: String, id: String): String {
    return if (id.startsWith(fileName, ignoreCase = true)) {
      fileName + id.substring(fileName.length)
    } else {
      fileName + id.capitalize()
    }
  }
}

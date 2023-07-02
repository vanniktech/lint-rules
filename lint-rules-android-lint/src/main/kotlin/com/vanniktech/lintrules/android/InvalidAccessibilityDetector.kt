@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants
import com.android.SdkConstants.ATTR_CONTENT_DESCRIPTION
import com.android.SdkConstants.ATTR_IMPORTANT_FOR_ACCESSIBILITY
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

val ISSUE_INVALID_ACCESSIBILITY = Issue.create(
  "InvalidAccessibility",
  "Marks invalid accessibility usages.",
  "Marks usages of invalid accessibility and suggests corrections.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(InvalidAccessibilityDetector::class.java, RESOURCE_FILE_SCOPE),
)

class InvalidAccessibilityDetector : LayoutDetector() {
  override fun getApplicableAttributes() = listOf(ATTR_CONTENT_DESCRIPTION)

  override fun visitAttribute(
    context: XmlContext,
    attribute: Attr,
  ) {
    if (attribute.value == "@null") {
      val fix = fix().name("Change").composite(
        fix().set(SdkConstants.ANDROID_URI, ATTR_IMPORTANT_FOR_ACCESSIBILITY, "no").build(),
        fix().unset(attribute.namespaceURI, attribute.localName).build(),
      ).autoFix()

      context.report(ISSUE_INVALID_ACCESSIBILITY, attribute, context.getValueLocation(attribute), "Either set a proper accessibility text or use $ATTR_IMPORTANT_FOR_ACCESSIBILITY", fix)
    }
  }
}

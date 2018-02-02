package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_ID
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.LintUtils
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

val ISSUE_WRONG_VIEW_ID_FORMAT = Issue.create("WrongViewIdFormat",
    "Flag view ids that are not in lowerCamelCase Format.",
    "View ids should be in lowerCamelCase format. This has the benefit of saving an unnecessary underscore and also just looks nicer.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(WrongViewIdFormatDetector::class.java, RESOURCE_FILE_SCOPE))

class WrongViewIdFormatDetector : LayoutDetector() {
  override fun getApplicableAttributes() = listOf(ATTR_ID)

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    if (!LintUtils.stripIdPrefix(attribute.value).isLowerCamelCase()) {
      val fix = fix().replace()
          .name("Convert to lowerCamelCase")
          .text(attribute.value)
          .with(attribute.value.idToSnakeCase())
          .build()

      context.report(ISSUE_WRONG_VIEW_ID_FORMAT, attribute, context.getValueLocation(attribute), "Id is not in lowerCamelCaseFormat", fix)
    }
  }
}

package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_ID
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.MENU
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LintUtils
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import java.util.EnumSet

val ISSUE_WRONG_MENU_ID_FORMAT = Issue.create("WrongMenuIdFormat",
    "Flag menu ids that are not in lowerCamelCase Format.",
    "Menu ids should be in lowerCamelCase format. This has the benefit of saving an unnecessary underscore and also just looks nicer.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(WrongMenuIdFormatDetector::class.java, RESOURCE_FILE_SCOPE))

class WrongMenuIdFormatDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(MENU).contains(folderType)

  override fun getApplicableAttributes() = listOf(ATTR_ID)

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    if (!LintUtils.stripIdPrefix(attribute.value).isLowerCamelCase()) {
      val fix = fix().replace()
          .name("Convert to lowerCamelCase")
          .text(attribute.value)
          .with(attribute.value.idToSnakeCase())
          .build()

      context.report(ISSUE_WRONG_MENU_ID_FORMAT, attribute, context.getValueLocation(attribute), "Id is not in lowerCamelCaseFormat", fix)
    }
  }
}

package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_ID
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.MENU
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import org.w3c.dom.Attr
import java.util.*

val ISSUE_MATCHING_MENU_ID = Issue.create("MatchingMenuId", "Flags menu ids that don't match with the file name.",
    "When the layout file is named menu_home all of the containing ids should be prefixed with menuHome to avoid ambiguity between different menu files across different menu items.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(MatchingMenuIdDetector::class.java, RESOURCE_FILE_SCOPE))

class MatchingMenuIdDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(MENU).contains(folderType)

  override fun getApplicableAttributes() = listOf(ATTR_ID)

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    val fileName = context.file.name.replace(".xml", "").toLowerCamelCase()
    val id = stripIdPrefix(attribute.value)

    if (!id.startsWith(fileName)) {
      val fix = fix()
          .replace()
          .text(id)
          .with(toBeReplaced(fileName, id))
          .autoFix()
          .build()

      context.report(ISSUE_MATCHING_MENU_ID, attribute, context.getValueLocation(attribute), "Id should start with: $fileName", fix)
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

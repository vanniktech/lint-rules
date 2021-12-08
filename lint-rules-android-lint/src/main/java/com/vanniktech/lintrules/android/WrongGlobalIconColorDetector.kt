@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import com.vanniktech.lintrules.android.ColorCasingDetector.Companion.COLOR_REGEX
import org.w3c.dom.Attr

val ISSUE_WRONG_GLOBAL_ICON_COLOR = Issue.create(
  "WrongGlobalIconColor",
  "Each icon should have the same global color defined.",
  "In order to reuse icons, it's best if all icons share the same color and can be used at every position in the app. On the target side, tinting can be applied.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(WrongGlobalIconColorDetector::class.java, RESOURCE_FILE_SCOPE)
)

class WrongGlobalIconColorDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = folderType == ResourceFolderType.DRAWABLE

  override fun getApplicableAttributes() = ALL

  override fun visitAttribute(
    context: XmlContext,
    attribute: Attr
  ) {
    val value = attribute.nodeValue

    val fileName = context.file.name
    val fileMatches = (fileName.startsWith("ic_") || fileName.contains("_ic_")) && EXCLUDES_FILE_NAME.none { fileName.contains(it) }
    if (fileMatches &&
      COLOR_REGEX.matches(value) &&
      value != EXPECTED_COLOR &&
      !EXCLUDES_COLORS.contains(value)
    ) {
      val fix = fix()
        .name("Use appropriate color")
        .replace()
        .text(value)
        .with(EXPECTED_COLOR)
        .autoFix()
        .build()

      context.report(ISSUE_WRONG_GLOBAL_ICON_COLOR, attribute, context.getValueLocation(attribute), "Should use global tint color", fix)
    }
  }

  companion object {
    val EXCLUDES_FILE_NAME = listOf(
      "ic_launcher_background",
      "ic_launcher_foreground"
    )

    val EXCLUDES_COLORS = listOf(
      "#00000000"
    )

    val COLOR_REGEX = Regex("(#[a-fA-F\\d]{3,8})|(([@?])[\\w]+:?[\\w]+/[\\w\\d_.]+)")
    const val EXPECTED_COLOR = "#62FF00"
  }
}

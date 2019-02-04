package com.vanniktech.lintrules.android

import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import org.w3c.dom.Element
import java.util.Locale.US

val ISSUE_COLOR_CASING = Issue.create("ColorCasing",
    "Raw colors should be defined with uppercase letters.",
    "Colors should have uppercase letters. #FF0099 is valid while #ff0099 isn't since the ff should be written in uppercase.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(ColorCasingDetector::class.java, RESOURCE_FILE_SCOPE))

class ColorCasingDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = true

  override fun getApplicableElements() = ALL

  override fun visitElement(context: XmlContext, element: Element) {
    element.attributes()
        .filter { it.nodeValue.matches(COLOR_REGEX) }
        .filter { it.nodeValue.any { it.isLowerCase() } }
        .forEach {
          val fix = fix()
              .name("Convert to uppercase")
              .replace()
              // .range(context.getValueLocation(it as Attr)) I thought this will help but it does not.
              .text(it.nodeValue)
              .with(it.nodeValue.toUpperCase(US))
              .autoFix()
              .build()

          context.report(ISSUE_COLOR_CASING, it, context.getValueLocation(it as Attr), "Should be using uppercase letters", fix)
      }
  }

  companion object {
    val COLOR_REGEX = Regex("#[a-fA-F\\d]{3,8}")
  }
}

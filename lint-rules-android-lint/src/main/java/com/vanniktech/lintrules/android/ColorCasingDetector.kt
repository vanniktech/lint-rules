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

val ISSUE_COLOR_CASING = Issue.create("ColorCasing",
    "Raw colors should be defined with lowercase letters.",
    "Colors should have lowercase letters. #ff0099 is valid while #FF0099 isn't since the FF should be written in lower case.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(ColorCasingDetector::class.java, RESOURCE_FILE_SCOPE))

class ColorCasingDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = true

  override fun getApplicableElements() = ALL

  override fun visitElement(context: XmlContext, element: Element) {
    element.attributes()
        .filter { it.nodeValue.matches(Regex("#[a-fA-F\\d]{3,8}")) }
        .filter { it.nodeValue.any { it.isUpperCase() } }
        .forEach {
          val fix = fix()
              .name("Convert to lowercase")
              .replace()
              .text(it.nodeValue)
              .with(it.nodeValue.toLowerCase())
              .autoFix()
              .build()

          context.report(ISSUE_COLOR_CASING, it, context.getValueLocation(it as Attr), "Should be using lowercase letters", fix)
      }
  }
}

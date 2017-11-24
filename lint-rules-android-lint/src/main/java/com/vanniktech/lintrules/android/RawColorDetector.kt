package com.vanniktech.lintrules.android

import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.DRAWABLE
import com.android.resources.ResourceFolderType.LAYOUT
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import org.w3c.dom.Element
import java.util.EnumSet

val ISSUE_RAW_COLOR = Issue.create("RawColor",
    "This value should be defined as a color resource.",
    "This value should be defined as a color resource.", CORRECTNESS, 8, WARNING,
    Implementation(RawColorDetector::class.java, RESOURCE_FILE_SCOPE))

class RawColorDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) =
    EnumSet.of(LAYOUT, DRAWABLE).contains(folderType)

  override fun getApplicableElements() = ALL

  override fun visitElement(context: XmlContext, element: Element) {
    (0 until element.attributes.length)
      .map { element.attributes.item(it) }
      .filterNot { "vector" == element.localName || "path" == element.localName }
      .filterNot { "http://schemas.android.com/tools".equals(it.namespaceURI, ignoreCase = true) }
      .filter { it.nodeValue.matches("#[a-fA-F\\d]{3,8}".toRegex()) }
      .forEach { context.report(ISSUE_RAW_COLOR, it, context.getValueLocation(it as Attr), "Should be using a color resource instead.") }
  }
}

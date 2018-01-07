package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Document

val ISSUE_XML_SPACING = Issue.create("XmlSpacing",
    "XML files should not contain any new lines.",
    "Having newlines in xml files just adds noise and should be avoided.",
    CORRECTNESS, 6, WARNING,
    Implementation(XmlSpacingDetector::class.java, Scope.RESOURCE_FILE_SCOPE))

class XmlSpacingDetector : ResourceXmlDetector() {
  override fun visitDocument(context: XmlContext, document: Document) {
    val content = context.client.readFile(context.file).toString()

    content.split("\n")
        .withIndex()
        .filter { it.value.isBlank() }
        .forEach {
          val location = Location.create(context.file, it.value, it.index - 1)
          context.report(ISSUE_XML_SPACING, location, "Unnecessary new line at line ${it.index + 1}.")
        }
  }
}

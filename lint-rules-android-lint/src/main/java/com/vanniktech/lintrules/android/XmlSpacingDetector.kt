package com.vanniktech.lintrules.android

import com.android.ide.common.blame.SourcePosition
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Document

val ISSUE_XML_SPACING = Issue.create("XmlSpacing",
    "XML files should not contain any new lines.",
    "Having newlines in xml files just adds noise and should be avoided. The only exception is the new lint at the end of the file.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(XmlSpacingDetector::class.java, RESOURCE_FILE_SCOPE))

class XmlSpacingDetector : ResourceXmlDetector() {
  override fun visitDocument(context: XmlContext, document: Document) {
    val contents = context.client.readFile(context.file).toString().split("\n")

    contents
        .withIndex()
        .windowed(2)
        .filter { it[0].value.isBlank() && it.getOrNull(1)?.value?.trim()?.startsWith("<!--") == false }
        .map { it[0] }
        .filterNot { it.index == contents.size - 1 }
        .forEach {
          val location = Location.create(context.file, SourcePosition(it.index, 0, it.value.length))
          val fix = fix()
              .name("Remove new line")
              .replace()
              .range(location)
              .all()
              .autoFix(true, false)
              .build()

          context.report(ISSUE_XML_SPACING, location, "Unnecessary new line at line ${it.index + 1}.", fix)
        }
  }
}

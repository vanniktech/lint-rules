package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Document

val ISSUE_MISSING_XML_HEADER = Issue.create("MissingXmlHeader",
    "Flags xml files that don't have a header.",
    "An xml file should always have the xml header to declare that it is an xml file despite the file ending.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(MissingXmlHeaderDetector::class.java, RESOURCE_FILE_SCOPE))

class MissingXmlHeaderDetector : ResourceXmlDetector() {
  override fun visitDocument(context: XmlContext, document: Document) {
    val content = context.client.readFile(context.file)

    if (!content.startsWith("<?xml")) {
      val fix = fix()
          .replace()
          .name("Add xml header")
          .text(content.toString())
          .with("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n$content")
          .build()

      context.report(ISSUE_MISSING_XML_HEADER, document, Location.create(context.file, content, 0, content.length), "Missing an xml header.", fix)
    }
  }
}

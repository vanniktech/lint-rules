package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Document

@JvmField val ISSUE_MISSING_XML_HEADER = Issue.create("MissingXmlHeader",
    "Missing Xml header.", "The xml file is missing the xml header.", CORRECTNESS, 5, WARNING,
    Implementation(MissingXmlHeaderDetector::class.java, RESOURCE_FILE_SCOPE))

class MissingXmlHeaderDetector : ResourceXmlDetector() {
  override fun visitDocument(context: XmlContext, document: Document) {
    if (!context.file.readText().startsWith("<?xml")) {
      context.report(ISSUE_MISSING_XML_HEADER, context.getLocation(document), "Missing the xml header.")
    }
  }
}

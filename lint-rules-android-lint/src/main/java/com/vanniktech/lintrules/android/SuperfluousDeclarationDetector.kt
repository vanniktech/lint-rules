package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element
import java.util.HashSet

abstract class SuperfluousDeclarationDetector(
  private val issue: Issue,
  private val message: String,
  private val applicableSuperfluousAttributes: Collection<String>
) : LayoutDetector() {
  override fun getApplicableElements() = ALL

  override fun visitElement(context: XmlContext, element: Element) {
    val attributes = (0 until element.attributes.length)
        .map { element.attributes.item(it) }
        .filterNot { it.hasToolsNamespace() }
        .filter { applicableSuperfluousAttributes.contains(it.localName) }
        .map { it.nodeValue }
        .toList()

    if (attributes.size == applicableSuperfluousAttributes.size && HashSet<String>(attributes).size == 1) {
      // Replacing with a Lint fix isn't possible yet. https://issuetracker.google.com/issues/74599279
      context.report(issue, element, context.getLocation(element), message)
    }
  }
}

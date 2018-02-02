package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Location
import org.w3c.dom.Element
import org.w3c.dom.Node

class ElementCollectReporter(
  private val attributeToCollect: String,
  private val elementsToReport: MutableList<Pair<Node, Location>> = mutableListOf()
) : MutableCollection<Pair<Node, Location>> by elementsToReport {
  private val items = ArrayList<CollectedElement>()

  fun collect(element: Element) {
    if (attributeToCollect == element.localName) {
      items.add(CollectedElement(element.getAttribute("name"), element.firstChild.nodeValue))
    }
  }

  @Suppress("Detekt.SpreadOperator") fun report(issue: Issue, context: Context, message: String) {
    elementsToReport
        .forEach { (node, location) ->
        val fixes = possibleSuggestions(node.nodeValue)
            .map { LintFix.create().replace().all().with(it).build() }

        val fix = if (fixes.isNotEmpty()) LintFix.create().group(*fixes.toTypedArray()) else null
        context.report(issue, location, message, fix)
      }
  }

  private fun possibleSuggestions(value: String) = items.filter { it.value == value }.map { "@$attributeToCollect/${it.name}" }

  private data class CollectedElement(
    val name: String,
    val value: String
  )
}

@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Node

val ISSUE_FORMAL_GERMAN = Issue.create(
  "FormalGerman",
  "Marks strings which contain formal German words.",
  "Informal language should be used at all times.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(FormalGermanDetector::class.java, RESOURCE_FILE_SCOPE),
)

class FormalGermanDetector : StringXmlDetector() {
  override fun checkText(context: XmlContext, element: Node, text: String) {
    val items = FORMAL
      .flatMap { regex -> regex.findAll(text).map { it.range to it.value.trim() } }
      .distinctBy { it.first.first }

    items.forEach { (range, word) ->
      context.report(ISSUE_FORMAL_GERMAN, element, context.getLocation(element.firstChild, range.first, range.last), "Formal language \"$word\" detected")
    }
  }

  companion object {
    val FORMAL = setOf(
      Regex("Sie\\W"),
      Regex("Ihr\\W"),
      Regex("Ihre\\W"),
      Regex("Ihrem\\W"),
    )
    const val CDATA = "<![CDATA["
  }
}

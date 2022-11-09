@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Node

val ISSUE_IMPLICIT_STRING_PLACEHOLDER = Issue.create(
  "ImplicitStringPlaceholder",
  "Marks implicit placeholders in strings without an index.",
  "It's better and more explicit to use numbered placeholders.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(ImplicitStringPlaceholderDetector::class.java, RESOURCE_FILE_SCOPE),
)

class ImplicitStringPlaceholderDetector : StringXmlDetector() {
  override fun checkText(context: XmlContext, element: Node, text: String, textNode: Node) {
    var index = 0
    Regex("%s|%d").findAll(text).forEach { match ->
      val old = match.value
      val new = "%${++index}$" + match.value.last()
      val fix = fix().replace().name("Fix $old with $new").text(old).with(new).autoFix().build()
      context.report(ISSUE_IMPLICIT_STRING_PLACEHOLDER, element, context.getLocation(textNode, match.range.first, match.range.last + 1), "Implicit placeholder", fix)
    }
  }
}

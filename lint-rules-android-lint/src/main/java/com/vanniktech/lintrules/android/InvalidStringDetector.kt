@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Node

val ISSUE_INVALID_STRING = Issue.create(
  "InvalidString",
  "Marks invalid translation strings.",
  "A translation string is invalid if it contains new lines instead of the escaped \\n or if it contains trailing whitespace.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(InvalidStringDetector::class.java, RESOURCE_FILE_SCOPE),
)

class InvalidStringDetector : StringXmlDetector() {
  override fun checkText(context: XmlContext, element: Node, text: String) {
    val message = when {
      text.contains("\n") -> "Text contains new line."
      text.length != text.trim().length -> "Text contains trailing whitespace."
      else -> null
    }

    message?.let {
      val fix = fix().replace().name("Fix it").text(text).with(text.trim()).autoFix().build()
      context.report(ISSUE_INVALID_STRING, element, context.getLocation(element), it, fix)
    }
  }
}

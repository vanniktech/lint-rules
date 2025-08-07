@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Node

val ISSUE_QUOTES = Issue.create(
  "Quotes",
  "Marks quotes that can be improved to enhance reading.",
  "It's better to replace them with their appropriate quotes in each language.",
  CORRECTNESS,
  PRIORITY,
  WARNING,
  Implementation(QuotesDetector::class.java, RESOURCE_FILE_SCOPE),
)

class QuotesDetector : StringXmlDetector() {
  override fun checkText(context: XmlContext, node: Node, textNode: Node) {
    val language = context.file.parentFile?.name?.removePrefix("values-")?.split("-")?.first()

    if (language == "iw") {
      return
    }

    val quotes = mutableListOf<Int>()

    val string = textNode.nodeValue
    string.forEachIndexed { index, char ->
      if (char in setOf(QUOTE_NORMAL, QUOTE_TOP_INWARDS, QUOTE_TOP_OPPOSITE, QUOTE_BOTTOM_INWARDS)) {
        quotes.add(index)
      }
    }

    val chars = string.toMutableList()

    quotes.forEachIndexed { index, stringIndex ->
      val desired = when {
        index % 2 == 0 -> when (language) {
          "de", "cs" -> QUOTE_BOTTOM_INWARDS
          else -> QUOTE_TOP_INWARDS
        }
        else -> when (language) {
          "de", "cs" -> QUOTE_TOP_INWARDS
          else -> QUOTE_TOP_OPPOSITE
        }
      }

      chars[stringIndex] = desired
    }

    val expected = chars.joinToString(separator = "")

    if (string != expected) {
      val fix = fix().replace().name("Fix quotes").text(string).with(expected).autoFix().range(context.getLocation(textNode)).build()
      context.report(ISSUE_QUOTES, node, context.getLocation(textNode, quotes.first(), quotes.last() + 1), "Invalid quotes", fix)
    }
  }
}

private const val QUOTE_NORMAL = '"'
private const val QUOTE_TOP_INWARDS = '“'
private const val QUOTE_BOTTOM_INWARDS = '„'
private const val QUOTE_TOP_OPPOSITE = '”'

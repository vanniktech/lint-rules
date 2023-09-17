@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Node

val ISSUE_STRING_NOT_CAPITALIZED = Issue.create(
  "StringNotCapitalized",
  "Marks strings which are not capitalized.",
  "Every string should be capitalized. If not, it is flagged.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(StringNotCapitalizedDetector::class.java, RESOURCE_FILE_SCOPE),
)

class StringNotCapitalizedDetector : StringXmlDetector() {
  override fun checkText(context: XmlContext, node: Node, textNode: Node) {
    val text = textNode.nodeValue
    val isConfig = context.file.nameWithoutExtension == "config"
    val isLink = LINK_PREFIXES.any { text.startsWith(it, ignoreCase = true) }
    val isNonTranslatable = node.attributes.getNamedItem("translatable")?.nodeValue == "false"
    val name = node.attributes.getNamedItem("name")?.nodeValue
    val isAbbreviation = name != null && IGNORED_NAMES.any {
      name.contains("_$it", ignoreCase = true) ||
        name.contains("${it}_", ignoreCase = true)
    }
    if (isConfig || isLink || isNonTranslatable || isAbbreviation) {
      return
    }

    val chars = text.toCharArray()
    val index = when {
      text.startsWith(CDATA) -> CDATA.length - 1
      else -> 0
    }
    val char = chars.getOrNull(index) ?: return

    if (char.isLetter() && char.isLowerCase()) {
      val new = chars.take(index).joinToString(separator = "") + char.uppercase() + chars.drop(index + 1).joinToString(separator = "")
      val fix = fix().replace().name("Fix it").text(text).with(new).autoFix().build()
      context.report(ISSUE_STRING_NOT_CAPITALIZED, node, context.getLocation(textNode, index, index + new.length), "String is not capitalized", fix)
    }
  }

  companion object {
    val IGNORED_NAMES = setOf(
      "placeholder",
      "abbreviation",
    )
    val LINK_PREFIXES = setOf(
      "www.",
      "https://",
      "http://",
    )
    const val CDATA = "<![CDATA["
  }
}

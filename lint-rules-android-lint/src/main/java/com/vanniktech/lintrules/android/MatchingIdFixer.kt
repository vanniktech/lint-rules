@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.XmlContext

class MatchingIdFixer(context: XmlContext, private val id: String) {
  private val layoutName = context.file.name.replace(".xml", "")
  val expectedPrefix = layoutName.toLowerCamelCase()

  fun needsFix() = !id.startsWith(expectedPrefix)

  fun fixedId(): String {
    return if (id.startsWith(expectedPrefix, ignoreCase = true)) {
      expectedPrefix + id.substring(expectedPrefix.length)
    } else {
      //noinspection AndroidLintDefaultLocale - https://issuetracker.google.com/issues/133465551
      expectedPrefix + id.capitalize()
    }
  }
}

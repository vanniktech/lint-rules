@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.usLocaleCapitalize

class MatchingIdFixer(context: XmlContext, private val id: String) {
  private val layoutName = context.file.name.replace(".xml", "")
  val expectedPrefix = layoutName.toLowerCamelCase()

  fun needsFix() = !id.startsWith(expectedPrefix)

  fun fixedId(): String = if (id.startsWith(expectedPrefix, ignoreCase = true)) {
    expectedPrefix + id.substring(expectedPrefix.length)
  } else {
    expectedPrefix + id.usLocaleCapitalize()
  }
}

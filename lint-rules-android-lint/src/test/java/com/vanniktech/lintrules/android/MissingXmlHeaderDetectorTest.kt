package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class MissingXmlHeaderDetectorTest : LintDetectorTest() {
  fun testHasXmlHeader() {
    @Language("XML") val source = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <resources>"
    </resources>"""

    assertThat(lintProject(LintDetectorTest.xml("/res/values/strings.xml", source))).isEqualTo(NO_WARNINGS)
  }

  fun testMissingHeader() {
    @Language("XML") val source = "<resources/>"

    assertThat(lintProject(LintDetectorTest.xml("/res/values/strings.xml", source))).startsWith("""res/values/strings.xml: Warning: Missing the xml header. [MissingXmlHeader]
        |0 errors, 1 warnings""".trimMargin())
  }

  override fun getDetector() = MissingXmlHeaderDetector()

  override fun getIssues() = listOf(ISSUE_MISSING_XML_HEADER)

  override fun allowCompilationErrors() = false
}

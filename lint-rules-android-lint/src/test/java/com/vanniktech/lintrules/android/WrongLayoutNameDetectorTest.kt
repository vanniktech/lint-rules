package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class WrongLayoutNameDetectorTest : LintDetectorTest() {
  fun testActivityFile() {
    @Language("XML") val source = """<?xml version="1.0" encoding="UTF-8"?><merge/>"""
    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo(NO_WARNINGS)
  }

  fun testRandomFile() {
    @Language("XML") val source = """<?xml version="1.0" encoding="UTF-8"?><merge/>"""
    assertThat(lintProject(xml("/res/layout/random.xml", source))).startsWith("""res/layout/random.xml: Warning: Layout does not start with one of the following prefixes: activity_, view_, dialog_, bottom_sheet_, adapter_item_, divider_, space_ [WrongLayoutName]
        |0 errors, 1 warnings""".trimMargin())
  }

  fun testColorFile() {
    @Language("XML") val source = """<?xml version="1.0" encoding="UTF-8"?><resources/>"""
    assertThat(lintProject(xml("/res/values/color.xml", source))).isEqualTo(NO_WARNINGS)
  }

  override fun getDetector() = WrongLayoutNameDetector()

  override fun getIssues() = listOf(ISSUE_WRONG_LAYOUT_NAME)

  override fun allowCompilationErrors() = false
}

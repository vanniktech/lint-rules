package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class XmlSpacingDetectorTest : LintDetectorTest() {
  fun testLayoutXmlFileWithoutAnyNewLines() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <merge xmlns:android="http://schemas.android.com/apk/res/android">
          <TextView
              android:layout_width="wrap_content"/>
        </merge>"""

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo(NO_WARNINGS)
  }

  fun testLayoutXmlFileWithNewLines() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>

        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">

          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"

              />

        </LinearLayout>"""

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo("res/layout/activity_home.xml: Warning: Unnecessary new line at line 10. [XmlSpacing]\n" +
        "res/layout/activity_home.xml: Warning: Unnecessary new line at line 2. [XmlSpacing]\n" +
        "res/layout/activity_home.xml: Warning: Unnecessary new line at line 4. [XmlSpacing]\n" +
        "res/layout/activity_home.xml: Warning: Unnecessary new line at line 8. [XmlSpacing]\n" +
        "0 errors, 4 warnings\n")
  }

  override fun getDetector() = XmlSpacingDetector()

  override fun getIssues() = listOf(ISSUE_XML_SPACING)

  override fun allowCompilationErrors() = false
}

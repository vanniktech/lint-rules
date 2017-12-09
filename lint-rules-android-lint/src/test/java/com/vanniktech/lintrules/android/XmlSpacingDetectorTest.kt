package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class XmlSpacingDetectorTest {
  @Test fun layoutXmlFileWithoutAnyNewLines() {
    lint()
      .files(xml("res/layout/activity_home.xml", """
          |<merge xmlns:android="http://schemas.android.com/apk/res/android">
          |  <TextView
          |      android:layout_width="wrap_content"/>
          |</merge>""".trimMargin()))
      .issues(ISSUE_XML_SPACING)
      .run()
      .expectClean()
  }

  @Test fun layoutXmlFileWithNewLines() {
    lint()
      .files(xml("res/layout/activity_home.xml", """
          |
          |<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">
          |
          |  <TextView
          |      xmlns:android="http://schemas.android.com/apk/res/android"
          |      android:layout_width="wrap_content"
          |
          |      />
          |
          |</LinearLayout>""".trimMargin()))
      .issues(ISSUE_XML_SPACING)
      .run()
      .expect("""
          |res/layout/activity_home.xml: Warning: Unnecessary new line at line 3. [XmlSpacing]
          |res/layout/activity_home.xml: Warning: Unnecessary new line at line 7. [XmlSpacing]
          |res/layout/activity_home.xml: Warning: Unnecessary new line at line 9. [XmlSpacing]
          |res/layout/activity_home.xml:1: Warning: Unnecessary new line at line 1. [XmlSpacing]
          |
          |^
          |0 errors, 4 warnings
          |""".trimMargin())
  }
}

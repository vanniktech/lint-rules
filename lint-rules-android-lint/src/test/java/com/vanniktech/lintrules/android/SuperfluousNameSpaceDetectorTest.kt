package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class SuperfluousNameSpaceDetectorTest : LintDetectorTest() {
  fun testAndroidNamespaceOnlyOnParent() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <merge xmlns:android="http://schemas.android.com/apk/res/android">
          <TextView
              android:layout_width="wrap_content"
              />
        </merge>"""

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo(NO_WARNINGS)
  }

  fun testAndroidNamespaceOnChild() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              />
        </LinearLayout>"""

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo("res/layout/activity_home.xml:4: Warning: This name space is already declared and hence not needed. [SuperfluousNameSpace]\n" +
        "              xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
        "              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 1 warnings\n")
  }

  fun testAndroidNamespaceOnChildsChild() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">
          <LinearLayout>
            <TextView
                xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="wrap_content"
                />
          </LinearLayout>
        </LinearLayout>
        """

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo("res/layout/activity_home.xml:5: Warning: This name space is already declared and hence not needed. [SuperfluousNameSpace]\n" +
        "                xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
        "                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 1 warnings\n")
  }

  override fun getDetector() = SuperfluousNameSpaceDetector()

  override fun getIssues() = listOf(ISSUE_SUPERFLUOUS_NAME_SPACE)

  override fun allowCompilationErrors() = false
}

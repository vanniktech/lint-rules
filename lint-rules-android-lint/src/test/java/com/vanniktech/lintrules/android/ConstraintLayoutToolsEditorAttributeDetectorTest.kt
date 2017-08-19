package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class ConstraintLayoutToolsEditorAttributeDetectorTest : LintDetectorTest() {
  fun testAndroidLayoutEditor() {
    @Language("XML") val source = """<TextView
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_editor_absoluteX="4dp"
        />"""

    assertThat(lintProject(xml("/res/layout/layout.xml", source))).isEqualTo(NO_WARNINGS)
  }

  fun testAppLayoutEditor() {
    @Language("XML") val source = """<TextView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        app:layout_editor_absoluteX="4dp"
        />"""

    assertThat(lintProject(xml("/res/layout/layout.xml", source))).isEqualTo(NO_WARNINGS)
  }

  fun testToolsLayoutEditor() {
    @Language("XML") val source = """<TextView
        xmlns:tools="http://schemas.android.com/tools"
        tools:layout_editor_absoluteX="4dp"
        />"""

    assertThat(lintProject(xml("/res/layout/layout.xml", source))).isEqualTo("res/layout/layout.xml:3: Warning: Don't use tools:layout_editor_absoluteX [ConstraintLayoutToolsEditorAttributeDetector]\n" +
        "        tools:layout_editor_absoluteX=\"4dp\"\n" +
        "        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 1 warnings\n")
  }

  fun testToolsWronglySpelledLayoutEditor() {
    @Language("XML") val source = """<TextView
        xmlns:tools="http://schemas.android.com/tools"
        tools:alayout_editor_absoluteX="4dp"
        />"""

    assertThat(lintProject(xml("/res/layout/layout.xml", source))).isEqualTo(NO_WARNINGS)
  }

  override fun getDetector() = ConstraintLayoutToolsEditorAttributeDetector()

  override fun getIssues() = listOf(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR)

  override fun allowCompilationErrors() = false
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class ConstraintLayoutToolsEditorAttributeDetectorTest {
  @Test fun androidLayoutEditor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:layout_editor_absoluteX="4dp"/>""".trimMargin()))
        .issues(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR)
        .run()
        .expectClean()
  }

  @Test fun appLayoutEditor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView
          |    xmlns:app="http://schemas.android.com/apk/res-auto"
          |    app:layout_editor_absoluteX="4dp"/>""".trimMargin()))
        .issues(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR)
        .run()
        .expectClean()
  }

  @Test fun toolsLayoutEditor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView
          |    xmlns:tools="http://schemas.android.com/tools"
          |    tools:layout_editor_absoluteX="4dp"/>""".trimMargin()))
        .issues(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR)
        .run()
        .expect("""
          |res/layout/layout.xml:3: Warning: Don't use tools:layout_editor_absoluteX [ConstraintLayoutToolsEditorAttribute]
          |    tools:layout_editor_absoluteX="4dp"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/layout.xml line 2: Remove:
          |@@ -2 +2
          |- <TextView xmlns:tools="http://schemas.android.com/tools"
          |-     tools:layout_editor_absoluteX="4dp" />
          |@@ -4 +2
          |+ <TextView xmlns:tools="http://schemas.android.com/tools" />
          |""".trimMargin())
  }

  @Test fun toolsWronglySpelledLayoutEditor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView
          |    xmlns:tools="http://schemas.android.com/tools"
          |    tools:alayout_editor_absoluteX="4dp"/>""".trimMargin()))
        .issues(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR)
        .run()
        .expectClean()
  }

  @Test fun shouldNotCrashWithStyle() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView
          |style="?android:attr/borderlessButtonStyle"/>""".trimMargin()))
        .issues(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR)
        .run()
        .expectClean()
  }
}

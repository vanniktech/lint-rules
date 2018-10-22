package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class UnusedMergeAttributesDetectorTest {
  @Test fun androidAttribute() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <merge
                xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                android:layout_marginTop="64dp"
                tools:parentTag="android.support.constraint.ConstraintLayout"
                />""").indented())
        .issues(ISSUE_UNUSED_MERGE_ATTRIBUTES)
        .run()
        .expect("""
            |res/layout/view_custom.xml:4: Warning: Attribute won't be used [UnusedMergeAttributes]
            |    android:layout_marginTop="64dp"
            |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/view_custom.xml line 3: Change to tools:
            |@@ -4 +4
            |-     android:layout_marginTop="64dp"
            |+     tools:layout_marginTop="64dp"
            |""".trimMargin())
  }

  @Test fun appAttribute() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <merge
                xmlns:app="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                app:layout_marginTop="64dp"
                tools:parentTag="android.support.constraint.ConstraintLayout"
                />""").indented())
        .issues(ISSUE_UNUSED_MERGE_ATTRIBUTES)
        .run()
        .expect("""
            |res/layout/view_custom.xml:4: Warning: Attribute won't be used [UnusedMergeAttributes]
            |    app:layout_marginTop="64dp"
            |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/view_custom.xml line 3: Change to tools:
            |@@ -4 +4
            |-     app:layout_marginTop="64dp"
            |+     tools:layout_marginTop="64dp"
            |""".trimMargin())
  }

  @Test fun ignoreMergeWithoutParentTag() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <merge
                xmlns:app="http://schemas.android.com/apk/res/android"
                app:layout_marginTop="64dp"
                />""").indented())
        .issues(ISSUE_UNUSED_MERGE_ATTRIBUTES)
        .run()
        .expectClean()
  }
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.android.WrongConstraintLayoutUsageDetector.ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE
import org.junit.Test

class WrongConstraintLayoutUsageDetectorTest {
  @Test fun constraintLeftToLeftOfIgnored() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:app="http://schemas.android.com/apk/res-auto"
          |    xmlns:tools="http://schemas.android.com/tools"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    app:layout_constraintLeft_toLeftOf="parent"
          |    tools:ignore="WrongConstraintLayoutUsage"/>""".trimMargin()))
        .issues(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE)
        .run()
        .expectClean()
  }

  @Test fun constraintLeftToLeftOf() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:app="http://schemas.android.com/apk/res-auto"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    app:layout_constraintLeft_toLeftOf="parent"/>""".trimMargin()))
        .issues(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE)
        .run()
        .expect("""
          |res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintStart_toStartOf instead. [WrongConstraintLayoutUsage]
          |    app:layout_constraintLeft_toLeftOf="parent"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Fix it:
          |@@ -6 +6
          |-     app:layout_constraintLeft_toLeftOf="parent"/>
          |@@ -7 +6
          |+     app:layout_constraintStart_toStartOf="parent"/>
          |""".trimMargin())
  }

  @Test fun constraintLeftToRightOf() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:app="http://schemas.android.com/apk/res-auto"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    app:layout_constraintLeft_toRightOf="parent"/>""".trimMargin()))
        .issues(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE)
        .run()
        .expect("""
          |res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintStart_toEndOf instead. [WrongConstraintLayoutUsage]
          |    app:layout_constraintLeft_toRightOf="parent"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Fix it:
          |@@ -6 +6
          |-     app:layout_constraintLeft_toRightOf="parent"/>
          |@@ -7 +6
          |+     app:layout_constraintStart_toEndOf="parent"/>
          |""".trimMargin())
  }

  @Test fun constraintRightToRightOf() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:app="http://schemas.android.com/apk/res-auto"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    app:layout_constraintRight_toRightOf="parent"/>""".trimMargin()))
        .issues(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE)
        .run()
        .expect("""
          |res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintEnd_toEndOf instead. [WrongConstraintLayoutUsage]
          |    app:layout_constraintRight_toRightOf="parent"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Fix it:
          |@@ -6 +6
          |-     app:layout_constraintRight_toRightOf="parent"/>
          |@@ -7 +6
          |+     app:layout_constraintEnd_toEndOf="parent"/>
          |""".trimMargin())
  }

  @Test fun constraintRightToLeftOf() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:app="http://schemas.android.com/apk/res-auto"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    app:layout_constraintRight_toLeftOf="parent"/>""".trimMargin()))
        .issues(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE)
        .run()
        .expect("""
          |res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintEnd_toStartOf instead. [WrongConstraintLayoutUsage]
          |    app:layout_constraintRight_toLeftOf="parent"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Fix it:
          |@@ -6 +6
          |-     app:layout_constraintRight_toLeftOf="parent"/>
          |@@ -7 +6
          |+     app:layout_constraintEnd_toStartOf="parent"/>
          |""".trimMargin())
  }

  @Test fun shouldNotCrashWithStyle() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    style="?android:attr/borderlessButtonStyle"/>""".trimMargin()))
        .issues(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE)
        .run()
        .expectClean()
  }
}

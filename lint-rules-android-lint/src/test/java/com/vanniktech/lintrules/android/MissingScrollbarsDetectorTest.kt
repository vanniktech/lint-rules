@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class MissingScrollbarsDetectorTest {
  @Test fun textView() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="Foo"
              />"""
        ).indented()
      )
      .issues(ISSUE_MISSING_SCROLLBARS)
      .run()
      .expectClean()
  }

  @Test fun scrollViewWithScrollbars() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <ScrollView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:scrollbars="none"
              />"""
        ).indented()
      )
      .issues(ISSUE_MISSING_SCROLLBARS)
      .run()
      .expectClean()
  }

  @Test fun scrollView() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <ScrollView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              />"""
        ).indented()
      )
      .issues(ISSUE_MISSING_SCROLLBARS)
      .run()
      .expect(
        """
          |res/layout/ids.xml:1: Warning: Missing scrollbars on ScrollView [MissingScrollbars]
          |<ScrollView
          |^
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
          |Fix for res/layout/ids.xml line 1: Set scrollbars to none:
          |@@ -4 +4
          |-     android:layout_height="wrap_content" />
          |+     android:layout_height="wrap_content"
          |+     android:scrollbars="none" />
          |Fix for res/layout/ids.xml line 1: Set scrollbars to vertical:
          |@@ -4 +4
          |-     android:layout_height="wrap_content" />
          |+     android:layout_height="wrap_content"
          |+     android:scrollbars="vertical" />
          |Fix for res/layout/ids.xml line 1: Set scrollbars to horizontal:
          |@@ -4 +4
          |-     android:layout_height="wrap_content" />
          |+     android:layout_height="wrap_content"
          |+     android:scrollbars="horizontal" />
          |""".trimMargin()
      )
  }

  @Test fun recyclerView() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <androidx.recyclerview.widget.RecyclerView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              />"""
        ).indented()
      )
      .issues(ISSUE_MISSING_SCROLLBARS)
      .run()
      .expect(
        """
          |res/layout/ids.xml:1: Warning: Missing scrollbars on androidx.recyclerview.widget.RecyclerView [MissingScrollbars]
          |<androidx.recyclerview.widget.RecyclerView
          |^
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
          |Fix for res/layout/ids.xml line 1: Set scrollbars to none:
          |@@ -4 +4
          |-     android:layout_height="wrap_content" />
          |+     android:layout_height="wrap_content"
          |+     android:scrollbars="none" />
          |Fix for res/layout/ids.xml line 1: Set scrollbars to vertical:
          |@@ -4 +4
          |-     android:layout_height="wrap_content" />
          |+     android:layout_height="wrap_content"
          |+     android:scrollbars="vertical" />
          |Fix for res/layout/ids.xml line 1: Set scrollbars to horizontal:
          |@@ -4 +4
          |-     android:layout_height="wrap_content" />
          |+     android:layout_height="wrap_content"
          |+     android:scrollbars="horizontal" />
          |""".trimMargin()
      )
  }
}

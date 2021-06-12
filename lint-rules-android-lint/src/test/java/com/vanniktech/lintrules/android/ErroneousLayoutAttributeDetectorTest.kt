@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class ErroneousLayoutAttributeDetectorTest {
  @Test fun normalTextView() {
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
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expectClean()
  }

  @Test fun erroneousFrameLayoutOrientation() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <FrameLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:orientation="horizontal"
              />"""
        ).indented()
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on FrameLayout [ErroneousLayoutAttribute]
          |    android:orientation="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
          |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
          |@@ -4 +4
          |-     android:layout_height="wrap_content"
          |-     android:orientation="horizontal" />
          |@@ -6 +4
          |+     android:layout_height="wrap_content" />
          |""".trimMargin()
      )
  }

  @Test fun erroneousMergeFrameLayoutOrientation() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <merge
              xmlns:tools="http://schemas.android.com/tools"
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:orientation="horizontal"
              tools:parentTag="FrameLayout"
              />"""
        ).indented()
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
          |res/layout/ids.xml:6: Warning: Attribute is erroneous on FrameLayout [ErroneousLayoutAttribute]
          |    android:orientation="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
          |Fix for res/layout/ids.xml line 6: Delete erroneous attribute:
          |@@ -6 +6
          |-     android:orientation="horizontal"
          |""".trimMargin()
      )
  }

  @Test fun erroneousImageViewMaxLines() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <ImageView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:maxLines="2"
              />"""
        ).indented()
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on ImageView [ErroneousLayoutAttribute]
          |    android:maxLines="2"
          |    ~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
          |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
          |@@ -4 +4
          |-     android:layout_height="wrap_content"
          |-     android:maxLines="2" />
          |@@ -6 +4
          |+     android:layout_height="wrap_content" />
          |""".trimMargin()
      )
  }

  @Test fun erroneousConstraintLayoutOrientation() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <androidx.constraintlayout.widget.ConstraintLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:orientation="horizontal"
              />"""
        ).indented()
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on androidx.constraintlayout.widget.ConstraintLayout [ErroneousLayoutAttribute]
          |    android:orientation="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
        |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
        |@@ -4 +4
        |-     android:layout_height="wrap_content"
        |-     android:orientation="horizontal" />
        |@@ -6 +4
        |+     android:layout_height="wrap_content" />
        |""".trimMargin()
      )
  }

  @Test fun erroneousConstraintLayoutGravity() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <androidx.constraintlayout.widget.ConstraintLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:gravity="horizontal"
              />"""
        ).indented()
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on androidx.constraintlayout.widget.ConstraintLayout [ErroneousLayoutAttribute]
          |    android:gravity="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin()
      )
      .expectFixDiffs(
        """
          |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
          |@@ -4 +4
          |-     android:layout_height="wrap_content"
          |-     android:gravity="horizontal" />
          |@@ -6 +4
          |+     android:layout_height="wrap_content" />
          |""".trimMargin()
      )
  }

  @Test fun validLayoutGravity() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <FrameLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              >
              <androidx.constraintlayout.widget.ConstraintLayout
                  android:layout_width="wrap_content"
                  android:layout_height="wrap_content"
                  android:layout_gravity="center"/>
          </FrameLayout>"""
        ).indented()
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expectClean()
  }
}

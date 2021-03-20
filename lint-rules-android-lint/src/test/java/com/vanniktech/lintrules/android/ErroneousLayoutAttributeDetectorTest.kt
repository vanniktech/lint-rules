@file:Suppress("UnstableAPIUSage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class ErroneousLayoutAttributeDetectorTest {
  @Test fun normalTextView() {
    lint()
      .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="Foo"
              />""").indented())
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expectClean()
  }

  @Test fun erroneousFrameLayoutOrientation() {
    lint()
      .files(xml("res/layout/ids.xml", """
          <FrameLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:orientation="horizontal"
              />""").indented())
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect("""
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on FrameLayout [ErroneousLayoutAttribute]
          |    android:orientation="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
      .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
          |@@ -5 +5
          |-     android:orientation="horizontal"
          |+    
          |""".trimMargin())
  }

  @Test fun erroneousImageViewMaxLines() {
    lint()
      .files(xml("res/layout/ids.xml", """
          <ImageView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:maxLines="2"
              />""").indented())
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect("""
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on ImageView [ErroneousLayoutAttribute]
          |    android:maxLines="2"
          |    ~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
      .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
          |@@ -5 +5
          |-     android:maxLines="2"
          |+    
          |""".trimMargin())
  }

  @Test fun erroneousConstraintLayoutOrientation() {
    lint()
      .files(xml("res/layout/ids.xml", """
          <androidx.constraintlayout.widget.ConstraintLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:orientation="horizontal"
              />""").indented())
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect("""
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on androidx.constraintlayout.widget.ConstraintLayout [ErroneousLayoutAttribute]
          |    android:orientation="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
      .expectFixDiffs("""
        |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
        |@@ -5 +5
        |-     android:orientation="horizontal"
        |+    
        |""".trimMargin())
  }

  @Test fun erroneousConstraintLayoutGravity() {
    lint()
      .files(xml("res/layout/ids.xml", """
          <androidx.constraintlayout.widget.ConstraintLayout
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:gravity="horizontal"
              />""").indented())
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect("""
          |res/layout/ids.xml:5: Warning: Attribute is erroneous on androidx.constraintlayout.widget.ConstraintLayout [ErroneousLayoutAttribute]
          |    android:gravity="horizontal"
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
      .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 5: Delete erroneous attribute:
          |@@ -5 +5
          |-     android:gravity="horizontal"
          |+    
          |""".trimMargin())
  }
}

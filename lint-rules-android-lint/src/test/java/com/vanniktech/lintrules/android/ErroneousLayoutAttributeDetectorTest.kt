@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

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
              />
          """,
        ).indented(),
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
              />
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
        res/layout/ids.xml:5: Warning: Attribute is erroneous on FrameLayout [ErroneousLayoutAttribute]
            android:orientation="horizontal"
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 5: Delete erroneous orientation attribute:
        @@ -4 +4
        -     android:layout_height="wrap_content"
        -     android:orientation="horizontal" />
        +     android:layout_height="wrap_content" />
        """.trimIndent(),
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
              />
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
        res/layout/ids.xml:6: Warning: Attribute is erroneous on FrameLayout [ErroneousLayoutAttribute]
            android:orientation="horizontal"
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 6: Delete erroneous orientation attribute:
        @@ -6 +6
        -     android:orientation="horizontal"
        """.trimIndent(),
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
              />
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
        res/layout/ids.xml:5: Warning: Attribute is erroneous on ImageView [ErroneousLayoutAttribute]
            android:maxLines="2"
            ~~~~~~~~~~~~~~~~~~~~
        0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 5: Delete erroneous maxLines attribute:
        @@ -4 +4
        -     android:layout_height="wrap_content"
        -     android:maxLines="2" />
        +     android:layout_height="wrap_content" />
        """.trimIndent(),
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
              />
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
        res/layout/ids.xml:5: Warning: Attribute is erroneous on androidx.constraintlayout.widget.ConstraintLayout [ErroneousLayoutAttribute]
            android:orientation="horizontal"
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 5: Delete erroneous orientation attribute:
        @@ -4 +4
        -     android:layout_height="wrap_content"
        -     android:orientation="horizontal" />
        +     android:layout_height="wrap_content" />
        """.trimIndent(),
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
              />
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
        res/layout/ids.xml:5: Warning: Attribute is erroneous on androidx.constraintlayout.widget.ConstraintLayout [ErroneousLayoutAttribute]
            android:gravity="horizontal"
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 5: Delete erroneous gravity attribute:
        @@ -4 +4
        -     android:layout_height="wrap_content"
        -     android:gravity="horizontal" />
        +     android:layout_height="wrap_content" />
        """.trimIndent(),
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
          </FrameLayout>
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expectClean()
  }

  @Test fun erroneousConstraintLayoutProperties() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              >
            <TextView
                android:id="@+id/text"
                android:layout_width="0dp"
                android:layout_height="0dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toStartOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                />
          </LinearLayout>
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expect(
        """
        res/layout/ids.xml:10: Warning: layout_constraintBottom_toBottomOf is not allowed since we have a LinearLayout parent [ErroneousLayoutAttribute]
              app:layout_constraintBottom_toBottomOf="parent"
              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        res/layout/ids.xml:11: Warning: layout_constraintEnd_toStartOf is not allowed since we have a LinearLayout parent [ErroneousLayoutAttribute]
              app:layout_constraintEnd_toStartOf="parent"
              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        res/layout/ids.xml:12: Warning: layout_constraintStart_toStartOf is not allowed since we have a LinearLayout parent [ErroneousLayoutAttribute]
              app:layout_constraintStart_toStartOf="parent"
              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        res/layout/ids.xml:13: Warning: layout_constraintTop_toTopOf is not allowed since we have a LinearLayout parent [ErroneousLayoutAttribute]
              app:layout_constraintTop_toTopOf="parent"
              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        0 errors, 4 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 10: Delete erroneous layout_constraintBottom_toBottomOf attribute:
        @@ -11 +11
        -         app:layout_constraintBottom_toBottomOf="parent"
        Autofix for res/layout/ids.xml line 11: Delete erroneous layout_constraintEnd_toStartOf attribute:
        @@ -12 +12
        -         app:layout_constraintEnd_toStartOf="parent"
        Autofix for res/layout/ids.xml line 12: Delete erroneous layout_constraintStart_toStartOf attribute:
        @@ -13 +13
        -         app:layout_constraintStart_toStartOf="parent"
        Autofix for res/layout/ids.xml line 13: Delete erroneous layout_constraintTop_toTopOf attribute:
        @@ -13 +13
        -         app:layout_constraintStart_toStartOf="parent"
        -         app:layout_constraintTop_toTopOf="parent" />
        +         app:layout_constraintStart_toStartOf="parent" />
        """.trimIndent(),
      )
  }

  @Test fun erroneousConstraintLayoutPropertiesHonorTools() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <merge xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              xmlns:tools="http://schemas.android.com/tools"
              tools:parentTag="androidx.constraintlayout.widget.ConstraintLayout"
              >
            <TextView
                android:id="@+id/text"
                android:layout_width="0dp"
                android:layout_height="0dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toStartOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                />
          </merge>
          """,
        ).indented(),
      )
      .issues(ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE)
      .run()
      .expectClean()
  }
}

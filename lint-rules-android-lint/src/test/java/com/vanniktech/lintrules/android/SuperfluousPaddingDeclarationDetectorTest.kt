package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class SuperfluousPaddingDeclarationDetectorTest {
  @Test fun androidPaddingSame() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:paddingTop="16dp"
              android:paddingBottom="16dp"
              android:paddingStart="16dp"
              android:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using padding instead. [SuperfluousPaddingDeclaration]
          |<TextView
          |^
          |0 errors, 1 warnings""".trimMargin())
      .expectFixDiffs("""""".trimMargin())
  }

  @Test fun androidPaddingDifferent() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:paddingTop="8dp"
              android:paddingBottom="16dp"
              android:paddingStart="16dp"
              android:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun androidPaddingSameIgnored() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:tools="http://schemas.android.com/tools"
              tools:ignore="SuperfluousPaddingDeclaration"
              android:paddingTop="16dp"
              android:paddingBottom="16dp"
              android:paddingStart="16dp"
              android:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun androidPaddingStartMissing() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:paddingTop="16dp"
              android:paddingBottom="16dp"
              android:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun androidPaddingEndMissing() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:paddingTop="16dp"
              android:paddingBottom="16dp"
              android:paddingStart="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun androidPaddingBottomMissing() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:paddingTop="16dp"
              android:paddingStart="16dp"
              android:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun androidPaddingPaddingTopMissing() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:paddingBottom="16dp"
              android:paddingStart="16dp"
              android:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun declarationsSplit() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <merge
              xmlns:android="http://schemas.android.com/apk/res/android"
              >

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:paddingStart="16dp"
                android:paddingEnd="16dp"/>

            <View
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:paddingTop="16dp"
                android:paddingBottom="16dp"/>

          </merge>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }

  @Test fun toolsPaddingSame() {
    lint()
        .files(xml("res/layout/ids.xml", """
          <TextView
              xmlns:tools="http://schemas.android.com/tools"
              tools:paddingTop="16dp"
              tools:paddingBottom="16dp"
              tools:paddingStart="16dp"
              tools:paddingEnd="16dp"/>""").indented())
        .issues(ISSUE_SUPERFLUOUS_PADDING_DECLARATION)
        .run()
        .expectClean()
  }
}

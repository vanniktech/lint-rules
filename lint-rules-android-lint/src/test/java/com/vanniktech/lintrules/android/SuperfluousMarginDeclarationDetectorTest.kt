package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION
import org.junit.Test

class SuperfluousMarginDeclarationDetectorTest {
  @Test fun androidMarginSame() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  android:layout_marginTop="16dp"
          |  android:layout_marginBottom="16dp"
          |  android:layout_marginStart="16dp"
          |  android:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expect("""
          |res/layout/ids.xml:1: Warning: Should be using layout_margin instead. [SuperfluousMarginDeclaration]
          |<TextView
          |^
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidMarginDifferent() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  android:layout_marginTop="8dp"
          |  android:layout_marginBottom="16dp"
          |  android:layout_marginStart="16dp"
          |  android:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun androidMarginSameIgnored() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  xmlns:tools="http://schemas.android.com/tools"
          |  tools:ignore="SuperfluousMarginDeclaration"
          |  android:layout_marginTop="16dp"
          |  android:layout_marginBottom="16dp"
          |  android:layout_marginStart="16dp"
          |  android:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun androidMarginStartMissing() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  android:layout_marginTop="16dp"
          |  android:layout_marginBottom="16dp"
          |  android:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun androidMarginEndMissing() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  android:layout_marginTop="16dp"
          |  android:layout_marginBottom="16dp"
          |  android:layout_marginStart="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun androidMarginBottomMissing() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  android:layout_marginTop="16dp"
          |  android:layout_marginStart="16dp"
          |  android:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun androidMarginMarginTopMissing() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:android="http://schemas.android.com/apk/res/android"
          |  android:layout_marginBottom="16dp"
          |  android:layout_marginStart="16dp"
          |  android:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun declarationsSplit() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<merge
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    >
          |
          |  <TextView
          |      android:layout_width="match_parent"
          |      android:layout_height="wrap_content"
          |      android:layout_marginStart="16dp"
          |      android:layout_marginEnd="16dp"/>
          |
          |  <View
          |      android:layout_width="match_parent"
          |      android:layout_height="match_parent"
          |      android:layout_marginTop="16dp"
          |      android:layout_marginBottom="16dp"/>
          |
          |</merge>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }

  @Test fun toolsMarginSame() {
    lint()
      .files(xml("res/layout/ids.xml", """
          |<TextView
          |  xmlns:tools="http://schemas.android.com/tools"
          |  tools:layout_marginTop="16dp"
          |  tools:layout_marginBottom="16dp"
          |  tools:layout_marginStart="16dp"
          |  tools:layout_marginEnd="16dp"/>""".trimMargin()))
      .issues(SUPERFLUOUS_MARGIN_DECLARATION)
      .run()
      .expectClean()
  }
}

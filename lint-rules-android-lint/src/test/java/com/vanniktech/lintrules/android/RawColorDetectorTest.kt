package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RawColorDetectorTest {
  @Test fun ignoreToolsColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView xmlns:tools="http://schemas.android.com/tools"
          |    tools:textColor="#fff"/>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expectClean()
  }

  @Test fun appCustomColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView xmlns:app="http://schemas.android.com/apk/res-auto"
          |    app:someCustomColor="#fff"/>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expect("""
          |res/layout/layout.xml:2: Warning: Should be using a color resource instead. [RawColor]
          |    app:someCustomColor="#fff"/>
          |                         ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun textColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:textColor="#fff"/>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expect("""
          |res/layout/layout.xml:2: Warning: Should be using a color resource instead. [RawColor]
          |    android:textColor="#fff"/>
          |                       ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun textColorSuggestion() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:textColor="#fff"/>""".trimMargin()),
            xml("res/values/dimens.xml", """
          |<resources>
          |  <color name="white">#fff</color>
          |</resources>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expect("""
          |res/layout/layout.xml:2: Warning: Should be using a color resource instead. [RawColor]
          |    android:textColor="#fff"/>
          |                       ~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/layout.xml line 1: Replace with @color/white:
          |@@ -2 +2
          |-     android:textColor="#fff"/>
          |@@ -3 +2
          |+     android:textColor="@color/white"/>
          |""".trimMargin())
  }

  @Test fun ignoreText() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:text="Blub!"/>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expectClean()
  }

  @Test fun textColorIgnored() {
    lint()
        .files(xml("res/layout/layout.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:tools="http://schemas.android.com/tools"
          |    android:textColor="#fff"
          |    tools:ignore="RawColor"/>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expectClean()
  }

  @Test fun androidDrawable() {
    lint()
        .files(xml("res/drawable/drawable.xml", """
          |<shape
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:shape="rectangle">
          |  <solid android:color="#1aeebf"/>
          |  <size android:height="4dp"/>
          |</shape>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expect("""
          |res/drawable/drawable.xml:4: Warning: Should be using a color resource instead. [RawColor]
          |  <solid android:color="#1aeebf"/>
          |                        ~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun ignorePath() {
    lint()
        .files(
        xml("res/drawable/drawable.xml", """
          |<vector xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:height="24dp"
          |    android:viewportHeight="24.0"
          |    android:viewportWidth="24.0"
          |    android:width="24dp">
          |  <path
          |      android:fillColor="#000000"
          |      android:pathData="M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
          |</vector>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expectClean()
  }

  @Test fun ignoreVector() {
    lint()
        .files(
        xml("res/drawable/drawable.xml", """
          |<vector xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:height="24dp"
          |    android:viewportHeight="24.0"
          |    android:viewportWidth="24.0"
          |    android:fillColor="#000000"
          |    android:width="24dp"/>""".trimMargin())
            )
        .issues(ISSUE_RAW_COLOR)
        .run()
        .expectClean()
  }
}

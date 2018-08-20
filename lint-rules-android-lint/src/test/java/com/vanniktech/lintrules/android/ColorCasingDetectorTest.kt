package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class ColorCasingDetectorTest {
  @Test fun lowercaseColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          <TextView xmlns:tools="http://schemas.android.com/tools"
              tools:textColor="#fff"/>""").indented())
        .issues(ISSUE_COLOR_CASING)
        .run()
        .expectClean()
  }

  @Test fun uppercaseColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          <TextView xmlns:tools="http://schemas.android.com/tools"
              tools:textColor="#FFF"/>""").indented())
        .issues(ISSUE_COLOR_CASING)
        .run()
        .expect("""
            |res/layout/layout.xml:2: Warning: Should be using lowercase letters [ColorCasing]
            |    tools:textColor="#FFF"/>
            |                     ~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/layout.xml line 2: Convert to lowercase:
            |@@ -2 +2
            |-     tools:textColor="#FFF"/>
            |@@ -3 +2
            |+     tools:textColor="#fff"/>
            |""".trimMargin())
  }

  @Test fun halfUppercaseColor() {
    lint()
        .files(xml("res/drawable/drawable.xml", """
          <shape
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:shape="rectangle">
            <solid android:color="#1Aeebf"/>
            <size android:height="4dp"/>
          </shape>""").indented())
        .issues(ISSUE_COLOR_CASING)
        .run()
        .expect("""
            |res/drawable/drawable.xml:4: Warning: Should be using lowercase letters [ColorCasing]
            |  <solid android:color="#1Aeebf"/>
            |                        ~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/drawable/drawable.xml line 4: Convert to lowercase:
            |@@ -4 +4
            |-   <solid android:color="#1Aeebf"/>
            |+   <solid android:color="#1aeebf"/>
            |""".trimMargin())
  }
}

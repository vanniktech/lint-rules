package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Ignore
import org.junit.Test

class ColorCasingDetectorTest {
  @Test fun uppercaseColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          <TextView xmlns:tools="http://schemas.android.com/tools"
              tools:textColor="#FFF"/>""").indented())
        .issues(ISSUE_COLOR_CASING)
        .run()
        .expectClean()
  }

  @Test fun lowercaseColor() {
    lint()
        .files(xml("res/layout/layout.xml", """
          <TextView xmlns:tools="http://schemas.android.com/tools"
              tools:textColor="#fff"/>""").indented())
        .issues(ISSUE_COLOR_CASING)
        .run()
        .expect("""
            |res/layout/layout.xml:2: Warning: Should be using uppercase letters [ColorCasing]
            |    tools:textColor="#fff"/>
            |                     ~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/layout.xml line 2: Convert to uppercase:
            |@@ -2 +2
            |-     tools:textColor="#fff"/>
            |@@ -3 +2
            |+     tools:textColor="#FFF"/>
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
            |res/drawable/drawable.xml:4: Warning: Should be using uppercase letters [ColorCasing]
            |  <solid android:color="#1Aeebf"/>
            |                        ~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/drawable/drawable.xml line 4: Convert to uppercase:
            |@@ -4 +4
            |-   <solid android:color="#1Aeebf"/>
            |+   <solid android:color="#1AEEBF"/>
            |""".trimMargin())
  }

  @Test @Ignore("https://issuetracker.google.com/issues/123835101") fun vectorDrawableWithSameColor() {
    lint()
        .files(xml("res/drawable/drawable.xml", """
            <?xml version="1.0" encoding="utf-8"?>
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:height="800dp"
                android:viewportHeight="800"
                android:viewportWidth="800"
                android:width="800dp">
              <path
                  android:fillColor="#ffe000"
                  android:pathData="M644.161,530.032 L644.161,529.032
            C644.161,522.469,638.821,517.129,632.258,517.129 L24.807,517.129 L24.807,282.871
            L775.194,282.871 L775.194,517.129 L683.872,517.129
            C677.309,517.129,671.969,522.469,671.969,529.032 L671.969,530.032
            L644.161,530.032 Z"/>
              <path
                  android:fillColor="#ffe000"
                  android:pathData="M683.871,516.129 L774.193,516.129 L774.193,283.871 L25.807,283.871
            L25.807,516.129 L632.258,516.129
            C639.384,516.129,645.161,521.906,645.161,529.032 L670.968,529.032
            C670.968,521.906,676.745,516.129,683.871,516.129 Z"/>
            </vector>""").indented())
        .issues(ISSUE_COLOR_CASING)
        .run()
        .expect("")
  }
}

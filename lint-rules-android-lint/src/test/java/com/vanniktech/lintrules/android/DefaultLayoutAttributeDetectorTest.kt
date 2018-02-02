package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class DefaultLayoutAttributeDetectorTest {
  @Test fun textStyleNormal() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    android:textStyle="normal"/>""".trimMargin()))
        .issues(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE)
        .run()
        .expect("""
          |res/layout/ids.xml:5: Warning: This is the default and hence you don't need to specify it. [DefaultLayoutAttribute]
          |    android:textStyle="normal"/>
          |                       ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 4: Remove:
          |@@ -4 +4
          |-     android:layout_height="wrap_content"
          |-     android:textStyle="normal" />
          |@@ -6 +4
          |+     android:layout_height="wrap_content" />
          |""".trimMargin())
  }

  @Test fun textStyleBold() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    android:textStyle="bold"/>""".trimMargin()))
        .issues(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE)
        .run()
        .expectClean()
  }

  @Test fun textStyleNormalIgnored() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:tools="http://schemas.android.com/tools
          |    android:layout_width="wrap_content"
          |    android:layout_height="wrap_content"
          |    android:textStyle="normal"
          |    tools:ignore="DefaultLayoutAttribute"/>""".trimMargin()))
        .issues(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE)
        .run()
        .expectClean()
  }

  @Test fun shouldNotCrashWithStyle() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    style="?android:attr/borderlessButtonStyle"/>""".trimMargin()))
        .issues(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE)
        .run()
        .expectClean()
  }
}

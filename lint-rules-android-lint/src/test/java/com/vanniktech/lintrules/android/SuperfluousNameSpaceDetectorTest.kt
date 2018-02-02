package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class SuperfluousNameSpaceDetectorTest {
  @Test fun androidNamespaceOnlyOnParent() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<merge xmlns:android="http://schemas.android.com/apk/res/android">
          |  <TextView
          |      android:layout_width="wrap_content"/>
          |</merge>""".trimMargin()))
        .issues(ISSUE_SUPERFLUOUS_NAME_SPACE)
        .run()
        .expectClean()
  }

  @Test fun androidNamespaceOnChild() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">
          |  <TextView
          |      xmlns:android="http://schemas.android.com/apk/res/android"
          |      android:layout_width="wrap_content"/>
          |</LinearLayout>""".trimMargin()))
        .issues(ISSUE_SUPERFLUOUS_NAME_SPACE)
        .run()
        .expect("""
          |res/layout/activity_home.xml:3: Warning: This name space is already declared and hence not needed. [SuperfluousNameSpace]
          |      xmlns:android="http://schemas.android.com/apk/res/android"
          |      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/activity_home.xml line 2: Remove namespace:
          |@@ -3 +3
          |-       xmlns:android="http://schemas.android.com/apk/res/android"
          |""".trimMargin())
  }

  @Test fun androidNamespaceOnChildsChild() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android">
          |  <LinearLayout>
          |    <TextView
          |        xmlns:android="http://schemas.android.com/apk/res/android"
          |        android:layout_width="wrap_content"/>
          |  </LinearLayout>
          |</LinearLayout>""".trimMargin()))
        .issues(ISSUE_SUPERFLUOUS_NAME_SPACE)
        .run()
        .expectFixDiffs("""
          |Fix for res/layout/activity_home.xml line 3: Remove namespace:
          |@@ -4 +4
          |-         xmlns:android="http://schemas.android.com/apk/res/android"
          |""".trimMargin())
  }
}

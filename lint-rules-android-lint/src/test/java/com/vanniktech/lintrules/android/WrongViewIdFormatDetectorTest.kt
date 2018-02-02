package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongViewIdFormatDetectorTest {
  @Test fun idLowerCamelCase() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:id="@+id/lowerCamelCase"/>""".trimMargin()))
        .issues(ISSUE_WRONG_VIEW_ID_FORMAT)
        .run()
        .expectClean()
  }

  @Test fun idDefinedLowerCamelCase() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:id="@id/lowerCamelCase"/>""".trimMargin()))
        .issues(ISSUE_WRONG_VIEW_ID_FORMAT)
        .run()
        .expectClean()
  }

  @Test fun idCamelCase() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:id="@+id/CamelCase"/>""".trimMargin()))
        .issues(ISSUE_WRONG_VIEW_ID_FORMAT)
        .run()
        .expect("""
          |res/layout/ids.xml:2: Warning: Id is not in lowerCamelCaseFormat [WrongViewIdFormat]
          |    android:id="@+id/CamelCase"/>
          |                ~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 1: Convert to lowerCamelCase:
          |@@ -2 +2
          |-     android:id="@+id/CamelCase"/>
          |@@ -3 +2
          |+     android:id="@+id/camelCase"/>
          |""".trimMargin())
  }

  @Test fun idSnakeCase() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:id="@+id/snake_case"/>""".trimMargin()))
        .issues(ISSUE_WRONG_VIEW_ID_FORMAT)
        .run()
        .expect("""
          |res/layout/ids.xml:2: Warning: Id is not in lowerCamelCaseFormat [WrongViewIdFormat]
          |    android:id="@+id/snake_case"/>
          |                ~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 1: Convert to lowerCamelCase:
          |@@ -2 +2
          |-     android:id="@+id/snake_case"/>
          |@@ -3 +2
          |+     android:id="@+id/snakeCase"/>
          |""".trimMargin())
  }
}

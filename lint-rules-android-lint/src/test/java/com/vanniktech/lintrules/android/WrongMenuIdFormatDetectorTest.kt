package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongMenuIdFormatDetectorTest {
  @Test fun idLowerCamelCase() {
    lint()
        .files(xml("res/menu/ids.xml", """
          |<menu xmlns:android="http://schemas.android.com/apk/res/android">
          |  <item android:id="@+id/lowerCamelCase"/>
          |</menu>""".trimMargin()))
        .issues(ISSUE_WRONG_MENU_ID_FORMAT)
        .run()
        .expectClean()
  }

  @Test fun idDefinedLowerCamelCase() {
    lint()
        .files(xml("res/menu/ids.xml", """
          |<menu xmlns:android="http://schemas.android.com/apk/res/android">
          |  <item android:id="@id/lowerCamelCase"/>
          |</menu>""".trimMargin()))
        .issues(ISSUE_WRONG_MENU_ID_FORMAT)
        .run()
        .expectClean()
  }

  @Test fun idCamelCase() {
    lint()
        .files(xml("res/menu/ids.xml", """
          |<menu xmlns:android="http://schemas.android.com/apk/res/android">
          |  <item android:id="@+id/CamelCase"/>
          |</menu>""".trimMargin()))
        .issues(ISSUE_WRONG_MENU_ID_FORMAT)
        .run()
        .expect("""
          |res/menu/ids.xml:2: Warning: Id is not in lowerCamelCaseFormat [WrongMenuIdFormat]
          |  <item android:id="@+id/CamelCase"/>
          |                    ~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/menu/ids.xml line 1: Convert to lowerCamelCase:
          |@@ -2 +2
          |-   <item android:id="@+id/CamelCase"/>
          |+   <item android:id="@+id/camelCase"/>
          |""".trimMargin())
  }

  @Test fun idSnakeCase() {
    lint()
        .files(xml("res/menu/ids.xml", """
          |<menu xmlns:android="http://schemas.android.com/apk/res/android">
          |  <item android:id="@+id/snake_case"/>
          |</menu>""".trimMargin()))
        .issues(ISSUE_WRONG_MENU_ID_FORMAT)
        .run()
        .expect("""
          |res/menu/ids.xml:2: Warning: Id is not in lowerCamelCaseFormat [WrongMenuIdFormat]
          |  <item android:id="@+id/snake_case"/>
          |                    ~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/menu/ids.xml line 1: Convert to lowerCamelCase:
          |@@ -2 +2
          |-   <item android:id="@+id/snake_case"/>
          |+   <item android:id="@+id/snakeCase"/>
          |""".trimMargin())
  }
}

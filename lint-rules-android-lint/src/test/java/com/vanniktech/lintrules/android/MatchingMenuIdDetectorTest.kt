package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class MatchingMenuIdDetectorTest {
  @Test fun idAndroidMainPasses() {
    lint()
        .files(xml("res/menu/menu_main.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android">
              <item android:id="@+id/menuMainSomething"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidMainWrongOrder() {
    lint()
        .files(xml("res/menu/menu_main.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android">
              <item android:id="@+id/mainMenuSomething"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expect("""
            |res/menu/menu_main.xml:2: Warning: Id should start with: menuMain [MatchingMenuId]
            |  <item android:id="@+id/mainMenuSomething"/>
            |                    ~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/menu/menu_main.xml line 2: Replace with menuMain:
            |@@ -2 +2
            |-   <item android:id="@+id/mainMenuSomething"/>
            |+   <item android:id="@+id/menuMain"/>
            |""".trimMargin())
  }

  @Test fun idAndroidMenuPasses() {
    lint()
        .files(xml("res/menu/menu_custom.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android">
              <item android:id="@+id/menuCustomTextView"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidMenuUpperCaseLetter() {
    lint()
        .files(xml("res/menu/menu_main.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android">
              <item android:id="@+id/MenuMainTextView"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expect("""
            |res/menu/menu_main.xml:2: Warning: Id should start with: menuMain [MatchingMenuId]
            |  <item android:id="@+id/MenuMainTextView"/>
            |                    ~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/menu/menu_main.xml line 2: Replace with menuMain:
            |@@ -2 +2
            |-   <item android:id="@+id/MenuMainTextView"/>
            |+   <item android:id="@+id/menuMain"/>
            |""".trimMargin())
  }

  @Test fun idAndroidViewWrongOrder() {
    lint()
        .files(xml("res/menu/view_custom.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android">
              <item android:id="@+id/mainViewTextView"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expect("""
            |res/menu/view_custom.xml:2: Warning: Id should start with: viewCustom [MatchingMenuId]
            |  <item android:id="@+id/mainViewTextView"/>
            |                    ~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/menu/view_custom.xml line 2: Replace with viewCustom:
            |@@ -2 +2
            |-   <item android:id="@+id/mainViewTextView"/>
            |+   <item android:id="@+id/viewCustom"/>
            |""".trimMargin())
  }

  @Test fun idAndroidLongId() {
    lint()
        .files(xml("res/menu/view_profile_attribute_display.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android">
              <item android:id="@+id/viewProfileAttributeDisplayHeader"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidViewWrongOrderIgnored() {
    lint()
        .files(xml("res/menu/view_custom.xml", """
            <menu xmlns:android="http://schemas.android.com/apk/res/android" xmlns:tools="http://schemas.android.com/tools" tools:ignore="MatchingMenuId" >
              <item android:id="@+id/mainViewTextView"/>
            </menu>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expectClean()
  }

  @Test fun shouldNotCrashWithStyle() {
    lint()
        .files(xml("res/layout/ids.xml", """
            <TextView
                style="?android:attr/borderlessButtonStyle"/>""").indented())
        .issues(ISSUE_MATCHING_MENU_ID)
        .run()
        .expectClean()
  }
}

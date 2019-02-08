package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class MatchingViewIdDetectorTest {
  @Test fun idAndroidActivityPasses() {
    lint()
        .files(xml("res/layout/activity_main.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/activityMainTextView"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidActivityWrongOrder() {
    lint()
        .files(xml("res/layout/activity_main.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainActivityTextView"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expect("""
            |res/layout/activity_main.xml:1: Warning: Id should start with: activityMain [MatchingViewId]
            |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainActivityTextView"/>
            |                                                                                 ~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/activity_main.xml line 1: Replace with activityMain:
            |@@ -1 +1
            |- <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainActivityTextView"/>
            |@@ -2 +1
            |+ <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/activityMain"/>
            |""".trimMargin())
  }

  @Test fun idAndroidViewPasses() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/viewCustomTextView"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidUpperCaseLetter() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/ViewCustomTextView"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expect("""
            |res/layout/view_custom.xml:1: Warning: Id should start with: viewCustom [MatchingViewId]
            |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/ViewCustomTextView"/>
            |                                                                                 ~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/view_custom.xml line 1: Replace with viewCustom:
            |@@ -1 +1
            |- <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/ViewCustomTextView"/>
            |@@ -2 +1
            |+ <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/viewCustom"/>
            |""".trimMargin())
  }

  @Test fun idAndroidViewWrongOrder() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainViewTextView"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expect("""
            |res/layout/view_custom.xml:1: Warning: Id should start with: viewCustom [MatchingViewId]
            |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainViewTextView"/>
            |                                                                                 ~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for res/layout/view_custom.xml line 1: Replace with viewCustom:
            |@@ -1 +1
            |- <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainViewTextView"/>
            |@@ -2 +1
            |+ <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/viewCustom"/>
            |""".trimMargin())
  }

  @Test fun usingAndroidIdShouldBeIgnored() {
    lint()
        .files(xml("res/layout/view_profile_attribute_display.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@android:id/text1"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidLongId() {
    lint()
        .files(xml("res/layout/view_profile_attribute_display.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/viewProfileAttributeDisplayHeader"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidViewWrongOrderIgnored() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android" xmlns:tools="http://schemas.android.com/tools" tools:ignore="MatchingViewId" android:id="@+id/mainViewTextView"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun shouldNotCrashWithStyle() {
    lint()
        .files(xml("res/layout/ids.xml", """
            <TextView
              style="?android:attr/borderlessButtonStyle"/>""").indented())
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }
}

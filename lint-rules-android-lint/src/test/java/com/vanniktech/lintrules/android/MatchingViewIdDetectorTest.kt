package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.android.MatchingViewIdDetector.ISSUE_MATCHING_VIEW_ID
import org.junit.Test

class MatchingViewIdDetectorTest {
  @Test fun idAndroidActivityPasses() {
    lint()
        .files(xml("res/layout/activity_main.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/activityMainTextView"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidActivityWrongOrder() {
    lint()
        .files(xml("res/layout/activity_main.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainActivityTextView"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expect("""
          |res/layout/activity_main.xml:1: Warning: Id should start with: activityMain [MatchingViewId]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainActivityTextView"/>
          |                                                                                 ~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun idAndroidViewPasses() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/viewCustomTextView"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidUpperCaseLetter() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/ViewCustomTextView"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expect("""
          |res/layout/view_custom.xml:1: Warning: Id should start with: viewCustom [MatchingViewId]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/ViewCustomTextView"/>
          |                                                                                 ~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun idAndroidViewWrongOrder() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainViewTextView"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expect("""
          |res/layout/view_custom.xml:1: Warning: Id should start with: viewCustom [MatchingViewId]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/mainViewTextView"/>
          |                                                                                 ~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun idAndroidLongId() {
    lint()
        .files(xml("res/layout/view_profile_attribute_display.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:id="@+id/viewProfileAttributeDisplayHeader"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun idAndroidViewWrongOrderIgnored() {
    lint()
        .files(xml("res/layout/view_custom.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" xmlns:tools="http://schemas.android.com/tools" tools:ignore="MatchingViewId" android:id="@+id/mainViewTextView"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }

  @Test fun shouldNotCrashWithStyle() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView
          |    style="?android:attr/borderlessButtonStyle"/>""".trimMargin()))
        .issues(ISSUE_MATCHING_VIEW_ID)
        .run()
        .expectClean()
  }
}

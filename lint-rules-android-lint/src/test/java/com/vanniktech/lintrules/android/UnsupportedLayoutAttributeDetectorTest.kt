package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class UnsupportedLayoutAttributeDetectorTest {
  @Test fun orientationInRelativeLayout() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<RelativeLayout
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:orientation="vertical"/>""".trimMargin()))
        .issues(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE)
        .run()
        .expect("""
          |res/layout/activity_home.xml:3: Error: orientation is not allowed in RelativeLayout [UnsupportedLayoutAttribute]
          |    android:orientation="vertical"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/activity_home.xml line 2: Remove unnecessary attribute:
          |@@ -2 +2
          |- <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
          |-     android:orientation="vertical" />
          |@@ -4 +2
          |+ <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android" />
          |""".trimMargin())
  }

  @Test fun orientationInScrollView() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<ScrollView
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:orientation="vertical"/>""".trimMargin()))
        .issues(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE)
        .run()
        .expect("""
          |res/layout/activity_home.xml:3: Error: orientation is not allowed in ScrollView [UnsupportedLayoutAttribute]
          |    android:orientation="vertical"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/activity_home.xml line 2: Remove unnecessary attribute:
          |@@ -2 +2
          |- <ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
          |-     android:orientation="vertical" />
          |@@ -4 +2
          |+ <ScrollView xmlns:android="http://schemas.android.com/apk/res/android" />
          |""".trimMargin())
  }

  @Test fun orientationInMergeScrollView() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<merge
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:tools="http://schemas.android.com/tools"
          |    tools:parentTag="ScrollView"
          |    android:orientation="vertical"/>""".trimMargin()))
        .issues(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE)
        .run()
        .expect("""
          |res/layout/activity_home.xml:5: Error: orientation is not allowed in ScrollView [UnsupportedLayoutAttribute]
          |    android:orientation="vertical"/>
          |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun orientationInLinearLayout() {
    lint()
        .files(xml("res/layout/activity_home.xml", """
          |<LinearLayout
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:orientation="vertical"/>""".trimMargin()))
        .issues(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE)
        .run()
        .expectClean()
  }
}

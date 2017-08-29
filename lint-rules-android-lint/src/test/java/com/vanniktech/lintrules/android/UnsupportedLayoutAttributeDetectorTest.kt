package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class UnsupportedLayoutAttributeDetectorTest : LintDetectorTest() {
  fun testOrientationInRelativeLayout() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <RelativeLayout
          xmlns:android="http://schemas.android.com/apk/res/android"
          android:orientation="vertical"/>
        """

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo("res/layout/activity_home.xml:4: Error: orientation is not allowed in RelativeLayout [UnsupportedLayoutAttribute]\n" +
        "          android:orientation=\"vertical\"/>\n" +
        "          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "1 errors, 0 warnings\n")
  }

  fun testOrientationInScrollView() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <ScrollView
          xmlns:android="http://schemas.android.com/apk/res/android"
          android:orientation="vertical"/>
        """

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo("res/layout/activity_home.xml:4: Error: orientation is not allowed in ScrollView [UnsupportedLayoutAttribute]\n" +
        "          android:orientation=\"vertical\"/>\n" +
        "          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "1 errors, 0 warnings\n")
  }

  fun testOrientationInMergeScrollView() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <merge
          xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:tools="http://schemas.android.com/tools"
          tools:parentTag="ScrollView"
          android:orientation="vertical"/>
        """

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo("res/layout/activity_home.xml:6: Error: orientation is not allowed in ScrollView [UnsupportedLayoutAttribute]\n" +
        "          android:orientation=\"vertical\"/>\n" +
        "          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "1 errors, 0 warnings\n")
  }

  fun testOrientationInLinearLayout() {
    @Language("XML") val source = """<?xml version="1.0" encoding="utf-8"?>
        <LinearLayout
          xmlns:android="http://schemas.android.com/apk/res/android"
          android:orientation="vertical"/>
        """

    assertThat(lintProject(xml("/res/layout/activity_home.xml", source))).isEqualTo(NO_WARNINGS)
  }

  override fun getDetector() = UnsupportedLayoutAttributeDetector()

  override fun getIssues() = listOf(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE)

  override fun allowCompilationErrors() = false
}

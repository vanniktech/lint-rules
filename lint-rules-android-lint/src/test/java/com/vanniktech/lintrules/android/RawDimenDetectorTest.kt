package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RawDimenDetectorTest {
  @Test fun toolsMargin() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:tools="http://schemas.android.com/tools" tools:layout_margin="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }

  @Test fun appCustom() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:app="http://schemas.android.com/apk/res-auto" app:someCustomAttribute="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:app="http://schemas.android.com/apk/res-auto" app:someCustomAttribute="16dp"/>
          |                                                                                       ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidMarginSuggestion() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |   android:layout_margin="16dp"/>""".trimMargin()),
            xml("res/values/dimens.xml", """
          |<resources>
          | <dimen name="content_margin">16dp</dimen>
          |</resources>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:2: Warning: Should be using a dimension resource instead. [RawDimen]
          |   android:layout_margin="16dp"/>
          |                          ~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/layout/ids.xml line 1: Replace with @dimen/content_margin:
          |@@ -2 +2
          |-    android:layout_margin="16dp"/>
          |@@ -3 +2
          |+    android:layout_margin="@dimen/content_margin"/>
          |""".trimMargin())
  }

  @Test fun androidMarginIgnored() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android"
          |   xmlns:tools="http://schemas.android.com/tools"
          |   tools:ignore="RawDimen"
          |   android:layout_margin="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }

  @Test fun androidDrawable() {
    lint()
        .files(xml("res/drawable/drawable.xml", """
          |<shape
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:shape="rectangle">
          |  <solid android:color="#1aeebf"/>
          |  <size android:height="4dp"/>
          |</shape>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/drawable/drawable.xml:5: Warning: Should be using a dimension resource instead. [RawDimen]
          |  <size android:height="4dp"/>
          |                        ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidLayoutWidth0Dp() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="0dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="0dp"/>
          |                                                                                           ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidLayoutWidth0DpIgnoreWhenLayoutWeightSet() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="0dp" android:layout_weight="1"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }

  @Test fun androidLayoutHeight0Dp() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_height="0dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_height="0dp"/>
          |                                                                                            ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidLayoutHeight0DpIgnoreWhenLayoutWeightSet() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_height="0dp" android:layout_weight="1"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }

  @Test fun androidLayoutWidth() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="16dp"/>
          |                                                                                           ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidLayoutHeight() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_height="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_height="16dp"/>
          |                                                                                            ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidMargin() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_margin="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_margin="16dp"/>
          |                                                                                            ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidMarginHalfDp() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_margin="0.5dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_margin="0.5dp"/>
          |                                                                                            ~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidMinusPaddingEnd() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:paddingEnd="-16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:paddingEnd="-16dp"/>
          |                                                                                         ~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun androidTextSize() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:textSize="16dp"/>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expect("""
          |res/layout/ids.xml:1: Warning: Should be using a dimension resource instead. [RawDimen]
          |<TextView xmlns:android="http://schemas.android.com/apk/res/android" android:textSize="16dp"/>
          |                                                                                       ~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun ignore0DpConstraintLayout() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<android.support.constraint.ConstraintLayout
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:layout_width="match_parent"
          |    android:layout_height="wrap_content">
          |
          |  <TextView
          |      android:layout_width="0dp"
          |      android:layout_height="wrap_content"/>
          |</android.support.constraint.ConstraintLayout>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }

  @Test fun ignore0DpMergeConstraintLayout() {
    lint()
        .files(xml("res/layout/ids.xml", """
          |<merge
          |    xmlns:android="http://schemas.android.com/apk/res/android"
          |    xmlns:tools="http://schemas.android.com/tools"
          |    android:layout_width="match_parent"
          |    android:layout_height="wrap_content"
          |    tools:parentTag="android.support.constraint.ConstraintLayout">
          |
          |  <TextView
          |      android:layout_width="0dp"
          |      android:layout_height="wrap_content"/>
          |</merge>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }

  @Test fun ignoreVector() {
    lint()
        .files(xml("res/drawable/icon.xml", """
          |<?xml version="1.0" encoding="UTF-8" standalone="no"?>
          |<vector xmlns:android="http://schemas.android.com/apk/res/android"
          |    android:height="24dp"
          |    android:viewportHeight="24.0"
          |    android:viewporstWidth="24.0"
          |    android:width="24dp">
          |  <path
          |      android:fillColor="#000000"
          |      android:pathData="M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
          |</vector>""".trimMargin()))
        .issues(ISSUE_RAW_DIMEN)
        .run()
        .expectClean()
  }
}

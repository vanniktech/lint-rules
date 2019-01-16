package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.gradle
import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongLayoutNameDetectorTest {
  @Test fun activityFile() {
    lint()
        .files(xml("res/layout/activity_home.xml", "<merge/>"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expectClean()
  }

  @Test fun activityFileWithResourcePrefix() {
    lint()
        .files(xml("res/layout/unit_test_prefix_activity_home.xml", "<merge/>"), resourcePrefix("unit_test_prefix_"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expectClean()
  }

  @Test fun layoutMatchesExactly() {
    lint()
        .files(xml("res/layout/calculator_view.xml", "<merge/>"), resourcePrefix("calculator_"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expectClean()
  }

  @Test fun randomLayoutFile() {
    lint()
        .files(xml("res/layout/random.xml", "<merge/>"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expect("""
          |res/layout/random.xml: Warning: Layout does not start with one of the following prefixes: activity_, view_, fragment_, dialog_, bottom_sheet_, adapter_item_, divider_, space_, popup_window_ [WrongLayoutName]
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun randomLayoutFileWithResourcePrefix() {
    lint()
        .files(xml("res/layout/random.xml", "<merge/>"), resourcePrefix("unit_test_prefix_"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expect("""
          |res/layout/random.xml: Warning: Layout does not start with one of the following prefixes: unit_test_prefix_activity_, unit_test_prefix_view_, unit_test_prefix_fragment_, unit_test_prefix_dialog_, unit_test_prefix_bottom_sheet_, unit_test_prefix_adapter_item_, unit_test_prefix_divider_, unit_test_prefix_space_, unit_test_prefix_popup_window_ [WrongLayoutName]
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun ignoreColorFile() {
    lint()
        .files(xml("res/values/color.xml", "<resources/>"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expectClean()
  }
}

fun resourcePrefix(prefix: String) = gradle("""
        apply plugin: 'com.android.library'

        android {
          resourcePrefix '$prefix'
        }""").indented()

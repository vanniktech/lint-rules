package com.vanniktech.lintrules.android

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

  @Test fun randomLayoutFile() {
    lint()
        .files(xml("res/layout/random.xml", "<merge/>"))
        .issues(ISSUE_WRONG_LAYOUT_NAME)
        .run()
        .expect("""
          |res/layout/random.xml: Warning: Layout does not start with one of the following prefixes: activity_, view_, fragment_, dialog_, bottom_sheet_, adapter_item_, divider_, space_ [WrongLayoutName]
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

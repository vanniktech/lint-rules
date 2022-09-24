@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongDrawableNameDetectorTest {
  @Test fun iconFile() {
    lint()
      .files(xml("src/main/res/drawable/ic_test.xml", "<merge/>"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }

  @Test fun imgFile() {
    lint()
      .files(xml("src/main/res/drawable/img_file.xml", "<merge/>"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }

  @Test fun resourcePrefixWithNoEndingUnderscore() {
    lint()
      .files(xml("src/main/res/drawable/unit_test_prefix_ic_test.xml", "<merge/>"), resourcePrefix("unit_test_prefix"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }

  @Test fun matchesExactly() {
    lint()
      .files(xml("src/main/res/drawable/calculator_img.xml", "<merge/>"), resourcePrefix("calculator_"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }

  @Test fun exoDrawableFile() {
    lint()
      .files(xml("res/drawable/exo_ic_play.xml", "<merge/>"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }

  @Test fun exoDrawableFileWithResourcePrefix() {
    lint()
      .files(xml("src/main/res/drawable/exo_ic_play.xml", "<merge/>"), resourcePrefix("unit_test_prefix_"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }

  @Test fun randomDrawableFile() {
    lint()
      .files(xml("res/drawable/random.xml", "<merge/>"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expect(
        """
          |res/drawable/random.xml: Warning: Drawable does not start with one of the following prefixes: animated_selector, animated_vector_, background_, ic_, img_, notification_icon_, ripple_, selector_, shape_, vector_ [WrongDrawableName]
          |0 errors, 1 warnings
        """.trimMargin(),
      )
  }

  @Test fun randomDrawableFileWithResourcePrefix() {
    lint()
      .files(xml("src/main/res/drawable/random.xml", "<merge/>"), resourcePrefix("unit_test_prefix_"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expect(
        """
          |src/main/res/drawable/random.xml: Warning: Drawable does not start with one of the following prefixes: unit_test_prefix_animated_selector, unit_test_prefix_animated_vector_, unit_test_prefix_background_, unit_test_prefix_ic_, unit_test_prefix_img_, unit_test_prefix_notification_icon_, unit_test_prefix_ripple_, unit_test_prefix_selector_, unit_test_prefix_shape_, unit_test_prefix_vector_ [WrongDrawableName]
          |0 errors, 1 warnings
        """.trimMargin(),
      )
  }

  @Test fun ignoreLayoutFile() {
    lint()
      .files(xml("src/main/res/layout/color.xml", "<resources/>"))
      .issues(ISSUE_WRONG_DRAWABLE_NAME)
      .run()
      .expectClean()
  }
}

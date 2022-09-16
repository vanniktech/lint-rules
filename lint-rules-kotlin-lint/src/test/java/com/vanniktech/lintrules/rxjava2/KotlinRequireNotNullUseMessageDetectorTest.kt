@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.kotlin

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class KotlinRequireNotNullUseMessageDetectorTest {
  @Test fun requireNotNull() {
    lint()
      .files(
        kotlin(
          """
          fun test(value: Int?) {
            requireNotNull(value)
          }
          """,
        ).indented(),
      )
      .issues(ISSUE_KOTLIN_REQUIRE_NOT_NULL_USE_MESSAGE)
      .run()
      .expect(
        """
          |src/test.kt:2: Warning: Provide a message [KotlinRequireNotNullUseMessage]
          |  requireNotNull(value)
          |  ~~~~~~~~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin(),
      )
  }

  @Test fun requireNotNullWithMessage() {
    lint()
      .files(
        kotlin(
          """
          fun test(value: Int?) {
            requireNotNull(value) { "Foo" }
          }
          """,
        ).indented(),
      )
      .issues(ISSUE_KOTLIN_REQUIRE_NOT_NULL_USE_MESSAGE)
      .run()
      .expectClean()
  }
}

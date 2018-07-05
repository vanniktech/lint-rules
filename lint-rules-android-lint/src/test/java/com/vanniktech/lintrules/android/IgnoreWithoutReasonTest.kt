package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class IgnoreWithoutReasonTest {

    @Test
    fun noAnnotations() {
        lint().allowCompilationErrors()
                .files(
                        java(
                                """
                          |package foo;
                          |
                          |class MyTest {
                          |  @Test fun something() {
                          |  }
                          |}""".trimMargin()
                        )
                )
                .issues(ISSUE_IGNORE_WITHOUT_REASON)
                .run()
                .expectClean()
    }

    @Test
    fun annotationWithReasonOnFunction() {
        lint().allowCompilationErrors()
                .files(
                        java(
                                """
                          |package foo;
                          |
                          |class MyTest {
                          |  @Ignore("reason")
                          |  @Test fun something() {
                          |  }
                          |}""".trimMargin()
                        )
                )
                .issues(ISSUE_IGNORE_WITHOUT_REASON)
                .run()
                .expectClean()
    }

    @Test
    fun annotationWithReasonOnClass() {
        lint().allowCompilationErrors()
                .files(
                        java(
                                """
                          |package foo;
                          |
                          |@Ignore("reason")
                          |class MyTest {
                          |
                          |  @Test fun something() {
                          |  }
                          |}""".trimMargin()
                        )
                )
                .issues(ISSUE_IGNORE_WITHOUT_REASON)
                .run()
                .expectClean()
    }

    @Test
    fun annotationWithoutReasonOnClass() {
        lint().allowCompilationErrors()
                .files(
                        java(
                                """
                          |package foo;
                          |
                          |@Ignore
                          |class MyTest {
                          |
                          |  @Test fun something() {
                          |  }
                          |}""".trimMargin()
                        )
                )
                .issues(ISSUE_IGNORE_WITHOUT_REASON)
                .run()
                .expect("""
src/foo/MyTest.java:4: Warning: Test is ignored without given any explanation. [IgnoreWithoutReason]
class MyTest {
      ~~~~~~
0 errors, 1 warnings
        """.trimMargin())
    }

    @Test
    fun annotationWithoutReasonOnFunction() {
        lint().allowCompilationErrors()
                .files(
                        java(
                                """
                          |package foo;
                          |
                          |class MyTest {
                          |  @Ignore
                          |  @Test fun something() {
                          |  }
                          |}""".trimMargin()
                        )
                )
                .issues(ISSUE_IGNORE_WITHOUT_REASON)
                .run()
                .expect("""
src/foo/MyTest.java:5: Warning: Test is ignored without given any explanation. [IgnoreWithoutReason]
  @Test fun something() {
            ~~~~~~~~~
0 errors, 1 warnings
        """.trimMargin())
    }
}

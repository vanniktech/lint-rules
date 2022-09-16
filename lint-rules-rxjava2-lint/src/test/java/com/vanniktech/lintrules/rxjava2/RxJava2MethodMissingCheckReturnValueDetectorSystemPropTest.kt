@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.After
import org.junit.Test

class RxJava2MethodMissingCheckReturnValueDetectorSystemPropTest {
  @After fun tearDown() {
    System.clearProperty(IGNORE_MODIFIERS_PROP)
  }

  @Test fun privateMethodReturningObservableMissingCheckReturnValue() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private")

    lint()
      .files(
        rxJava2(),
        java(
          """
        package foo;

        import io.reactivex.Observable;

        class Example {
          private Observable<Object> foo() {
            return null;
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun multipleIgnoredModifiers() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private,protected")

    lint()
      .files(
        rxJava2(),
        java(
          """
        package foo;

        import io.reactivex.Observable;

        class Example {
          private Observable<Object> foo() {
            return null;
          }

          protected Observable<Object> bar() {
            return null;
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun protectedMethodReturningObservableMissingCheckReturnValue() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private")

    lint()
      .files(
        rxJava2(),
        java(
          """
        package foo;

        import io.reactivex.Observable;

        class Example {
          protected Observable<Object> foo() {
            return null;
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run().expect(
        """
        |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
        |  protected Observable<Object> foo() {
        |                               ~~~
        |0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
        |Fix for src/foo/Example.java line 6: Add @CheckReturnValue:
        |@@ -6 +6
        |-   protected Observable<Object> foo() {
        |+   @io.reactivex.annotations.CheckReturnValue protected Observable<Object> foo() {
        |
        """.trimMargin(),
      )
  }

  @Test fun kotlinPrivateMethodReturningTestSubscriberMissingCheckReturnValue() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private")

    lint()
      .files(
        rxJava2(),
        kt(
          """
        package foo

        import io.reactivex.Observable

        class Example {
          private fun foo(): Observable<Object>? {
            return null
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  companion object {
    private const val IGNORE_MODIFIERS_PROP = "com.vanniktech.lintrules.rxjava2.RxJava2MethodMissingCheckReturnValueDetector.ignoreMethodAccessModifiers"
  }
}

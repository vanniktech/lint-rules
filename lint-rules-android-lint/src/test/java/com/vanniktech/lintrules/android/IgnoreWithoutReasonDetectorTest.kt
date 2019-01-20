package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class IgnoreWithoutReasonDetectorTest {
  @Test fun noAnnotations() {
    lint()
        .files(stubJUnitTest, java("""
              package foo;

              import org.junit.Test;

              class MyTest {
                @Test fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expectClean()
  }

  @Test fun ignoreAnnotationWithReasonOnFunction() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.Ignore;
              import org.junit.Test;

              class MyTest {
                @Test @Ignore("reason") fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expectClean()
  }

  @Test fun ignoreAnnotationWithReasonOnClass() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.Ignore;
              import org.junit.Test;

              @Ignore("reason") class MyTest {
                @Test fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expectClean()
  }

  @Test fun ignoreAnnotationWithoutReasonOnClass() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.Ignore;
              import org.junit.Test;

              @Ignore class MyTest {
                @Test fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expect("""
            |src/foo/MyTest.java:6: Warning: Test is ignored without given any explanation. [IgnoreWithoutReason]
            |@Ignore class MyTest {
            |~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun ignoreAnnotationWithoutReasonOnFunction() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.Ignore;
              import org.junit.Test;

              class MyTest {
                @Test @Ignore fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expect("""
            |src/foo/MyTest.java:7: Warning: Test is ignored without given any explanation. [IgnoreWithoutReason]
            |  @Test @Ignore fun something() {
            |        ~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun disabledAnnotationWithReasonOnFunction() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.jupiter.api.Disabled;
              import org.junit.jupiter.api.Test;

              class MyTest {
                @Test @Disabled("reason") fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expectClean()
  }

  @Test fun disabledAnnotationWithReasonOnClass() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.jupiter.api.Disabled;
              import org.junit.jupiter.api.Test;

              @Disabled("reason") class MyTest {
                @Test fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expectClean()
  }

  @Test fun disabledAnnotationWithoutReasonOnClass() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.jupiter.api.Disabled;
              import org.junit.jupiter.api.Test;

              @Disabled class MyTest {
                @Test fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expect("""
            |src/foo/MyTest.java:6: Warning: Test is ignored without given any explanation. [IgnoreWithoutReason]
            |@Disabled class MyTest {
            |~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun disabledAnnotationWithoutReasonOnFunction() {
    lint()
        .files(stubJUnitTest, stubJUnitIgnore, java("""
              package foo;

              import org.junit.jupiter.api.Disabled;
              import org.junit.jupiter.api.Test;

              class MyTest {
                @Test @Disabled fun something() {
                }
              }""").indented()
        )
        .issues(ISSUE_IGNORE_WITHOUT_REASON)
        .run()
        .expect("""
            |src/foo/MyTest.java:7: Warning: Test is ignored without given any explanation. [IgnoreWithoutReason]
            |  @Test @Disabled fun something() {
            |        ~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
  }
}

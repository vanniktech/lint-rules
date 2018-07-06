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

  @Test fun annotationWithReasonOnFunction() {
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

  @Test fun annotationWithReasonOnClass() {
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

  @Test fun annotationWithoutReasonOnClass() {
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

  @Test fun annotationWithoutReasonOnFunction() {
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
}

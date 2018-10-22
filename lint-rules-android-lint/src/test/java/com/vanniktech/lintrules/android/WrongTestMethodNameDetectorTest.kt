package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongTestMethodNameDetectorTest {
  @Test fun methodStartingWithTest() {
    lint()
        .files(java("""
          package foo;

          public class Something {
            public void testThis() { }
          }""").indented())
        .issues(ISSUE_WRONG_TEST_METHOD_NAME)
        .run()
        .expectClean()
  }

  @Test fun methodStartingWithTestAndSomethingAnnotation() {
    lint()
        .files(stubAnnotationSomething, java("""
          package foo;

          import my.custom.Something;

          public class MyTest {
            @Something public void test() { }
          }""").indented())
        .issues(ISSUE_WRONG_TEST_METHOD_NAME)
        .run()
        .expectClean()
  }

  @Test fun methodNotStartingWithTestAndTestAnnotation() {
    lint()
        .files(stubJUnitTest, java("""
          package foo;

          import org.junit.Test;

          public class MyTest {
            @Test public void myTest() { }
          }""").indented())
        .issues(ISSUE_WRONG_TEST_METHOD_NAME)
        .run()
        .expectClean()
  }

  @Test fun methodStartingWithTestAndJUnitTestAnnotation() {
    lint()
        .files(stubJUnitTest, java("""
          package foo;

          import org.junit.Test;

          public class MyTest {
            @Test public void testSomething() { }
          }""").indented())
        .issues(ISSUE_WRONG_TEST_METHOD_NAME)
        .run()
        .expect("""
          |src/foo/MyTest.java:6: Warning: Test method starts with test. [WrongTestMethodName]
          |  @Test public void testSomething() { }
          |                    ~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/MyTest.java line 5: Remove test prefix:
          |@@ -6 +6
          |-   @Test public void testSomething() { }
          |+   @Test public void something() { }
          |""".trimMargin())
  }

  @Test fun methodStartingWithTestAndCustomTestAnnotation() {
    lint()
        .files(stubAnnotationTest, java("""
          package foo;

          import my.custom.Test;

          public class MyTest {
            @Test public void testSomething() { }
          }""").indented())
        .issues(ISSUE_WRONG_TEST_METHOD_NAME)
        .run()
        .expect("""
          |src/foo/MyTest.java:6: Warning: Test method starts with test. [WrongTestMethodName]
          |  @Test public void testSomething() { }
          |                    ~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/MyTest.java line 5: Remove test prefix:
          |@@ -6 +6
          |-   @Test public void testSomething() { }
          |+   @Test public void something() { }
          |""".trimMargin())
  }
}

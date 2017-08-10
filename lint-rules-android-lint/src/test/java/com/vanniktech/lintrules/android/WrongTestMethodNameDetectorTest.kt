package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS
import org.fest.assertions.api.Assertions.assertThat
import org.intellij.lang.annotations.Language

class WrongTestMethodNameDetectorTest : LintDetectorTest() {
  private val stubTestJUnit: LintDetectorTest.TestFile = java(""
      + "package org.junit;\n"
      + "public @interface Test {\n"
      + "}")

  private val stubTestCustom: LintDetectorTest.TestFile = java(""
      + "package my.custom;\n"
      + "public @interface Test {\n"
      + "}")

  private val stubSomething: LintDetectorTest.TestFile = java(""
      + "package my.custom;\n"
      + "public @interface Something {\n"
      + "}")

  fun testMethodNotStartingWithTest() {
    @Language("java") val source = """
      package foo;

      public class MyTest {
        public void myTest() {

        }
      }""".trimMargin()

    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS)
  }

  fun testNormalMethodStartingWithTest() {
    @Language("java") val source = """
      package foo;

      public class MyTest {
        public void testSomething() {

        }
      }""".trimMargin()

    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS)
  }

  fun testJUnitMethodStartingWithTest() {
    @Language("java") val source = """
      package foo;

      import org.junit.Test;

      public class MyTest {
        @Test public void testSomething() {

        }
      }""".trimMargin()

    assertThat(lintProject(stubTestJUnit, java(source))).startsWith("""src/foo/MyTest.java:6: Warning: Test method starts with test. [WrongTestMethodName]
        |        @Test public void testSomething() {
        |                          ~~~~~~~~~~~~~
        |0 errors, 1 warnings""".trimMargin())
  }

  fun testJCustomMethodStartingWithTest() {
    @Language("java") val source = """
      package foo;

      import my.custom.Test;

      public class MyTest {
        @Test public void testSomething() {

        }
      }""".trimMargin()

    assertThat(lintProject(stubTestCustom, java(source))).startsWith("""src/foo/MyTest.java:6: Warning: Test method starts with test. [WrongTestMethodName]
        |        @Test public void testSomething() {
        |                          ~~~~~~~~~~~~~
        |0 errors, 1 warnings""".trimMargin())
  }

  fun testSomethingMethodStartingWithTest() {
    @Language("java") val source = """
      package foo;

      import my.custom.Something;

      public class MyTest {
        @Something public void testSomething() {

        }
      }""".trimMargin()

    assertThat(lintProject(stubSomething, java(source))).isEqualTo(NO_WARNINGS)
  }

  fun testJUnitMethodStartingNotWithTest() {
    @Language("java") val source = """
      package foo;

      import org.junit.Test;

      public class MyTest {
        @Test public void something() {

        }
      }""".trimMargin()

    assertThat(lintProject(stubTestJUnit, java(source))).isEqualTo(NO_WARNINGS)
  }

  override fun getDetector() = WrongTestMethodNameDetector()

  override fun getIssues() = listOf(ISSUE_WRONG_TEST_METHOD_NAME)

  override fun allowCompilationErrors() = false
}

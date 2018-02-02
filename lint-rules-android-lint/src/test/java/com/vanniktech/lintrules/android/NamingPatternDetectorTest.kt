package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class NamingPatternDetectorTest {
  @Test fun incorrectVariableName() {
    lint()
        .files(java("""
          |package foo;
          |
          |class Foo {
          |  private void fun() {
          |    String iOSVersion;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_NAMING_PATTERN)
        .run()
        .expect("""
          |src/foo/Foo.java:5: Warning: Not named in defined camel case. [NamingPattern]
          |    String iOSVersion;
          |           ~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun incorrectMethodName() {
    lint()
        .files(java("""
          |package foo;
          |
          |class Foo {
          |  private void makeHTTPRequest() {
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_NAMING_PATTERN)
        .run()
        .expect("""
          |src/foo/Foo.java:4: Warning: Not named in defined camel case. [NamingPattern]
          |  private void makeHTTPRequest() {
          |               ~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun ignoreEnumConstants() {
    lint()
        .files(java("""
          |package foo;
          |
          |public enum Enum {
          |  FOO
          |}""".trimMargin()))
        .issues(ISSUE_NAMING_PATTERN)
        .run()
        .expectClean()
  }

  @Test fun ignoreInterfaceConstants() {
    lint()
        .files(java("""
          |package foo;
          |
          |interface Something {
          |  String FOO = "bar";
          |}""".trimMargin()))
        .issues(ISSUE_NAMING_PATTERN)
        .run()
        .expectClean()
  }

  @Test fun correctClassName() {
    lint()
        .files(java("""
          |package foo;
          |
          |class XmlHttpRequest {
          |}""".trimMargin()))
        .issues(ISSUE_NAMING_PATTERN)
        .run()
        .expectClean()
  }

  @Test fun incorrectClassName() {
    lint()
        .files(java("""
          |package foo;
          |
          |class XMLHTTPRequest {
          |}""".trimMargin()))
        .issues(ISSUE_NAMING_PATTERN)
        .run()
        .expect("""
          |src/foo/XMLHTTPRequest.java:3: Warning: Not named in defined camel case. [NamingPattern]
          |class XMLHTTPRequest {
          |      ~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }
}

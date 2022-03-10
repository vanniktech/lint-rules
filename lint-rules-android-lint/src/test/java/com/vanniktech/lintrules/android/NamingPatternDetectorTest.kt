@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class NamingPatternDetectorTest {
  @Test fun incorrectVariableName() {
    lint()
      .files(
        java(
          """
            package foo;

            class Foo {
              private void fun() {
                String iOSVersion;
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expect(
        """
            |src/foo/Foo.java:5: Warning: iOSVersion is not named in defined camel case [NamingPattern]
            |    String iOSVersion;
            |           ~~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun incorrectMethodName() {
    lint()
      .files(
        java(
          """
            package foo;

            class Foo {
              private void makeHTTPRequest() {
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expect(
        """
            |src/foo/Foo.java:4: Warning: makeHTTPRequest is not named in defined camel case [NamingPattern]
            |  private void makeHTTPRequest() {
            |               ~~~~~~~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun ignoreEnumConstants() {
    lint()
      .files(
        java(
          """
            package foo;

            public enum Enum {
              FOO
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun ignoreInterfaceConstants() {
    lint()
      .files(
        java(
          """
            package foo;

            interface Something {
              String FOO = "bar";
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun correctClassName() {
    lint()
      .files(
        java(
          """
            package foo;

            class XmlHttpRequest {
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun allowiOSInTheMiddle() {
    lint()
      .files(
        java(
          """
            package foo;

            class NowAvailableOniOSView {
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun allowI18NInTheMiddle() {
    lint()
      .files(
        java(
          """
            package foo;

            class SomethingI18NActivity {
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun incorrectClassName() {
    lint()
      .files(
        java(
          """
            package foo;

            class XMLHTTPRequest {
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expect(
        """
            |src/foo/XMLHTTPRequest.java:3: Warning: XMLHTTPRequest is not named in defined camel case [NamingPattern]
            |class XMLHTTPRequest {
            |      ~~~~~~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun kotlinValGetMethodIgnored() {
    lint()
      .files(
        kt(
          """
            package foo

            class Foo {
              val aTimes = 0
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun kotlinExtensionOnCompanionIgnored() {
    lint()
      .files(
        kt(
          """
            package foo

            class Foo {
                companion object
            }

            val Foo.Companion.INSTANCE = 5
            val Foo.Companion.INSTANCE get() = 5
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun kotlinTopLevelValIgnored() {
    lint()
      .files(
        kt(
          """
            package foo

            val MY_CONST = 0
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }

  @Test fun kotlinValInvalidName() {
    lint()
      .files(
        kt(
          """
            package foo

            class Foo {
              val ATimes = 0
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expect(
        """
            |src/foo/Foo.kt:4: Warning: ATimes is not named in defined camel case [NamingPattern]
            |  val ATimes = 0
            |      ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun kotlinTopLevelFunction() {
    lint()
      .files(
        kt(
          """
            package foo

            fun teFO() = Unit
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expect(
        """
            |src/foo/test.kt:3: Warning: teFO is not named in defined camel case [NamingPattern]
            |fun teFO() = Unit
            |    ~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun kotlinFunction() {
    lint()
      .files(
        kt(
          """
            package foo

            class Test {
              fun teFO() = Unit
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expect(
        """
            |src/foo/Test.kt:4: Warning: teFO is not named in defined camel case [NamingPattern]
            |  fun teFO() = Unit
            |      ~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun kotlinClass() {
    lint()
      .files(
        kt(
          """
            package foo

            class BADName
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .allowDuplicates()
      .run()
      .expect(
        """
            |src/foo/BADName.kt:3: Warning: BADName is not named in defined camel case [NamingPattern]
            |class BADName
            |      ~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun ignoreKotlinCreator() {
    lint()
      .files(
        kt(
          """
            package foo

            class Test {
              companion object {
                val CREATOR = Runnable { }
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_NAMING_PATTERN)
      .run()
      .expectClean()
  }
}

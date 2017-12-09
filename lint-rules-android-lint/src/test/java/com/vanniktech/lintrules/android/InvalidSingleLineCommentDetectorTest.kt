package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.android.InvalidSingleLineCommentDetector.ISSUE_INVALID_SINGLE_LINE_COMMENT
import org.junit.Test

class InvalidSingleLineCommentDetectorTest {
  @Test fun invalidSingleLineCommentNoSpace() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    //Something.
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expect("""
          |src/foo/Example.java:5: Warning: Comment does not contain a space at the beginning. [InvalidSingleLineComment]
          |    //Something.
          |      ^
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun invalidSingleLineCommentNotStartingCapitalLetter() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // something.
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expect("""
          |src/foo/Example.java:5: Warning: Comments first word should be capitalized. [InvalidSingleLineComment]
          |    // something.
          |       ^
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun invalidSingleLineCommentNoPeriodAtEnd() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // Something
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expect("""
          |src/foo/Example.java:5: Warning: Comment does not end with a period. [InvalidSingleLineComment]
          |    // Something
          |    ~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun invalidSingleLineCommentEndingWithPeriod() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // Something. Do not modify!
          |    // Something. Do not modify.
          |    // Something. Do not modify?
          |    // Something. (Do not modify)
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentNoSpaceBefore() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    int foo = 5 + 5;// Something.
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expect("""
          |src/foo/Example.java:5: Warning: Comment does not start with a single space.. [InvalidSingleLineComment]
          |    int foo = 5 + 5;// Something.
          |                   ~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun invalidSingleLineCommentIgnoresLinks() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    String link1 = "https://android.com/";
          |    String link2 = "http://android.com/";
          |    String link3 = "market://details?id=5";
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentIgnoresNoPmd() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // NOPMD
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentAllowsStartingDigit() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // 2 plus 4 equals 6.
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentIgnoresNoInspection() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    //noinspection
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentLinkInComment() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // http://stackoverflow.com/a/38480079
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentIgnoresJustComment() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    //
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expectClean()
  }

  @Test fun invalidSingleLineCommentIgnoresJustCommentWithTrailingWhitespace() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    //
          |  }
          |}""".trimMargin().replace("//", "// ")))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expect("""
          |src/foo/Example.java:5: Warning: Comment contains trailing whitespace. [InvalidSingleLineComment]
          |    //
          |    ~~~
          |0 errors, 1 warnings""".trimMargin().replace("//", "// "))
  }

  @Test fun invalidSingleLineCommentTrailingWhitespace() {
    lint()
      .files(java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    // Something.
          |  }
          |}""".trimMargin().replace("// Something.", "// Something. ")))
      .issues(ISSUE_INVALID_SINGLE_LINE_COMMENT)
      .run()
      .expect("""
          |src/foo/Example.java:5: Warning: Comment contains trailing whitespace. [InvalidSingleLineComment]
          |    // Something.
          |    ~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin().replace("// Something.", "// Something. "))
  }
}

package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class InvalidSingleLineCommentDetectorTest extends LintDetectorTest {
  public void testInvalidSingleLineCommentNoSpace() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    //Something.\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Comment does not contain a space at the beginning. [InvalidSingleLineComment]\n"
        + "    //Something.\n"
        + "      ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testInvalidSingleLineCommentNotStartingCapitalLetter() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // something.\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Comments first word should be capitalized. [InvalidSingleLineComment]\n"
        + "    // something.\n"
        + "       ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testInvalidSingleLineCommentNoPeriodAtEnd() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // Something\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Comment does not end with a period. [InvalidSingleLineComment]\n"
        + "    // Something\n"
        + "    ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testInvalidSingleLineCommentEndingWithPeriod() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // Something. Do not modify!\n"
        + "    // Something. Do not modify.\n"
        + "    // Something. Do not modify?\n"
        + "    // Something. (Do not modify)\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testInvalidSingleLineCommentNoSpaceBefore() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    int foo = 5 + 5;// Something.\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Comment does not start with a single space.. [InvalidSingleLineComment]\n"
        + "    int foo = 5 + 5;// Something.\n"
        + "                   ~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testInvalidSingleLineCommentIgnoresLinks() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    String link1 = \"https://android.com/\";\n"
        + "    String link2 = \"http://android.com/\";\n"
        + "    String link3 = \"market://details?id=5\";\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testInvalidSingleLineCommentIgnoresNoPmd() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // NOPMD\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testInvalidSingleLineCommentIgnoresNoInspection() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    //noinspection\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testInvalidSingleLineCommentLinkInComment() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // http://stackoverflow.com/a/38480079\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testInvalidSingleLineCommentIgnoresJustComment() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    //\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testInvalidSingleLineCommentIgnoresJustCommentWithTrailingWhitespace() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // \n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Comment contains trailing whitespace. [InvalidSingleLineComment]\n"
        + "    // \n"
        + "    ~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testInvalidSingleLineCommentTrailingWhitespace() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    // Something. \n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Comment contains trailing whitespace. [InvalidSingleLineComment]\n"
        + "    // Something. \n"
        + "    ~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new InvalidSingleLineCommentDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(InvalidSingleLineCommentDetector.ISSUE_INVALID_SINGLE_LINE_COMMENT);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

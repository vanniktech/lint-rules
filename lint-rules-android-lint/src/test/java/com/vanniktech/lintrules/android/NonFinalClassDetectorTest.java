package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.NonFinalClassDetector.ISSUE_NON_FINAL_CLASS;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class NonFinalClassDetectorTest extends LintDetectorTest {
  public void testFinalClass() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "public final class Example {\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testAbstractClass() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "public abstract class Example {\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testNonFinalClassContainingAuthorHeader() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "/**"
        + " * @author something"
        + " */"
        + "public class Example {\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:2: Warning: Class is not marked as final. [NonFinalClass]\n"
        + "/** * @author something */public class Example {\n"
        + "                          ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testNonFinalClass() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "public class Example {\n"
        + "}";
    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:2: Warning: Class is not marked as final. [NonFinalClass]\n"
        + "public class Example {\n"
        + "^\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }

  @Override protected Detector getDetector() {
    return new NonFinalClassDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_NON_FINAL_CLASS);
  }
}

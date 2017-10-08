package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

public class AndroidDetectorTest extends LintDetectorTest {
  static final String NO_WARNINGS = "No warnings.";

  private final TestFile resourcesStub = java(""
      + "package android.content.res;\n"
      + "public class Resources {\n"
      + "  public void getDrawable(int id) {}\n"
      + "  public void getColor(int id) {}\n"
      + "  public void getColorStateList(int id) {}\n"
      + "}");

  public void testCallingGetDrawable() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    resources.getDrawable(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using deprecated getDrawable() [ResourcesGetDrawable]\n"
        + "    resources.getDrawable(0);\n"
        + "              ~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetDrawableSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    //noinspection AndroidLintResourcesGetDrawable\n"
        + "    resources.getDrawable(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingGetColor() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    resources.getColor(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using deprecated getColor() [ResourcesGetColor]\n"
        + "    resources.getColor(0);\n"
        + "              ~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetColorSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    //noinspection AndroidLintResourcesGetColor\n"
        + "    resources.getColor(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingGetColorStateList() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    resources.getColorStateList(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using deprecated getColorStateList() [ResourcesGetColorStateList]\n"
        + "    resources.getColorStateList(0);\n"
        + "              ~~~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetColorStateListSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    //noinspection AndroidLintResourcesGetColorStateList\n"
        + "    resources.getColorStateList(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new AndroidDetector();
  }

  @Override protected List<Issue> getIssues() {
    return asList(AndroidDetector.getIssues());
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

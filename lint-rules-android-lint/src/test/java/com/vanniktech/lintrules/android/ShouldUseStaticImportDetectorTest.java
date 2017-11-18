package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.ShouldUseStaticImportDetector.ISSUE_SHOULD_USE_STATIC_IMPORT;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class ShouldUseStaticImportDetectorTest extends LintDetectorTest {
  public void testTimeUnitSeconds() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.concurrent.TimeUnit;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    TimeUnit.SECONDS.toDays(1);\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Should statically import SECONDS [ShouldUseStaticImport]\n"
        + "    TimeUnit.SECONDS.toDays(1);\n"
        + "             ~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testTimeUnitMinutes() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.concurrent.TimeUnit;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    TimeUnit.MINUTES.toDays(1);\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Should statically import MINUTES [ShouldUseStaticImport]\n"
        + "    TimeUnit.MINUTES.toDays(1);\n"
        + "             ~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testLocaleCanada() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.Locale;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Locale.CANADA.getCountry();\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Should statically import CANADA [ShouldUseStaticImport]\n"
        + "    Locale.CANADA.getCountry();\n"
        + "           ~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testSameNameButNoMatch() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.Locale;\n"
        + "public class Example {\n"
        + "  enum Something { DEBUG, RELEASE } \n"
        + "  public void foo() {\n"
        + "    Something ignore = Something.RELEASE;\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testSetLocaleCanada() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.Locale;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Locale.setDefault(Locale.CANADA);\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:5: Warning: Should statically import CANADA [ShouldUseStaticImport]\n"
        + "    Locale.setDefault(Locale.CANADA);\n"
        + "                             ~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testLocaleCanadaIgnored() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.Locale;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    //noinspection AndroidLintShouldUseStaticImport\n"
        + "    Locale.CANADA.getCountry();\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testLocaleCanadaStaticallyImported() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import static java.util.Locale.CANADA;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    CANADA.getCountry();\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReference() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import static java.util.Arrays.asList;"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    asList(1, 2).sort(Integer::compare);\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testArraysAsList() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.Arrays;"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Arrays.asList(1, 2);\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:4: Warning: Should statically import asList [ShouldUseStaticImport]\n"
        + "    Arrays.asList(1, 2);\n"
        + "           ~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCollectionsSingletonList() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import java.util.Collections;"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Collections.singletonList(1);\n"
        + "  }\n"
        + "}";

    assertThat(lintProject(java(source))).isEqualTo("src/foo/Example.java:4: Warning: Should statically import singletonList [ShouldUseStaticImport]\n"
        + "    Collections.singletonList(1);\n"
        + "                ~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }

  @Override protected Detector getDetector() {
    return new ShouldUseStaticImportDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_SHOULD_USE_STATIC_IMPORT);
  }
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.android.ShouldUseStaticImportDetector.ISSUE_SHOULD_USE_STATIC_IMPORT
import org.junit.Test

class ShouldUseStaticImportDetectorTest {
  @Test fun timeUnitSeconds() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.concurrent.TimeUnit;
          |class Example {
          |  public void foo() {
          |    TimeUnit.SECONDS.toDays(1);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expect("""
          |src/foo/Example.java:5: Warning: Should statically import SECONDS [ShouldUseStaticImport]
          |    TimeUnit.SECONDS.toDays(1);
          |             ~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun timeUnitMinutes() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.concurrent.TimeUnit;
          |class Example {
          |  public void foo() {
          |    TimeUnit.MINUTES.toDays(1);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expect("""
          |src/foo/Example.java:5: Warning: Should statically import MINUTES [ShouldUseStaticImport]
          |    TimeUnit.MINUTES.toDays(1);
          |             ~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun localeCanada() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.Locale;
          |class Example {
          |  public void foo() {
          |    Locale.CANADA.getCountry();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expect("""
          |src/foo/Example.java:5: Warning: Should statically import CANADA [ShouldUseStaticImport]
          |    Locale.CANADA.getCountry();
          |           ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun sameNameButNoMatch() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.Locale;
          |class Example {
          |  enum Something { DEBUG, RELEASE }
          |  public void foo() {
          |    Something ignore = Something.RELEASE;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expectClean()
  }

  @Test fun setLocaleCanada() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.Locale;
          |class Example {
          |  public void foo() {
          |    Locale.setDefault(Locale.CANADA);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expect("""
          |src/foo/Example.java:5: Warning: Should statically import CANADA [ShouldUseStaticImport]
          |    Locale.setDefault(Locale.CANADA);
          |                             ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun localeCanadaStaticallyImported() {
    lint()
        .files(java("""
          |package foo;
          |import static java.util.Locale.CANADA;
          |class Example {
          |  public void foo() {
          |    CANADA.getCountry();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expectClean()
  }

  @Test fun methodReference() {
    lint()
        .files(java("""
          |package foo;
          |import static java.util.Arrays.asList;
          |class Example {
          |  public void foo() {
          |    asList(1, 2).sort(Integer::compare);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expectClean()
  }

  @Test fun arraysAsList() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.Arrays;
          |class Example {
          |  public void foo() {
          |    Arrays.asList(1, 2);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expect("""
          |src/foo/Example.java:5: Warning: Should statically import asList [ShouldUseStaticImport]
          |    Arrays.asList(1, 2);
          |           ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun collectionsSingletonList() {
    lint()
        .files(java("""
          |package foo;
          |import java.util.Collections;
          |class Example {
          |  public void foo() {
          |    Collections.singletonList(1);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SHOULD_USE_STATIC_IMPORT)
        .run()
        .expect("""
          |src/foo/Example.java:5: Warning: Should statically import singletonList [ShouldUseStaticImport]
          |    Collections.singletonList(1);
          |                ~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }
}

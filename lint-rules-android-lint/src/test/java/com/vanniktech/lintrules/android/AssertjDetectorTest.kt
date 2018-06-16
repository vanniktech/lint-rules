package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Ignore
import org.junit.Test

class AssertjDetectorTest {
  @Test fun javaAssertionsImport() {
    lint()
        .allowCompilationErrors()
        .files(java("""
            package foo;

            import org.assertj.core.api.Assertions;""").indented())
        .issues(ISSUE_ASSERTJ_IMPORT)
        .run()
        .expect("""
            |src/foo/package-info.java:3: Warning: Should use Java6Assertions instead [AssertjImport]
            |import org.assertj.core.api.Assertions;
            |       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for src/foo/package-info.java line 3: Replace with org.assertj.core.api.Java6Assertions:
            |@@ -3 +3
            |- import org.assertj.core.api.Assertions;
            |@@ -4 +3
            |+ import org.assertj.core.api.Java6Assertions;
            |""".trimMargin())
  }

  @Test fun javaStaticAssertionsThatImport() {
    lint()
        .allowCompilationErrors()
        .files(java("""
            package foo;

            import static org.assertj.core.api.Assertions.assertThat;""").indented())
        .issues(ISSUE_ASSERTJ_IMPORT)
        .run()
        .expect("""
            |src/foo/package-info.java:3: Warning: Should use Java6Assertions instead [AssertjImport]
            |import static org.assertj.core.api.Assertions.assertThat;
            |              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for src/foo/package-info.java line 3: Replace with org.assertj.core.api.Java6Assertions.assertThat:
            |@@ -3 +3
            |- import static org.assertj.core.api.Assertions.assertThat;
            |@@ -4 +3
            |+ import static org.assertj.core.api.Java6Assertions.assertThat;
            |""".trimMargin())
  }

  @Test @Ignore("https://issuetracker.google.com/issues/80491636") fun kotlinAssertionsImport() {
    lint()
        .allowCompilationErrors()
        .files(kt("import org.assertj.core.api.Assertions"))
        .issues(ISSUE_ASSERTJ_IMPORT)
        .run()
        .expect("""
            |src/test.kt:1: Warning: Should use Java6Assertions instead [AssertjImport]
            |import org.assertj.core.api.Assertions
            |       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |""".trimMargin())
  }

  @Test @Ignore("https://issuetracker.google.com/issues/80491636") fun kotlinStaticAssertionsThatImport() {
    lint()
        .allowCompilationErrors()
        .files(kt("import org.assertj.core.api.Assertions.assertThat"))
        .issues(ISSUE_ASSERTJ_IMPORT)
        .run()
        .expect("""
            |src/test.kt:1: Warning: Should use Java6Assertions instead [AssertjImport]
            |import org.assertj.core.api.Assertions.assertThat
            |       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |""".trimMargin())
  }
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class InvalidImportDetectorTest {
  private val r = java("""
      |package foo;
      |public final class R {
      |  public static final class drawable {
      |  }
      |}""".trimMargin())

  @Test fun normalRImport() {
    lint()
      .files(r, java("""
          |package foo;
          |
          |import foo.R;
          |
          |class Example {
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_IMPORT)
      .run()
      .expectClean()
  }

  @Test fun rDrawableImport() {
    lint()
      .files(r, java("""
          |package foo;
          |
          |import foo.R.drawable;
          |
          |class Example {
          |}""".trimMargin()))
      .issues(ISSUE_INVALID_IMPORT)
      .run()
      .expect("""
          |src/foo/Example.java:3: Warning: Importing a class from R.java [InvalidImport]
          |import foo.R.drawable;
          |       ~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }
}

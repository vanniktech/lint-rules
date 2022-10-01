@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.gradle
import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.manifest
import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class TodoDetectorTest {
  @Test fun javaFile() {
    lint()
      .files(
        java(
          """
            package foo;

            class Example {
              // TODO something
            }
          """,
        ).indented(),
      )
      .issues(ISSUE_TODO)
      .run()
      .expect(
        """
            |src/foo/Example.java:4: Error: Contains todo [Todo]
            |  // TODO something
            |     ~~~~
            |1 errors, 0 warnings
        """.trimMargin(),
      )
  }

  @Test fun layoutFile() {
    lint()
      .files(
        xml(
          "res/layout/layout.xml",
          """
            <TextView xmlns:android="http://schemas.android.com/apk/res/android"
                android:text="Blub!"/>
            <!-- TODO(NIK) Fix blub. -->
          """,
        ).indented(),
      )
      .issues(ISSUE_TODO)
      .run()
      .expect(
        """
            |res/layout/layout.xml:3: Error: Contains todo [Todo]
            |<!-- TODO(NIK) Fix blub. -->
            |     ~~~~
            |1 errors, 0 warnings
        """.trimMargin(),
      )
  }

  @Test fun gradleFile() {
    lint()
      .files(
        gradle(
          """
            buildscript {
              repositories {
                mavenCentral() // TODO: we should remove it.
              }
            }
          """,
        ).indented(),
      )
      .issues(ISSUE_TODO)
      .run()
      .expect(
        """
            |build.gradle:3: Error: Contains todo [Todo]
            |    mavenCentral() // TODO: we should remove it.
            |                      ~~~~
            |1 errors, 0 warnings
        """.trimMargin(),
      )
  }

  @Test fun ignoresCommentContainingTodo() {
    lint()
      .files(
        gradle(
          """
            buildscript {
              repositories {
                mavenCentral() // Etiquetar automáticamente todos los lugares
                mavenCentral() // Todos os cartões aprendidos. Volte a verificar amanhã
              }
            }
          """,
        ).indented(),
      )
      .issues(ISSUE_TODO)
      .run()
      .expectClean()
  }

  @Test fun manifestFile() {
    lint()
      .files(
        manifest().withSource(
          """
            <!-- Todo: Something. -->
            <manifest package="com.vanniktech.lintrulesandroid"/>
          """,
        ).indented(),
      )
      .issues(ISSUE_TODO)
      .run()
      .expect(
        """
            |AndroidManifest.xml:1: Error: Contains todo [Todo]
            |<!-- Todo: Something. -->
            |     ~~~~
            |1 errors, 0 warnings
        """.trimMargin(),
      )
  }
}

@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.gradle
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class JcenterDetectorTest {
  @Test fun jcenter() {
    lint()
      .files(
        gradle(
          """
            buildscript {
              repositories {
                jcenter()
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_JCENTER)
      .run()
      .expect(
        """
            |build.gradle:3: Warning: Don't use jcenter() [JCenter]
            |    jcenter()
            |    ~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
      .expectFixDiffs(
        """
            |Fix for build.gradle line 3: Replace with mavenCentral():
            |@@ -3 +3
            |-     jcenter()
            |+     mavenCentral()
            |
        """.trimMargin()
      )
  }

  @Test fun noJcenter() {
    lint()
      .files(
        gradle(
          """
            buildscript {
              repositories {
                mavenCentral()
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_JCENTER)
      .run()
      .expectClean()
  }
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class AlertDialogUsageDetectorTest {
  @Test fun constructorParameterInKotlin() {
    lint()
      .allowCompilationErrors()
      .files(
        kt(
          """
            import android.app.AlertDialog

            class Test(dialog: AlertDialog)"""
        ).indented()
      )
      .issues(ISSUE_ALERT_DIALOG_USAGE)
      .run()
      .expect(
        """
            |src/Test.kt:3: Warning: Should not be using android.app.AlertDialog [AlertDialogUsage]
            |class Test(dialog: AlertDialog)
            |           ~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin()
      )
  }

  @Test fun constructorParameterInJava() {
    lint()
      .allowCompilationErrors()
      .files(
        java(
          """
            import android.app.AlertDialog;

            class Test {
              public Test(AlertDialog dialog) { }
            }"""
        ).indented()
      )
      .issues(ISSUE_ALERT_DIALOG_USAGE)
      .run()
      .expect(
        """
            |src/Test.java:4: Warning: Should not be using android.app.AlertDialog [AlertDialogUsage]
            |  public Test(AlertDialog dialog) { }
            |              ~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin()
      )
  }

  @Test fun superParameterInKotlin() {
    lint()
      .allowCompilationErrors()
      .files(
        kt(
          """
            import android.app.AlertDialog

            class Test : AlertDialog"""
        ).indented()
      )
      .issues(ISSUE_ALERT_DIALOG_USAGE)
      .run()
      .expect(
        """
            |src/Test.kt:3: Warning: Should not be using android.app.AlertDialog [AlertDialogUsage]
            |class Test : AlertDialog
            |             ~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin()
      )
  }

  @Test fun superParameterInJava() {
    lint()
      .allowCompilationErrors()
      .files(
        java(
          """
            import android.app.AlertDialog;

            class Test extends AlertDialog {
            }"""
        ).indented()
      )
      .issues(ISSUE_ALERT_DIALOG_USAGE)
      .run()
      .expect(
        """
            |src/Test.java:3: Warning: Should not be using android.app.AlertDialog [AlertDialogUsage]
            |class Test extends AlertDialog {
            |                   ~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin()
      )
  }
}

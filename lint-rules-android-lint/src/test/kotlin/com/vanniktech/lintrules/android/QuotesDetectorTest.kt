@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class QuotesDetectorTest {
  @Test fun english() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string_1">Hallo "Niklas"</string>
            <string name="my_string_2">Hallo “Niklas”</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_QUOTES)
      .run()
      .expect(
        """
        res/values/strings.xml:2: Warning: Invalid quotes [Quotes]
          <string name="my_string_1">Hallo "Niklas"</string>
                                           ~~~~~~~~
        0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for res/values/strings.xml line 2: Fix quotes:
        @@ -2 +2
        -   <string name="my_string_1">Hallo "Niklas"</string>
        +   <string name="my_string_1">Hallo “Niklas”</string>
        """.trimIndent(),
      )
  }

  @Test fun russian() {
    lint()
      .files(
        xml(
          "res/values-ru/strings.xml",
          """
          <resources>
            <string name="my_string_1">Ви дійсно хочете видалити всі карти з колоди “%1＄s”</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_QUOTES)
      .run()
      .expectClean()
  }

  @Test fun hebrew() {
    lint()
      .files(
        xml(
          "res/values-iw/strings.xml",
          """
          <resources>
            <string name="email_address">כתובת דוא\"ל</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_QUOTES)
      .run()
      .expectClean()
  }

  @Test fun germanCzech() {
    listOf("de", "cs").forEach { locale ->
      lint()
        .files(
          xml(
            "res/values-$locale/strings.xml",
            """
          <resources>
            <string name="my_string_1">Hallo "Niklas"</string>
            <string name="my_string_2">Hallo „Niklas“</string>
            <string name="my_string_3">Bist du dir sicher, dass du die „%1＄＄s“ Frage löschen möchtest?</string>
          </resources>
          """,
          ).indented(),
        )
        .issues(ISSUE_QUOTES)
        .run()
        .expect(
          """
        res/values-$locale/strings.xml:2: Warning: Invalid quotes [Quotes]
          <string name="my_string_1">Hallo "Niklas"</string>
                                           ~~~~~~~~
        0 errors, 1 warnings
          """.trimIndent(),
        )
        .expectFixDiffs(
          """
        Autofix for res/values-$locale/strings.xml line 2: Fix quotes:
        @@ -2 +2
        -   <string name="my_string_1">Hallo "Niklas"</string>
        +   <string name="my_string_1">Hallo „Niklas“</string>
          """.trimIndent(),
        )
    }
  }
}

@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class StringNotCapitalizedDetectorTest {
  @Test fun validString() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string">My string</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectClean()
  }

  @Test fun ignoreInConfigFile() {
    lint()
      .files(
        xml(
          "res/values/config.xml",
          """
          <resources>
            <string name="api_key">abbc</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectClean()
  }

  @Test fun ignoreAbbreviation() {
    lint()
      .files(
        xml(
          "res/values-es/strings.xml",
          """
          <resources>
            <string name="time_abbreviation_minutes">m</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectClean()
  }

  @Test fun ignoreWhenNameContainsPlaceholder() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_placeholder">your</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectClean()
  }

  @Test fun ignoreTranslateableFalse() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="fun" translatable="false">log10</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectClean()
  }

  @Test fun ignoreLinks() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="link1">https://www.google.com</string>
            <string name="link2">http://www.google.com</string>
            <string name="link3">www.google.com</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectClean()
  }

  @Test fun differentCharsets() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="key1">Χρόνος</string>
            <string name="key2">Цвет</string>
            <string name="key3">Обычай</string>
            <string name="key4">легальный</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expectFixDiffs(
        """
          |Autofix for res/values/strings.xml line 5: Fix it:
          |@@ -5 +5
          |-   <string name="key4">легальный</string>
          |+   <string name="key4">Легальный</string>
        """.trimMargin(),
      )
  }

  @Test fun lowercase() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string">my string</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_STRING_NOT_CAPITALIZED)
      .run()
      .expect(
        """
        |res/values/strings.xml:2: Warning: String is not capitalized [StringNotCapitalized]
        |  <string name="my_string">my string</string>
        |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        |0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
        |Autofix for res/values/strings.xml line 2: Fix it:
        |@@ -2 +2
        |-   <string name="my_string">my string</string>
        |+   <string name="my_string">My string</string>
        """.trimMargin(),
      )
  }
}

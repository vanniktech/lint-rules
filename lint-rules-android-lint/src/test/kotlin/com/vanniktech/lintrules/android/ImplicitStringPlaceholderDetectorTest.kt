@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class ImplicitStringPlaceholderDetectorTest {
  @Test fun valid() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string">My string</string>
            <string name="my_string2">Hello %1＄s</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_IMPLICIT_STRING_PLACEHOLDER)
      .run()
      .expectClean()
  }

  @Test fun invalid() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string">Hello %s</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_IMPLICIT_STRING_PLACEHOLDER)
      .run()
      .expect(
        """
          res/values/strings.xml:2: Warning: Implicit placeholder [ImplicitStringPlaceholder]
            <string name="my_string">Hello %s</string>
                                           ~~
          0 errors, 1 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
          Autofix for res/values/strings.xml line 2: Fix %s with %1＄s:
          @@ -2 +2
          -   <string name="my_string">Hello %s</string>
          +   <string name="my_string">Hello %1＄s</string>
        """.trimIndent(),
      )
  }

  @Test fun multiple() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_IMPLICIT_STRING_PLACEHOLDER)
      .run()
      .expect(
        """
          res/values/strings.xml:2: Warning: Implicit placeholder [ImplicitStringPlaceholder]
            <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
                                           ~~
          res/values/strings.xml:2: Warning: Implicit placeholder [ImplicitStringPlaceholder]
            <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
                                               ~~
          res/values/strings.xml:2: Warning: Implicit placeholder [ImplicitStringPlaceholder]
            <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
                                                                              ~~
          0 errors, 3 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
          Autofix for res/values/strings.xml line 2: Fix %s with %1＄s:
          @@ -2 +2
          -   <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
          +   <string name="my_string">Hello %1＄s, %d days ago, what did you like %s?</string>
          Autofix for res/values/strings.xml line 2: Fix %d with %2＄d:
          @@ -2 +2
          -   <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
          +   <string name="my_string">Hello %s, %2＄d days ago, what did you like %s?</string>
          Autofix for res/values/strings.xml line 2: Fix %s with %3＄s:
          @@ -2 +2
          -   <string name="my_string">Hello %s, %d days ago, what did you like %s?</string>
          +   <string name="my_string">Hello %s, %d days ago, what did you like %3＄s?</string>
        """.trimIndent(),
      )
  }
}

@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class FormalGermanDetectorTest {
  @Test fun informal() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string_1">Wie lautet dein Name?</string>
            <string name="my_string_2">Wie heisst du?</string>
            <string name="my_string_3">Frag nach seinen Namen.</string>
            <string name="my_string_4">Wie du willst</string>
            <string name="my_string_5">Deine Historie</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_FORMAL_GERMAN)
      .run()
      .expectClean()
  }

  @Test fun otherWords() {
    lint()
      .files(
        xml(
          "res/values/strings.xml",
          """
          <resources>
            <string name="my_string_1">Siege</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_FORMAL_GERMAN)
      .run()
      .expectClean()
  }

  @Test fun formal() {
    lint()
      .files(
        xml(
          "res/values/config.xml",
          """
          <resources>
            <string name="my_string_1">Wie lautet Ihr Name?</string>
            <string name="my_string_2">Wie heissen Sie?</string>
            <string name="my_string_3">Frag nach Ihrem Namen.</string>
            <string name="my_string_4">Wie Sie möchten</string>
            <string name="my_string_5">Ihre Historie</string>
          </resources>
          """,
        ).indented(),
      )
      .issues(ISSUE_FORMAL_GERMAN)
      .run()
      .expect(
        """
        res/values/config.xml:2: Warning: Formal language "Ihr" detected [FormalGerman]
          <string name="my_string_1">Wie lautet Ihr Name?</string>
                                                ^
        res/values/config.xml:3: Warning: Formal language "Sie?" detected [FormalGerman]
          <string name="my_string_2">Wie heissen Sie?</string>
                                                 ^
        res/values/config.xml:4: Warning: Formal language "Ihrem" detected [FormalGerman]
          <string name="my_string_3">Frag nach Ihrem Namen.</string>
                                               ^
        res/values/config.xml:5: Warning: Formal language "Sie" detected [FormalGerman]
          <string name="my_string_4">Wie Sie möchten</string>
                                         ^
        res/values/config.xml:6: Warning: Formal language "Ihre" detected [FormalGerman]
          <string name="my_string_5">Ihre Historie</string>
                                     ^
        0 errors, 5 warnings
        """.trimIndent(),
      )
  }
}

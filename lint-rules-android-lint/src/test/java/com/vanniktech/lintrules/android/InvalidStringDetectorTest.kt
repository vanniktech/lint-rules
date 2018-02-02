package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class InvalidStringDetectorTest {
  @Test fun validString() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<resources>
          |  <string name="my_string">My string</string>
          |</resources>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expectClean()
  }

  @Test fun stringContainingNewLine() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<resources>
          |  <string name="my_string">My string"
          |</string>
          |</resources>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expect("""
          |res/values/strings.xml:2: Warning: Text contains new line. [InvalidString]
          |  <string name="my_string">My string"
          |  ^
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/values/strings.xml line 1: Fix it:
          |@@ -2 +2
          |-   <string name="my_string">My string"
          |- </string>
          |+   <string name="my_string">My string"</string>
          |""".trimMargin())
  }

  @Test fun trailingWhitespaceAtEndString() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<resources>
          |  <string name="my_string">My string   </string>
          |</resources>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expect("""
          |res/values/strings.xml:2: Warning: Text contains trailing whitespace. [InvalidString]
          |  <string name="my_string">My string   </string>
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/values/strings.xml line 1: Fix it:
          |@@ -2 +2
          |-   <string name="my_string">My string   </string>
          |+   <string name="my_string">My string</string>
          |""".trimMargin())
  }

  @Test fun trailingWhitespaceAtBeginningOfString() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<resources>
          |  <string name="my_string">  My string</string>
          |</resources>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expect("""
          |res/values/strings.xml:2: Warning: Text contains trailing whitespace. [InvalidString]
          |  <string name="my_string">  My string</string>
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/values/strings.xml line 1: Fix it:
          |@@ -2 +2
          |-   <string name="my_string">  My string</string>
          |+   <string name="my_string">My string</string>
          |""".trimMargin())
  }

  @Test fun validPluralString() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<plurals name="days">
          |  <item quantity="one">%d Day</item>
          |  <item quantity="other">%d Days</item>
          |</plurals>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expectClean()
  }

  @Test fun trailingWhitespaceAtPluralStrings() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<plurals name="days">
          |  <item quantity="one">  %d Day</item>
          |  <item quantity="other">%d Days   </item>
          |</plurals>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expect("""
          |res/values/strings.xml:2: Warning: Text contains trailing whitespace. [InvalidString]
          |  <item quantity="one">  %d Day</item>
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |res/values/strings.xml:3: Warning: Text contains trailing whitespace. [InvalidString]
          |  <item quantity="other">%d Days   </item>
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 2 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/values/strings.xml line 1: Fix it:
          |@@ -2 +2
          |-   <item quantity="one">  %d Day</item>
          |+   <item quantity="one">%d Day</item>
          |Fix for res/values/strings.xml line 2: Fix it:
          |@@ -3 +3
          |-   <item quantity="other">%d Days   </item>
          |+   <item quantity="other">%d Days</item>
          |""".trimMargin())
  }

  @Test fun validStringArrayString() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<string-array name="foo">
          |  <item>1</item>
          |  <item>2</item>
          |</string-array>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expectClean()
  }

  @Test fun trailingWhitespaceAtStringArrayStrings() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<string-array name="bar">
          |  <item>   1</item>
          |  <item>2   </item>
          |</string-array>""".trimMargin()))
        .issues(ISSUE_INVALID_STRING)
        .run()
        .expect("""
          |res/values/strings.xml:2: Warning: Text contains trailing whitespace. [InvalidString]
          |  <item>   1</item>
          |  ~~~~~~~~~~~~~~~~~
          |res/values/strings.xml:3: Warning: Text contains trailing whitespace. [InvalidString]
          |  <item>2   </item>
          |  ~~~~~~~~~~~~~~~~~
          |0 errors, 2 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/values/strings.xml line 1: Fix it:
          |@@ -2 +2
          |-   <item>   1</item>
          |+   <item>1</item>
          |Fix for res/values/strings.xml line 2: Fix it:
          |@@ -3 +3
          |-   <item>2   </item>
          |+   <item>2</item>
          |""".trimMargin())
  }
}

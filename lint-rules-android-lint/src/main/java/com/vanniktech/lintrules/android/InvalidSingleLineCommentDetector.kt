package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope.GRADLE_FILE
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import org.jetbrains.uast.UClass
import java.net.MalformedURLException
import java.net.URL
import java.util.EnumSet
import java.util.regex.Pattern

private const val COMMENT = "//"
private const val COMMENT_BEGINNING_INDEX = 3
private val allowedEndings = listOf(".", "?", "!", ")")
private val pattern = Pattern.compile("[\\t]*$COMMENT.*")

val ISSUE_INVALID_SINGLE_LINE_COMMENT = Issue.create("InvalidSingleLineComment",
    "Marks single line comments that are not sentences.",
    "Single line comments should always be sentences. They're part of the code and hence they deserve as much detail and respect as code.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(InvalidSingleLineCommentDetector::class.java, EnumSet.of(JAVA_FILE, GRADLE_FILE), EnumSet.of(JAVA_FILE, GRADLE_FILE)))

class InvalidSingleLineCommentDetector : Detector(), Detector.UastScanner, Detector.GradleScanner {
  override fun getApplicableUastTypes() = listOf(UClass::class.java)

  @Suppress("Detekt.ComplexMethod") override fun afterCheckFile(context: Context) {
    val source = context.getContents().toString()
    val matcher = pattern.matcher(source)

    while (matcher.find()) {
      val group = matcher.group().replaceFirst(COMMENT.toRegex(), "")
      val start = matcher.start()

      val beforeStart = if (start > 0) source[start - 1] else null

      if (shouldSkip(group, beforeStart)) {
        continue
      }

      val end = matcher.end()

      if (beforeStart != null && !Character.isWhitespace(beforeStart)) {
        handlePrecedingSpace(context, source, start)
      } else if (group[0] != ' ') {
        handleRecedingSpace(context, source, start)
      } else if (" " == group || group.trim { it <= ' ' }.length != group.length - 1) {
        handleTrailingWhiteSpace(context, source, start, end, group)
      } else if (Character.isLowerCase(group[1])) {
        handleFirstWordCapitalization(context, source, start, end, group)
      } else if (allowedEndings.none { group.endsWith(it) }) {
        handlePeriod(context, source, start, end, group)
      }
    }
  }

  private fun handlePeriod(context: Context, source: String, start: Int, end: Int, group: String) {
    val location = Location.create(context.file, source, start, end)
    val fix = fix()
        .name("Add period")
        .replace()
        .text(group)
        .with("$group.")
        .autoFix(true, false)
        .build()

    context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment does not end with a period.", fix)
  }

  private fun handleFirstWordCapitalization(context: Context, source: String, start: Int, end: Int, group: String) {
    val location = Location.create(context.file, source, start + COMMENT_BEGINNING_INDEX, end + COMMENT_BEGINNING_INDEX + 1)
    val fix = fix()
        .name("Capitalized first word")
        .replace()
        .text(group.substring(1))
        .with(Character.toUpperCase(group[1]) + group.substring(2))
        .autoFix(true, false)
        .build()

    context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comments first word should be capitalized.", fix)
  }

  private fun handleTrailingWhiteSpace(context: Context, source: String, start: Int, end: Int, group: String) {
    val location = Location.create(context.file, source, start, end)
    val fix = fix()
        .name("Remove trailing whitespace")
        .replace()
        .text(group)
        .with(group.trimEnd())
        .autoFix(true, false)
        .build()

    context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment contains trailing whitespace.", fix)
  }

  private fun handleRecedingSpace(context: Context, source: String, start: Int) {
    val location = Location.create(context.file, source, start, start + COMMENT_BEGINNING_INDEX)
    val fix = fix()
        .name("Add space")
        .replace()
        .text(COMMENT)
        .with("// ")
        .autoFix(true, false)
        .build()

    context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment does not contain a space at the beginning.", fix)
  }

  private fun handlePrecedingSpace(context: Context, source: String, start: Int) {
    val location = Location.create(context.file, source, start - 1, start + 2)
    val fix = fix()
        .name("Add space")
        .replace()
        .text(COMMENT)
        .with(" //")
        .autoFix(true, false)
        .build()

    context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment declaration is not preceded by a single space.", fix)
  }

  private fun shouldSkip(group: String, beforeStart: Char?): Boolean {
    val isEmpty = group.isEmpty()
    val isNoPmd = " NOPMD" == group
    val isInspection = group.startsWith("noinspection")
    val isALink = beforeStart != null && beforeStart == ':'
    val isHttpLink = isUrl(group)
    val startsWithQuote = group == "\""
    return isEmpty || isNoPmd || isInspection || isALink || isHttpLink || startsWithQuote
  }

  private fun isUrl(string: String) = listOfNotNull(string, string.split(" ").lastOrNull())
      .any {
        try {
          URL(it)
          true
        } catch (ignore: MalformedURLException) {
          false
        }
      }
}

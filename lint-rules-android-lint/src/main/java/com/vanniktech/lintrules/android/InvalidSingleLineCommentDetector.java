package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.Location;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.text.StringsKt;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.GRADLE_FILE;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class InvalidSingleLineCommentDetector extends Detector implements Detector.UastScanner, Detector.GradleScanner {
  private static final String COMMENT = "//";

  static final Issue ISSUE_INVALID_SINGLE_LINE_COMMENT = Issue.create("InvalidSingleLineComment",
      "Marks single line comments that are not sentences.",
      "Single line comments should always be sentences. They're part of the code and hence they deserve as much detail and respect as code.",
      CORRECTNESS, 8, WARNING,
      new Implementation(InvalidSingleLineCommentDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES, GRADLE_FILE)));

  @Override public List<Class<? extends UElement>> getApplicableUastTypes() {
    return Collections.<Class<? extends UElement>>singletonList(UClass.class);
  }

  @Override @SuppressWarnings("PMD.CyclomaticComplexity") public void afterCheckFile(final Context context) {
    final String source = String.valueOf(context.getContents());

    final Pattern compile = Pattern.compile("[\\t]*" + COMMENT + ".*");
    final Matcher matcher = compile.matcher(source);

    while (matcher.find()) {
      final String group = matcher.group().replaceFirst(COMMENT, "");
      final int start = matcher.start();

      final Character beforeStart = start > 0 ? source.charAt(start - 1) : null;

      final boolean isEmpty = group.isEmpty();
      final boolean isNoPmd = " NOPMD".equals(group);
      final boolean isInspection = group.startsWith("noinspection");
      final boolean isALink = beforeStart != null && beforeStart == ':';
      final boolean isHttpLink = isUrl(group);

      if (isEmpty || isNoPmd || isInspection || isALink || isHttpLink) {
        continue;
      }

      final int end = matcher.end();

      if (beforeStart != null && !Character.isWhitespace(beforeStart)) {
        final Location location = Location.create(context.file, source, start - 1, start + 2);
        final LintFix fix = fix()
            .name("Add space")
            .replace()
            .text(COMMENT)
            .with(" //")
            .build();

        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment declaration is not preceded by a single space.", fix);
      } else if (group.charAt(0) != ' ') {
        final Location location = Location.create(context.file, source, start, start + 3);
        final LintFix fix = fix()
            .name("Add space")
            .replace()
            .text(COMMENT)
            .with("// ")
            .build();

        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment does not contain a space at the beginning.", fix);
      } else if (" ".equals(group) || group.trim().length() != group.length() - 1) {
        final Location location = Location.create(context.file, source, start, end);
        final LintFix fix = fix()
            .name("Remove trailing whitespace")
            .replace()
            .text(group)
            .with(String.valueOf(StringsKt.trimEnd(group)))
            .build();

        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment contains trailing whitespace.", fix);
      } else if (Character.isLowerCase(group.charAt(1))) {
        final Location location = Location.create(context.file, source, start + 3, end + 4);
        final LintFix fix = fix()
            .name("Capitalized first word")
            .replace()
            .text(group.substring(1))
            .with(Character.toUpperCase(group.charAt(1)) + group.substring(2))
            .build();

        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comments first word should be capitalized.", fix);
      } else if (!group.endsWith(".") && !group.endsWith("?") && !group.endsWith("!") && !group.endsWith(")")) {
        final Location location = Location.create(context.file, source, start, end);
        final LintFix fix = fix()
            .name("Add period")
            .replace()
            .text(group)
            .with(group + ".")
            .build();

        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment does not end with a period.", fix);
      }
    }
  }

  private static boolean isUrl(final String string) {
    try {
      new URL(string);
      return true;
    } catch (final MalformedURLException ignore) {
      return false;
    }
  }
}

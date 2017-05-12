package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class InvalidSingleLineCommentDetector extends Detector implements Detector.JavaPsiScanner {
  static final Issue ISSUE_INVALID_SINGLE_LINE_COMMENT = Issue.create("InvalidSingleLineComment", "Single line comment should be a sentence.",
      "Single line comment should be a sentence.", CORRECTNESS, 8, WARNING,
      new Implementation(InvalidSingleLineCommentDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  @Override public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
    return Collections.<Class<? extends PsiElement>>singletonList(PsiClass.class);
  }

  @Override public void afterCheckFile(final Context context) {
    final JavaContext javaContext = (JavaContext) context;

    final String source = String.valueOf(javaContext.getContents());

    final Pattern compile = Pattern.compile("[\\t]*//.*");
    final Matcher matcher = compile.matcher(source);

    while (matcher.find()) {
      final String group = matcher.group().replace("//", "");

      if (group.isEmpty() || " ".equals(group) || " NOPMD".equals(group) || group.startsWith("noinspection")) {
        continue;
      }

      if (group.charAt(0) != ' ') {
        final Location location = Location.create(context.file, source, matcher.start() + 2, matcher.end() + 3);
        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment does not contain a space at the beginning.");
      } else if (!Character.isUpperCase(group.charAt(1))) {
        final Location location = Location.create(context.file, source, matcher.start() + 3, matcher.end() + 4);
        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comments first word should be capitalized.");
      } else if (group.trim().length() != group.length() - 1) {
        final Location location = Location.create(context.file, source, matcher.start(), matcher.end());
        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comments contains trailing whitespace.");
      } else if (!group.endsWith(".")) {
        final Location location = Location.create(context.file, source, matcher.start(), matcher.end());
        context.report(ISSUE_INVALID_SINGLE_LINE_COMMENT, location, "Comment does not end with a period.");
      }
    }
  }
}

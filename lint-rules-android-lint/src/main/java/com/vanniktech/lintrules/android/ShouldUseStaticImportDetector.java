package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceExpression;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public class ShouldUseStaticImportDetector extends Detector implements Detector.JavaPsiScanner {
  static final Issue ISSUE_SHOULD_USE_STATIC_IMPORT =
      Issue.create("ShouldUseStaticImport", "Should be using a static import.",
          "Should be using a static import.", CORRECTNESS, 3,
          WARNING, new Implementation(ShouldUseStaticImportDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  @Override public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
    return Collections.<Class<? extends PsiElement>>singletonList(PsiReferenceExpression.class);
  }

  @Override public JavaElementVisitor createPsiVisitor(final JavaContext context) {
    return new ShouldUseStaticImportDetectorVisitor(context);
  }

  static class ShouldUseStaticImportDetectorVisitor extends JavaElementVisitor {
    static final Set<String> IMPORTS = new HashSet<>();

    static {
      IMPORTS.add("java.util.concurrent.TimeUnit.NANOSECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.MICROSECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.MILLISECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.SECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.MINUTES");
      IMPORTS.add("java.util.concurrent.TimeUnit.HOURS");
      IMPORTS.add("java.util.concurrent.TimeUnit.DAYS");
      IMPORTS.add("java.util.Locale.ENGLISH");
      IMPORTS.add("java.util.Locale.FRENCH");
      IMPORTS.add("java.util.Locale.GERMAN");
      IMPORTS.add("java.util.Locale.ITALIAN");
      IMPORTS.add("java.util.Locale.JAPANESE");
      IMPORTS.add("java.util.Locale.KOREAN");
      IMPORTS.add("java.util.Locale.CHINESE");
      IMPORTS.add("java.util.Locale.SIMPLIFIED_CHINESE");
      IMPORTS.add("java.util.Locale.TRADITIONAL_CHINESE");
      IMPORTS.add("java.util.Locale.FRANCE");
      IMPORTS.add("java.util.Locale.GERMANY");
      IMPORTS.add("java.util.Locale.ITALY");
      IMPORTS.add("java.util.Locale.JAPAN");
      IMPORTS.add("java.util.Locale.KOREA");
      IMPORTS.add("java.util.Locale.CHINA");
      IMPORTS.add("java.util.Locale.PRC");
      IMPORTS.add("java.util.Locale.TAIWAN");
      IMPORTS.add("java.util.Locale.UK");
      IMPORTS.add("java.util.Locale.US");
      IMPORTS.add("java.util.Locale.CANADA");
      IMPORTS.add("java.util.Locale.CANADA_FRENCH");
      IMPORTS.add("java.util.Locale.ROOT");
      IMPORTS.add("android.view.View.VISIBLE");
      IMPORTS.add("android.view.View.GONE");
      IMPORTS.add("android.view.View.INVISIBLE");
    }

    final JavaContext context;

    ShouldUseStaticImportDetectorVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override public void visitReferenceExpression(final PsiReferenceExpression expression) {
      final String qualifiedName = expression.getQualifiedName();
      final boolean isIgnored = context.getDriver().isSuppressed(context, ISSUE_SHOULD_USE_STATIC_IMPORT, expression);
      final boolean isNotAStaticImport = expression.getText().contains(".");

      if (!isIgnored && IMPORTS.contains(qualifiedName) && isNotAStaticImport) {
        context.report(ISSUE_SHOULD_USE_STATIC_IMPORT, context.getLocation(expression), "Should statically import " + getName(qualifiedName));
      }
    }

    static String getName(final String name) {
      final String[] split = name.split("\\.");
      return split[split.length - 1];
    }
  }
}

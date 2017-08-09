package com.vanniktech.lintrules.android;

import com.android.annotations.Nullable;
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
      // TimeUnit.
      IMPORTS.add("java.util.concurrent.TimeUnit.NANOSECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.MICROSECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.MILLISECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.SECONDS");
      IMPORTS.add("java.util.concurrent.TimeUnit.MINUTES");
      IMPORTS.add("java.util.concurrent.TimeUnit.HOURS");
      IMPORTS.add("java.util.concurrent.TimeUnit.DAYS");
      // Locales.
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
      // Android View.
      IMPORTS.add("android.view.View.VISIBLE");
      IMPORTS.add("android.view.View.GONE");
      IMPORTS.add("android.view.View.INVISIBLE");
      // Arrays.
      IMPORTS.add("java.util.Arrays.asList");
      // Android Version Codes.
      IMPORTS.add("android.os.Build.VERSION_CODES.CUR_DEVELOPMENT");
      IMPORTS.add("android.os.Build.VERSION_CODES.BASE");
      IMPORTS.add("android.os.Build.VERSION_CODES.BASE_1_1");
      IMPORTS.add("android.os.Build.VERSION_CODES.CUPCAKE");
      IMPORTS.add("android.os.Build.VERSION_CODES.DONUT");
      IMPORTS.add("android.os.Build.VERSION_CODES.ECLAIR");
      IMPORTS.add("android.os.Build.VERSION_CODES.ECLAIR_0_1");
      IMPORTS.add("android.os.Build.VERSION_CODES.ECLAIR_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.FROYO");
      IMPORTS.add("android.os.Build.VERSION_CODES.GINGERBREAD");
      IMPORTS.add("android.os.Build.VERSION_CODES.GINGERBREAD_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.HONEYCOMB");
      IMPORTS.add("android.os.Build.VERSION_CODES.HONEYCOMB_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.HONEYCOMB_MR2");
      IMPORTS.add("android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH");
      IMPORTS.add("android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.JELLY_BEAN");
      IMPORTS.add("android.os.Build.VERSION_CODES.JELLY_BEAN_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.JELLY_BEAN_MR2");
      IMPORTS.add("android.os.Build.VERSION_CODES.KITKAT");
      IMPORTS.add("android.os.Build.VERSION_CODES.KITKAT_WATCH");
      IMPORTS.add("android.os.Build.VERSION_CODES.LOLLIPOP");
      IMPORTS.add("android.os.Build.VERSION_CODES.LOLLIPOP_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.M");
      IMPORTS.add("android.os.Build.VERSION_CODES.N");
      IMPORTS.add("android.os.Build.VERSION_CODES.N_MR1");
      IMPORTS.add("android.os.Build.VERSION_CODES.O");
      // Future proof Version Codes.
      IMPORTS.add("android.os.Build.VERSION_CODES.P");
      IMPORTS.add("android.os.Build.VERSION_CODES.Q");
      IMPORTS.add("android.os.Build.VERSION_CODES.R");
      IMPORTS.add("android.os.Build.VERSION_CODES.S");
      IMPORTS.add("android.os.Build.VERSION_CODES.T");
      IMPORTS.add("android.os.Build.VERSION_CODES.U");
      IMPORTS.add("android.os.Build.VERSION_CODES.V");
      IMPORTS.add("android.os.Build.VERSION_CODES.W");
      IMPORTS.add("android.os.Build.VERSION_CODES.X");
      IMPORTS.add("android.os.Build.VERSION_CODES.Y");
      IMPORTS.add("android.os.Build.VERSION_CODES.Z");
      // Collections.
      IMPORTS.add("java.util.Collections.emptyEnumeration");
      IMPORTS.add("java.util.Collections.emptyIterator");
      IMPORTS.add("java.util.Collections.emptyList");
      IMPORTS.add("java.util.Collections.emptyListIterator");
      IMPORTS.add("java.util.Collections.emptyMap");
      IMPORTS.add("java.util.Collections.emptySet");
      IMPORTS.add("java.util.Collections.singleton");
      IMPORTS.add("java.util.Collections.singletonList");
      IMPORTS.add("java.util.Collections.singletonMap");
      IMPORTS.add("java.util.Collections.singletonIterator");
      IMPORTS.add("java.util.Collections.singletonSpliterator");
      IMPORTS.add("java.util.Collections.unmodifiableCollection");
      IMPORTS.add("java.util.Collections.unmodifiableSet");
      IMPORTS.add("java.util.Collections.unmodifiableSortedSet");
      IMPORTS.add("java.util.Collections.unmodifiableNavigableSet");
      IMPORTS.add("java.util.Collections.unmodifiableList");
      IMPORTS.add("java.util.Collections.unmodifiableMap");
      IMPORTS.add("java.util.Collections.unmodifiableSortedMap");
      IMPORTS.add("java.util.Collections.unmodifiableNavigableMap");
      // Retention Policy.
      IMPORTS.add("java.lang.annotation.RetentionPolicy.SOURCE");
      IMPORTS.add("java.lang.annotation.RetentionPolicy.CLASS");
      IMPORTS.add("java.lang.annotation.RetentionPolicy.RUNTIME");
      // Mockito.
      IMPORTS.add("org.mockito.quality.Strictness.STRICT_STUBS");
      IMPORTS.add("org.mockito.quality.Strictness.WARN");
      IMPORTS.add("org.mockito.quality.Strictness.LENIENT");
    }

    final JavaContext context;

    ShouldUseStaticImportDetectorVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override public void visitReferenceExpression(final PsiReferenceExpression expression) {
      final String qualifiedName = getQualifiedName(expression);

      final boolean isIgnored = context.getDriver().isSuppressed(context, ISSUE_SHOULD_USE_STATIC_IMPORT, expression);
      final boolean isNotAStaticImport = expression.getText().contains(".");

      if (!isIgnored && IMPORTS.contains(qualifiedName) && isNotAStaticImport) {
        context.report(ISSUE_SHOULD_USE_STATIC_IMPORT, context.getLocation(expression), "Should statically import " + getName(qualifiedName));
      }
    }

    @Nullable private static String getQualifiedName(final PsiReferenceExpression expression) {
      try {
        return expression.getQualifiedName();
      } catch (final Exception ignore) {
        // UnimplementedLintPsiApiException can be thrown here, for instance for method references.
        return null;
      }
    }

    static String getName(final String name) {
      final String[] split = name.split("\\.");
      return split[split.length - 1];
    }
  }
}

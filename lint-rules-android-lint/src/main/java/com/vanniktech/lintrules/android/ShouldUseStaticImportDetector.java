package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UImportStatement;
import org.jetbrains.uast.UReferenceExpression;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;

@SuppressWarnings("checkstyle:multiplestringliterals") public class ShouldUseStaticImportDetector extends Detector implements Detector.UastScanner {
  private static final Map<String, String> METHODS_TO_STATICALLY_IMPORT = new HashMap<>();
  private static final Map<String, String> REFERENCES_TO_STATICALLY_IMPORT = new HashMap<>();

  static final Issue ISSUE_SHOULD_USE_STATIC_IMPORT =
      Issue.create("ShouldUseStaticImport", "Should be using a static import.",
          "Should be using a static import.", CORRECTNESS, 3,
          WARNING, new Implementation(ShouldUseStaticImportDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  static {
    // Arrays.
    METHODS_TO_STATICALLY_IMPORT.put("asList", "java.util.Arrays");

    // Collections.
    METHODS_TO_STATICALLY_IMPORT.put("emptyEnumeration", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("emptyIterator", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("emptyList", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("emptyListIterator", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("emptyMap", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("emptySet", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("singleton", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("singletonList", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("singletonMap", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("singletonIterator", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("singletonSpliterator", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableCollection", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableSet", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableSortedSet", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableNavigableSet", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableList", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableMap", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableSortedMap", "java.util.Collections");
    METHODS_TO_STATICALLY_IMPORT.put("unmodifiableNavigableMap", "java.util.Collections");

    // Mockito.
    METHODS_TO_STATICALLY_IMPORT.put("mock", "org.mockito.Mockito.mock");
    METHODS_TO_STATICALLY_IMPORT.put("mockingDetails", "org.mockito.Mockito.mockingDetails");
    METHODS_TO_STATICALLY_IMPORT.put("spy", "org.mockito.Mockito.spy");
    METHODS_TO_STATICALLY_IMPORT.put("verify", "org.mockito.Mockito.verify");
    METHODS_TO_STATICALLY_IMPORT.put("reset", "org.mockito.Mockito.reset");
    METHODS_TO_STATICALLY_IMPORT.put("clearInvocations", "org.mockito.Mockito.clearInvocations");
    METHODS_TO_STATICALLY_IMPORT.put("verifyNoMoreInteractions", "org.mockito.Mockito.verifyNoMoreInteractions");
    METHODS_TO_STATICALLY_IMPORT.put("verifyZeroInteractions", "org.mockito.Mockito.verifyZeroInteractions");
    METHODS_TO_STATICALLY_IMPORT.put("when", "org.mockito.Mockito.when");
    METHODS_TO_STATICALLY_IMPORT.put("doThrow", "org.mockito.Mockito.doThrow");
    METHODS_TO_STATICALLY_IMPORT.put("doCallRealMethod", "org.mockito.Mockito.doCallRealMethod");
    METHODS_TO_STATICALLY_IMPORT.put("doAnswer", "org.mockito.Mockito.doAnswer");
    METHODS_TO_STATICALLY_IMPORT.put("doNothing", "org.mockito.Mockito.doNothing");
    METHODS_TO_STATICALLY_IMPORT.put("doReturn", "org.mockito.Mockito.doReturn");
    METHODS_TO_STATICALLY_IMPORT.put("inOrder", "org.mockito.Mockito.inOrder");
    METHODS_TO_STATICALLY_IMPORT.put("ignoreStubs", "org.mockito.Mockito.ignoreStubs");
    METHODS_TO_STATICALLY_IMPORT.put("never", "org.mockito.Mockito.never");
    METHODS_TO_STATICALLY_IMPORT.put("times", "org.mockito.Mockito.times");
    METHODS_TO_STATICALLY_IMPORT.put("atLeastOnce", "org.mockito.Mockito.atLeastOnce");
    METHODS_TO_STATICALLY_IMPORT.put("atLeast", "org.mockito.Mockito.atLeast");
    METHODS_TO_STATICALLY_IMPORT.put("atMost", "org.mockito.Mockito.atMost");
    METHODS_TO_STATICALLY_IMPORT.put("calls", "org.mockito.Mockito.calls");
    METHODS_TO_STATICALLY_IMPORT.put("only", "org.mockito.Mockito.only");
    METHODS_TO_STATICALLY_IMPORT.put("timeout", "org.mockito.Mockito.timeout");
    METHODS_TO_STATICALLY_IMPORT.put("after", "org.mockito.Mockito.after");
    METHODS_TO_STATICALLY_IMPORT.put("validateMockitoUsage", "org.mockito.Mockito.validateMockitoUsage");
    METHODS_TO_STATICALLY_IMPORT.put("withSettings", "org.mockito.Mockito.withSettings");
    METHODS_TO_STATICALLY_IMPORT.put("description", "org.mockito.Mockito.description");
    METHODS_TO_STATICALLY_IMPORT.put("debug", "org.mockito.Mockito.debug");
    METHODS_TO_STATICALLY_IMPORT.put("framework", "org.mockito.Mockito.framework");
    METHODS_TO_STATICALLY_IMPORT.put("mockitoSession", "org.mockito.Mockito.mockitoSession");

    // TimeUnit.
    REFERENCES_TO_STATICALLY_IMPORT.put("NANOSECONDS", "java.util.concurrent.TimeUnit");
    REFERENCES_TO_STATICALLY_IMPORT.put("MICROSECONDS", "java.util.concurrent.TimeUnit");
    REFERENCES_TO_STATICALLY_IMPORT.put("MILLISECONDS", "java.util.concurrent.TimeUnit");
    REFERENCES_TO_STATICALLY_IMPORT.put("SECONDS", "java.util.concurrent.TimeUnit");
    REFERENCES_TO_STATICALLY_IMPORT.put("MINUTES", "java.util.concurrent.TimeUnit");
    REFERENCES_TO_STATICALLY_IMPORT.put("HOURS", "java.util.concurrent.TimeUnit");
    REFERENCES_TO_STATICALLY_IMPORT.put("DAYS", "java.util.concurrent.TimeUnit");
    // Locales.
    REFERENCES_TO_STATICALLY_IMPORT.put("ENGLISH", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("FRENCH", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("GERMAN", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("ITALIAN", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("JAPANESE", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("KOREAN", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("CHINESE", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("SIMPLIFIED_CHINESE", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("TRADITIONAL_CHINESE", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("FRANCE", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("GERMANY", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("ITALY", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("JAPAN", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("KOREA", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("CHINA", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("PRC", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("TAIWAN", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("UK", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("US", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("CANADA", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("CANADA_FRENCH", "java.util.Locale");
    REFERENCES_TO_STATICALLY_IMPORT.put("ROOT", "java.util.Locale");
    // Android View.
    REFERENCES_TO_STATICALLY_IMPORT.put("VISIBLE", "android.view.View");
    REFERENCES_TO_STATICALLY_IMPORT.put("GONE", "android.view.View");
    REFERENCES_TO_STATICALLY_IMPORT.put("INVISIBLE", "android.view.View");
    // Android Service.
    REFERENCES_TO_STATICALLY_IMPORT.put("START_CONTINUATION_MASK", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_STICKY_COMPATIBILITY", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_STICKY", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_NOT_STICKY", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_REDELIVER_INTENT", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_TASK_REMOVED_COMPLETE", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_FLAG_REDELIVERY", "android.app.Service");
    REFERENCES_TO_STATICALLY_IMPORT.put("START_FLAG_RETRY", "android.app.Service");
    // Android Versions.
    REFERENCES_TO_STATICALLY_IMPORT.put("INCREMENTAL", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("RELEASE", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("BASE_OS", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("SECURITY_PATCH", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("SDK", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("SDK_INT", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("PREVIEW_SDK_INT", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("CODENAME", "android.os.Build.VERSION");
    REFERENCES_TO_STATICALLY_IMPORT.put("RESOURCES_SDK_INT", "android.os.Build.VERSION");
    // Android Version Codes.
    REFERENCES_TO_STATICALLY_IMPORT.put("CUR_DEVELOPMENT", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("BASE", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("BASE_1_1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("CUPCAKE", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("DONUT", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("ECLAIR", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("ECLAIR_0_1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("ECLAIR_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("FROYO", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("GINGERBREAD", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("GINGERBREAD_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("HONEYCOMB", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("HONEYCOMB_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("HONEYCOMB_MR2", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("ICE_CREAM_SANDWICH", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("ICE_CREAM_SANDWICH_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("JELLY_BEAN", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("JELLY_BEAN_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("JELLY_BEAN_MR2", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("KITKAT", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("KITKAT_WATCH", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("LOLLIPOP", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("LOLLIPOP_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("VERSION_CODES.M", "android.os.Build");
    REFERENCES_TO_STATICALLY_IMPORT.put("VERSION_CODES.N", "android.os.Build");
    REFERENCES_TO_STATICALLY_IMPORT.put("N_MR1", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("O", "android.os.Build.VERSION_CODES");
    // Future proof Version Codes.
    REFERENCES_TO_STATICALLY_IMPORT.put("P", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("Q", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("R", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("S", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("T", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("U", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("V", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("W", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("X", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("Y", "android.os.Build.VERSION_CODES");
    REFERENCES_TO_STATICALLY_IMPORT.put("Z", "android.os.Build.VERSION_CODES");
    // Retention Policy.
    REFERENCES_TO_STATICALLY_IMPORT.put("SOURCE", "java.lang.annotation.RetentionPolicy");
    REFERENCES_TO_STATICALLY_IMPORT.put("CLASS", "java.lang.annotation.RetentionPolicy");
    REFERENCES_TO_STATICALLY_IMPORT.put("RUNTIME", "java.lang.annotation.RetentionPolicy");
    // Mockito.
    REFERENCES_TO_STATICALLY_IMPORT.put("STRICT_STUBS", "org.mockito.quality.Strictness");
    REFERENCES_TO_STATICALLY_IMPORT.put("WARN", "org.mockito.quality.Strictness");
    REFERENCES_TO_STATICALLY_IMPORT.put("LENIENT", "org.mockito.quality.Strictness");
  }

  @NonNull private static UFile getUFile(@Nullable final UElement element) {
    if (element instanceof UFile) {
      return (UFile) element;
    }

    if (element != null) {
      return getUFile(element.getUastParent());
    }

    throw new IllegalArgumentException("Can't get file from element");
  }

  @Override public List<String> getApplicableMethodNames() {
    return new ArrayList<>(METHODS_TO_STATICALLY_IMPORT.keySet());
  }

  @Override public void visitMethod(final JavaContext context, final UCallExpression node, final PsiMethod method) {
    final String methodName = node.getMethodName();
    final String className = METHODS_TO_STATICALLY_IMPORT.get(methodName);

    if (methodName != null && className != null) {
      final PsiElement psi = node.getPsi();
      final boolean isStaticallyImported = psi != null && psi.getText().startsWith(methodName);
      final boolean matches = context.getEvaluator().isMemberInClass(method, className);

      if (!isStaticallyImported && matches) {
        context.report(ISSUE_SHOULD_USE_STATIC_IMPORT, node, context.getNameLocation(node), "Should statically import " + methodName);
      }
    }
  }

  @Override public List<String> getApplicableReferenceNames() {
    return new ArrayList<>(REFERENCES_TO_STATICALLY_IMPORT.keySet());
  }

  @Override public void visitReference(final JavaContext context, final UReferenceExpression expression, final PsiElement referenced) {
    final String name = expression.asRenderString();
    final UFile uFile = getUFile(expression);

    boolean isStaticallyImported = false;

    for (final UImportStatement uImportStatement : uFile.getImports()) {
      if (uImportStatement.asSourceString().contains(name)) {
        isStaticallyImported = true;
        break;
      }
    }

    if (!isStaticallyImported) {
      context.report(ISSUE_SHOULD_USE_STATIC_IMPORT, expression, context.getLocation(expression), "Should statically import " + name);
    }
  }
}

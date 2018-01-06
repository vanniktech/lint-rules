package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UMethod;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class RxJava2MethodMissingCheckReturnValueDetector extends Detector implements Detector.UastScanner {
  static final Issue ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE = Issue.create("RxJava2MethodMissingCheckReturnValue",
      "Method is missing the @CheckReturnValue annotation.",
      "Methods returning RxJava Reactive Types should be annotated with the @CheckReturnValue annotation. Static analyze tools such as Lint or ErrorProne can detect when the return value of a method is not used. This is usually an indication of a bug. If this is done on purpose (e.g. fire & forget) it should be stated explicitly.",
      MESSAGES, 8, WARNING,
      new Implementation(RxJava2MethodMissingCheckReturnValueDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  @Override public List<Class<? extends UElement>> getApplicableUastTypes() {
    return Collections.<Class<? extends UElement>>singletonList(UMethod.class);
  }

  @Override public UElementHandler createUastHandler(final JavaContext context) {
    return new CheckReturnValueVisitor(context);
  }

  static class CheckReturnValueVisitor extends UElementHandler {
    static boolean isRxJava2TypeThatRequiresCheckReturnValueAnnotation(final PsiType psiType) {
      final String canonicalText = psiType.getCanonicalText()
          .replaceAll("<[\\w.]*>", ""); // We need to remove the generics.
      return canonicalText.matches("io\\.reactivex\\.[\\w]+")
          || "io.reactivex.disposables.Disposable".equals(canonicalText)
          || "io.reactivex.observers.TestObserver".equals(canonicalText)
          || "io.reactivex.subscribers.TestSubscriber".equals(canonicalText);
    }

    private final JavaContext context;

    CheckReturnValueVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override public void visitMethod(final UMethod method) {
      final PsiType returnType = method.getReturnType();

      if (returnType != null && isRxJava2TypeThatRequiresCheckReturnValueAnnotation(returnType)) {
        final PsiAnnotation[] annotations = context.getEvaluator().getAllAnnotations(method, true);

        for (final PsiAnnotation annotation : annotations) {
          if ("io.reactivex.annotations.CheckReturnValue".equals(annotation.getQualifiedName())) {
            return;
          }
        }

        context.report(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE, method, context.getNameLocation(method), "Method should have @CheckReturnValue annotation.");
      }
    }
  }
}

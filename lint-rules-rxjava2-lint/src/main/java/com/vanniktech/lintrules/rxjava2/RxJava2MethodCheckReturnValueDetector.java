package com.vanniktech.lintrules.rxjava2;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import java.util.Collections;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class RxJava2MethodCheckReturnValueDetector extends Detector implements Detector.JavaPsiScanner {
  static Issue[] getIssues() {
    return new Issue[] {
        METHOD_MISSING_CHECK_RETURN_VALUE
    };
  }

  static final Issue METHOD_MISSING_CHECK_RETURN_VALUE =
      Issue.create("MethodMissingCheckReturnValue", "Method is missing the @CheckReturnValue annotation",
          "Methods returning RxJava Reactive Types should be annotated with the @CheckReturnValue annotation.",
              MESSAGES, 8, WARNING,
          new Implementation(RxJava2MethodCheckReturnValueDetector.class, JAVA_FILE_SCOPE));

  @Override
  public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
    return Collections.<Class<? extends PsiElement>>singletonList(PsiMethod.class);
  }

  @Override
  public JavaElementVisitor createPsiVisitor(@NonNull final JavaContext context) {
    return new CheckReturnValueVisitor(context);
  }

  static class CheckReturnValueVisitor extends JavaElementVisitor {
    private final JavaContext context;

    CheckReturnValueVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override
    public void visitMethod(final PsiMethod method) {
      final PsiType returnType = method.getReturnType();

      if (returnType != null && Utils.isRxJava2TypeThatRequiresCheckReturnValueAnnotation(returnType)) {
        final PsiAnnotation[] annotations = method.getModifierList().getAnnotations();

        for (final PsiAnnotation annotation : annotations) {
          if ("io.reactivex.annotations.CheckReturnValue".equals(annotation.getQualifiedName())) {
            return;
          }
        }

        context.report(METHOD_MISSING_CHECK_RETURN_VALUE, context.getLocation(method), "Method should have @CheckReturnValue annotation");
      }
    }
  }
}

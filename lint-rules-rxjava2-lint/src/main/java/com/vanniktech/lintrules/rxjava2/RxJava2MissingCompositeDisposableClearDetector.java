package com.vanniktech.lintrules.rxjava2;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.ERROR;

public final class RxJava2MissingCompositeDisposableClearDetector extends Detector implements Detector.JavaPsiScanner {
  static final Issue ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR =
      Issue.create("MissingCompositeDisposableClear", "Not calling clear() on CompositeDisposable",
          "A class is using CompositeDisposable and not clearing the List.",
              MESSAGES, 10, ERROR,
          new Implementation(RxJava2MissingCompositeDisposableClearDetector.class, JAVA_FILE_SCOPE));

  @Override
  public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
    return Collections.<Class<? extends PsiElement>>singletonList(PsiClass.class);
  }

  @Override
  public JavaElementVisitor createPsiVisitor(@NonNull final JavaContext context) {
    return new MissingCompositeDisposableClearVisitor(context);
  }

  static class MissingCompositeDisposableClearVisitor extends JavaElementVisitor {
    private final JavaContext context;

    MissingCompositeDisposableClearVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override public void visitClass(final PsiClass clazz) {
      final PsiField[] fields = clazz.getFields();

      final Set<PsiField> compositeDisposables = new HashSet<>();

      for (final PsiField field : fields) {
        if ("io.reactivex.disposables.CompositeDisposable".equals(field.getType().getCanonicalText())) {
          compositeDisposables.add(field);
        }
      }

      final PsiMethod[] allMethods = clazz.getAllMethods();

      for (final PsiMethod allMethod : allMethods) {
        final PsiCodeBlock body = allMethod.getBody();

        if (body != null) {
          final PsiStatement[] statements = body.getStatements();

          for (final PsiStatement statement : statements) {
            final Iterator<PsiField> iterator = compositeDisposables.iterator();

            if (statement instanceof PsiExpressionStatement) {
              final PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;

              while (iterator.hasNext()) {
                final PsiField psiField = iterator.next();

                if (expressionStatement.getExpression().getText().equals(psiField.getName() + ".clear()")) {
                  iterator.remove();
                }
              }
            }
          }
        }
      }

      for (final PsiField compositeDisposable : compositeDisposables) {
        context.report(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR, context.getLocation(compositeDisposable), "clear() is not called.");
      }
    }
  }
}

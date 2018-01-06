package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UField;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.ERROR;

public final class RxJava2MissingCompositeDisposableClearDetector extends Detector implements Detector.UastScanner {
  static final Issue ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR = Issue.create("RxJava2MissingCompositeDisposableClear",
      "Marks CompositeDisposables that are not being cleared.",
      "A class is using CompositeDisposable and not calling clear(). This can leave operations running and even cause memory leaks. It's best to always call clear() once you're done. e.g. in onDestroy() for Activitys.",
      CORRECTNESS, 10, ERROR,
      new Implementation(RxJava2MissingCompositeDisposableClearDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  @Override public List<Class<? extends UElement>> getApplicableUastTypes() {
    return Collections.<Class<? extends UElement>>singletonList(UClass.class);
  }

  @Override public UElementHandler createUastHandler(final JavaContext context) {
    return new MissingCompositeDisposableClearVisitor(context);
  }

  static class MissingCompositeDisposableClearVisitor extends UElementHandler {
    private final JavaContext context;

    MissingCompositeDisposableClearVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override @SuppressWarnings("PMD.CyclomaticComplexity") public void visitClass(final UClass clazz) {
      final UField[] fields = clazz.getFields();

      final Set<UField> compositeDisposables = new HashSet<>();

      for (final UField field : fields) {
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
            final Iterator<UField> iterator = compositeDisposables.iterator();

            if (statement instanceof PsiExpressionStatement) {
              final PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;

              while (iterator.hasNext()) {
                final UField field = iterator.next();

                if (expressionStatement.getExpression().getText().equals(field.getName() + ".clear()")) { // NOPMD
                  iterator.remove();
                }
              }
            }
          }
        }
      }

      for (final UField compositeDisposable : compositeDisposables) {
        context.report(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR, compositeDisposable, context.getLocation(compositeDisposable), "clear() is not called.");
      }
    }
  }
}

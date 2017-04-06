package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.ERROR;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class RxJava2Detector extends Detector implements Detector.JavaPsiScanner {
  @Override public List<String> getApplicableMethodNames() {
    return Arrays.asList("dispose", "addAll", "subscribe");
  }

  @Override public void visitMethod(final JavaContext context, final JavaElementVisitor visitor,
      final PsiMethodCallExpression call, final PsiMethod method) {
    final PsiReferenceExpression methodExpression = call.getMethodExpression();
    final String fullyQualifiedMethodName = methodExpression.getQualifiedName();

    handleCompositeDisposable(context, call, methodExpression, fullyQualifiedMethodName);
    handleSubscribeCalls(context, call, methodExpression, fullyQualifiedMethodName);
  }

  private void handleCompositeDisposable(final JavaContext context, final PsiMethodCallExpression call,
      final PsiReferenceExpression methodExpression, final String fullyQualifiedMethodName) {
    final boolean isCompositeDisposableDisposeSuppressed = context.getDriver().isSuppressed(context, COMPOSITE_DISPOSABLE_DISPOSE, methodExpression);
    if ("io.reactivex.disposables.CompositeDisposable.dispose".equals(fullyQualifiedMethodName) && !isCompositeDisposableDisposeSuppressed) {
      context.report(COMPOSITE_DISPOSABLE_DISPOSE, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using dispose() instead of clear()");
    }

    final boolean isCompositeDisposableAddAllSuppressed = context.getDriver().isSuppressed(context, COMPOSITE_DISPOSABLE_ADD_ALL, methodExpression);
    if ("io.reactivex.disposables.CompositeDisposable.addAll".equals(fullyQualifiedMethodName) && !isCompositeDisposableAddAllSuppressed) {
      context.report(COMPOSITE_DISPOSABLE_ADD_ALL, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using addAll() instead of add() separately");
    }
  }

  private void handleSubscribeCalls(final JavaContext context, final PsiMethodCallExpression call,
      final PsiReferenceExpression methodExpression, final String fullyQualifiedMethodName) {
    if ("io.reactivex.Observable.subscribe".equals(fullyQualifiedMethodName)
        || "io.reactivex.Flowable.subscribe".equals(fullyQualifiedMethodName)
        || "io.reactivex.Single.subscribe".equals(fullyQualifiedMethodName)
        || "io.reactivex.Completable.subscribe".equals(fullyQualifiedMethodName)
        || "io.reactivex.Maybe.subscribe".equals(fullyQualifiedMethodName)) {

      final boolean isSubscribeMissingErrorConsumerSuppressed = context.getDriver().isSuppressed(context, SUBSCRIBE_MISSING_ERROR_CONSUMER, methodExpression);
      if (call.getArgumentList().getExpressions().length < 2 && !isSubscribeMissingErrorConsumerSuppressed) {
        context.report(SUBSCRIBE_MISSING_ERROR_CONSUMER, call, context.getLocation(methodExpression.getReferenceNameElement()),
            "Using a version of subscribe() without an error Consumer");
      }
    }
  }

  static Issue[] getIssues() {
    return new Issue[] {
        COMPOSITE_DISPOSABLE_DISPOSE, COMPOSITE_DISPOSABLE_ADD_ALL, SUBSCRIBE_MISSING_ERROR_CONSUMER
    };
  }

  static final Issue COMPOSITE_DISPOSABLE_DISPOSE =
      Issue.create("CompositeDisposableDispose", "Using dispose() instead of clear()",
          "Instead of using dispose(), clear() should be used.",
              MESSAGES, 8, WARNING,
          new Implementation(RxJava2Detector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  static final Issue COMPOSITE_DISPOSABLE_ADD_ALL =
      Issue.create("CompositeDisposableAddAll", "Using addAll() instead of add() separately",
          "Instead of using addAll(), add() should be used separately.",
              MESSAGES, 5, WARNING,
          new Implementation(RxJava2Detector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  static final Issue SUBSCRIBE_MISSING_ERROR_CONSUMER =
      Issue.create("SubscribeMissingErrorConsumer", "Using a version of subscribe() without an error Consumer",
          "When calling subscribe() an error Consumer should always be used.",
              MESSAGES, 10, ERROR,
          new Implementation(RxJava2Detector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));
}

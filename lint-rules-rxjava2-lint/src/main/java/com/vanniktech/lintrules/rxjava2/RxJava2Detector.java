package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import java.util.EnumSet;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.ERROR;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class RxJava2Detector extends Detector implements Detector.JavaPsiScanner {
  @Override public List<String> getApplicableMethodNames() {
    return asList("dispose", "addAll", "subscribe");
  }

  @Override public void visitMethod(final JavaContext context, final JavaElementVisitor visitor, final PsiMethodCallExpression call, final PsiMethod method) {
    final LintDriver driver = context.getDriver();
    final JavaEvaluator evaluator = context.getEvaluator();
    final PsiReferenceExpression methodExpression = call.getMethodExpression();

    final boolean isCompositeDisposableDisposeSuppressed = driver.isSuppressed(context, COMPOSITE_DISPOSABLE_DISPOSE, methodExpression);

    if ("dispose".equals(methodExpression.getReferenceName()) && evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable") && !isCompositeDisposableDisposeSuppressed) {
      context.report(COMPOSITE_DISPOSABLE_DISPOSE, call, context.getLocation(methodExpression.getReferenceNameElement()), "Using dispose() instead of clear()");
    }

    final boolean isCompositeDisposableAddAllSuppressed = driver.isSuppressed(context, COMPOSITE_DISPOSABLE_ADD_ALL, methodExpression);

    if ("addAll".equals(methodExpression.getReferenceName()) && evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable") && !isCompositeDisposableAddAllSuppressed) {
      context.report(COMPOSITE_DISPOSABLE_ADD_ALL, call, context.getLocation(methodExpression.getReferenceNameElement()), "Using addAll() instead of add() separately");
    }

    final boolean isInObservable = evaluator.isMemberInClass(method, "io.reactivex.Observable");
    final boolean isInFlowable = evaluator.isMemberInClass(method, "io.reactivex.Flowable");
    final boolean isInSingle = evaluator.isMemberInClass(method, "io.reactivex.Single");
    final boolean isInCompletable = evaluator.isMemberInClass(method, "io.reactivex.Completable");
    final boolean isInMaybe = evaluator.isMemberInClass(method, "io.reactivex.Maybe");

    if ("subscribe".equals(methodExpression.getReferenceName()) && (isInObservable || isInFlowable || isInSingle || isInCompletable || isInMaybe)) {
      final boolean isSubscribeMissingErrorConsumerSuppressed = driver.isSuppressed(context, SUBSCRIBE_MISSING_ERROR_CONSUMER, methodExpression);
      if (call.getArgumentList().getExpressions().length < 2 && !isSubscribeMissingErrorConsumerSuppressed) {
        context.report(SUBSCRIBE_MISSING_ERROR_CONSUMER, call, context.getLocation(methodExpression.getReferenceNameElement()), "Using a version of subscribe() without an error Consumer");
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

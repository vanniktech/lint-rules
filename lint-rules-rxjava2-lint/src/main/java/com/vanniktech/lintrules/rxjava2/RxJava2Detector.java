package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiMethod;
import java.util.EnumSet;
import java.util.List;
import org.jetbrains.uast.UCallExpression;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.ERROR;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class RxJava2Detector extends Detector implements Detector.UastScanner {
  @Override public List<String> getApplicableMethodNames() {
    return asList("dispose", "addAll", "subscribe");
  }

  @Override public void visitMethod(final JavaContext context, final UCallExpression node, final PsiMethod method) {
    final String methodName = node.getMethodName();
    final JavaEvaluator evaluator = context.getEvaluator();

    if ("dispose".equals(methodName) && evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable")) {
      context.report(COMPOSITE_DISPOSABLE_DISPOSE, node, context.getNameLocation(node), "Using dispose() instead of clear()");
    }

    if ("addAll".equals(methodName) && evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable")) {
      context.report(COMPOSITE_DISPOSABLE_ADD_ALL, node, context.getNameLocation(node), "Using addAll() instead of add() separately");
    }

    final boolean isInObservable = evaluator.isMemberInClass(method, "io.reactivex.Observable");
    final boolean isInFlowable = evaluator.isMemberInClass(method, "io.reactivex.Flowable");
    final boolean isInSingle = evaluator.isMemberInClass(method, "io.reactivex.Single");
    final boolean isInCompletable = evaluator.isMemberInClass(method, "io.reactivex.Completable");
    final boolean isInMaybe = evaluator.isMemberInClass(method, "io.reactivex.Maybe");

    if ("subscribe".equals(methodName) && (isInObservable || isInFlowable || isInSingle || isInCompletable || isInMaybe) && node.getValueArgumentCount() < 2) {
      context.report(SUBSCRIBE_MISSING_ERROR_CONSUMER, node, context.getNameLocation(node), "Using a version of subscribe() without an error Consumer");
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

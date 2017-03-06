package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.rxjava2.RxJava2MethodCheckReturnValueDetector.ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class RxJava2MethodCheckReturnValueDetectorTest extends RxJavaLintDetectorTest {
  public void testMethodReturningObservableMissingCheckReturnValueSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "import android.annotation.SuppressLint;\n"
        + "public class Example {\n"
        + "  @SuppressLint(\"MethodMissingCheckReturnValue\") public Observable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubSuppressLint, stubObservable, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningObservableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "public class Example {\n"
        + "  public Observable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubObservable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Observable<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningObservableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Observable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubObservable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningFlowableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "public class Example {\n"
        + "  public Flowable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubFlowable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Flowable<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningFlowableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Flowable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubFlowable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningSingleMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "public class Example {\n"
        + "  public Single<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubSingle, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Single<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningSingleHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Single<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubSingle, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningMaybeMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "public class Example {\n"
        + "  public Maybe<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubMaybe, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Maybe<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningMaybeHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Maybe<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubMaybe, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningCompletableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "public class Example {\n"
        + "  public Completable foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompletable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Completable foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningCompletableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Completable foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompletable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningDisposableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.Disposable;\n"
        + "public class Example {\n"
        + "  public Disposable foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubDisposable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Disposable foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningDisposableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.Disposable;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Disposable foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubDisposable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningTestObserverMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.observers.TestObserver;\n"
        + "public class Example {\n"
        + "  public TestObserver foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubTestObserver, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public TestObserver foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningTestObserverHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.observers.TestObserver;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public TestObserver foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubTestObserver, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningTestSubscriberMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.subscribers.TestSubscriber;\n"
        + "public class Example {\n"
        + "  public TestSubscriber foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubTestSubscriber, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public TestSubscriber foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningTestSubscriberHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.subscribers.TestSubscriber;\n"
        + "import io.reactivex.annotations.CheckReturnValue;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public TestSubscriber foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubTestSubscriber, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new RxJava2MethodCheckReturnValueDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE);
  }

  @Override protected boolean allowCompilationErrors() {
    return true;
  }
}

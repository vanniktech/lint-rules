package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.Arrays;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static org.fest.assertions.api.Assertions.assertThat;

public class RxJava2DetectorTest extends LintDetectorTest {
  private static final String NO_WARNINGS = "No warnings.";

  private final TestFile compositeDisposableStub = java(""
      + "package io.reactivex.disposables;\n"
      + "public class CompositeDisposable {\n"
      + "  public void dispose() {}\n"
      + "  public void addAll() {}\n"
      + "}");

  private final TestFile consumerStub = java(""
      + "package io.reactivex.functions;\n"
      + "public interface Consumer<T> {\n"
      + "  void accept(T t) throws Exception;\n"
      + "}");

  private final TestFile actionStub = java(""
      + "package io.reactivex.functions;\n"
      + "public interface Action {\n"
      + "  void run() throws Exception;\n"
      + "}");

  private final TestFile observableStub = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Observable<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onNext) {}\n"
      + "  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}\n"
      + "}");

  private final TestFile flowableStub = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Flowable<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onNext) {}\n"
      + "  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}\n"
      + "}");

  private final TestFile singleStub = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Single<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onSuccess) {}\n"
      + "  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}\n"
      + "}");

  private final TestFile completableStub = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Action;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Completable {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Action onComplete) {}\n"
      + "  public void subscribe(Action onComplete, Consumer<Throwable> onError) {}\n"
      + "}");

  private final TestFile maybeStub = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Maybe<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onSuccess) {}\n"
      + "  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}\n"
      + "}");

  public void testCallingCompositeDisposableDispose() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    CompositeDisposable cd = null;\n"
        + "    cd.dispose();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(compositeDisposableStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using dispose() instead of clear() [CompositeDisposableDispose]\n"
        + "    cd.dispose();\n"
        + "       ~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingCompositeDisposableAddAll() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    CompositeDisposable cd = null;\n"
        + "    cd.addAll();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(compositeDisposableStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using addAll() instead of add() separately [CompositeDisposableAddAll]\n"
        + "    cd.addAll();\n"
        + "       ~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingObservableSubscribe() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Observable o = null;\n"
        + "    o.subscribe();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, observableStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    o.subscribe();\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingObservableSubscribeOnSuccess() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Observable o = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    o.subscribe(c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, observableStub, java(source))).isEqualTo("src/foo/Example.java:7: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    o.subscribe(c);\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingObservableSubscribeOnSuccessWithError() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Observable o = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    osubscribe(c, c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, observableStub, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCallingFlowableSubscribe() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Flowable f = null;\n"
        + "    f.subscribe();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, flowableStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    f.subscribe();\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingFlowableSubscribeOnSuccess() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Flowable f = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    f.subscribe(c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, flowableStub, java(source))).isEqualTo("src/foo/Example.java:7: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    f.subscribe(c);\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingFlowableSubscribeOnSuccessWithError() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Flowable f = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    f.subscribe(c, c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, flowableStub, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCallingSingleSubscribe() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Single s = null;\n"
        + "    s.subscribe();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, singleStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    s.subscribe();\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingSingleSubscribeOnSuccess() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Single s = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    s.subscribe(c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, singleStub, java(source))).isEqualTo("src/foo/Example.java:7: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    s.subscribe(c);\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingSingleSubscribeOnSuccessWithError() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Single s = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    s.subscribe(c, c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, singleStub, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCallingCompletableSubscribe() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Completable cp = null;\n"
        + "    cp.subscribe();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(actionStub, completableStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    cp.subscribe();\n"
        + "       ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingCompletableSubscribeOnSuccess() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Completable cp = null;\n"
        + "    Action a = new Action() { @Override public void run() throws Exception { } };\n"
        + "    cp.subscribe(a);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(actionStub, completableStub, java(source))).isEqualTo("src/foo/Example.java:7: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    cp.subscribe(a);\n"
        + "       ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingCompletableSubscribeOnSuccessWithError() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Completable cp = null;\n"
        + "    Action a = new Action() { @Override public void run() throws Exception { } };\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    cp.subscribe(a, c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(actionStub, consumerStub, completableStub, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCallingMaybeSubscribe() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Maybe m = null;\n"
        + "    m.subscribe();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, maybeStub, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    m.subscribe();\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingMaybeSubscribeOnSuccess() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Maybe m = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    m.subscribe(c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, maybeStub, java(source))).isEqualTo("src/foo/Example.java:7: "
        + "Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]\n"
        + "    m.subscribe(c);\n"
        + "      ~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testCallingMaybeSubscribeOnSuccessWithError() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Maybe m = null;\n"
        + "    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };\n"
        + "    m.subscribe(c, c);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(consumerStub, maybeStub, java(source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new RxJava2Detector();
  }

  @Override protected List<Issue> getIssues() {
    return Arrays.asList(RxJava2Detector.getIssues());
  }

  @Override protected boolean allowCompilationErrors() {
    return true;
  }
}

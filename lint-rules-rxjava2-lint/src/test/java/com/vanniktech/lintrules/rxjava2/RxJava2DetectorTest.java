package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.Arrays;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static org.fest.assertions.api.Assertions.assertThat;

public class RxJava2DetectorTest extends RxJavaLintDetectorTest {
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
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using dispose() instead of clear() [CompositeDisposableDispose]\n"
        + "    cd.dispose();\n"
        + "       ~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingCompositeDisposableDisposeSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    CompositeDisposable cd = null;\n"
        + "    //noinspection AndroidLintCompositeDisposableDispose\n"
        + "    cd.dispose();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo(NO_WARNINGS);
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
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using addAll() instead of add() separately [CompositeDisposableAddAll]\n"
        + "    cd.addAll();\n"
        + "       ~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingCompositeDisposableSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    CompositeDisposable cd = null;\n"
        + "    //noinspection AndroidLintCompositeDisposableAddAll\n"
        + "    cd.addAll();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCallingObservableSubscribeSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Observable o = null;\n"
        + "    //noinspection AndroidLintSubscribeMissingErrorConsumer\n"
        + "    o.subscribe();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubConsumer, stubObservable, java(source))).isEqualTo(NO_WARNINGS);
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
    assertThat(lintProject(stubConsumer, stubObservable, java(source))).isEqualTo("src/foo/Example.java:6: "
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
    assertThat(lintProject(stubConsumer, stubObservable, java(source))).isEqualTo("src/foo/Example.java:7: "
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
    assertThat(lintProject(stubConsumer, stubObservable, java(source))).isEqualTo(NO_WARNINGS);
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
    assertThat(lintProject(stubConsumer, stubFlowable, java(source))).isEqualTo("src/foo/Example.java:6: "
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
    assertThat(lintProject(stubConsumer, stubFlowable, java(source))).isEqualTo("src/foo/Example.java:7: "
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
    assertThat(lintProject(stubConsumer, stubFlowable, java(source))).isEqualTo(NO_WARNINGS);
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
    assertThat(lintProject(stubConsumer, stubSingle, java(source))).isEqualTo("src/foo/Example.java:6: "
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
    assertThat(lintProject(stubConsumer, stubSingle, java(source))).isEqualTo("src/foo/Example.java:7: "
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
    assertThat(lintProject(stubConsumer, stubSingle, java(source))).isEqualTo(NO_WARNINGS);
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
    assertThat(lintProject(stubAction, stubCompletable, java(source))).isEqualTo("src/foo/Example.java:6: "
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
    assertThat(lintProject(stubAction, stubCompletable, java(source))).isEqualTo("src/foo/Example.java:7: "
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
    assertThat(lintProject(stubAction, stubConsumer, stubCompletable, java(source))).isEqualTo(NO_WARNINGS);
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
    assertThat(lintProject(stubConsumer, stubMaybe, java(source))).isEqualTo("src/foo/Example.java:6: "
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
    assertThat(lintProject(stubConsumer, stubMaybe, java(source))).isEqualTo("src/foo/Example.java:7: "
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
    assertThat(lintProject(stubConsumer, stubMaybe, java(source))).isEqualTo(NO_WARNINGS);
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

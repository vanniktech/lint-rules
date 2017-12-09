package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2DetectorTest {
  @Test fun callingCompositeDisposableDispose() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    cd.dispose();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Warning: Using dispose() instead of clear() [CompositeDisposableDispose]
          |    cd.dispose();
          |       ~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingCompositeDisposableDisposeSuppressed() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    //noinspection AndroidLintCompositeDisposableDispose
          |    cd.dispose();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingCompositeDisposableAddAll() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    cd.addAll();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Warning: Using addAll() instead of add() separately [CompositeDisposableAddAll]
          |    cd.addAll();
          |       ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingCompositeDisposableSuppressed() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    //noinspection AndroidLintCompositeDisposableAddAll
          |    cd.addAll();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingObservableSubscribeSuppressed() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |class Example {
          |  public void foo() {
          |    Observable o = null;
          |    //noinspection AndroidLintSubscribeMissingErrorConsumer
          |    o.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingObservableSubscribe() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |class Example {
          |  public void foo() {
          |    Observable o = null;
          |    o.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    o.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingObservableSubscribeOnSuccess() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |class Example {
          |  public void foo() {
          |    Observable o = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    o.subscribe(c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    o.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingObservableSubscribeOnSuccessWithError() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |class Example {
          |  public void foo() {
          |    Observable o = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    osubscribe(c, c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingFlowableSubscribe() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubFlowable, java("""
          |package foo;
          |import io.reactivex.Flowable;
          |class Example {
          |  public void foo() {
          |    Flowable f = null;
          |    f.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    f.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingFlowableSubscribeOnSuccess() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubFlowable, java("""
          |package foo;
          |import io.reactivex.Flowable;
          |class Example {
          |  public void foo() {
          |    Flowable f = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    f.subscribe(c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    f.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingFlowableSubscribeOnSuccessWithError() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubFlowable, java("""
          |package foo;
          |import io.reactivex.Flowable;
          |class Example {
          |  public void foo() {
          |    Flowable f = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    f.subscribe(c, c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingSingleSubscribe() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubSingle, java("""
          |package foo;
          |import io.reactivex.Single;
          |class Example {
          |  public void foo() {
          |    Single s = null;
          |    s.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    s.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingSingleSubscribeOnSuccess() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubSingle, java("""
          |package foo;
          |import io.reactivex.Single;
          |class Example {
          |  public void foo() {
          |    Single s = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    s.subscribe(c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    s.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingSingleSubscribeOnSuccessWithError() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubSingle, java("""
          |package foo;
          |import io.reactivex.Single;
          |class Example {
          |  public void foo() {
          |    Single s = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    s.subscribe(c, c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingCompletableSubscribe() {
    lint()
      .allowCompilationErrors()
      .files(stubAction, stubCompletable, java("""
          |package foo;
          |import io.reactivex.Completable;
          |class Example {
          |  public void foo() {
          |    Completable cp = null;
          |    cp.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    cp.subscribe();
          |       ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingCompletableSubscribeOnSuccess() {
    lint()
      .allowCompilationErrors()
      .files(stubAction, stubCompletable, java("""
          |package foo;
          |import io.reactivex.Completable;
          |class Example {
          |  public void foo() {
          |    Completable cp = null;
          |    Action a = new Action() { @Override public void run() { } };
          |    cp.subscribe(a);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    cp.subscribe(a);
          |       ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingCompletableSubscribeOnSuccessWithError() {
    lint()
      .allowCompilationErrors()
      .files(stubAction, stubConsumer, stubCompletable, java("""
          |package foo;
          |import io.reactivex.Completable;
          |class Example {
          |  public void foo() {
          |    Completable cp = null;
          |    Action a = new Action() { @Override public void run() { } };
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    cp.subscribe(a, c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }

  @Test fun callingMaybeSubscribe() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubMaybe, java("""
          |package foo;
          |import io.reactivex.Maybe;
          |class Example {
          |  public void foo() {
          |    Maybe m = null;
          |    m.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    m.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingMaybeSubscribeOnSuccess() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubMaybe, java("""
          |package foo;
          |import io.reactivex.Maybe;
          |class Example {
          |  public void foo() {
          |    Maybe m = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    m.subscribe(c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingErrorConsumer]
          |    m.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingMaybeSubscribeOnSuccessWithError() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubMaybe, java("""
          |package foo;
          |import io.reactivex.Maybe;
          |class Example {
          |  public void foo() {
          |    Maybe m = null;
          |    Consumer c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    m.subscribe(c, c);
          |  }
          |}""".trimMargin()))
      .issues(*RxJava2Detector.getIssues())
      .run()
      .expectClean()
  }
}

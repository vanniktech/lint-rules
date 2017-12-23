package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2SubscribeMissingOnErrorTest {
  @Test fun callingObservableSubscribeSuppressed() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |class Example {
          |  public void foo() {
          |    Observable o = null;
          |    //noinspection AndroidLintSubscribeMissingOnError
          |    o.subscribe();
          |  }
          |}""".trimMargin()))
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:6: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expect("""
          |src/foo/Example.java:7: Error: Using a version of subscribe() without an error Consumer [SubscribeMissingOnError]
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
      .issues(SUBSCRIBE_MISSING_ON_ERROR)
      .run()
      .expectClean()
  }
}

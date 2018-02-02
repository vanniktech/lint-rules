package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2SubscribeMissingOnErrorDetectorTest {
  @Test fun callingObservableSubscribe() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |
          |class Example {
          |  public void foo() {
          |    Observable<Object> o = null;
          |    o.subscribe();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:8: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    o.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingObservableSubscribeOnSuccess() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Observable<Object> o = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    o.subscribe(c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:10: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    o.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingObservableSubscribeOnSuccessWithError() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Observable<Object> o = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    o.subscribe(c, c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expectClean()
  }

  @Test fun callingFlowableSubscribe() {
    lint()
        .files(reactiveStreams(), rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Flowable;
          |
          |class Example {
          |  public void foo() {
          |    Flowable<Object> f = null;
          |    f.subscribe();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:8: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    f.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingFlowableSubscribeOnSuccess() {
    lint()
        .files(reactiveStreams(), rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Flowable;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Flowable<Object> f = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    f.subscribe(c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:10: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    f.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingFlowableSubscribeOnSuccessWithError() {
    lint()
        .files(reactiveStreams(), rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Flowable;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Flowable<Object> f = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    f.subscribe(c, c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expectClean()
  }

  @Test fun callingSingleSubscribe() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Single;
          |
          |class Example {
          |  public void foo() {
          |    Single<Object> s = null;
          |    s.subscribe();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:8: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    s.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingSingleSubscribeOnSuccess() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Single;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Single<Object> s = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    s.subscribe(c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:10: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    s.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingSingleSubscribeOnSuccessWithError() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Single;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Single<Object> s = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    s.subscribe(c, c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expectClean()
  }

  @Test fun callingCompletableSubscribe() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Completable;
          |
          |class Example {
          |  public void foo() {
          |    Completable cp = null;
          |    cp.subscribe();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:8: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    cp.subscribe();
          |       ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingCompletableSubscribeOnSuccess() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Completable;
          |import io.reactivex.functions.Action;
          |
          |class Example {
          |  public void foo() {
          |    Completable cp = null;
          |    Action a = new Action() { @Override public void run() { } };
          |    cp.subscribe(a);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:10: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    cp.subscribe(a);
          |       ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingCompletableSubscribeOnSuccessWithError() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Completable;
          |import io.reactivex.functions.Action;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Completable cp = null;
          |    Action a = new Action() { @Override public void run() { } };
          |    Consumer<Throwable> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    cp.subscribe(a, c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expectClean()
  }

  @Test fun callingMaybeSubscribe() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Maybe;
          |
          |class Example {
          |  public void foo() {
          |    Maybe<Object> m = null;
          |    m.subscribe();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:8: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    m.subscribe();
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingMaybeSubscribeOnSuccess() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Maybe;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Maybe<Object> m = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    m.subscribe(c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expect("""
          |src/foo/Example.java:10: Error: Using a version of subscribe() without an error Consumer. [RxJava2SubscribeMissingOnError]
          |    m.subscribe(c);
          |      ~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun callingMaybeSubscribeOnSuccessWithError() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Maybe;
          |import io.reactivex.functions.Consumer;
          |
          |class Example {
          |  public void foo() {
          |    Maybe<Object> m = null;
          |    Consumer<Object> c = new Consumer() { @Override public void accept(Object o) throws Exception { } };
          |    m.subscribe(c, c);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_SUBSCRIBE_MISSING_ON_ERROR)
        .run()
        .expectClean()
  }
}

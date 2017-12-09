package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.rxjava2.RxJava2MethodCheckReturnValueDetector.ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE
import org.junit.Test

class RxJava2MethodCheckReturnValueDetectorTest {
  @Test fun methodNoReturnStatement() {
    lint()
      .allowCompilationErrors()
      .files(stubSuppressLint, java("""
          |package foo;
          |class Example {
          |  public void foo() {
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningObservableMissingCheckReturnValueSuppressed() {
    lint()
      .allowCompilationErrors()
      .files(stubSuppressLint, stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |import android.annotation.SuppressLint;
          |class Example {
          |  @SuppressLint("MethodMissingCheckReturnValue") public Observable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningObservableMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubObservable, java("""
          |package foo;
          |import io.reactivex.Observable;
          |class Example {
          |  public Observable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public Observable<Object> foo() {
          |                            ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningObservableHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubObservable, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.Observable;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public Observable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningFlowableMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubFlowable, java("""
          |package foo;
          |import io.reactivex.Flowable;
          |class Example {
          |  public Flowable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public Flowable<Object> foo() {
          |                          ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningFlowableHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubFlowable, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.Flowable;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public Flowable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningSingleMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubSingle, java("""
          |package foo;
          |import io.reactivex.Single;
          |class Example {
          |  public Single<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public Single<Object> foo() {
          |                        ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningSingleHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubConsumer, stubSingle, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.Single;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public Single<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningMaybeMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubMaybe, java("""
          |package foo;
          |import io.reactivex.Maybe;
          |class Example {
          |  public Maybe<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public Maybe<Object> foo() {
          |                       ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningMaybeHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubMaybe, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.Maybe;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public Maybe<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningCompletableMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubCompletable, java("""
          |package foo;
          |import io.reactivex.Completable;
          |class Example {
          |  public Completable foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public Completable foo() {
          |                     ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningCompletableHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubCompletable, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.Completable;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public Completable foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningDisposableMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.Disposable;
          |class Example {
          |  public Disposable foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public Disposable foo() {
          |                    ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningDisposableHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubDisposable, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.disposables.Disposable;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public Disposable foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningTestObserverMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubTestObserver, java("""
          |package foo;
          |import io.reactivex.observers.TestObserver;
          |class Example {
          |  public TestObserver foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public TestObserver foo() {
          |                      ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningTestObserverHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubTestObserver, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.observers.TestObserver;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public TestObserver foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodReturningTestSubscriberMissingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubTestSubscriber, java("""
          |package foo;
          |import io.reactivex.subscribers.TestSubscriber;
          |class Example {
          |  public TestSubscriber foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect("""
          |src/foo/Example.java:4: Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]
          |  public TestSubscriber foo() {
          |                        ~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun methodReturningTestSubscriberHavingCheckReturnValue() {
    lint()
      .allowCompilationErrors()
      .files(stubTestSubscriber, stubCheckReturnValue, java("""
          |package foo;
          |import io.reactivex.subscribers.TestSubscriber;
          |import io.reactivex.annotations.CheckReturnValue;
          |class Example {
          |  @CheckReturnValue public TestSubscriber foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }
}

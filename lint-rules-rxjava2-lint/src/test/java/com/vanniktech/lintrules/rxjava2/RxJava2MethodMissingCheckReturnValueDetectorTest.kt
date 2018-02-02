package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2MethodMissingCheckReturnValueDetectorTest {
  @Test fun methodNoReturnStatement() {
    lint()
        .files(java("""
          |package foo;
          |
          |class Example {
          |  public void foo() {
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expectClean()
  }

  @Test fun methodReturningObservableMissingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |
          |class Example {
          |  private Observable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  private Observable<Object> foo() {
          |                             ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   private Observable<Object> foo() {
          |+   io.reactivex.annotations.CheckReturnValue private Observable<Object> foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningObservableHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Observable;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Flowable;
          |
          |class Example {
          |  protected Flowable<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  protected Flowable<Object> foo() {
          |                             ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   protected Flowable<Object> foo() {
          |+   io.reactivex.annotations.CheckReturnValue protected Flowable<Object> foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningFlowableHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Flowable;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Single;
          |
          |class Example {
          |  public Single<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  public Single<Object> foo() {
          |                        ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   public Single<Object> foo() {
          |+   io.reactivex.annotations.CheckReturnValue public Single<Object> foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningSingleHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Maybe;
          |
          |class Example {
          |  public Maybe<Object> foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  public Maybe<Object> foo() {
          |                       ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   public Maybe<Object> foo() {
          |+   io.reactivex.annotations.CheckReturnValue public Maybe<Object> foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningMaybeHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Maybe;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Completable;
          |
          |class Example {
          |  public Completable foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  public Completable foo() {
          |                     ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   public Completable foo() {
          |+   io.reactivex.annotations.CheckReturnValue public Completable foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningCompletableHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.Completable;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.Disposable;
          |
          |class Example {
          |  public Disposable foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  public Disposable foo() {
          |                    ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   public Disposable foo() {
          |+   io.reactivex.annotations.CheckReturnValue public Disposable foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningDisposableHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.Disposable;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.observers.TestObserver;
          |
          |class Example {
          |  public TestObserver foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  public TestObserver foo() {
          |                      ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   public TestObserver foo() {
          |+   io.reactivex.annotations.CheckReturnValue public TestObserver foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningTestObserverHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.observers.TestObserver;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.subscribers.TestSubscriber;
          |
          |class Example {
          |  public TestSubscriber foo() {
          |    return null;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation. [RxJava2MethodMissingCheckReturnValue]
          |  public TestSubscriber foo() {
          |                        ~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/Example.java line 5: Add @CheckReturnValue:
          |@@ -6 +6
          |-   public TestSubscriber foo() {
          |+   io.reactivex.annotations.CheckReturnValue public TestSubscriber foo() {
          |""".trimMargin())
  }

  @Test fun methodReturningTestSubscriberHavingCheckReturnValue() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.subscribers.TestSubscriber;
          |import io.reactivex.annotations.CheckReturnValue;
          |
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

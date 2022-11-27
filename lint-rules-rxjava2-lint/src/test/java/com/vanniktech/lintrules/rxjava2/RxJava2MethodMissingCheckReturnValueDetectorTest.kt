@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.rxjava2.RxJava2MethodMissingCheckReturnValueDetector.CheckReturnValueVisitor.Companion.IGNORE_MODIFIERS_PROP
import org.junit.Before
import org.junit.Test

class RxJava2MethodMissingCheckReturnValueDetectorTest {
  @Before fun tearDown() {
    System.clearProperty(IGNORE_MODIFIERS_PROP)
  }

  @Test fun methodNoReturnStatement() {
    lint()
      .files(
        java(
          """
          package foo;

          class Example {
            public void foo() {
            }
          }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun methodMissingCheckReturnValue() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.Observable;
          import io.reactivex.Flowable;
          import io.reactivex.Single;
          import io.reactivex.Maybe;
          import io.reactivex.Completable;
          import io.reactivex.disposables.Disposable;
          import io.reactivex.disposables.CompositeDisposable;
          import io.reactivex.observers.TestObserver;
          import io.reactivex.subscribers.TestSubscriber;
          import io.reactivex.Scheduler;

          class Example {
            private Observable<Object> observable() {
              return null;
            }
            
            protected Flowable<Object> flowable() {
              return null;
            }
            
            protected Single<Object> single() {
              return null;
            }
            
            protected Maybe<Object> single() {
              return null;
            }
            
            public Completable completable() {
              return null;
            }
            
            public Disposable disposable() {
              return null;
            }
            
            public CompositeDisposable compositeDisposable() {
              return null;
            }
            
            public TestObserver testObserver() {
              return null;
            }
            
            public TestSubscriber testSubscriber() {
              return null;
            }
            
            public Scheduler scheduler() {
              return null;
            }
            
            private Observable<List<Object>> observableList() {
              return null;
            }
          }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expect(
        """
        src/foo/Example.java:15: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          private Observable<Object> observable() {
                                     ~~~~~~~~~~
        src/foo/Example.java:19: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          protected Flowable<Object> flowable() {
                                     ~~~~~~~~
        src/foo/Example.java:23: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          protected Single<Object> single() {
                                   ~~~~~~
        src/foo/Example.java:27: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          protected Maybe<Object> single() {
                                  ~~~~~~
        src/foo/Example.java:31: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          public Completable completable() {
                             ~~~~~~~~~~~
        src/foo/Example.java:35: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          public Disposable disposable() {
                            ~~~~~~~~~~
        src/foo/Example.java:39: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          public CompositeDisposable compositeDisposable() {
                                     ~~~~~~~~~~~~~~~~~~~
        src/foo/Example.java:43: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          public TestObserver testObserver() {
                              ~~~~~~~~~~~~
        src/foo/Example.java:47: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          public TestSubscriber testSubscriber() {
                                ~~~~~~~~~~~~~~
        src/foo/Example.java:51: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          public Scheduler scheduler() {
                           ~~~~~~~~~
        src/foo/Example.java:55: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
          private Observable<List<Object>> observableList() {
                                           ~~~~~~~~~~~~~~
        0 errors, 11 warnings
        """.trimIndent(),
      )
      .expectFixDiffs(
        """
        Autofix for src/foo/Example.java line 15: Add @CheckReturnValue:
        @@ -15 +15
        -   private Observable<Object> observable() {
        +   @io.reactivex.annotations.CheckReturnValue private Observable<Object> observable() {
        Autofix for src/foo/Example.java line 19: Add @CheckReturnValue:
        @@ -19 +19
        -   protected Flowable<Object> flowable() {
        +   @io.reactivex.annotations.CheckReturnValue protected Flowable<Object> flowable() {
        Autofix for src/foo/Example.java line 23: Add @CheckReturnValue:
        @@ -23 +23
        -   protected Single<Object> single() {
        +   @io.reactivex.annotations.CheckReturnValue protected Single<Object> single() {
        Autofix for src/foo/Example.java line 27: Add @CheckReturnValue:
        @@ -27 +27
        -   protected Maybe<Object> single() {
        +   @io.reactivex.annotations.CheckReturnValue protected Maybe<Object> single() {
        Autofix for src/foo/Example.java line 31: Add @CheckReturnValue:
        @@ -31 +31
        -   public Completable completable() {
        +   @io.reactivex.annotations.CheckReturnValue public Completable completable() {
        Autofix for src/foo/Example.java line 35: Add @CheckReturnValue:
        @@ -35 +35
        -   public Disposable disposable() {
        +   @io.reactivex.annotations.CheckReturnValue public Disposable disposable() {
        Autofix for src/foo/Example.java line 39: Add @CheckReturnValue:
        @@ -39 +39
        -   public CompositeDisposable compositeDisposable() {
        +   @io.reactivex.annotations.CheckReturnValue public CompositeDisposable compositeDisposable() {
        Autofix for src/foo/Example.java line 43: Add @CheckReturnValue:
        @@ -43 +43
        -   public TestObserver testObserver() {
        +   @io.reactivex.annotations.CheckReturnValue public TestObserver testObserver() {
        Autofix for src/foo/Example.java line 47: Add @CheckReturnValue:
        @@ -47 +47
        -   public TestSubscriber testSubscriber() {
        +   @io.reactivex.annotations.CheckReturnValue public TestSubscriber testSubscriber() {
        Autofix for src/foo/Example.java line 51: Add @CheckReturnValue:
        @@ -51 +51
        -   public Scheduler scheduler() {
        +   @io.reactivex.annotations.CheckReturnValue public Scheduler scheduler() {
        Autofix for src/foo/Example.java line 55: Add @CheckReturnValue:
        @@ -55 +55
        -   private Observable<List<Object>> observableList() {
        +   @io.reactivex.annotations.CheckReturnValue private Observable<List<Object>> observableList() {
        """.trimIndent(),
      )
  }

  @Test fun methodWithCheckReturnValue() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.Observable;
          import io.reactivex.Flowable;
          import io.reactivex.Single;
          import io.reactivex.Maybe;
          import io.reactivex.Completable;
          import io.reactivex.disposables.Disposable;
          import io.reactivex.disposables.CompositeDisposable;
          import io.reactivex.observers.TestObserver;
          import io.reactivex.subscribers.TestSubscriber;
          import io.reactivex.Scheduler;
          import io.reactivex.annotations.CheckReturnValue;

          class Example {
          @CheckReturnValue private Observable<Object> observable() {
              return null;
            }
            
            @CheckReturnValue protected Flowable<Object> flowable() {
              return null;
            }
            
            @CheckReturnValue protected Single<Object> single() {
              return null;
            }
            
            @CheckReturnValue protected Maybe<Object> single() {
              return null;
            }
            
            @CheckReturnValue public Completable completable() {
              return null;
            }
            
            @CheckReturnValue public Disposable disposable() {
              return null;
            }
            
            @CheckReturnValue public CompositeDisposable compositeDisposable() {
              return null;
            }
            
            @CheckReturnValue public TestObserver testObserver() {
              return null;
            }
            
            @CheckReturnValue public TestSubscriber testSubscriber() {
              return null;
            }
            
            @CheckReturnValue public Scheduler scheduler() {
              return null;
            }
            
            @CheckReturnValue private Observable<List<Object>> observableList() {
              return null;
            }
          }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun lateInitKotlinSchedulerVariableIsIgnored() {
    lint()
      .files(
        rxJava2(),
        kt(
          """
          package foo

          import io.reactivex.Scheduler

          class Example {
            lateinit var scheduler: Scheduler
          }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun privateMethodReturningObservableMissingCheckReturnValue() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private")

    lint()
      .files(
        rxJava2(),
        java(
          """
        package foo;

        import io.reactivex.Observable;

        class Example {
          private Observable<Object> foo() {
            return null;
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun multipleIgnoredModifiers() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private,protected")

    lint()
      .files(
        rxJava2(),
        java(
          """
        package foo;

        import io.reactivex.Observable;

        class Example {
          private Observable<Object> foo() {
            return null;
          }

          protected Observable<Object> bar() {
            return null;
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }

  @Test fun protectedMethodReturningObservableMissingCheckReturnValue() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private")

    lint()
      .files(
        rxJava2(),
        java(
          """
        package foo;

        import io.reactivex.Observable;

        class Example {
          protected Observable<Object> foo() {
            return null;
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run().expect(
        """
        |src/foo/Example.java:6: Warning: Method should have @CheckReturnValue annotation [RxJava2MethodMissingCheckReturnValue]
        |  protected Observable<Object> foo() {
        |                               ~~~
        |0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
        |Fix for src/foo/Example.java line 6: Add @CheckReturnValue:
        |@@ -6 +6
        |-   protected Observable<Object> foo() {
        |+   @io.reactivex.annotations.CheckReturnValue protected Observable<Object> foo() {
        """.trimMargin(),
      )
  }

  @Test fun kotlinPrivateMethodReturningTestSubscriberMissingCheckReturnValue() {
    System.setProperty(IGNORE_MODIFIERS_PROP, "private")

    lint()
      .files(
        rxJava2(),
        kt(
          """
        package foo

        import io.reactivex.Observable

        class Example {
          private fun foo(): Observable<Object>? {
            return null
          }
        }
          """,
        ).indented(),
      )
      .issues(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE)
      .run()
      .expectClean()
  }
}

@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2SchedulersFactoryCallDetectorTest {
  @Test fun ioCallInsideDaggerProvidesMethod() {
    lint()
      .files(
        dagger2(), rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.Scheduler;
          import io.reactivex.schedulers.Schedulers;
          import dagger.Provides;

          class Example {
            @Provides Scheduler provideSchedulerIo() {
              return Schedulers.io();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expectClean()
  }

  @Test fun ioCallWithSchedulerSupport() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.Scheduler;
          import io.reactivex.schedulers.Schedulers;
          import io.reactivex.annotations.SchedulerSupport;

          class Example {
            @SchedulerSupport("io") Scheduler provideSchedulerIo() {
              return Schedulers.io();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expectClean()
  }

  @Test fun ioCallInsideCheckReturnValueMethod() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.annotations.CheckReturnValue;
          import io.reactivex.Scheduler;
          import io.reactivex.schedulers.Schedulers;

          class Example {
            @CheckReturnValue Scheduler provideSchedulerIo() {
              return Schedulers.io();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.java:9: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    return Schedulers.io();
          |                      ~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun ioCall() {
    lint()
      .files(
        rxJava2(),
        kt(
          """
          package foo

          import io.reactivex.schedulers.Schedulers

          class Example {
            fun test() {
              Schedulers.io()
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.kt:7: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    Schedulers.io()
          |               ~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun computationCall() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.schedulers.Schedulers;

          class Example {
            void test() {
              Schedulers.computation();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.java:7: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    Schedulers.computation();
          |               ~~~~~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun newThreadCall() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.schedulers.Schedulers;

          class Example {
            void test() {
              Schedulers.newThread();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.java:7: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    Schedulers.newThread();
          |               ~~~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun singleCall() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.schedulers.Schedulers;

          class Example {
            void test() {
              Schedulers.single();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.java:7: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    Schedulers.single();
          |               ~~~~~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun fromCall() {
    lint()
      .files(
        rxJava2(),
        java(
          """
          package foo;

          import io.reactivex.schedulers.Schedulers;

          class Example {
            void test() {
              Schedulers.from(null);
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.java:7: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    Schedulers.from(null);
          |               ~~~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun mainThreadCall() {
    lint()
      .files(
        rxJava2(), rxAndroid2(),
        java(
          """
          package foo;

          import io.reactivex.android.schedulers.AndroidSchedulers;

          class Example {
            void test() {
              AndroidSchedulers.mainThread();
            }
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expect(
        """
          |src/foo/Example.java:7: Warning: Inject this Scheduler instead of calling it directly [RxJava2SchedulersFactoryCall]
          |    AndroidSchedulers.mainThread();
          |                      ~~~~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun fromWithinLazyDoesNotCrash() {
    lint()
      .files(
        rxJava2(),
        kt(
          """
          package foo

          class Example {
            private val something by lazy { from() }

            fun from() = 5
          }
          """
        ).indented()
      )
      .issues(ISSUE_RAW_SCHEDULER_CALL)
      .run()
      .expectClean()
  }
}

package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2MissingCompositeDisposableClearDetectorTest {
  @Test fun noCompositeDisposable() {
    lint()
        .files(rxJava2(), java("""
          package foo;

          class Example {
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun compositeDisposableMissingClear() {
    lint()
        .files(rxJava2(), java("""
          package foo;

          import io.reactivex.disposables.CompositeDisposable;

          class Example {
            CompositeDisposable cd;
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.java:6: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd;
          |  ~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun disposableMissingDisposeIsIgnored() {
    lint()
        .files(rxJava2(), java("""
          package foo;

          import io.reactivex.disposables.Disposable;

          class Example {
            Disposable disposable;
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun multipleCompositeDisposableMissingClear() {
    lint()
        .files(rxJava2(), java("""
          package foo;

          import io.reactivex.disposables.CompositeDisposable;

          class Example {
            CompositeDisposable cd1;
            CompositeDisposable cd2;
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.java:6: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd1;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |src/foo/Example.java:7: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd2;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |2 errors, 0 warnings
          |""".trimMargin())
  }

  @Test fun multipleCompositeDisposableMissingClearInKotlin() {
    lint()
        .files(rxJava2(), kt("""
          package foo

          import io.reactivex.disposables.CompositeDisposable

          class Example {
            val cd1: CompositeDisposable
            val cd2: CompositeDisposable
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.kt:6: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  val cd1: CompositeDisposable
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |src/foo/Example.kt:7: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  val cd2: CompositeDisposable
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |2 errors, 0 warnings
          |""".trimMargin())
  }

  @Test fun compositeDisposableHavingClear() {
    lint()
        .files(rxJava2(), java("""
          package foo;

          import io.reactivex.disposables.CompositeDisposable;

          class Example {
            CompositeDisposable cd;
            public void foo() {
             cd.clear();
            }
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun compositeDisposableHavingClearInKotlin() {
    lint()
        .files(rxJava2(), kt("""
          package foo

          import io.reactivex.disposables.CompositeDisposable

          class Example {
            val cd = CompositeDisposable()
            fun foo() {
             cd.clear()
            }
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun missingOneCompositeDisposableClear() {
    lint()
        .files(rxJava2(), java("""
          package foo;

          import io.reactivex.disposables.CompositeDisposable;

          class Example {
            CompositeDisposable cd1;
            CompositeDisposable cd2;
            public void foo() {
             cd1.clear();
            }
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.java:7: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd2;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun missingOneCompositeDisposableClearInKotlin() {
    lint()
        .files(rxJava2(), kt("""
          package foo

          import io.reactivex.disposables.CompositeDisposable

          class Example {
            val cd1 = CompositeDisposable()
            val cd2 = CompositeDisposable()
            fun foo() {
             cd1.clear()
            }
          }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.kt:7: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  val cd2 = CompositeDisposable()
          |  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun clearInIfStatement() {
    lint()
        .files(rxJava2(), java("""
        package foo;

        import io.reactivex.disposables.CompositeDisposable;

        class Example {
          CompositeDisposable cd;
          CompositeDisposable cd2;

          public void foo() {
            if (true) {
              cd.clear();
            }
          }

          public void foo2(){
            if (false) {

            } else {
              cd2.clear();
            }
          }
        }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun clearInIfStatementInKotlin() {
    lint()
        .files(rxJava2(), kt("""
        package foo

        import io.reactivex.disposables.CompositeDisposable

        class Example {
          val cd: CompositeDisposable
          val cd2: CompositeDisposable

          fun foo() {
            if (true) {
              cd.clear()
            }
          }

          fun foo2(){
            if (false) {

            } else {
              cd2.clear()
            }
          }
        }""").indented())
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }
}

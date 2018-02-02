package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.rxjava2.RxJava2MissingCompositeDisposableClearDetector.ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR
import org.junit.Test

class RxJava2MissingCompositeDisposableClearDetectorTest {
  @Test fun noCompositeDisposable() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.CompositeDisposable;
          |
          |class Example {
          |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun compositeDisposableMissingClear() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.CompositeDisposable;
          |
          |class Example {
          |  CompositeDisposable cd;
          |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.java:6: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd;
          |  ~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun disposableMissingClearIsIgnored() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.Disposable;
          |
          |class Example {
          |  Disposable disposable;
          |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun multipleCompositeDisposableMissingClear() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.CompositeDisposable;
          |
          |class Example {
          |  CompositeDisposable cd1;
          |  CompositeDisposable cd2;
          |  CompositeDisposable cd3;
          |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.java:6: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd1;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |src/foo/Example.java:7: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd2;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |src/foo/Example.java:8: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd3;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |3 errors, 0 warnings
          |""".trimMargin())
  }

  @Test fun compositeDisposableHavingClear() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.CompositeDisposable;
          |
          |class Example {
          |  CompositeDisposable cd;
          |  public void foo() {
          |   cd.clear();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }

  @Test fun multipleCompositeDisposableClear() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |
          |import io.reactivex.disposables.CompositeDisposable;
          |
          |class Example {
          |  CompositeDisposable cd1;
          |  CompositeDisposable cd2;
          |  CompositeDisposable cd3;
          |  public void foo() {
          |   cd1.clear();
          |  }
          |  public void bar() {
          |   cd2.clear();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expect("""
          |src/foo/Example.java:8: Error: clear() is not called. [RxJava2MissingCompositeDisposableClear]
          |  CompositeDisposable cd3;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun clearInIfStatement() {
    lint()
        .files(rxJava2(), java("""
        |package foo;
        |
        |import io.reactivex.disposables.CompositeDisposable;
        |
        |class Example {
        |  CompositeDisposable cd;
        |  CompositeDisposable cd2;
        |
        |  public void foo() {
        |    if (true) {
        |      cd.clear();
        |    }
        |  }
        |
        |  public void foo2(){
        |    if (false) {
        |
        |    } else {
        |      cd2.clear();
        |    }
        |  }
        |}""".trimMargin()))
        .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
        .run()
        .expectClean()
  }
}

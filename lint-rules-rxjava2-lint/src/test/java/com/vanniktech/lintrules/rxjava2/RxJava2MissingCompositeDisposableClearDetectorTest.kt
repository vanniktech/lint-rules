package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.rxjava2.RxJava2MissingCompositeDisposableClearDetector.ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR
import org.junit.Test

class RxJava2MissingCompositeDisposableClearDetectorTest {
  @Test fun noCompositeDisposable() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |}""".trimMargin()))
      .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
      .run()
      .expectClean()
  }

  @Test fun compositeDisposableMissingClearSuppressed() {
    lint()
      .allowCompilationErrors()
      .files(stubSuppressLint, stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |import android.annotation.SuppressLint;
          |class Example {
          |  @SuppressLint("MissingCompositeDisposableClear") CompositeDisposable cd;
          |}""".trimMargin()))
      .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
      .run()
      .expectClean()
  }

  @Test fun compositeDisposableMissingClear() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  CompositeDisposable cd;
          |}""".trimMargin()))
      .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
      .run()
      .expect("""
          |src/foo/Example.java:4: Error: clear() is not called. [MissingCompositeDisposableClear]
          |  CompositeDisposable cd;
          |  ~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }

  @Test fun disposableMissingClearIsIgnored() {
    lint()
      .allowCompilationErrors()
      .files(stubDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.Disposable;
          |class Example {
          |  Disposable disposable;
          |}""".trimMargin()))
      .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
      .run()
      .expectClean()
  }

  @Test fun multipleCompositeDisposableMissingClear() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  CompositeDisposable cd1;
          |  CompositeDisposable cd2;
          |  CompositeDisposable cd3;
          |}""".trimMargin()))
      .issues(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR)
      .run()
      .expect("""
          |src/foo/Example.java:4: Error: clear() is not called. [MissingCompositeDisposableClear]
          |  CompositeDisposable cd1;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |src/foo/Example.java:5: Error: clear() is not called. [MissingCompositeDisposableClear]
          |  CompositeDisposable cd2;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |src/foo/Example.java:6: Error: clear() is not called. [MissingCompositeDisposableClear]
          |  CompositeDisposable cd3;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |3 errors, 0 warnings
          |""".trimMargin())
  }

  @Test fun compositeDisposableHavingClear() {
    lint()
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
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
      .allowCompilationErrors()
      .files(stubCompositeDisposable, java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
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
          |src/foo/Example.java:6: Error: clear() is not called. [MissingCompositeDisposableClear]
          |  CompositeDisposable cd3;
          |  ~~~~~~~~~~~~~~~~~~~~~~~~
          |1 errors, 0 warnings""".trimMargin())
  }
}

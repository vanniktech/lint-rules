package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2DisposableAddAllCallDetectorTest {
  @Test fun callingCompositeDisposableAddAll() {
    lint()
        .files(rxJava2(), java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    cd.addAll();
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_DISPOSABLE_ADD_ALL_CALL)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Calling addAll instead of add separately. [RxJava2DisposableAddAllCall]
          |    cd.addAll();
          |       ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }
}

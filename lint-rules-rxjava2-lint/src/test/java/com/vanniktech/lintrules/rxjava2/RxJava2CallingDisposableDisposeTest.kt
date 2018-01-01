package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RxJava2CallingDisposableDisposeTest {
  @Test fun callingCompositeDisposableDispose() {
    lint()
      .files(rxJava2(), java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    cd.dispose();
          |  }
          |}""".trimMargin()))
      .issues(CALLING_COMPOSITE_DISPOSABLE_DISPOSE)
      .run()
      .expect("""
          |src/foo/Example.java:6: Warning: Using dispose() instead of clear() [CallingCompositeDisposableDispose]
          |    cd.dispose();
          |       ~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingCompositeDisposableDisposeSuppressed() {
    lint()
      .files(rxJava2(), java("""
          |package foo;
          |import io.reactivex.disposables.CompositeDisposable;
          |class Example {
          |  public void foo() {
          |    CompositeDisposable cd = null;
          |    //noinspection AndroidLintCallingCompositeDisposableDispose
          |    cd.dispose();
          |  }
          |}""".trimMargin()))
      .issues(CALLING_COMPOSITE_DISPOSABLE_DISPOSE)
      .run()
      .expectClean()
  }
}

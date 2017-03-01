package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;

public abstract class RxJavaLintDetectorTest extends LintDetectorTest {
  static final String NO_WARNINGS = "No warnings.";

  final TestFile stubCompositeDisposable = java(""
      + "package io.reactivex.disposables;\n"
      + "public class CompositeDisposable {\n"
      + "  public void dispose() {}\n"
      + "  public void addAll() {}\n"
      + "  public void clear() {}\n"
      + "}");

  final TestFile stubConsumer = java(""
      + "package io.reactivex.functions;\n"
      + "public interface Consumer<T> {\n"
      + "  void accept(T t) throws Exception;\n"
      + "}");

  final TestFile stubDisposable = java(""
      + "package io.reactivex.disposables;\n"
      + "public interface Disposable<T> {\n"
      + "  boolean isDisposed();\n"
      + "  void dispose();\n"
      + "}");

  final TestFile stubAction = java(""
      + "package io.reactivex.functions;\n"
      + "public interface Action {\n"
      + "  void run() throws Exception;\n"
      + "}");

  final TestFile stubCheckReturnValue = java(""
      + "package io.reactivex.annotations;\n"
      + "public @interface CheckReturnValue {\n"
      + "}");

  final TestFile stubObservable = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Observable<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onNext) {}\n"
      + "  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}\n"
      + "}");

  final TestFile stubFlowable = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Flowable<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onNext) {}\n"
      + "  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}\n"
      + "}");

  final TestFile stubSingle = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Single<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onSuccess) {}\n"
      + "  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}\n"
      + "}");

  final TestFile stubCompletable = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Action;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Completable {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Action onComplete) {}\n"
      + "  public void subscribe(Action onComplete, Consumer<Throwable> onError) {}\n"
      + "}");

  final TestFile stubMaybe = java(""
      + "package io.reactivex;\n"
      + "import io.reactivex.functions.Consumer;\n"
      + "public class Maybe<T> {\n"
      + "  public void subscribe() {}\n"
      + "  public void subscribe(Consumer<T> onSuccess) {}\n"
      + "  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}\n"
      + "}");

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

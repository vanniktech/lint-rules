package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java

val stubCompositeDisposable = java("""
    |package io.reactivex.disposables;
    |class CompositeDisposable {
    |  public void dispose() {}
    |  public void addAll() {}
    |  public void clear() {}
    |}""".trimMargin())

val stubSuppressLint = java("""
    |package io.reactivex.functions;
    |public @interface SuppressLint {
    |  String[] value();
    |}""".trimMargin())

val stubConsumer = java("""
    |package io.reactivex.functions;
    |public interface Consumer<T> {
    |  void accept(T t) throws Exception;
    |}""".trimMargin())

val stubDisposable = java("""
    |package io.reactivex.disposables;
    |public interface Disposable<T> {
    |  boolean isDisposed();
    |  void dispose();
    |}""".trimMargin())

val stubTestObserver = java("""
    |package io.reactivex.observers;
    |public interface TestObserver<T> {
    |}""".trimMargin())

val stubTestSubscriber = java("""
    |package io.reactivex.subscribers;
    |public interface TestSubscriber<T> {
    |}""".trimMargin())

val stubAction = java("""
    |package io.reactivex.functions;
    |public interface Action {
    |  void run() throws Exception;
    |}""".trimMargin())

val stubCheckReturnValue = java("""
    |package io.reactivex.annotations;
    |public @interface CheckReturnValue {
    |}""".trimMargin())

val stubObservable = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |class Observable<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onNext) {}
    |  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubFlowable = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |class Flowable<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onNext) {}
    |  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubSingle = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |class Single<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onSuccess) {}
    |  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubCompletable = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Action;
    |import io.reactivex.functions.Consumer;
    |class Completable {
    |  public void subscribe() {}
    |  public void subscribe(Action onComplete) {}
    |  public void subscribe(Action onComplete, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubMaybe = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |class Maybe<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onSuccess) {}
    |  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}
    |}""".trimMargin())

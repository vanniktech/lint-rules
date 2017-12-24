package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.java

val stubCompositeDisposable = java("""
    |package io.reactivex.disposables;
    |public class CompositeDisposable {
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
    |import io.reactivex.annotations.SchedulerSupport;
    |import io.reactivex.annotations.CheckReturnValue;
    |
    |public class Observable<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onNext) {}
    |  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}
    |
    |  @CheckReturnValue
    |  @SchedulerSupport(SchedulerSupport.NONE)
    |  public static <T> Observable<T> just(T item) {
    |      return null;
    |  }
    |
    |  @CheckReturnValue
    |  @SchedulerSupport(SchedulerSupport.COMPUTATION)
    |  public static Observable<Long> interval(long period, TimeUnit unit) {
    |      return null;
    |  }
    |
    |  @CheckReturnValue
    |  @SchedulerSupport(SchedulerSupport.CUSTOM)
    |  public static Observable<Long> interval(long period, TimeUnit unit, Scheduler scheduler) {
    |      return null;
    |  }
    |}""".trimMargin())

val stubFlowable = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |public class Flowable<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onNext) {}
    |  public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubSingle = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |public class Single<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onSuccess) {}
    |  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubCompletable = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Action;
    |import io.reactivex.functions.Consumer;
    |public class Completable {
    |  public void subscribe() {}
    |  public void subscribe(Action onComplete) {}
    |  public void subscribe(Action onComplete, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubMaybe = java("""
    |package io.reactivex;
    |import io.reactivex.functions.Consumer;
    |public class Maybe<T> {
    |  public void subscribe() {}
    |  public void subscribe(Consumer<T> onSuccess) {}
    |  public void subscribe(Consumer<T> onSuccess, Consumer<Throwable> onError) {}
    |}""".trimMargin())

val stubScheduler = java("""
    |package io.reactivex.schedulers;
    |public abstract class Scheduler {
    |}""".trimMargin())

val stubSchedulers = java("""
    |package io.reactivex.schedulers;
    |
    |import java.util.concurrent.Executor;
    |
    |public final class Schedulers {
    |  public static Scheduler computation() {
    |    return null;
    |  }
    |  public static Scheduler io() {
    |    return null;
    |  }
    |  public static Scheduler newThread() {
    |    return null;
    |  }
    |  public static Scheduler single() {
    |    return null;
    |  }
    |  public static Scheduler from(Executor executor) {
    |    return null;
    |  }
    |}""".trimMargin())

val stubAndroidSchedulers = java("""
    |package io.reactivex.android.schedulers;
    |
    |import io.reactivex.schedulers.Scheduler;
    |
    |public final class AndroidSchedulers {
    |  public static Scheduler mainThread() {
    |    return null;
    |  }
    |}""".trimMargin())

val stubSchedulerSupport = java("""
    |package io.reactivex.annotations;
    |
    |import io.reactivex.schedulers.Scheduler;
    |
    |public @interface SchedulerSupport {
    |  String NONE = "none";
    |  String CUSTOM = "custom";
    |  String COMPUTATION = "io.reactivex:computation";
    |  String IO = "io.reactivex:io";
    |  String NEW_THREAD = "io.reactivex:new-thread";
    |  String TRAMPOLINE = "io.reactivex:trampoline";
    |  String SINGLE = "io.reactivex:single";
    |
    |  String value();
    |}""".trimMargin())

val stubProvides = java("""
    |package dagger;
    |
    |public @interface Provides {
    |}""".trimMargin())

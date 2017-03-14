Lint-Rules
==========

[![Build Status](https://travis-ci.org/vanniktech/lint-rules.svg?branch=master)](https://travis-ci.org/vanniktech/lint-rules?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

A set of very opionated lint rules.


## Android Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-android:0.3.0'
```

- **WindowFindViewById** - Marks the usage of `findViewById()` on `Window`.
- **ViewFindViewById** - Marks the usage of `findViewById()` on `View`.
- **DialogFindViewById** - Marks the usage of `findViewById()` on `Dialog`.
- **ActivityFindViewById** - Marks the usage of `findViewById()` on `Activity`.
- **ResourcesGetDrawable** - Marks the usage of the deprecated method `getDrawable()` on `Context`.
- **ResourcesGetColor** - Marks the usage of the deprecated method `getColor()` on `Context`.
- **ResourcesGetColorStateList** - Marks the usage of the deprecated method `getColorStateList()` on `Context`.
- **RawColor** - Marks raw color values in layout xml files.
- **RawDimen** - Marks raw dimen values in layout xml files.
- **WrongViewIdFormat** - Marks Android View Ids that are not in lowerCamelCase format.
- **WrongMenuIdFormat** - Marks Android Menu Ids that are not in lowerCamelCase format.

## RxJava 2 Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-rxjava2:0.3.0'
```

- **SubscribeMissingErrorConsumer** - Marks all usages of `subscribe()` method without an error `Consumer`.
- **CompositeDisposableDispose** - Marks the usage of `dispose()` on `CompositeDisposable`.
- **CompositeDisposableAddAll** - Marks the usage of `addAll()` on `CompositeDisposable`.
- **MissingCompositeDisposableClear** - Marks `CompositeDisposable` fields where `clear()` is not called.
- **MethodMissingCheckReturnValue** - Marks methods that return `Observable`, `Flowable`, `Maybe`, `Single`, `Completable`, `TestObserver`, `TestSubscriber` or `Disposable` that do not have the `@CheckReturnValue` annotation.
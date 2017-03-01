Lint-Rules
==========

[![Build Status](https://travis-ci.org/vanniktech/lint-rules.svg?branch=master)](https://travis-ci.org/vanniktech/lint-rules?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

A set of very opionated lint rules.


## Android Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-android:0.1.0'
```

- **WindowFindViewById** - Marks the usage of `findViewById()` on `Window`.
- **ViewFindViewById** - Marks the usage of `findViewById()` on `View`.
- **DialogFindViewById** - Marks the usage of `findViewById()` on `Dialog`.
- **ActivityFindViewById** - Marks the usage of `findViewById()` on `Activity`.
- **ResourcesGetDrawable** - Marks the usage of the deprecated method `getDrawable()` on `Context`.
- **ResourcesGetColor** - Marks the usage of the deprecated method `getColor()` on `Context`.
- **ResourcesGetColorStateList** - Marks the usage of the deprecated method `getColorStateList()` on `Context`.

## RxJava 2 Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-rxjava2:0.1.0'
```

- **SubscribeMissingErrorConsumer** - Marks all usages of `subscribe()` method without an error `Consumer`.
- **CompositeDisposableDispose** - Marks the usage of `dispose()` on `CompositeDisposable`.
- **CompositeDisposableAddAll** - Marks the usage of `addAll()` on `CompositeDisposable`.
Lint-Rules
==========

[![Build Status](https://travis-ci.org/vanniktech/lint-rules.svg?branch=master)](https://travis-ci.org/vanniktech/lint-rules?branch=master)
[![Codecov](https://codecov.io/github/vanniktech/lint-rules/coverage.svg?branch=master)](https://codecov.io/github/vanniktech/lint-rules?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

A set of very opionated lint rules.

## Android Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-android:0.5.0'
compile 'com.vanniktech:lint-rules-android:0.6.0-SNAPSHOT'
```

- **WindowFindViewById** - Marks the usage of `findViewById()` on `Window`.
- **ViewFindViewById** - Marks the usage of `findViewById()` on `View`.
- **DialogFindViewById** - Marks the usage of `findViewById()` on `Dialog`.
- **ActivityFindViewById** - Marks the usage of `findViewById()` on `Activity`.
- **ResourcesGetDrawable** - Marks the usage of the deprecated method `getDrawable()` on `Context`.
- **ResourcesGetColor** - Marks the usage of the deprecated method `getColor()` on `Context`.
- **ResourcesGetColorStateList** - Marks the usage of the deprecated method `getColorStateList()` on `Context`.
- **RawColor** - Marks raw color values in drawable and layout xml files.
- **RawDimen** - Marks raw dimen values in drawable and layout xml files.
- **WrongViewIdFormat** - Marks Android View Ids that are not in lowerCamelCase format.
- **WrongMenuIdFormat** - Marks Android Menu Ids that are not in lowerCamelCase format.
- **InvalidSingleLineComment** - Marks single line comments that are not sentences.
- **InvalidString** - Marks invalid XML strings that contains for instance line breaks instead of `\n` or trailing whitespace.
- **MatchingMenuId** - Marks Menu ids that do not match to the name of the mMenu file.
- **MatchingViewIdDetector** - Marks View ids that do not match to the name of the View file.
- **ShouldUseStaticImport** - Marks references that should be statically imported instead (e.g. Collections.singletonList, TimeUnit.SECONDS, etc..).
- **SuperfluousMarginDeclarationDetector** - Marks margin declaration where all the values for start, end, top and bototm are the same.
- **SuperfluousPaddingDeclarationDetector** - Marks padding declaration where all the values for start, end, top and bototm are the same.
- **WrongConstraintLayoutUsage** - Marks usages of references to `left`, `right` (e.g. `layout_constraintLeft_toLeftOf`) that do not necessarily support RTL.
- **MissingXmlHeader** - Marks xml files where the xml header is missing.
- **WrongTestMethodName** - Marks test methods that are starting with test.
- **WrongLayoutName** - Marks layout files that are not prefixed with a certain whielisted strings e.g. (activity_, dialog_, bottom_sheet_, divider_, etc.).
- **AnnotationOrderDetector** - Marks annotations that are not in the correct order. e.g. (@Deprecated comes before @Override and so on).

## RxJava 2 Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-rxjava2:0.5.0'
compile 'com.vanniktech:lint-rules-rxjava2:0.6.0-SNAPSHOT
```

- **SubscribeMissingErrorConsumer** - Marks all usages of `subscribe()` method without an error `Consumer`.
- **CompositeDisposableDispose** - Marks the usage of `dispose()` on `CompositeDisposable`.
- **CompositeDisposableAddAll** - Marks the usage of `addAll()` on `CompositeDisposable`.
- **MissingCompositeDisposableClear** - Marks `CompositeDisposable` fields where `clear()` is not called.
- **MethodMissingCheckReturnValue** - Marks methods that return classes from io.reactivex.* like: `Observable`, `Flowable`, `Maybe`, `Single`, `Completable`, `TestObserver`, `TestSubscriber` or `Disposable` that do not have the `@CheckReturnValue` annotation.

# License

Copyright (C) 2017 Vanniktech - Niklas Baudy

Licensed under the Apache License, Version 2.0
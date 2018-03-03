Lint-Rules
==========

[![Build Status](https://travis-ci.org/vanniktech/lint-rules.svg?branch=master)](https://travis-ci.org/vanniktech/lint-rules?branch=master)
[![Codecov](https://codecov.io/github/vanniktech/lint-rules/coverage.svg?branch=master)](https://codecov.io/github/vanniktech/lint-rules?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

A set of very opinionated lint rules.

## Android Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-android:0.6.0'
compile 'com.vanniktech:lint-rules-android:0.7.0-SNAPSHOT'
```

- **ConstraintLayoutToolsEditorAttribute** - The tools:layout_editor xml properties are only used for previewing and won't be used in your APK hence they're unnecessary and just add overhead.
- **DefaultLayoutAttribute** - Flags default layout values that are not needed. One for instance is the textStyle="normal" that can be just removed.
- **InvalidImport** - Flags invalid imports. One example is com.foo.bar.R.drawable. Instead just the generated class R should be imported and not R.drawable. Also you should never import anything that's in an internal package.
- **InvalidSingleLineComment** - Single line comments should always be sentences. They're part of the code and hence they deserve as much detail and respect as code.
- **InvalidString** - A translation string is invalid if it contains new lines instead of the escaped \\n or if it contains trailing whitespace.
- **MatchingMenuId** - When the layout file is named menu_home all of the containing ids should be prefixed with menuHome to avoid ambiguity between different menu files across different menu items.
- **MatchingViewId** - When the layout file is named activity_home all of the containing ids should be prefixed with activityHome to avoid ambiguity between different layout files across different views.
- **MissingXmlHeader** - An xml file should always have the xml header to declare that it is an xml file despite the file ending.
- **NamingPattern** - Sometimes there is more than one reasonable way to convert an English phrase into camel case, such as when acronyms or unusual constructs like "IPv6" or "iOS" are present. XML HTTP request becomes XmlHttpRequest. XMLHTTPRequest would be incorrect.
- **RawColor** - Color value should all be defined as color resources. This has the benefit that you can easily see all of your colors in one file. One benefit is an easier addition to Dark Theme for instance. This check will run on layouts as well as xml drawables.
- **RawDimen** - Dimensions should all be defined as dimension resources. This has the benefit that you can easily see all of your dimensions in one file. One benefit is that when designers change the outline across the entire app you only have to adjust it in one place. This check will run on layouts as well as xml drawables.
- **ResourcesGetColorCall** - Instead of getColor(), ContextCompat or the method with the Theme Overload should be used instead.
- **ResourcesGetColorStateListCall** - Instead of getColorStateList(), ContextCompat or the method with the Theme Overload should be used instead.
- **ResourcesGetDrawableCall** - Instead of getDrawable(), ContextCompat or the method with the Theme Overload should be used instead.
- **ShouldUseStaticImport** - Certain declarations like TimeUnit.SECONDS should be statically imported to increase the readability.
- **SuperfluousMarginDeclaration** - Instead of using start-, end-, bottom- and top margins, layout_margin can be used.
- **SuperfluousNameSpace** - Re-declaring a namespace is unnecessary and hence can be just removed.
- **SuperfluousPaddingDeclaration** - Instead of using start-, end-, bottom- and top paddings, padding can be used.
- **UnsupportedLayoutAttribute** - Some layout attributes are not supported. Your app will still compile but it makes no sense to have them around. This can happen when refactoring a LinearLayout to a ScrollView. The orientation is no longer needed and can be removed.
- **WrongAnnotationOrder** - Annotations should always be applied with the same order to have consistency across the code base.
- **WrongConstraintLayoutUsage** - Instead of using left & right constraints start & right should be used.
- **WrongLayoutName** - The layout file name should be prefixed with one of the following: activity_, view_, fragment_, dialog_, bottom_sheet_, adapter_item_, divider_, space_. This will improve consistency in your code base as well as enforce a certain structure.
- **WrongMenuIdFormat** - Menu ids should be in lowerCamelCase format. This has the benefit of saving an unnecessary underscore and also just looks nicer.
- **WrongTestMethodName** - The @Test annotation already states that this ia test hence the test prefix is not necessary.
- **WrongViewIdFormat** - View ids should be in lowerCamelCase format. This has the benefit of saving an unnecessary underscore and also just looks nicer.
- **XmlSpacing** - Having newlines in xml files just adds noise and should be avoided. The only exception is the new lint at the end of the file.

## RxJava 2 Lint Rules

```groovy
compile 'com.vanniktech:lint-rules-rxjava2:0.6.0'
compile 'com.vanniktech:lint-rules-rxjava2:0.7.0-SNAPSHOT
```

- **RxJava2DefaultScheduler** - Calling this method will rely on a default scheduler. This is not necessary the best default. Being explicit and taking the overload for passing one is preferred.
- **RxJava2DisposableAddAllCall** - Instead of using addAll(), add() should be used separately for each Disposable.
- **RxJava2DisposableDisposeCall** - Instead of using dispose(), clear() should be used. Calling clear will result in a CompositeDisposable that can be used further to add more Disposables. When using dispose() this is not the case.
- **RxJava2MethodMissingCheckReturnValue** - Methods returning RxJava Reactive Types should be annotated with the @CheckReturnValue annotation. Static analyze tools such as Lint or ErrorProne can detect when the return value of a method is not used. This is usually an indication of a bug. If this is done on purpose (e.g. fire & forget) it should be stated explicitly.
- **RxJava2MissingCompositeDisposableClear** - A class is using CompositeDisposable and not calling clear(). This can leave operations running and even cause memory leaks. It's best to always call clear() once you're done. e.g. in onDestroy() for Activitys.
- **RxJava2SchedulersFactoryCall** - Injecting the Schedulers instead of accessing them via the factory methods has the benefit that unit testing is way easier. Instead of overriding them via the Plugin mechanism we can just pass a custom Scheduler.
- **RxJava2SubscribeMissingOnError** - When calling the subscribe() method an error Consumer should always be used. Otherwise errors might be thrown and may crash the application or get forwarded to the Plugin Error handler.

# License

Copyright (C) 2017 Vanniktech - Niklas Baudy

Licensed under the Apache License, Version 2.0
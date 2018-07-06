package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles

val stubJUnitTest = TestFiles.java("""
    package org.junit;

    public @interface Test { }""").indented()

val stubJUnitIgnore = TestFiles.java("""
    package org.junit;

    public @interface Test { }""").indented()

val stubAnnotationTest = TestFiles.java("""
    package my.custom;

    public @interface Test { }""").indented()

val stubAnnotationSomething = TestFiles.java("""
    package my.custom;

    public @interface Something { }""").indented()

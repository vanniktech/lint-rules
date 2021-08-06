@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles

val stubJUnitTest = TestFiles.java(
  """
    package org.junit;

    public @interface Test { }"""
).indented()

val stubJUnitIgnore = TestFiles.java(
  """
    package org.junit;

    public @interface Test { }"""
).indented()

val stubAnnotationTest = TestFiles.java(
  """
    package my.custom;

    public @interface Test { }"""
).indented()

val stubAnnotationSomething = TestFiles.java(
  """
    package my.custom;

    public @interface Something { }"""
).indented()

fun viewBindingProject() = TestFiles.gradle(
  """
        apply plugin: 'com.android.library'

        android {
          buildFeatures {
            viewBinding = true
          }
        }"""
)
  .indented()

fun resourcePrefix(prefix: String) = TestFiles.gradle(
  """
        apply plugin: 'com.android.library'

        android {
          resourcePrefix '$prefix'
        }"""
)
  .indented()

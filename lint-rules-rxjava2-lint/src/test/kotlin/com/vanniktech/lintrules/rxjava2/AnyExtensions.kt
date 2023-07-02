@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.checks.infrastructure.TestFiles.bytes

fun Any.dagger2() = bytes("libs/dagger-2.14.1.jar", javaClass.getResourceAsStream("/dagger-2.14.1.jar").readBytes())
fun Any.reactiveStreams() = bytes("libs/reactive-streams-1.0.2.jar", javaClass.getResourceAsStream("/reactive-streams-1.0.2.jar").readBytes())
fun Any.rxJava2() = bytes("libs/rxjava-2.1.7.jar", javaClass.getResourceAsStream("/rxjava-2.1.7.jar").readBytes())
fun Any.rxAndroid2() = bytes("libs/rxandroid-2.0.1.jar", javaClass.getResourceAsStream("/rxandroid-2.0.1.jar").readBytes())

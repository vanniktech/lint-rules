package com.vanniktech.lintrules.android

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {
  @Test fun toSnakeCase() {
    assertEquals("something", "Something".toSnakeCase())
    assertEquals("something_foo", "SomethingFoo".toSnakeCase())
    assertEquals("foo2bar_something", "Foo2BarSomething".toSnakeCase())
    assertEquals("a_b_c_d_e", "ABCDE".toSnakeCase())
  }
}

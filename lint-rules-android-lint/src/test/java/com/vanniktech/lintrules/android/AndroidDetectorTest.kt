package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR
import com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR_STATE_LIST
import com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_DRAWABLE
import org.junit.Test

class AndroidDetectorTest {
  private val resourcesStub = java("""
      |package android.content.res;
      |public class Resources {
      |  public void getDrawable(int id) {}
      |  public void getColor(int id) {}
      |  public void getColorStateList(int id) {}
      |}""".trimMargin())

  @Test fun callingGetDrawable() {
    lint()
        .files(resourcesStub, java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    Resources resources = null;
          |    resources.getDrawable(0);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_RESOURCES_GET_DRAWABLE)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Calling deprecated getDrawable. [ResourcesGetDrawableCall]
          |    resources.getDrawable(0);
          |              ~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingGetColor() {
    lint()
        .files(resourcesStub, java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    Resources resources = null;
          |    resources.getColor(0);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_RESOURCES_GET_COLOR)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Calling deprecated getColor. [ResourcesGetColorCall]
          |    resources.getColor(0);
          |              ~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingGetColorStateList() {
    lint()
        .files(resourcesStub, java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    Resources resources = null;
          |    resources.getColorStateList(0);
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_RESOURCES_GET_COLOR_STATE_LIST)
        .run()
        .expect("""
          |src/foo/Example.java:6: Warning: Calling deprecated getColorStateList. [ResourcesGetColorStateListCall]
          |    resources.getColorStateList(0);
          |              ~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
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
      .issues(*AndroidDetector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Warning: Using deprecated getDrawable() [ResourcesGetDrawable]
          |    resources.getDrawable(0);
          |              ~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingGetDrawableSuppressed() {
    lint()
      .files(resourcesStub, java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    Resources resources = null;
          |    //noinspection AndroidLintResourcesGetDrawable
          |    resources.getDrawable(0);
          |  }
          |}""".trimMargin()))
      .issues(*AndroidDetector.getIssues())
      .run()
      .expectClean()
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
      .issues(*AndroidDetector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Warning: Using deprecated getColor() [ResourcesGetColor]
          |    resources.getColor(0);
          |              ~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingGetColorSuppressed() {
    lint()
      .files(resourcesStub, java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    Resources resources = null;
          |    //noinspection AndroidLintResourcesGetColor
          |    resources.getColor(0);
          |  }
          |}""".trimMargin()))
      .issues(*AndroidDetector.getIssues())
      .run()
      .expectClean()
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
      .issues(*AndroidDetector.getIssues())
      .run()
      .expect("""
          |src/foo/Example.java:6: Warning: Using deprecated getColorStateList() [ResourcesGetColorStateList]
          |    resources.getColorStateList(0);
          |              ~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun callingGetColorStateListSuppressed() {
    lint()
      .files(resourcesStub, java("""
          |package foo;
          |import android.content.res.Resources;
          |class Example {
          |  public void foo() {
          |    Resources resources = null;
          |    //noinspection AndroidLintResourcesGetColorStateList
          |    resources.getColorStateList(0);
          |  }
          |}""".trimMargin()))
      .issues(*AndroidDetector.getIssues())
      .run()
      .expectClean()
  }
}

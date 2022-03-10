@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import org.junit.Test

@Suppress("Detekt.LargeClass") class AnnotationOrderDetectorTest {
  @Test fun noAnnotations() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              void something() {
                int something;
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun overrideComesFirstOnVariables() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              void something() {
                @Test @Override int something;
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:5: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
            |    @Test @Override int something;
            |                        ~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
      .expectFixDiffs(
        """
            |Fix for src/foo/MyTest.java line 5: Fix order:
            |@@ -5 +5
            |-     @Test @Override int something;
            |+     @Override @Test int something;
            |
        """.trimMargin()
      )
  }

  @Test fun overrideComesFirstOnFields() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Test @Override int something;
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
            |  @Test @Override int something;
            |                      ~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun overrideComesFirstOnClass() {
    lint()
      .files(
        java(
          """
            package foo;

            @Test @Override public class MyTest {
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:3: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
            |@Test @Override public class MyTest {
            |                             ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
      .expectFixDiffs(
        """
            |Fix for src/foo/MyTest.java line 3: Fix order:
            |@@ -3 +3
            |- @Test @Override public class MyTest {
            |+ @Override @Test public class MyTest {
            |
        """.trimMargin()
      )
  }

  @Test fun overrideComesFirstOnParameters() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              public void myTest(@Test @Override int something) { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
            |  public void myTest(@Test @Override int something) { }
            |                                         ~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun overrideBeforeTest() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Test @Override public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
            |  @Test @Override public void myTest() { }
            |                              ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun nullableBeforeStringRes() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @StringRes @Nullable public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Nullable @StringRes [WrongAnnotationOrder]
            |  @StringRes @Nullable public void myTest() { }
            |                                   ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun testBeforeIgnore() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Ignore @Test public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Test @Ignore [WrongAnnotationOrder]
            |  @Ignore @Test public void myTest() { }
            |                            ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun deprecatedBeforeOverride() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Override @Deprecated public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Deprecated @Override [WrongAnnotationOrder]
            |  @Override @Deprecated public void myTest() { }
            |                                    ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun overrideBeforeSuppressWarnings() {
    lint()
      .files(
        kt(
          """
            package foo

            class MyTest {
              @SuppressWarnings @Override fun myTest() = Unit
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.kt:4: Warning: Annotations are in wrong order. Should be @Override @SuppressWarnings [WrongAnnotationOrder]
            |  @SuppressWarnings @Override fun myTest() = Unit
            |                                  ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
      .expectFixDiffs(
        """
            |Fix for src/foo/MyTest.kt line 4: Fix order:
            |@@ -4 +4
            |-   @SuppressWarnings @Override fun myTest() = Unit
            |+   @Override @SuppressWarnings fun myTest() = Unit
            |
        """.trimMargin()
      )
  }

  @Test fun bindsBeforeSingletonBeforeNonNull() {
    lint()
      .files(
        java(
          """
            package foo;

            class MyTest {
              @Binds @NonNull @Singleton void test() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Binds @Singleton @NonNull [WrongAnnotationOrder]
            |  @Binds @NonNull @Singleton void test() { }
            |                                  ~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
      .expectFixDiffs(
        """
            |Fix for src/foo/MyTest.java line 4: Fix order:
            |@@ -4 +4
            |-   @Binds @NonNull @Singleton void test() { }
            |+   @Binds @Singleton @NonNull void test() { }
            |
        """.trimMargin()
      )
  }

  @Test fun singletonBeforeNonNullBeforeCustom() {
    lint()
      .files(
        java(
          """
            package foo;

            class MyTest {
              @Custom @NonNull @Singleton void test() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Singleton @NonNull @Custom [WrongAnnotationOrder]
            |  @Custom @NonNull @Singleton void test() { }
            |                                   ~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
      .expectFixDiffs(
        """
            |Fix for src/foo/MyTest.java line 4: Fix order:
            |@@ -4 +4
            |-   @Custom @NonNull @Singleton void test() { }
            |+   @Singleton @NonNull @Custom void test() { }
            |
        """.trimMargin()
      )
  }

  @Test fun nullableBeforeNonNull() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @NonNull @Nullable public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Nullable @NonNull [WrongAnnotationOrder]
            |  @NonNull @Nullable public void myTest() { }
            |                                 ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun checkResultBeforeCheckReturnValue() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @CheckReturnValue @CheckResult public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @CheckResult @CheckReturnValue [WrongAnnotationOrder]
            |  @CheckReturnValue @CheckResult public void myTest() { }
            |                                             ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun injectBeforeNullable() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Nullable @Inject public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Inject @Nullable [WrongAnnotationOrder]
            |  @Nullable @Inject public void myTest() { }
            |                                ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun injectBeforeNonNull() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @NonNull @Inject public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Inject @NonNull [WrongAnnotationOrder]
            |  @NonNull @Inject public void myTest() { }
            |                               ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun singletonBeforeComponent() {
    lint()
      .files(
        java(
          """
            package foo;

            @Component @Singleton public interface MyComponent {
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyComponent.java:3: Warning: Annotations are in wrong order. Should be @Singleton @Component [WrongAnnotationOrder]
            |@Component @Singleton public interface MyComponent {
            |                                       ~~~~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun suppressWarningsBeforeModule() {
    lint()
      .files(
        java(
          """
            package foo;

            @Module @SuppressWarnings public interface MyModule {
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyModule.java:3: Warning: Annotations are in wrong order. Should be @SuppressWarnings @Module [WrongAnnotationOrder]
            |@Module @SuppressWarnings public interface MyModule {
            |                                           ~~~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun assistedModuleBeforeModule() {
    lint()
      .files(
        java(
          """
            package foo;

            @AssistedModule @Module(includes = AssistedInject_SearchModule.class) public interface MyModule {
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun injectBeforeCustom() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Custom @Inject public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Inject @Custom [WrongAnnotationOrder]
            |  @Custom @Inject public void myTest() { }
            |                              ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun checkReturnValueBeforeAuthenticatedBeforePost() {
    lint()
      .files(
        kt(
          """
            package foo

            interface MyTest {
              @CheckReturnValue @Authenticated @Post fun myTest()
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun jsonBeforeJsonQualifier() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @JsonQualifier @Json public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Json @JsonQualifier [WrongAnnotationOrder]
            |  @JsonQualifier @Json public void myTest() { }
            |                                   ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun nullableJsonJsonQualifierNegativeCase() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Nullable @Json @JsonDate public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun jvmStaticProvidesSingletonNamedNullable() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @JvmStatic @Provides @Singleton @Named @Nullable public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun documentedBeforeRetention() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Retention @Documented public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Documented @Retention [WrongAnnotationOrder]
            |  @Retention @Documented public void myTest() { }
            |                                     ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun nullableBeforeJson() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Json @Nullable public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Nullable @Json [WrongAnnotationOrder]
            |  @Json @Nullable public void myTest() { }
            |                              ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun retentionBeforeIntDef() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @IntDef @Retention public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Retention @IntDef [WrongAnnotationOrder]
            |  @IntDef @Retention public void myTest() { }
            |                                 ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun suppressBeforeSuppressLintBeforeSuppressWarnings() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @SuppressWarnings @SuppressLint @Suppress public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE, TestMode.FULLY_QUALIFIED)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Suppress @SuppressLint @SuppressWarnings [WrongAnnotationOrder]
            |  @SuppressWarnings @SuppressLint @Suppress public void myTest() { }
            |                                                        ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun restrictToBeforeKeepBeforeTargetApi() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @TargetApi @RestrictTo @Keep public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @RestrictTo @Keep @TargetApi [WrongAnnotationOrder]
            |  @TargetApi @RestrictTo @Keep public void myTest() { }
            |                                           ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun bindsBeforeIntoMapBeforeActivityKey() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @ActivityKey @IntoMap @Binds public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expect(
        """
            |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Binds @IntoMap @ActivityKey [WrongAnnotationOrder]
            |  @ActivityKey @IntoMap @Binds public void myTest() { }
            |                                           ~~~~~~
            |0 errors, 1 warnings
        """.trimMargin()
      )
  }

  @Test fun kotlinAnnotationClass() {
    lint()
      .files(
        kt(
          """
            package foo

            annotation class Foo
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun kotlinAnnotationClassWithRetention() {
    lint()
      .files(
        kt(
          """
            package foo

            import kotlin.annotation.AnnotationRetention.RUNTIME

            @Retention(RUNTIME) annotation class Foo
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun bindsBeforeIntoMapBeforeActivityKeyNegativeCase() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Binds @IntoMap @ActivityKey public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun restrictToBeforeKeepNegativeCase() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @RestrictTo @Keep public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun singleCustomAnnotation() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Custom public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun twoCustomsAnnotation() {
    lint()
      .files(
        java(
          """
            package foo;

            public class MyTest {
              @Custom @MyCustom public void myTest() { }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }

  @Test fun kotlinCustomAnnotationOnNullableReturn() {
    lint()
      .files(
        kt(
          """
            package foo

            annotation class Custom

            class MyTest {
              @Custom fun myTest(): String? {
                return null
              }
            }
          """
        ).indented()
      )
      .issues(ISSUE_WRONG_ANNOTATION_ORDER)
      .skipTestModes(TestMode.WHITESPACE)
      .run()
      .expectClean()
  }
}

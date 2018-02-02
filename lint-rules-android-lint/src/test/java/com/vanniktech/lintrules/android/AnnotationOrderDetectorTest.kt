package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class AnnotationOrderDetectorTest {
  @Test fun noAnnotations() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  void something() {
          |    int something;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expectClean()
  }

  @Test fun overrideComesFirstOnVariables() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  void something() {
          |    @Test @Override int something;
          |  }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:5: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
          |    @Test @Override int something;
          |                        ~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun overrideComesFirstOnFields() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Test @Override int something;
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
          |  @Test @Override int something;
          |                      ~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun overrideComesFirstOnClass() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |@Test @Override public class MyTest {
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:3: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
          |@Test @Override public class MyTest {
          |                             ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun overrideComesFirstOnParameters() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  public void myTest(@Test @Override int something) { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
          |  public void myTest(@Test @Override int something) { }
          |                                         ~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun overrideBeforeTest() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Test @Override public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @Test [WrongAnnotationOrder]
          |  @Test @Override public void myTest() { }
          |                              ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun nullableBeforeStringRes() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @StringRes @Nullable public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Nullable @StringRes [WrongAnnotationOrder]
          |  @StringRes @Nullable public void myTest() { }
          |                                   ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun testBeforeIgnore() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Ignore @Test public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Test @Ignore [WrongAnnotationOrder]
          |  @Ignore @Test public void myTest() { }
          |                            ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun deprecatedBeforeOverride() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Override @Deprecated public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Deprecated @Override [WrongAnnotationOrder]
          |  @Override @Deprecated public void myTest() { }
          |                                    ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun overrideBeforeSuppressWarnings() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @SuppressWarnings @Override public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Override @SuppressWarnings [WrongAnnotationOrder]
          |  @SuppressWarnings @Override public void myTest() { }
          |                                          ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun nullableBeforeNonNull() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @NonNull @Nullable public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Nullable @NonNull [WrongAnnotationOrder]
          |  @NonNull @Nullable public void myTest() { }
          |                                 ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun checkResultBeforeCheckReturnValue() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @CheckReturnValue @CheckResult public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @CheckResult @CheckReturnValue [WrongAnnotationOrder]
          |  @CheckReturnValue @CheckResult public void myTest() { }
          |                                             ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun injectBeforeNullable() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Nullable @Inject public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Inject @Nullable [WrongAnnotationOrder]
          |  @Nullable @Inject public void myTest() { }
          |                                ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun injectBeforeNonNull() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @NonNull @Inject public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Inject @NonNull [WrongAnnotationOrder]
          |  @NonNull @Inject public void myTest() { }
          |                               ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun singletonBeforeComponent() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |@Component @Singleton public interface MyComponent {
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyComponent.java:3: Warning: Annotations are in wrong order. Should be @Singleton @Component [WrongAnnotationOrder]
          |@Component @Singleton public interface MyComponent {
          |                                       ~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun suppressWarningsBeforeModule() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |@Module @SuppressWarnings public interface MyModule {
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyModule.java:3: Warning: Annotations are in wrong order. Should be @SuppressWarnings @Module [WrongAnnotationOrder]
          |@Module @SuppressWarnings public interface MyModule {
          |                                           ~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun injectBeforeCustom() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Custom @Inject public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Inject @Custom [WrongAnnotationOrder]
          |  @Custom @Inject public void myTest() { }
          |                              ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun jsonBeforeJsonQualifier() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @JsonQualifier @Json public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Json @JsonQualifier [WrongAnnotationOrder]
          |  @JsonQualifier @Json public void myTest() { }
          |                                   ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun nullableJsonJsonQualifierNegativeCase() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Nullable @Json @JsonDate public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expectClean()
  }

  @Test fun providesSingletonNamedNullable() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Provides @Singleton @Named @Nullable public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expectClean()
  }

  @Test fun documentedBeforeRetention() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Retention @Documented public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Documented @Retention [WrongAnnotationOrder]
          |  @Retention @Documented public void myTest() { }
          |                                     ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun nullableBeforeJson() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Json @Nullable public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Nullable @Json [WrongAnnotationOrder]
          |  @Json @Nullable public void myTest() { }
          |                              ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun retentionBeforeIntDef() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @IntDef @Retention public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Retention @IntDef [WrongAnnotationOrder]
          |  @IntDef @Retention public void myTest() { }
          |                                 ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun suppressBeforeSuppressLintBeforeSuppressWarnings() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @SuppressWarnings @SuppressLint @Suppress public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Suppress @SuppressLint @SuppressWarnings [WrongAnnotationOrder]
          |  @SuppressWarnings @SuppressLint @Suppress public void myTest() { }
          |                                                        ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun keepBeforeRestrictToBeforeTargetApi() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @TargetApi @RestrictTo @Keep public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Keep @RestrictTo @TargetApi [WrongAnnotationOrder]
          |  @TargetApi @RestrictTo @Keep public void myTest() { }
          |                                           ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun bindsBeforeIntoMapBeforeActivityKey() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @ActivityKey @IntoMap @Binds public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expect("""
          |src/foo/MyTest.java:4: Warning: Annotations are in wrong order. Should be @Binds @IntoMap @ActivityKey [WrongAnnotationOrder]
          |  @ActivityKey @IntoMap @Binds public void myTest() { }
          |                                           ~~~~~~
          |0 errors, 1 warnings""".trimMargin())
  }

  @Test fun bindsBeforeIntoMapBeforeActivityKeyNegativeCase() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Binds @IntoMap @ActivityKey public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expectClean()
  }

  @Test fun singleCustomAnnotation() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Custom public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expectClean()
  }

  @Test fun twoCustomsAnnotation() {
    lint()
        .allowCompilationErrors()
        .files(java("""
          |package foo;
          |
          |public class MyTest {
          |  @Custom @MyCustom public void myTest() { }
          |}""".trimMargin()))
        .issues(ISSUE_WRONG_ANNOTATION_ORDER)
        .run()
        .expectClean()
  }
}

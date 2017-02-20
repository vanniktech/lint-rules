package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.Arrays;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static org.fest.assertions.api.Assertions.assertThat;

public class RxJava2MethodCheckReturnValueDetectorTest extends RxJavaLintDetectorTest {
  public void testMethodReturningObservableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "public class Example {\n"
        + "  public Observable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubObservable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Observable<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningObservableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Observable;\n"
        + "import io.reactivex.annotations;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Observable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubObservable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningFlowableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "public class Example {\n"
        + "  public Flowable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubFlowable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Flowable<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningFlowableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Flowable;\n"
        + "import io.reactivex.annotations;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Flowable<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubFlowable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningSingleMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "public class Example {\n"
        + "  public Single<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubSingle, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Single<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningSingleHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Single;\n"
        + "import io.reactivex.annotations;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Single<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubSingle, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningMaybeMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "public class Example {\n"
        + "  public Maybe<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubMaybe, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Maybe<Object> foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningMaybeHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Maybe;\n"
        + "import io.reactivex.annotations;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Maybe<Object> foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubMaybe, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMethodReturningCompletableMissingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "public class Example {\n"
        + "  public Completable foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompletable, java(source))).isEqualTo("src/foo/Example.java:4: "
        + "Warning: Method should have @CheckReturnValue annotation [MethodMissingCheckReturnValue]\n"
        + "  public Completable foo() {\n"
        + "  ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMethodReturningCompletableHavingCheckReturnValue() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.Completable;\n"
        + "import io.reactivex.annotations;\n"
        + "public class Example {\n"
        + "  @CheckReturnValue public Completable foo() {\n"
        + "    return null;\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompletable, stubCheckReturnValue, java(source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new RxJava2MethodCheckReturnValueDetector();
  }

  @Override protected List<Issue> getIssues() {
    return Arrays.asList(RxJava2MethodCheckReturnValueDetector.getIssues());
  }

  @Override protected boolean allowCompilationErrors() {
    return true;
  }
}

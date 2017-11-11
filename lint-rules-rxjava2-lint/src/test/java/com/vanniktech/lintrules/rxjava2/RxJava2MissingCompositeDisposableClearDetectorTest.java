package com.vanniktech.lintrules.rxjava2;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.rxjava2.RxJava2MissingCompositeDisposableClearDetector.ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class RxJava2MissingCompositeDisposableClearDetectorTest extends RxJavaLintDetectorTest {
  public void testNoCompositeDisposable() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCompositeDisposableMissingClearSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "import android.annotation.SuppressLint;\n"
        + "public class Example {\n"
        + "  @SuppressLint(\"MissingCompositeDisposableClear\") CompositeDisposable cd;\n"
        + "}";
    assertThat(lintProject(stubSuppressLint, stubCompositeDisposable, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testCompositeDisposableMissingClear() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  CompositeDisposable cd;\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo("src/foo/Example.java:4: Error: clear() is not called. [MissingCompositeDisposableClear]\n"
        + "  CompositeDisposable cd;\n"
        + "  ~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testDisposableMissingClearIsIgnored() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.Disposable;\n"
        + "public class Example {\n"
        + "  Disposable disposable;\n"
        + "}";
    assertThat(lintProject(stubDisposable, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMultipleCompositeDisposableMissingClear() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  CompositeDisposable cd1;\n"
        + "  CompositeDisposable cd2;\n"
        + "  CompositeDisposable cd3;\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo("src/foo/Example.java:4: Error: clear() is not called. [MissingCompositeDisposableClear]\n"
        + "  CompositeDisposable cd1;\n"
        + "  ~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "src/foo/Example.java:5: Error: clear() is not called. [MissingCompositeDisposableClear]\n"
        + "  CompositeDisposable cd2;\n"
        + "  ~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "src/foo/Example.java:6: Error: clear() is not called. [MissingCompositeDisposableClear]\n"
        + "  CompositeDisposable cd3;\n"
        + "  ~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "3 errors, 0 warnings\n"
        + "");
  }

  public void testCompositeDisposableHavingClear() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  CompositeDisposable cd;\n"
        + "  public void foo() {\n"
        + "   cd.clear();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo(NO_WARNINGS);
  }

  public void testMultipleCompositeDisposableClear() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import io.reactivex.disposables.CompositeDisposable;\n"
        + "public class Example {\n"
        + "  CompositeDisposable cd1;\n"
        + "  CompositeDisposable cd2;\n"
        + "  CompositeDisposable cd3;\n"
        + "  public void foo() {\n"
        + "   cd1.clear();\n"
        + "  }\n"
        + "  public void bar() {\n"
        + "   cd2.clear();\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(stubCompositeDisposable, java(source))).isEqualTo("src/foo/Example.java:6: Error: clear() is not called. [MissingCompositeDisposableClear]\n"
        + "  CompositeDisposable cd3;\n"
        + "  ~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new RxJava2MissingCompositeDisposableClearDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR);
  }
}

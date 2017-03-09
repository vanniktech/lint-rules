package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.Arrays;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static org.fest.assertions.api.Assertions.assertThat;

public class AndroidDetectorTest extends LintDetectorTest {
  static final String NO_WARNINGS = "No warnings.";

  private final TestFile windowStub = java(""
      + "package android.view;\n"
      + "public class Window {\n"
      + "  public void findViewById(int id) {}\n"
      + "}");

  private final TestFile viewStub = java(""
      + "package android.view;\n"
      + "public class View {\n"
      + "  public void findViewById(int id) {}\n"
      + "}");

  private final TestFile dialogStub = java(""
      + "package android.app;\n"
      + "public class Dialog {\n"
      + "  public void findViewById(int id) {}\n"
      + "}");

  private final TestFile activityStub = java(""
      + "package android.app;\n"
      + "public class Activity {\n"
      + "  public void findViewById(int id) {}\n"
      + "}");

  private final TestFile resourcesStub = java(""
      + "package android.content.res;\n"
      + "public class Resources {\n"
      + "  public void getDrawable(int id) {}\n"
      + "  public void getColor(int id) {}\n"
      + "  public void getColorStateList(int id) {}\n"
      + "}");

  public void testCallingWindowFindViewByIdSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.view.Window;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Window window = null;\n"
        + "    //noinspection AndroidLintWindowFindViewById\n"
        + "    window.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), windowStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingWindowFindViewById() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.view.Window;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Window window = null;\n"
        + "    window.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), windowStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using findViewById instead of ButterKnife [WindowFindViewById]\n"
        + "    window.findViewById(0);\n"
        + "           ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingWindowFindViewByIdExtending() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.view.Window;\n"
        + "public class Example extends Window {\n"
        + "  public void foo() {\n"
        + "    findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), windowStub)).isEqualTo("src/foo/Example.java:5: "
        + "Warning: Using findViewById instead of ButterKnife [WindowFindViewById]\n"
        + "    findViewById(0);\n"
        + "    ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingViewFindViewByIdSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.view.View;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    View view = null;\n"
        + "    //noinspection AndroidLintViewFindViewById\n"
        + "    view.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), viewStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingViewFindViewById() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.view.View;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    View view = null;\n"
        + "    view.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), viewStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using findViewById instead of ButterKnife [ViewFindViewById]\n"
        + "    view.findViewById(0);\n"
        + "         ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingViewFindViewByIdExtending() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.view.View;\n"
        + "public class Example extends View {\n"
        + "  public void foo() {\n"
        + "    findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), viewStub)).isEqualTo("src/foo/Example.java:5: "
        + "Warning: Using findViewById instead of ButterKnife [ViewFindViewById]\n"
        + "    findViewById(0);\n"
        + "    ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingDialogFindViewByIdSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.app.Dialog;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Dialog dialog = null;\n"
        + "    //noinspection AndroidLintDialogFindViewById\n"
        + "    dialog.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), dialogStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingDialogFindViewById() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.app.Dialog;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Dialog dialog = null;\n"
        + "    dialog.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), dialogStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using findViewById instead of ButterKnife [DialogFindViewById]\n"
        + "    dialog.findViewById(0);\n"
        + "           ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingDialogFindViewByIdExtending() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.app.Dialog;\n"
        + "public class Example extends Dialog {\n"
        + "  public void foo() {\n"
        + "    findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), dialogStub)).isEqualTo("src/foo/Example.java:5: "
        + "Warning: Using findViewById instead of ButterKnife [DialogFindViewById]\n"
        + "    findViewById(0);\n"
        + "    ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingActivityFindViewByIdSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.app.Activity;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Activity activity = null;\n"
        + "    //noinspection AndroidLintActivityFindViewById\n"
        + "    activity.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), activityStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingActivityFindViewById() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.app.Activity;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Activity activity = null;\n"
        + "    activity.findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), activityStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using findViewById instead of ButterKnife [ActivityFindViewById]\n"
        + "    activity.findViewById(0);\n"
        + "             ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingActivityFindViewByIdExtending() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.app.Activity;\n"
        + "public class Example extends Activity {\n"
        + "  public void foo() {\n"
        + "    findViewById(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), activityStub)).isEqualTo("src/foo/Example.java:5: "
        + "Warning: Using findViewById instead of ButterKnife [ActivityFindViewById]\n"
        + "    findViewById(0);\n"
        + "    ~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetDrawable() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    resources.getDrawable(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using deprecated getDrawable() [ResourcesGetDrawable]\n"
        + "    resources.getDrawable(0);\n"
        + "              ~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetDrawableSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    //noinspection AndroidLintResourcesGetDrawable\n"
        + "    resources.getDrawable(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingGetColor() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    resources.getColor(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using deprecated getColor() [ResourcesGetColor]\n"
        + "    resources.getColor(0);\n"
        + "              ~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetColorSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    //noinspection AndroidLintResourcesGetColor\n"
        + "    resources.getColor(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo(NO_WARNINGS);
  }

  public void testCallingGetColorStateList() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    resources.getColorStateList(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo("src/foo/Example.java:6: "
        + "Warning: Using deprecated getColorStateList() [ResourcesGetColorStateList]\n"
        + "    resources.getColorStateList(0);\n"
        + "              ~~~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testCallingGetColorStateListSuppressed() throws Exception {
    @Language("JAVA") final String source = ""
        + "package foo;\n"
        + "import android.content.res.Resources;\n"
        + "public class Example {\n"
        + "  public void foo() {\n"
        + "    Resources resources = null;\n"
        + "    //noinspection AndroidLintResourcesGetColorStateList\n"
        + "    resources.getColorStateList(0);\n"
        + "  }\n"
        + "}";
    assertThat(lintProject(java(source), resourcesStub)).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new AndroidDetector();
  }

  @Override protected List<Issue> getIssues() {
    return Arrays.asList(AndroidDetector.getIssues());
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

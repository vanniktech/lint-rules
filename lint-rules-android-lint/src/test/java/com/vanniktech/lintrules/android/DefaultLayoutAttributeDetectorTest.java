package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.DefaultLayoutAttributeDetector.ISSUE_DEFAULT_LAYOUT_ATTRIBUTE;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public final class DefaultLayoutAttributeDetectorTest extends LintDetectorTest {
  public void testTextStyleNormal() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "  android:layout_width=\"wrap_content\"\n"
        + "  android:layout_height=\"wrap_content\"\n"
        + "  android:textStyle=\"normal\"\n"
        + "  />";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:5: Warning: textStyle=\"normal\" is the default and hence you don't need to specify it. [DefaultLayoutAttribute]\n"
        + "  android:textStyle=\"normal\"\n"
        + "                     ~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testTextStyleBold() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "  android:layout_width=\"wrap_content\"\n"
        + "  android:layout_height=\"wrap_content\"\n"
        + "  android:textStyle=\"bold\"\n"
        + "  />";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testTextStyleNormalIgnored() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "  xmlns:tools=\"http://schemas.android.com/tools\"\n"
        + "  android:layout_width=\"wrap_content\"\n"
        + "  android:layout_height=\"wrap_content\"\n"
        + "  android:textStyle=\"normal\"\n"
        + "  tools:ignore=\"DefaultLayoutAttribute\"\n"
        + "  />";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testShouldNotCrashWithStyle() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      style=\"?android:attr/borderlessButtonStyle\"\n"
        + "      />";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new DefaultLayoutAttributeDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

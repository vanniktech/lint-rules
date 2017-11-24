package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.WrongConstraintLayoutUsageDetector.ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class WrongConstraintLayoutUsageDetectorTest extends LintDetectorTest {
  public void testConstraintLeftToLeftOfIgnored() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "      xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n"
        + "      xmlns:tools=\"http://schemas.android.com/tools\"\n"
        + "      android:layout_width=\"wrap_content\"\n"
        + "      android:layout_height=\"wrap_content\"\n"
        + "      app:layout_constraintLeft_toLeftOf=\"parent\"\n"
        + "      tools:ignore=\"WrongConstraintLayoutUsage\"/>";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testConstraintLeftToLeftOf() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "      xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n"
        + "      android:layout_width=\"wrap_content\"\n"
        + "      android:layout_height=\"wrap_content\"\n"
        + "      app:layout_constraintLeft_toLeftOf=\"parent\"/>";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintStart_toStartOf instead. [WrongConstraintLayoutUsage]\n"
        + "      app:layout_constraintLeft_toLeftOf=\"parent\"/>\n"
        + "      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testConstraintLeftToRightOf() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "      xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n"
        + "      android:layout_width=\"wrap_content\"\n"
        + "      android:layout_height=\"wrap_content\"\n"
        + "      app:layout_constraintLeft_toRightOf=\"parent\"/>";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintStart_toEndOf instead. [WrongConstraintLayoutUsage]\n"
        + "      app:layout_constraintLeft_toRightOf=\"parent\"/>\n"
        + "      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testConstraintRightToRightOf() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "      xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n"
        + "      android:layout_width=\"wrap_content\"\n"
        + "      android:layout_height=\"wrap_content\"\n"
        + "      app:layout_constraintRight_toRightOf=\"parent\"/>";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintEnd_toEndOf instead. [WrongConstraintLayoutUsage]\n"
        + "      app:layout_constraintRight_toRightOf=\"parent\"/>\n"
        + "      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testConstraintRightToLeftOf() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "      xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n"
        + "      android:layout_width=\"wrap_content\"\n"
        + "      android:layout_height=\"wrap_content\"\n"
        + "      app:layout_constraintRight_toLeftOf=\"parent\"/>";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:6: Error: This attribute won't work with RTL. Please use layout_constraintEnd_toStartOf instead. [WrongConstraintLayoutUsage]\n"
        + "      app:layout_constraintRight_toLeftOf=\"parent\"/>\n"
        + "      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "1 errors, 0 warnings\n");
  }

  public void testShouldNotCrashWithStyle() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      style=\"?android:attr/borderlessButtonStyle\"/>";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new WrongConstraintLayoutUsageDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

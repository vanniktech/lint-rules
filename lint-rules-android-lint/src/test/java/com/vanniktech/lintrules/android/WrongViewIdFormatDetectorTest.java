package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.ISSUE_WRONG_VIEW_ID_FORMAT;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class WrongViewIdFormatDetectorTest extends LintDetectorTest {
  public void testIdLowerCamelCase() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/lowerCamelCase\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdCamelCase() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView\n"
        + "    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "    android:id=\"@+id/CamelCase\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:4: Warning: Id is not in lowerCamelCaseFormat [WrongViewIdFormat]\n"
        + "    android:id=\"@+id/CamelCase\"/>\n"
        + "                ~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testIdSnakeCase() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView\n"
        + "    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "    android:id=\"@+id/snake_case\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:4: Warning: Id is not in lowerCamelCaseFormat [WrongViewIdFormat]\n"
        + "    android:id=\"@+id/snake_case\"/>\n"
        + "                ~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new WrongViewIdFormatDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_WRONG_VIEW_ID_FORMAT);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

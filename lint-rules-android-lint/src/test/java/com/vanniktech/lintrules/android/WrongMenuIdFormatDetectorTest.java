package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.WrongMenuIdFormatDetector.ISSUE_WRONG_MENU_ID_FORMAT;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class WrongMenuIdFormatDetectorTest extends LintDetectorTest {
  public void testIdLowerCamelCase() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/lowerCamelCase\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdCamelCase() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/CamelCase\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/ids.xml", source))).isEqualTo("res/menu/ids.xml:3: Warning: Id is not in lowerCamelCaseFormat [WrongMenuIdFormat]\n"
        + "  <item android:id=\"@+id/CamelCase\"/>\n"
        + "                    ~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testIdSnakeCase() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/snake_case\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/ids.xml", source))).isEqualTo("res/menu/ids.xml:3: Warning: Id is not in lowerCamelCaseFormat [WrongMenuIdFormat]\n"
        + "  <item android:id=\"@+id/snake_case\"/>\n"
        + "                    ~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new WrongMenuIdFormatDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_WRONG_MENU_ID_FORMAT);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

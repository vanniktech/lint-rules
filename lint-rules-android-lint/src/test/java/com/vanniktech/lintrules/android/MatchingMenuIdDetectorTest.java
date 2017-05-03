package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.MatchingMenuIdDetector.ISSUE_MATCHING_MENU_ID;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class MatchingMenuIdDetectorTest extends LintDetectorTest {
  public void testIdAndroidMainPasses() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/menuMainSomething\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/menu_main.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdAndroidMainWrongOrder() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/mainMenuSomething\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/menu_main.xml", source))).isEqualTo("res/menu/menu_main.xml:3: Warning: Id should start with: menuMain [MatchingMenuId]\n"
        + "  <item android:id=\"@+id/mainMenuSomething\"/>\n"
        + "                    ~~~~~~~~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testIdAndroidMenuPasses() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/menuCustomTextView\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/menu_custom.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdAndroidViewWrongOrder() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/mainViewTextView\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/view_custom.xml", source))).isEqualTo("res/menu/view_custom.xml:3: Warning: Id should start with: viewCustom [MatchingMenuId]\n"
        + "  <item android:id=\"@+id/mainViewTextView\"/>\n"
        + "                    ~~~~~~~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testIdAndroidLongId() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">\n"
        + "  <item android:id=\"@+id/viewProfileAttributeDisplayHeader\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/view_profile_attribute_display.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdAndroidViewWrongOrderIgnored() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\" xmlns:tools=\"http://schemas.android.com/tools\" tools:ignore=\"MatchingMenuId\" >\n"
        + "  <item android:id=\"@+id/mainViewTextView\"/>\n"
        + "</menu>\n";

    assertThat(lintProject(xml("/res/menu/view_custom.xml", source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new MatchingMenuIdDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_MATCHING_MENU_ID);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

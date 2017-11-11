package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.MatchingViewIdDetector.ISSUE_MATCHING_VIEW_ID;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class MatchingViewIdDetectorTest extends LintDetectorTest {
  public void testIdAndroidActivityPasses() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/activityMainTextView\"/>\n";

    assertThat(lintProject(xml("/res/layout/activity_main.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdAndroidActivityWrongOrder() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/mainActivityTextView\"/>\n";

    assertThat(lintProject(xml("/res/layout/activity_main.xml", source))).isEqualTo("res/layout/activity_main.xml:2: Warning: Id should start with: activityMain [MatchingViewId]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/mainActivityTextView\"/>\n"
        + "                                                                                 ~~~~~~~~~~~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testIdAndroidViewPasses() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/viewCustomTextView\"/>\n";

    assertThat(lintProject(xml("/res/layout/view_custom.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdAndroidViewWrongOrder() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/mainViewTextView\"/>\n";

    assertThat(lintProject(xml("/res/layout/view_custom.xml", source))).isEqualTo("res/layout/view_custom.xml:2: Warning: Id should start with: viewCustom [MatchingViewId]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/mainViewTextView\"/>\n"
        + "                                                                                 ~~~~~~~~~~~~~~~~~~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testIdAndroidLongId() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:id=\"@+id/viewProfileAttributeDisplayHeader\"/>\n";

    assertThat(lintProject(xml("/res/layout/view_profile_attribute_display.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testIdAndroidViewWrongOrderIgnored() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" xmlns:tools=\"http://schemas.android.com/tools\" tools:ignore=\"MatchingViewId\" android:id=\"@+id/mainViewTextView\"/>\n";

    assertThat(lintProject(xml("/res/layout/view_custom.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testShouldNotCrashWithStyle() throws Exception {
    @Language("XML") final String source = "<TextView\n"
        + "      style=\"?android:attr/borderlessButtonStyle\"\n"
        + "      />";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new MatchingViewIdDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_MATCHING_VIEW_ID);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.RawColorDetector.ISSUE_RAW_COLOR;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class RawColorDetectorTest extends LintDetectorTest {
  public void testToolsTextColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:tools=\"http://schemas.android.com/tools\" tools:textColor=\"#fff\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidTextColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:textColor=\"#fff\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:textColor=\"#fff\"/>\n"
        + "                                                                                        ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidTextColorHint() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:textColorHint=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:textColorHint=\"#FFF\"/>\n"
        + "                                                                                            ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidRippleColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:rippleColor=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:rippleColor=\"#FFF\"/>\n"
        + "                                                                                          ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidItemTextColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:itemTextColor=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:itemTextColor=\"#FFF\"/>\n"
        + "                                                                                            ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidCacheColorHint() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:cacheColorHint=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:cacheColorHint=\"#FFF\"/>\n"
        + "                                                                                             ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidCardBackgroundColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:cardBackgroundColor=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:cardBackgroundColor=\"#FFF\"/>\n"
        + "                                                                                                  ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidTabIndicatorColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:tabIndicatorColor=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:tabIndicatorColor=\"#FFF\"/>\n"
        + "                                                                                                ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidTabSelectedTextColor() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:tabSelectedTextColor=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:tabSelectedTextColor=\"#FFF\"/>\n"
        + "                                                                                                   ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidBackground() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:background=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:background=\"#FFF\"/>\n"
        + "                                                                                         ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidBackgroundTint() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:backgroundTint=\"#FFF\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using color instead. [RawColor]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:backgroundTint=\"#FFF\"/>\n"
        + "                                                                                             ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new RawColorDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_RAW_COLOR);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

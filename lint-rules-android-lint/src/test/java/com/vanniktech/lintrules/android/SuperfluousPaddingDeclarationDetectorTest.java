package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.SuperfluousPaddingDeclarationDetector.SUPERFLUOUS_PADDING_DECLARATION;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class SuperfluousPaddingDeclarationDetectorTest extends LintDetectorTest {
  public void testAndroidPaddingSame() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:paddingTop=\"16dp\"\n"
        + "  android:paddingBottom=\"16dp\"\n"
        + "  android:paddingStart=\"16dp\"\n"
        + "  android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using padding instead. [SuperfluousPaddingDeclaration]\n"
        + "<TextView  xmlns:android=\"http://schemas.android.com/apk/res/android\"  android:paddingTop=\"16dp\"\n"
        + "^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidPaddingDifferent() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:paddingTop=\"8dp\"\n"
        + "  android:paddingBottom=\"16dp\"\n"
        + "  android:paddingStart=\"16dp\"\n"
        + "  android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidPaddingSameIgnored() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  xmlns:tools=\"http://schemas.android.com/tools\""
        + "  tools:ignore=\"SuperfluousPaddingDeclaration\""
        + "  android:paddingTop=\"16dp\"\n"
        + "  android:paddingBottom=\"16dp\"\n"
        + "  android:paddingStart=\"16dp\"\n"
        + "  android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidPaddingStartMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:paddingTop=\"16dp\"\n"
        + "  android:paddingBottom=\"16dp\"\n"
        + "  android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidPaddingEndMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:paddingTop=\"16dp\"\n"
        + "  android:paddingBottom=\"16dp\"\n"
        + "  android:paddingStart=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidPaddingBottomMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:paddingTop=\"16dp\"\n"
        + "  android:paddingStart=\"16dp\"\n"
        + "  android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidPaddingPaddingTopMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:paddingBottom=\"16dp\"\n"
        + "  android:paddingStart=\"16dp\"\n"
        + "  android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testDeclarationsSplit() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<merge\n"
        + "    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
        + "    >\n"
        + "\n"
        + "  <TextView\n"
        + "      android:layout_width=\"match_parent\"\n"
        + "      android:layout_height=\"wrap_content\"\n"
        + "      android:paddingStart=\"16dp\"\n"
        + "      android:paddingEnd=\"16dp\"/>\n"
        + "\n"
        + "  <View\n"
        + "      android:layout_width=\"match_parent\"\n"
        + "      android:layout_height=\"match_parent\"\n"
        + "      android:paddingTop=\"16dp\"\n"
        + "      android:paddingBottom=\"16dp\"/>\n"
        + "\n"
        + "</merge>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testToolsPaddingSame() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:tools=\"http://schemas.android.com/tools\""
        + "  tools:paddingTop=\"16dp\"\n"
        + "  tools:paddingBottom=\"16dp\"\n"
        + "  tools:paddingStart=\"16dp\"\n"
        + "  tools:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new SuperfluousPaddingDeclarationDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(SUPERFLUOUS_PADDING_DECLARATION);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

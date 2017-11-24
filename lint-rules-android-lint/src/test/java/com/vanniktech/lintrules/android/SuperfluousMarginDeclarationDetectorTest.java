package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class SuperfluousMarginDeclarationDetectorTest extends LintDetectorTest {
  public void testAndroidMarginSame() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:layout_marginTop=\"16dp\"\n"
        + "  android:layout_marginBottom=\"16dp\"\n"
        + "  android:layout_marginStart=\"16dp\"\n"
        + "  android:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using layout_margin instead. [SuperfluousMarginDeclaration]\n"
        + "<TextView  xmlns:android=\"http://schemas.android.com/apk/res/android\"  android:layout_marginTop=\"16dp\"\n"
        + "^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testAndroidMarginDifferent() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:layout_marginTop=\"8dp\"\n"
        + "  android:layout_marginBottom=\"16dp\"\n"
        + "  android:layout_marginStart=\"16dp\"\n"
        + "  android:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidMarginSameIgnored() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  xmlns:tools=\"http://schemas.android.com/tools\""
        + "  tools:ignore=\"SuperfluousMarginDeclaration\""
        + "  android:layout_marginTop=\"16dp\"\n"
        + "  android:layout_marginBottom=\"16dp\"\n"
        + "  android:layout_marginStart=\"16dp\"\n"
        + "  android:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidMarginStartMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:layout_marginTop=\"16dp\"\n"
        + "  android:layout_marginBottom=\"16dp\"\n"
        + "  android:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidMarginEndMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:layout_marginTop=\"16dp\"\n"
        + "  android:layout_marginBottom=\"16dp\"\n"
        + "  android:layout_marginStart=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidMarginBottomMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:layout_marginTop=\"16dp\"\n"
        + "  android:layout_marginStart=\"16dp\"\n"
        + "  android:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testAndroidMarginMarginTopMissing() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:android=\"http://schemas.android.com/apk/res/android\""
        + "  android:layout_marginBottom=\"16dp\"\n"
        + "  android:layout_marginStart=\"16dp\"\n"
        + "  android:layout_marginEnd=\"16dp\"/>\n";

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
        + "      android:layout_marginStart=\"16dp\"\n"
        + "      android:layout_marginEnd=\"16dp\"\n/>"
        + "\n"
        + "  <View\n"
        + "      android:layout_width=\"match_parent\"\n"
        + "      android:layout_height=\"match_parent\"\n"
        + "      android:layout_marginTop=\"16dp\"\n"
        + "      android:layout_marginBottom=\"16dp\"\n/>"
        + "\n"
        + "</merge>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testToolsMarginSame() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView"
        + "  xmlns:tools=\"http://schemas.android.com/tools\""
        + "  tools:layout_marginTop=\"16dp\"\n"
        + "  tools:layout_marginBottom=\"16dp\"\n"
        + "  tools:layout_marginStart=\"16dp\"\n"
        + "  tools:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo(NO_WARNINGS);
  }

  @Override protected Detector getDetector() {
    return new SuperfluousMarginDeclarationDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(SUPERFLUOUS_MARGIN_DECLARATION);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.RawDimenDetector.ISSUE_RAW_DIMEN;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class RawDimenDetectorTest extends LintDetectorTest {
  public void testMargin() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_margin=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_margin=\"16dp\"/>\n"
        + "                                                                                            ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMarginLeft() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginLeft=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginLeft=\"16dp\"/>\n"
        + "                                                                                                ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMarginTop() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginTop=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginTop=\"16dp\"/>\n"
        + "                                                                                               ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMarginRight() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginRight=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginRight=\"16dp\"/>\n"
        + "                                                                                                 ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMarginBottom() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginBottom=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginBottom=\"16dp\"/>\n"
        + "                                                                                                  ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMarginStart() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginStart=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginStart=\"16dp\"/>\n"
        + "                                                                                                 ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testMarginEnd() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:layout_marginEnd=\"16dp\"/>\n"
        + "                                                                                               ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPadding() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:padding=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:padding=\"16dp\"/>\n"
        + "                                                                                      ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPaddingLeft() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingLeft=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingLeft=\"16dp\"/>\n"
        + "                                                                                          ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPaddingTop() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingTop=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingTop=\"16dp\"/>\n"
        + "                                                                                         ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPaddingRight() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingRight=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingRight=\"16dp\"/>\n"
        + "                                                                                           ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPaddingBottom() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingBottom=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingBottom=\"16dp\"/>\n"
        + "                                                                                            ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPaddingStart() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingStart=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingStart=\"16dp\"/>\n"
        + "                                                                                           ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testPaddingEnd() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingEnd=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:paddingEnd=\"16dp\"/>\n"
        + "                                                                                         ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  public void testTextSize() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:textSize=\"16dp\"/>\n";

    assertThat(lintProject(xml("/res/layout/ids.xml", source))).isEqualTo("res/layout/ids.xml:2: Warning: Should be using dimen instead. [RawDimen]\n"
        + "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\" android:textSize=\"16dp\"/>\n"
        + "                                                                                       ~~~~\n"
        + "0 errors, 1 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new RawDimenDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_RAW_DIMEN);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

package com.vanniktech.lintrules.android;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.List;
import org.intellij.lang.annotations.Language;

import static com.vanniktech.lintrules.android.AndroidDetectorTest.NO_WARNINGS;
import static com.vanniktech.lintrules.android.InvalidStringDetector.ISSUE_INVALID_STRING;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class InvalidStringDetectorTest extends LintDetectorTest {
  public void testValidString() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<resources>\n"
        + "  <string name=\"my_string\">My string</string>\n"
        + "</resources>";

    assertThat(lintProject(xml("/res/values/strings.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testStringContainingNewLine() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<resources>\n"
        + "  <string name=\"my_string\">My string\n"
        + "</string>\n"
        + "</resources>";

    assertThat(lintProject(xml("/res/values/strings.xml", source))).isEqualTo("res/values/strings.xml:3: Warning: Text contains new line. [InvalidString]\n"
        + "  <string name=\"my_string\">My string\n"
        + "                           ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testTrailingWhitespaceAtEndString() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<resources>\n"
        + "  <string name=\"my_string\">My string   </string>\n"
        + "</resources>";

    assertThat(lintProject(xml("/res/values/strings.xml", source))).isEqualTo("res/values/strings.xml:3: Warning: Text contains trailing whitespace. [InvalidString]\n"
        + "  <string name=\"my_string\">My string   </string>\n"
        + "                           ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testTrailingWhitespaceAtBeginningString() throws Exception {
    @Language("XML") final String source = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<resources>\n"
        + "  <string name=\"my_string\">  My string</string>\n"
        + "</resources>";

    assertThat(lintProject(xml("/res/values/strings.xml", source))).isEqualTo("res/values/strings.xml:3: Warning: Text contains trailing whitespace. [InvalidString]\n"
        + "  <string name=\"my_string\">  My string</string>\n"
        + "                             ^\n"
        + "0 errors, 1 warnings\n");
  }

  public void testTrailingWhitespacePluralString() throws Exception {
    @Language("XML") final String source = "<plurals name=\"days\">\n"
        + "    <item quantity=\"one\">%d Day</item>\n"
        + "    <item quantity=\"other\">%d Days</item>\n"
        + "  </plurals>";

    assertThat(lintProject(xml("/res/values/strings.xml", source))).isEqualTo(NO_WARNINGS);
  }

  public void testTrailingWhitespacePluralStringTrailing() throws Exception {
    @Language("XML") final String source = "<plurals name=\"days\">\n"
        + "    <item quantity=\"one\">  %d Day</item>\n"
        + "    <item quantity=\"other\">%d Days   </item>\n"
        + "  </plurals>";

    assertThat(lintProject(xml("/res/values/strings.xml", source))).isEqualTo("res/values/strings.xml:2: Warning: Text contains trailing whitespace. [InvalidString]\n"
        + "    <item quantity=\"one\">  %d Day</item>\n"
        + "                           ^\n"
        + "res/values/strings.xml:3: Warning: Text contains trailing whitespace. [InvalidString]\n"
        + "    <item quantity=\"other\">%d Days   </item>\n"
        + "                           ^\n"
        + "0 errors, 2 warnings\n");
  }

  @Override protected Detector getDetector() {
    return new InvalidStringDetector();
  }

  @Override protected List<Issue> getIssues() {
    return singletonList(ISSUE_INVALID_STRING);
  }

  @Override protected boolean allowCompilationErrors() {
    return false;
  }
}

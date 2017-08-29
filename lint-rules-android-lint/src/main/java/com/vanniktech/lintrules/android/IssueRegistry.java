package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Issue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vanniktech.lintrules.android.AnnotationOrderDetectorKt.ISSUE_WRONG_ANNOTATION_ORDER;
import static com.vanniktech.lintrules.android.ConstraintLayoutToolsEditorAttributeDetectorKt.ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR;
import static com.vanniktech.lintrules.android.DefaultLayoutAttributeDetector.ISSUE_DEFAULT_LAYOUT_ATTRIBUTE;
import static com.vanniktech.lintrules.android.InvalidSingleLineCommentDetector.ISSUE_INVALID_SINGLE_LINE_COMMENT;
import static com.vanniktech.lintrules.android.InvalidStringDetector.ISSUE_INVALID_STRING;
import static com.vanniktech.lintrules.android.RawDimenDetectorKt.ISSUE_RAW_DIMEN;
import static com.vanniktech.lintrules.android.SuperfluousNameSpaceDetectorKt.ISSUE_SUPERFLUOUS_NAME_SPACE;
import static com.vanniktech.lintrules.android.UnsupportedLayoutAttributeDetectorKt.ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE;
import static com.vanniktech.lintrules.android.WrongLayoutNameDetectorKt.ISSUE_WRONG_LAYOUT_NAME;
import static com.vanniktech.lintrules.android.MatchingMenuIdDetector.ISSUE_MATCHING_MENU_ID;
import static com.vanniktech.lintrules.android.MatchingViewIdDetector.ISSUE_MATCHING_VIEW_ID;
import static com.vanniktech.lintrules.android.MissingXmlHeaderDetectorKt.ISSUE_MISSING_XML_HEADER;
import static com.vanniktech.lintrules.android.RawColorDetector.ISSUE_RAW_COLOR;
import static com.vanniktech.lintrules.android.ShouldUseStaticImportDetector.ISSUE_SHOULD_USE_STATIC_IMPORT;
import static com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION;
import static com.vanniktech.lintrules.android.SuperfluousPaddingDeclarationDetector.SUPERFLUOUS_PADDING_DECLARATION;
import static com.vanniktech.lintrules.android.WrongConstraintLayoutUsageDetector.ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE;
import static com.vanniktech.lintrules.android.WrongMenuIdFormatDetector.ISSUE_WRONG_MENU_ID_FORMAT;
import static com.vanniktech.lintrules.android.WrongTestMethodNameDetectorKt.ISSUE_WRONG_TEST_METHOD_NAME;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.ISSUE_WRONG_VIEW_ID_FORMAT;
import static com.vanniktech.lintrules.android.XmlSpacingDetectorKt.ISSUE_XML_SPACING;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  @Override public List<Issue> getIssues() {
    final List<Issue> issues = new ArrayList<>();
    Collections.addAll(issues, AndroidDetector.getIssues());
    issues.add(ISSUE_WRONG_VIEW_ID_FORMAT);
    issues.add(ISSUE_WRONG_MENU_ID_FORMAT);
    issues.add(ISSUE_RAW_DIMEN);
    issues.add(ISSUE_RAW_COLOR);
    issues.add(SUPERFLUOUS_MARGIN_DECLARATION);
    issues.add(SUPERFLUOUS_PADDING_DECLARATION);
    issues.add(ISSUE_SHOULD_USE_STATIC_IMPORT);
    issues.add(ISSUE_MATCHING_VIEW_ID);
    issues.add(ISSUE_MATCHING_MENU_ID);
    issues.add(ISSUE_INVALID_SINGLE_LINE_COMMENT);
    issues.add(ISSUE_INVALID_STRING);
    issues.add(ISSUE_DEFAULT_LAYOUT_ATTRIBUTE);
    issues.add(ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE);
    issues.add(ISSUE_MISSING_XML_HEADER);
    issues.add(ISSUE_WRONG_TEST_METHOD_NAME);
    issues.add(ISSUE_WRONG_LAYOUT_NAME);
    issues.add(ISSUE_WRONG_ANNOTATION_ORDER);
    issues.add(ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR);
    issues.add(ISSUE_SUPERFLUOUS_NAME_SPACE);
    issues.add(ISSUE_XML_SPACING);
    issues.add(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE);
    return issues;
  }
}

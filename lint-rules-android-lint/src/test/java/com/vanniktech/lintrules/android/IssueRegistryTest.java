package com.vanniktech.lintrules.android;

import org.junit.Test;

import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_ACTIVITY_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_DIALOG_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR_STATE_LIST;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_DRAWABLE;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_VIEW_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_WINDOW_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AnnotationOrderDetectorKt.ISSUE_WRONG_ANNOTATION_ORDER;
import static com.vanniktech.lintrules.android.ConstraintLayoutToolsEditorAttributeDetectorKt.ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR;
import static com.vanniktech.lintrules.android.DefaultLayoutAttributeDetector.ISSUE_DEFAULT_LAYOUT_ATTRIBUTE;
import static com.vanniktech.lintrules.android.InvalidSingleLineCommentDetector.ISSUE_INVALID_SINGLE_LINE_COMMENT;
import static com.vanniktech.lintrules.android.InvalidStringDetector.ISSUE_INVALID_STRING;
import static com.vanniktech.lintrules.android.MatchingMenuIdDetector.ISSUE_MATCHING_MENU_ID;
import static com.vanniktech.lintrules.android.MatchingViewIdDetector.ISSUE_MATCHING_VIEW_ID;
import static com.vanniktech.lintrules.android.MissingXmlHeaderDetectorKt.ISSUE_MISSING_XML_HEADER;
import static com.vanniktech.lintrules.android.RawColorDetector.ISSUE_RAW_COLOR;
import static com.vanniktech.lintrules.android.RawDimenDetectorKt.ISSUE_RAW_DIMEN;
import static com.vanniktech.lintrules.android.ShouldUseStaticImportDetector.ISSUE_SHOULD_USE_STATIC_IMPORT;
import static com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION;
import static com.vanniktech.lintrules.android.SuperfluousNameSpaceDetectorKt.ISSUE_SUPERFLUOUS_NAME_SPACE;
import static com.vanniktech.lintrules.android.SuperfluousPaddingDeclarationDetector.SUPERFLUOUS_PADDING_DECLARATION;
import static com.vanniktech.lintrules.android.UnsupportedLayoutAttributeDetectorKt.ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE;
import static com.vanniktech.lintrules.android.WrongConstraintLayoutUsageDetector.ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE;
import static com.vanniktech.lintrules.android.WrongLayoutNameDetectorKt.ISSUE_WRONG_LAYOUT_NAME;
import static com.vanniktech.lintrules.android.WrongMenuIdFormatDetector.ISSUE_WRONG_MENU_ID_FORMAT;
import static com.vanniktech.lintrules.android.WrongTestMethodNameDetectorKt.ISSUE_WRONG_TEST_METHOD_NAME;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.ISSUE_WRONG_VIEW_ID_FORMAT;
import static com.vanniktech.lintrules.android.XmlSpacingDetectorKt.ISSUE_XML_SPACING;
import static org.fest.assertions.api.Assertions.assertThat;

public class IssueRegistryTest {
  @Test public void getIssues() {
    assertThat(new IssueRegistry().getIssues()).containsExactly(
        ISSUE_WINDOW_FIND_VIEW_BY_ID,
        ISSUE_VIEW_FIND_VIEW_BY_ID,
        ISSUE_DIALOG_FIND_VIEW_BY_ID,
        ISSUE_ACTIVITY_FIND_VIEW_BY_ID,
        ISSUE_RESOURCES_GET_DRAWABLE,
        ISSUE_RESOURCES_GET_COLOR,
        ISSUE_RESOURCES_GET_COLOR_STATE_LIST,
        ISSUE_WRONG_VIEW_ID_FORMAT,
        ISSUE_WRONG_MENU_ID_FORMAT,
        ISSUE_RAW_DIMEN,
        ISSUE_RAW_COLOR,
        SUPERFLUOUS_MARGIN_DECLARATION,
        SUPERFLUOUS_PADDING_DECLARATION,
        ISSUE_SHOULD_USE_STATIC_IMPORT,
        ISSUE_MATCHING_VIEW_ID,
        ISSUE_MATCHING_MENU_ID,
        ISSUE_INVALID_SINGLE_LINE_COMMENT,
        ISSUE_INVALID_STRING,
        ISSUE_DEFAULT_LAYOUT_ATTRIBUTE,
        ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE,
        ISSUE_MISSING_XML_HEADER,
        ISSUE_WRONG_TEST_METHOD_NAME,
        ISSUE_WRONG_LAYOUT_NAME,
        ISSUE_WRONG_ANNOTATION_ORDER,
        ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR,
        ISSUE_SUPERFLUOUS_NAME_SPACE,
        ISSUE_XML_SPACING,
        ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE
    );
  }
}

package com.vanniktech.lintrules.android

import com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR
import com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR_STATE_LIST
import com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_DRAWABLE
import com.vanniktech.lintrules.android.DefaultLayoutAttributeDetector.ISSUE_DEFAULT_LAYOUT_ATTRIBUTE
import com.vanniktech.lintrules.android.InvalidSingleLineCommentDetector.ISSUE_INVALID_SINGLE_LINE_COMMENT
import com.vanniktech.lintrules.android.MatchingMenuIdDetector.ISSUE_MATCHING_MENU_ID
import com.vanniktech.lintrules.android.MatchingViewIdDetector.ISSUE_MATCHING_VIEW_ID
import com.vanniktech.lintrules.android.ShouldUseStaticImportDetector.ISSUE_SHOULD_USE_STATIC_IMPORT
import com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION
import com.vanniktech.lintrules.android.SuperfluousPaddingDeclarationDetector.SUPERFLUOUS_PADDING_DECLARATION
import com.vanniktech.lintrules.android.WrongConstraintLayoutUsageDetector.ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class IssueRegistryTest {
  @Test fun getIssues() {
    assertThat(IssueRegistry().issues).containsExactly(
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
    )
  }
}

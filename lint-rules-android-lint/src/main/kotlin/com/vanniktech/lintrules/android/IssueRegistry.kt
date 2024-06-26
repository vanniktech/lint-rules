@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

internal const val PRIORITY = 10 // Does not matter anyway within Lint.

class IssueRegistry : com.android.tools.lint.client.api.IssueRegistry() {
  override val api get() = CURRENT_API

  override val vendor get() = Vendor(
    vendorName = "vanniktech/lint-rules/",
    feedbackUrl = "https://github.com/vanniktech/lint-rules/issues",
  )

  override val issues
    get() = listOf(
      ISSUE_ALERT_DIALOG_USAGE,
      ISSUE_ASSERTJ_IMPORT,
      ISSUE_COLOR_CASING,
      ISSUE_CONSTRAINT_LAYOUT_TOOLS_EDITOR_ATTRIBUTE_DETECTOR,
      ISSUE_DEFAULT_LAYOUT_ATTRIBUTE,
      ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE,
      ISSUE_FORMAL_GERMAN,
      ISSUE_IMPLICIT_STRING_PLACEHOLDER,
      ISSUE_INVALID_ACCESSIBILITY,
      ISSUE_INVALID_IMPORT,
      ISSUE_INVALID_SINGLE_LINE_COMMENT,
      ISSUE_INVALID_STRING,
      ISSUE_JCENTER,
      ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS,
      ISSUE_MATCHING_MENU_ID,
      ISSUE_MATCHING_VIEW_ID,
      ISSUE_MISSING_SCROLLBARS,
      ISSUE_MISSING_XML_HEADER,
      ISSUE_NAMING_PATTERN,
      ISSUE_QUOTES,
      ISSUE_RAW_COLOR,
      ISSUE_RAW_DIMEN,
      ISSUE_RESOURCES_GET_COLOR,
      ISSUE_RESOURCES_GET_COLOR_STATE_LIST,
      ISSUE_RESOURCES_GET_DRAWABLE,
      ISSUE_SHOULD_USE_STATIC_IMPORT,
      ISSUE_STRING_NOT_CAPITALIZED,
      ISSUE_SUPERFLUOUS_MARGIN_DECLARATION,
      ISSUE_SUPERFLUOUS_NAME_SPACE,
      ISSUE_SUPERFLUOUS_PADDING_DECLARATION,
      ISSUE_TODO,
      ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE,
      ISSUE_UNUSED_MERGE_ATTRIBUTES,
      ISSUE_WRONG_ANNOTATION_ORDER,
      ISSUE_WRONG_CONSTRAINT_LAYOUT_USAGE,
      ISSUE_WRONG_DRAWABLE_NAME,
      ISSUE_WRONG_GLOBAL_ICON_COLOR,
      ISSUE_WRONG_LAYOUT_NAME,
      ISSUE_WRONG_MENU_ID_FORMAT,
      ISSUE_WRONG_TEST_METHOD_NAME,
      ISSUE_WRONG_VIEW_ID_FORMAT,
      ISSUE_XML_SPACING,
    )
}

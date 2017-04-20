package com.vanniktech.lintrules.android;

import org.junit.Test;

import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_ACTIVITY_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_DIALOG_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_COLOR_STATE_LIST;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_RESOURCES_GET_DRAWABLE;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_VIEW_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.AndroidDetector.ISSUE_WINDOW_FIND_VIEW_BY_ID;
import static com.vanniktech.lintrules.android.MatchingViewIdDetector.ISSUE_MATCHING_VIEW_ID;
import static com.vanniktech.lintrules.android.RawColorDetector.ISSUE_RAW_COLOR;
import static com.vanniktech.lintrules.android.RawDimenDetector.ISSUE_RAW_DIMEN;
import static com.vanniktech.lintrules.android.ShouldUseStaticImportDetector.ISSUE_SHOULD_USE_STATIC_IMPORT;
import static com.vanniktech.lintrules.android.SuperfluousMarginDeclarationDetector.SUPERFLUOUS_MARGIN_DECLARATION;
import static com.vanniktech.lintrules.android.SuperfluousPaddingDeclarationDetector.SUPERFLUOUS_PADDING_DECLARATION;
import static com.vanniktech.lintrules.android.WrongMenuIdFormatDetector.ISSUE_WRONG_MENU_ID_FORMAT;
import static com.vanniktech.lintrules.android.WrongViewIdFormatDetector.ISSUE_WRONG_VIEW_ID_FORMAT;
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
        ISSUE_MATCHING_VIEW_ID
    );
  }
}

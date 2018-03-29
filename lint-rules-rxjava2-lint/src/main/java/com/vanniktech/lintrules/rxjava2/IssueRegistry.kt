package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.CURRENT_API

internal const val PRIORITY = 10 // Does not matter anyways within Lint.

class IssueRegistry : com.android.tools.lint.client.api.IssueRegistry() {
  override val api = CURRENT_API

  override val issues get() = listOf(
      ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE,
      ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR,
      ISSUE_DISPOSABLE_ADD_ALL_CALL,
      ISSUE_DEFAULT_SCHEDULER,
      ISSUE_DISPOSABLE_DISPOSE_CALL,
      ISSUE_SUBSCRIBE_MISSING_ON_ERROR,
      ISSUE_RAW_SCHEDULER_CALL
  )
}

package com.vanniktech.lintrules.rxjava2

import com.vanniktech.lintrules.rxjava2.RxJava2MethodCheckReturnValueDetector.ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE
import com.vanniktech.lintrules.rxjava2.RxJava2MissingCompositeDisposableClearDetector.ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR

class IssueRegistry : com.android.tools.lint.client.api.IssueRegistry() {
  override fun getIssues() = listOf(
      ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE,
      ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR,
      CALLING_COMPOSITE_DISPOSABLE_ADD_ALL,
      DEFAULT_SCHEDULER,
      CALLING_COMPOSITE_DISPOSABLE_DISPOSE,
      SUBSCRIBE_MISSING_ON_ERROR,
      ISSUE_RAW_SCHEDULER_CALL
  )
}

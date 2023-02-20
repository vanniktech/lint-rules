@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.kotlin

import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

internal const val PRIORITY = 10 // Does not matter anyway within Lint.

class IssueRegistry : com.android.tools.lint.client.api.IssueRegistry() {
  override val api get() = CURRENT_API

  override val vendor get() = Vendor(
    vendorName = "vanniktech/lint-rules/",
    feedbackUrl = "https://github.com/vanniktech/lint-rules/issues",
  )

  override val issues get() = listOf(
    ISSUE_KOTLIN_REQUIRE_NOT_NULL_USE_MESSAGE,
  )
}

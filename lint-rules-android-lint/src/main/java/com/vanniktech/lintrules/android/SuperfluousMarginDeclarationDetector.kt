@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import java.util.Arrays.asList

val ISSUE_SUPERFLUOUS_MARGIN_DECLARATION = Issue.create(
  "SuperfluousMarginDeclaration",
  "Flags margin declarations that can be simplified.",
  "Instead of using start-, end-, bottom- and top margins, layout_margin can be used.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(SuperfluousMarginDeclarationDetector::class.java, RESOURCE_FILE_SCOPE),
)

class SuperfluousMarginDeclarationDetector : SuperfluousDeclarationDetector(
  applicableSuperfluousAttributes = asList(
    ATTR_LAYOUT_MARGIN_TOP,
    ATTR_LAYOUT_MARGIN_BOTTOM,
    ATTR_LAYOUT_MARGIN_START,
    ATTR_LAYOUT_MARGIN_END,
  ),
  issue = ISSUE_SUPERFLUOUS_MARGIN_DECLARATION,
  message = "Should be using layout_margin instead.",
)

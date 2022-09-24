@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_PADDING_BOTTOM
import com.android.SdkConstants.ATTR_PADDING_END
import com.android.SdkConstants.ATTR_PADDING_START
import com.android.SdkConstants.ATTR_PADDING_TOP
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import java.util.Arrays.asList

val ISSUE_SUPERFLUOUS_PADDING_DECLARATION = Issue.create(
  "SuperfluousPaddingDeclaration",
  "Flags padding declarations that can be simplified.",
  "Instead of using start-, end-, bottom- and top paddings, padding can be used.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(SuperfluousPaddingDeclarationDetector::class.java, RESOURCE_FILE_SCOPE),
)

class SuperfluousPaddingDeclarationDetector : SuperfluousDeclarationDetector(
  applicableSuperfluousAttributes = asList(
    ATTR_PADDING_TOP,
    ATTR_PADDING_BOTTOM,
    ATTR_PADDING_START,
    ATTR_PADDING_END,
  ),
  issue = ISSUE_SUPERFLUOUS_PADDING_DECLARATION,
  message = "Should be using padding instead.",
)

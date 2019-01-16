package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Project
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.computeResourcePrefix
import org.w3c.dom.Document

private val allowedPrefixes = listOf("activity_", "view_", "fragment_", "dialog_", "bottom_sheet_", "adapter_item_", "divider_", "space_", "popup_window_")

val ISSUE_WRONG_LAYOUT_NAME = Issue.create("WrongLayoutName",
    "Layout names should be prefixed accordingly.",
    "The layout file name should be prefixed with one of the following: ${allowedPrefixes.joinToString()}. This will improve consistency in your code base as well as enforce a certain structure.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(WrongLayoutNameDetector::class.java, RESOURCE_FILE_SCOPE))

class WrongLayoutNameDetector : LayoutDetector() {
  override fun visitDocument(context: XmlContext, document: Document) {
    val modified = allowedPrefixes.map { context.project.resourcePrefix() + it }
    val doesNotStartWithPrefix = modified.none { context.file.name.startsWith(it) }
    val notEquals = modified.map { it.dropLast(1) }.none { context.file.name == "$it.xml" }

    if (doesNotStartWithPrefix && notEquals) {
      context.report(ISSUE_WRONG_LAYOUT_NAME, document, context.getLocation(document), "Layout does not start with one of the following prefixes: ${modified.joinToString()}")
    }
  }
}

fun Project.resourcePrefix() = if (isGradleProject) computeResourcePrefix(gradleProjectModel).orEmpty() else ""

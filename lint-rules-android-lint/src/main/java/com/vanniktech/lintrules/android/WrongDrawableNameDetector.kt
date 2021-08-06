@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Document

private val allowedPrefixes = listOf(
  "ic_",
  "img_",
  "notification_icon",
  "ripple",
  "selector_",
  "vector_",
  "animated_vector_",
  "shape_"
)

val ISSUE_WRONG_DRAWABLE_NAME = Issue.create(
  "WrongDrawableName",
  "Drawable names should be prefixed accordingly.",
  "The drawable file name should be prefixed with one of the following: ${allowedPrefixes.joinToString()}. This will improve consistency in your code base as well as enforce a certain structure.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(WrongDrawableNameDetector::class.java, RESOURCE_FILE_SCOPE)
)

class WrongDrawableNameDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = folderType == ResourceFolderType.DRAWABLE

  override fun visitDocument(context: XmlContext, document: Document) {
    val modified = fileNameSuggestions(allowedPrefixes, context)

    if (modified != null) {
      context.report(ISSUE_WRONG_DRAWABLE_NAME, document, context.getLocation(document), "Drawable does not start with one of the following prefixes: ${modified.joinToString()}")
    }
  }
}

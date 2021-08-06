@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants.TOOLS_URI
import com.android.SdkConstants.VIEW_MERGE
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element

val ISSUE_UNUSED_MERGE_ATTRIBUTES = Issue.create(
  "UnusedMergeAttributes",
  "Flags android and app attributes that are used on a <merge> attribute for custom Views.",
  "Adding android, app and other attributes to <merge> won't be used by the system for custom views and hence can lead to errors.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(UnusedMergeAttributesDetector::class.java, RESOURCE_FILE_SCOPE)
)

class UnusedMergeAttributesDetector : LayoutDetector() {
  override fun getApplicableElements() = listOf(VIEW_MERGE)

  override fun visitElement(context: XmlContext, element: Element) {
    val hasParentTag = element.parentTag().isNotEmpty()

    if (hasParentTag) {
      element.attributes()
        .filterNot { it.hasToolsNamespace() }
        .filterNot { it.prefix == "xmlns" }
        .forEach {
          val fix = fix().name("Change to tools").composite(
            fix().set(TOOLS_URI, it.localName, it.nodeValue).build(),
            fix().unset(it.namespaceURI, it.localName).build()
          ).autoFix()

          context.report(ISSUE_UNUSED_MERGE_ATTRIBUTES, it, context.getLocation(it), "Attribute won't be used", fix)
        }
    }
  }
}

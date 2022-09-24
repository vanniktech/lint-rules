@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants
import com.android.SdkConstants.CONSTRAINT_LAYOUT
import com.android.SdkConstants.FRAME_LAYOUT
import com.android.SdkConstants.IMAGE_VIEW
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.forEach
import org.w3c.dom.Element

val ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE = Issue.create(
  id = "ErroneousLayoutAttribute",
  briefDescription = "Layout attribute that's not applicable to a particular view.",
  explanation = "Flags if a layout attribute is not applicable to a particular view.",
  category = Category.CORRECTNESS,
  priority = PRIORITY,
  severity = WARNING,
  implementation = Implementation(ErroneousLayoutAttributeDetector::class.java, RESOURCE_FILE_SCOPE),
)

val ERRONEOUS_LAYOUT_ATTRIBUTES = mapOf(
  CONSTRAINT_LAYOUT.newName() to listOf(
    SdkConstants.ATTR_ORIENTATION,
    SdkConstants.ATTR_GRAVITY,
    SdkConstants.ATTR_SCALE_TYPE,
  ),
  IMAGE_VIEW to listOf(
    "maxLines",
  ),
  FRAME_LAYOUT to listOf(
    SdkConstants.ATTR_ORIENTATION,
    SdkConstants.ATTR_GRAVITY,
  ),
)

class ErroneousLayoutAttributeDetector : LayoutDetector() {
  override fun getApplicableElements() = ALL

  override fun visitElement(
    context: XmlContext,
    element: Element,
  ) {
    val layoutName = element.layoutName()
    val erroneousAttributes = ERRONEOUS_LAYOUT_ATTRIBUTES[layoutName].orEmpty()

    if (erroneousAttributes.isNotEmpty()) {
      element.attributes.forEach { attribute ->
        erroneousAttributes.forEach { erroneousAttribute ->
          val tag = attribute.layoutAttribute()

          if (erroneousAttribute == tag) {
            context.report(
              issue = ISSUE_ERRONEOUS_LAYOUT_ATTRIBUTE,
              scope = attribute,
              location = context.getLocation(attribute),
              message = "Attribute is erroneous on $layoutName",
              quickfixData = fix()
                .unset(attribute.namespaceURI, attribute.localName)
                .name("Delete erroneous attribute")
                .autoFix()
                .build(),
            )
          }
        }
      }
    }
  }
}

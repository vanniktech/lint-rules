package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_ORIENTATION
import com.android.SdkConstants.RELATIVE_LAYOUT
import com.android.SdkConstants.SCROLL_VIEW
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr

@Suppress("Detekt.VariableMaxLength") val ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE = Issue.create("UnsupportedLayoutAttribute",
    "Marks layout attributes which are not supported.",
    "Some layout attributes are not supported. Your app will still compile but it makes no sense to have them around. This can happen when refactoring a LinearLayout to a ScrollView. The orientation is no longer needed and can be removed.",
    Category.CORRECTNESS, PRIORITY, ERROR,
    Implementation(UnsupportedLayoutAttributeDetector::class.java, Scope.RESOURCE_FILE_SCOPE))

private val UNSUPPORTED_ATTRIBUTES = mapOf(
    RELATIVE_LAYOUT to ATTR_ORIENTATION,
    SCROLL_VIEW to ATTR_ORIENTATION
)

class UnsupportedLayoutAttributeDetector : LayoutDetector() {
  override fun getApplicableAttributes() = ALL

  override fun visitAttribute(context: XmlContext, attribute: Attr) {
    UNSUPPORTED_ATTRIBUTES
        .filter { attribute.hasOwner(it.key) }
        .filter { attribute.localName == it.value }
        .forEach { (value, key) ->
          val fix = fix().name("Remove unnecessary attribute").unset(attribute.namespaceURI, key).build()
          context.report(ISSUE_UNSUPPORTED_LAYOUT_ATTRIBUTE, context.getLocation(attribute), "$key is not allowed in $value", fix)
        }
  }
}

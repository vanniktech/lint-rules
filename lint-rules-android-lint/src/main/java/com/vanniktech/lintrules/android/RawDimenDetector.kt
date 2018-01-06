package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_LAYOUT_HEIGHT
import com.android.SdkConstants.ATTR_LAYOUT_WIDTH
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.DRAWABLE
import com.android.resources.ResourceFolderType.LAYOUT
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import org.w3c.dom.Element
import java.util.EnumSet

val ISSUE_RAW_DIMEN = Issue.create("RawDimen",
    "Flags dimensions that are not defined as resource.",
    "Dimensions should all be defined as dimension resources. This has the benefit that you can easily see all of your dimensions in one file. One benefit is that when designers change the outline across the entire app you only have to adjust it in one place. This check will run on layouts as well as xml drawables.",
    CORRECTNESS, 8, WARNING,
    Implementation(RawDimenDetector::class.java, RESOURCE_FILE_SCOPE))

class RawDimenDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(LAYOUT, DRAWABLE).contains(folderType)

  override fun getApplicableElements() = ALL

  override fun visitElement(context: XmlContext, element: Element) {
    val hasLayoutWeight = element.attributes.getNamedItem("android:layout_weight") != null
    val isParentConstraintLayout = element.hasParent(CONSTRAINT_LAYOUT)
    val isVectorGraphic = "vector" == element.localName || "path" == element.localName

    (0 until element.attributes.length)
        .map { element.attributes.item(it) }
        .filterNot { it.hasToolsNamespace() }
        .filterNot { isVectorGraphic }
        .filterNot { (hasLayoutWeight || isParentConstraintLayout) && it.nodeValue[0] == '0' && (ATTR_LAYOUT_WIDTH == it.localName || ATTR_LAYOUT_HEIGHT == it.localName) }
        .filter { it.nodeValue.matches("-?[\\d.]+(sp|dp|dip)".toRegex()) }
        .forEach { context.report(ISSUE_RAW_DIMEN, it, context.getValueLocation(it as Attr), "Should be using a dimension resource instead.") }
  }

  companion object {
    const val CONSTRAINT_LAYOUT = "android.support.constraint.ConstraintLayout"
  }
}

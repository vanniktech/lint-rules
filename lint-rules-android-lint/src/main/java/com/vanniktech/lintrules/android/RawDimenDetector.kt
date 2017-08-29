package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_LAYOUT_HEIGHT
import com.android.SdkConstants.ATTR_LAYOUT_WIDTH
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.DRAWABLE
import com.android.resources.ResourceFolderType.LAYOUT
import com.android.tools.lint.detector.api.Category.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import org.w3c.dom.Element

@JvmField val ISSUE_RAW_DIMEN = Issue.create("RawDimen",
    "This value should be defined as a dimen.",
    "This value should be defined as a dimen.", CORRECTNESS, 8, WARNING,
    Implementation(RawDimenDetector::class.java, RESOURCE_FILE_SCOPE))

class RawDimenDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = folderType == LAYOUT || folderType == DRAWABLE

  override fun getApplicableElements() = Detector.XmlScanner.ALL

  override fun visitElement(context: XmlContext, element: Element) {
    val hasLayoutWeight = element.attributes.getNamedItem("android:layout_weight") != null
    val isParentConstraintLayout = CONSTRAINT_LAYOUT == element.parentNode?.localName || CONSTRAINT_LAYOUT == element.parentNode?.attributes?.getNamedItem("tools:parentTag")?.nodeValue
    val isVectorGraphic = "vector" == element.localName || "path" == element.localName

    (0 until element.attributes.length)
        .map { element.attributes.item(it) }
        .filterNot { it.hasToolsNamespace() }
        .filterNot { context.driver.isSuppressed(context, ISSUE_RAW_DIMEN, it) }
        .filterNot { isVectorGraphic }
        .filterNot { (hasLayoutWeight || isParentConstraintLayout) && it.nodeValue[0] == '0' && (ATTR_LAYOUT_WIDTH == it.localName || ATTR_LAYOUT_HEIGHT == it.localName) }
        .filter { it.nodeValue.matches("-?[\\d.]+(sp|dp|dip)".toRegex()) }
        .forEach { context.report(ISSUE_RAW_DIMEN, context.getValueLocation(it as Attr), "Should be using dimen instead.") }
  }

  companion object {
    const val CONSTRAINT_LAYOUT = "android.support.constraint.ConstraintLayout"
  }
}

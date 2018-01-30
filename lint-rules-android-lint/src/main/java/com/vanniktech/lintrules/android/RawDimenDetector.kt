package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_LAYOUT_HEIGHT
import com.android.SdkConstants.ATTR_LAYOUT_WIDTH
import com.android.SdkConstants.CLASS_CONSTRAINT_LAYOUT
import com.android.SdkConstants.TAG_DIMEN
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.DRAWABLE
import com.android.resources.ResourceFolderType.LAYOUT
import com.android.resources.ResourceFolderType.VALUES
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Context
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
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(RawDimenDetector::class.java, RESOURCE_FILE_SCOPE))

class RawDimenDetector : ResourceXmlDetector() {
  private var collector = ElementCollectReporter(TAG_DIMEN)

  override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(LAYOUT, DRAWABLE, VALUES).contains(folderType)

  override fun getApplicableElements() = ALL

  override fun beforeCheckProject(context: Context) = beforeCheckLibraryProject(context)

  override fun beforeCheckLibraryProject(context: Context) {
    collector = ElementCollectReporter(TAG_DIMEN)
  }

  override fun visitElement(context: XmlContext, element: Element) {
    collector.collect(element)

    val hasLayoutWeight = element.attributes.getNamedItem("android:layout_weight") != null
    val isParentConstraintLayout = element.hasParent(CLASS_CONSTRAINT_LAYOUT)
    val isVectorGraphic = "vector" == element.localName || "path" == element.localName

    (0 until element.attributes.length)
        .map { element.attributes.item(it) }
        .filterNot { it.hasToolsNamespace() }
        .filterNot { isVectorGraphic }
        .filterNot { (hasLayoutWeight || isParentConstraintLayout) && it.nodeValue[0] == '0' && (ATTR_LAYOUT_WIDTH == it.localName || ATTR_LAYOUT_HEIGHT == it.localName) }
        .filter { it.nodeValue.matches("-?[\\d.]+(sp|dp|dip)".toRegex()) }
        .filterNot { context.driver.isSuppressed(context, ISSUE_RAW_DIMEN, it) }
        .map { it to context.getValueLocation(it as Attr) }
        .toCollection(collector)
  }

  override fun afterCheckProject(context: Context) = afterCheckLibraryProject(context)

  override fun afterCheckLibraryProject(context: Context) {
    collector.report(ISSUE_RAW_DIMEN, context, "Should be using a dimension resource instead.")
  }
}

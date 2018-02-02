package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_COLOR
import com.android.SdkConstants.ATTR_PATH
import com.android.SdkConstants.TAG_VECTOR
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

val ISSUE_RAW_COLOR = Issue.create("RawColor",
    "Flags color that are not defined as resource.",
    "Color value should all be defined as color resources. This has the benefit that you can easily see all of your colors in one file. One benefit is an easier addition to Dark Theme for instance. This check will run on layouts as well as xml drawables.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(RawColorDetector::class.java, RESOURCE_FILE_SCOPE))

class RawColorDetector : ResourceXmlDetector() {
  private var collector = ElementCollectReporter(ATTR_COLOR)

  override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(LAYOUT, DRAWABLE, VALUES).contains(folderType)

  override fun getApplicableElements() = ALL

  override fun beforeCheckProject(context: Context) = beforeCheckLibraryProject(context)

  override fun beforeCheckLibraryProject(context: Context) {
    collector = ElementCollectReporter(ATTR_COLOR)
  }

  override fun visitElement(context: XmlContext, element: Element) {
    collector.collect(element)

    (0 until element.attributes.length)
        .map { element.attributes.item(it) }
        .filterNot { TAG_VECTOR == element.localName || ATTR_PATH == element.localName }
        .filterNot { it.hasToolsNamespace() }
        .filter { it.nodeValue.matches("#[a-fA-F\\d]{3,8}".toRegex()) }
        .filterNot { context.driver.isSuppressed(context, ISSUE_RAW_COLOR, it) }
        .map { it to context.getValueLocation(it as Attr) }
        .toCollection(collector)
  }

  override fun afterCheckProject(context: Context) = afterCheckLibraryProject(context)

  override fun afterCheckLibraryProject(context: Context) {
    collector.report(ISSUE_RAW_COLOR, context, "Should be using a color resource instead.")
  }
}

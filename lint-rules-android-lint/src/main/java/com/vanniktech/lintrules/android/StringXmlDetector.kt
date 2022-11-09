@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.SdkConstants.TAG_PLURALS
import com.android.SdkConstants.TAG_STRING
import com.android.SdkConstants.TAG_STRING_ARRAY
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.VALUES
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.EnumSet

abstract class StringXmlDetector : ResourceXmlDetector() {
  final override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(VALUES).contains(folderType)

  final override fun getApplicableElements() = listOf(TAG_STRING, TAG_STRING_ARRAY, TAG_PLURALS)

  final override fun visitElement(context: XmlContext, element: Element) {
    element.children()
      .forEach { child ->
        val isStringResource = child.isTextNode() && TAG_STRING == element.localName
        val isStringArrayOrPlurals = child.isElementNode() && (TAG_STRING_ARRAY == element.localName || TAG_PLURALS == element.localName)

        if (isStringResource) {
          checkText(context, element, child)
        } else if (isStringArrayOrPlurals) {
          child.children()
            .filter { it.isTextNode() }
            .forEach { checkText(context, child, it) }
        }
      }
  }

  abstract fun checkText(context: XmlContext, node: Node, textNode: Node)
}

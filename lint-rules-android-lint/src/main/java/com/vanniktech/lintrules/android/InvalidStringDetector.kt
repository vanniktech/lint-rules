package com.vanniktech.lintrules.android

import com.android.SdkConstants.TAG_PLURALS
import com.android.SdkConstants.TAG_STRING
import com.android.SdkConstants.TAG_STRING_ARRAY
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.VALUES
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.Arrays.asList
import java.util.EnumSet

val ISSUE_INVALID_STRING = Issue.create("InvalidString",
    "Marks invalid translation strings.",
    "A translation string is invalid if it contains new lines instead of the escaped \\\n or if it contains trailing whitespace.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(InvalidStringDetector::class.java, RESOURCE_FILE_SCOPE))

class InvalidStringDetector : ResourceXmlDetector() {
  override fun appliesTo(folderType: ResourceFolderType) = EnumSet.of(VALUES).contains(folderType)

  override fun getApplicableElements() = asList(TAG_STRING, TAG_STRING_ARRAY, TAG_PLURALS)

  override fun visitElement(context: XmlContext, element: Element) {
    element.children()
        .forEach { child ->
          val isStringResource = child.isTextNode() && TAG_STRING == element.localName
          val isStringArrayOrPlurals = child.isElementNode() && (TAG_STRING_ARRAY == element.localName || TAG_PLURALS == element.localName)

          if (isStringResource) {
            checkText(context, element, child.nodeValue)
          } else if (isStringArrayOrPlurals) {
            child.children()
                .filter { it.isTextNode() }
                .forEach { checkText(context, child, it.nodeValue) }
          }
        }
  }

  private fun checkText(context: XmlContext, element: Node, text: String) {
    val message = when {
      text.contains("\n") -> "Text contains new line."
      text.length != text.trim().length -> "Text contains trailing whitespace."
      else -> null
    }

    message?.let {
      val fix = fix().replace().name("Fix it").text(text).with(text.trim()).build()
      context.report(ISSUE_INVALID_STRING, element, context.getLocation(element), it, fix)
    }
  }
}

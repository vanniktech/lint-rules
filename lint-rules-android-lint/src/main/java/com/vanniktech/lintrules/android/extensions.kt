package com.vanniktech.lintrules.android

import com.android.SdkConstants.TOOLS_URI
import com.vanniktech.lintrules.android.MatchingViewIdDetector.toLowerCamelCase
import org.w3c.dom.Attr
import org.w3c.dom.Node

internal fun Node.hasToolsNamespace() = TOOLS_URI.equals(namespaceURI, ignoreCase = true)

internal fun Node.hasParent(parent: String) = parentNode.isOf(parent)

internal fun Attr.hasOwner(parent: String) = ownerElement.isOf(parent)

internal fun Node.isOf(value: String) = value == localName || value == attributes?.getNamedItem("tools:parentTag")?.nodeValue

internal fun String.isLowerCamelCase() = !Character.isUpperCase(this[0]) && !contains("_")

internal fun String.idToSnakeCase(): String {
  val parts = split("/")
  return "${parts[0]}/${toLowerCamelCase(parts[1])}"
}

internal fun Node.children() = (0 until childNodes.length).map { childNodes.item(it) }

internal fun Node.isTextNode() = nodeType == Node.TEXT_NODE

internal fun Node.isElementNode() = nodeType == Node.ELEMENT_NODE

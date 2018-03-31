@file:Suppress("Detekt.TooManyFunctions")

package com.vanniktech.lintrules.android

import com.android.SdkConstants.TOOLS_URI
import com.google.common.base.CaseFormat
import com.vanniktech.lintrules.android.MatchingViewIdDetector.toLowerCamelCase
import org.w3c.dom.Attr
import org.w3c.dom.Node

internal fun Node.hasToolsNamespace() = TOOLS_URI.equals(namespaceURI, ignoreCase = true)

internal fun Node.hasParent(parent: String) = parentNode.isOf(parent)

internal fun Attr.hasOwner(parent: String) = ownerElement.isOf(parent)

internal fun Node.isOf(value: String) = value == localName || value == attributes?.getNamedItem("tools:parentTag")?.nodeValue

internal fun String.isLowerCamelCase() = !Character.isUpperCase(this[0]) && !contains("_")

internal fun String.idToLowerCamelCase(): String {
  val parts = split("/")
  return "${parts[0]}/${toLowerCamelCase(parts[1])}"
}

internal fun String.toSnakeCase(): String =
    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this)

internal fun Node.children() = (0 until childNodes.length).map { childNodes.item(it) }

internal fun Node.isTextNode() = nodeType == Node.TEXT_NODE

internal fun Node.isElementNode() = nodeType == Node.ELEMENT_NODE

internal fun Node.attributes() = (0 until attributes.length).map { attributes.item(it) }

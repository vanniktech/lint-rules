@file:Suppress("Detekt.TooManyFunctions")

package com.vanniktech.lintrules.android

import com.android.SdkConstants.TOOLS_URI
import org.w3c.dom.Attr
import org.w3c.dom.Node
import java.util.Locale.US
import java.util.regex.Pattern

internal fun Node.hasToolsNamespace() = TOOLS_URI.equals(namespaceURI, ignoreCase = true)

internal fun Node.hasParent(parent: String) = parentNode.isOf(parent)

internal fun Attr.hasOwner(parent: String) = ownerElement.isOf(parent)

internal fun Node.isOf(value: String) = value == localName || value == attributes?.getNamedItem("tools:parentTag")?.nodeValue

internal fun String.isLowerCamelCase() = !Character.isUpperCase(this[0]) && !contains("_")

internal fun String.idToLowerCamelCase(): String {
  val parts = split("/")
  return "${parts[0]}/${parts[1].toLowerCamelCase()}"
}

internal fun String.toSnakeCase() = toCharArray().fold("", { accumulator, current ->
  val prefix = when {
    current.isUpperCase() && accumulator.lastOrNull()?.isDigit() == false -> "_"
    else -> ""
  }

  accumulator + prefix + current.toLowerCase()
})

internal fun Node.children() = (0 until childNodes.length).map { childNodes.item(it) }

internal fun Node.isTextNode() = nodeType == Node.TEXT_NODE

internal fun Node.isElementNode() = nodeType == Node.ELEMENT_NODE

internal fun Node.attributes() = (0 until attributes.length).map { attributes.item(it) }

internal fun String.toLowerCamelCase(): String {
  val p = Pattern.compile("_(.)")
  val matcher = p.matcher(this)
  val sb = StringBuffer()

  while (matcher.find()) {
    matcher.appendReplacement(sb, matcher.group(1).toUpperCase(US))
  }

  matcher.appendTail(sb)

  sb.setCharAt(0, Character.toLowerCase(sb[0]))
  return sb.toString()
}

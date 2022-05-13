@file:Suppress("Detekt.TooManyFunctions")

package com.vanniktech.lintrules.android

import com.android.SdkConstants.ATTR_PARENT_TAG
import com.android.SdkConstants.TOOLS_URI
import com.android.tools.lint.detector.api.Project
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.Locale.US
import java.util.regex.Pattern

internal fun Element.layoutName() = when {
  nodeName == "merge" -> parentTag()
  else -> nodeName
}

internal fun Element.parentTag() = getAttributeNS(TOOLS_URI, ATTR_PARENT_TAG)

internal fun Node.layoutAttribute() = nodeName.split(":").getOrNull(1)

internal fun Node.hasToolsNamespace() = TOOLS_URI.equals(namespaceURI, ignoreCase = true)

internal fun Node.hasParent(parent: String) = parentNode.isOf(parent)

internal fun Attr.hasOwner(parent: String) = ownerElement.isOf(parent)

internal fun Node.isOf(value: String) = value == localName || value == attributes?.getNamedItem("tools:parentTag")?.nodeValue

internal fun String.isLowerCamelCase() = !Character.isUpperCase(this[0]) && !contains("_")

internal fun String.idToLowerCamelCase(): String {
  val parts = split("/")
  return "${parts[0]}/${parts[1].toLowerCamelCase()}"
}

internal fun String.toSnakeCase() = toCharArray().fold("") { accumulator, current ->
  val prefix = when {
    current.isUpperCase() && accumulator.lastOrNull()?.isDigit() == false -> "_"
    else -> ""
  }

  accumulator + prefix + current.toLowerCase()
}

internal fun Node.children() = (0 until childNodes.length).map { childNodes.item(it) }

internal fun Node.isTextNode() = nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE

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

internal fun String.forceUnderscoreIfNeeded() = if (isNotEmpty() && !endsWith("_")) plus("_") else this

internal fun Project.resourcePrefix() = buildModule?.resourcePrefix.orEmpty()

internal fun fileNameSuggestions(allowedPrefixes: List<String>, context: XmlContext): List<String>? {
  val modified = allowedPrefixes.map {
    val resourcePrefix = context.project.resourcePrefix()
      .forceUnderscoreIfNeeded()

    if (resourcePrefix != it) resourcePrefix + it else it
  }

  val doesNotStartWithPrefix = modified.none { context.file.name.startsWith(it) }
  val notEquals = modified.map {
    it.dropLast(1) // Drop the trailing underscore.
  }.none { context.file.name == "$it.xml" }

  return modified.takeIf { doesNotStartWithPrefix && notEquals }
}

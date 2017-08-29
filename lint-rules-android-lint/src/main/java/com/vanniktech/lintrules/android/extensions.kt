package com.vanniktech.lintrules.android

import com.android.SdkConstants.TOOLS_URI
import org.w3c.dom.Attr
import org.w3c.dom.Node

internal fun Node.hasToolsNamespace() = TOOLS_URI.equals(namespaceURI, ignoreCase = true)

internal fun Node.hasParent(parent: String) = parentNode.isOf(parent)

internal fun Attr.hasOwner(parent: String) = ownerElement.isOf(parent)

internal fun Node.isOf(value: String) = value == localName || value == attributes?.getNamedItem("tools:parentTag")?.nodeValue

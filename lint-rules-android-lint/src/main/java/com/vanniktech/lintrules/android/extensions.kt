package com.vanniktech.lintrules.android

import com.android.SdkConstants.TOOLS_URI
import org.w3c.dom.Node

internal fun Node.hasToolsNamespace() = TOOLS_URI.equals(namespaceURI, ignoreCase = true)

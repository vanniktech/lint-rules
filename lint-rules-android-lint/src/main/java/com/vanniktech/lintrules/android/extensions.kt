package com.vanniktech.lintrules.android

import org.w3c.dom.Node

internal fun Node.hasToolsNamespace() = "http://schemas.android.com/tools".equals(namespaceURI, ignoreCase = true)

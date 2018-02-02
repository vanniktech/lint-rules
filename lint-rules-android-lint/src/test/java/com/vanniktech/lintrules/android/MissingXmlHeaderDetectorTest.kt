package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class MissingXmlHeaderDetectorTest {
  @Test fun hasXmlHeader() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<?xml version="1.0" encoding="UTF-8" standalone="no"?>
          |<resources/>""".trimMargin())
            )
        .issues(ISSUE_MISSING_XML_HEADER)
        .run()
        .expectClean()
  }

  @Test fun missingHeader() {
    lint()
        .files(xml("res/values/strings.xml", """
          |<resources/>""".trimMargin())
            )
        .issues(ISSUE_MISSING_XML_HEADER)
        .run()
        .expect("""
          |res/values/strings.xml:1: Warning: Missing an xml header. [MissingXmlHeader]
          |<resources/>
          |~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for res/values/strings.xml line 0: Add xml header:
          |@@ -1 +1
          |+ <?xml version="1.0" encoding="utf-8"?>
          |""".trimMargin())
  }
}

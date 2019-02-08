package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import org.jetbrains.uast.UImportStatement
import java.util.EnumSet

val ISSUE_ASSERTJ_IMPORT = Issue.create("AssertjImport",
    "Flags Java 6 incompatible imports.",
    "Importing org.assertj.core.api.Assertions is not ideal. Since it can require Java 8. It's simple as instead org.assertj.core.api.Java6Assertions can be imported and provides guarantee to run on Java 6 as well.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(AssertjDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES), EnumSet.of(JAVA_FILE, TEST_SOURCES))
)

class AssertjDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

  override fun createUastHandler(context: JavaContext) = AssertjDetectorHandler(context)

  class AssertjDetectorHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitImportStatement(node: UImportStatement) {
      node.importReference?.let { importReference ->
        val import = importReference.asSourceString()

        if (import.startsWith("org.assertj.core.api.Assertions")) {
          val fix = LintFix.create()
              .replace()
              .text(import)
              .with(import.replace("org.assertj.core.api.Assertions", "org.assertj.core.api.Java6Assertions"))
              .autoFix()
              .build()

          context.report(ISSUE_ASSERTJ_IMPORT, node, context.getLocation(importReference), "Should use Java6Assertions instead", fix)
        }
      }
    }
  }
}

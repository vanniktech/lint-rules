package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import org.jetbrains.uast.UImportStatement
import java.util.EnumSet

val ISSUE_INVALID_IMPORT = Issue.create("InvalidImport",
    "Flags invalid imports.",
    "Flags invalid imports. One example is com.foo.bar.R.drawable. Instead just the generated class R should be imported and not R.drawable. Also you should never import anything that's in an internal package.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(InvalidImportDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

private val DISALLOWED_IMPORTS = listOf(".R.", "internal.", "internaI.")

class InvalidImportDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

  override fun createUastHandler(context: JavaContext) = InvalidImportHandler(context)

  class InvalidImportHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitImportStatement(importStatement: UImportStatement) {
      importStatement.importReference?.let { importReference ->
        if (DISALLOWED_IMPORTS.any { importReference.asSourceString().contains(it) }) {
          context.report(ISSUE_INVALID_IMPORT, importStatement, context.getLocation(importReference), "Forbidden import")
        }
      }
    }
  }
}

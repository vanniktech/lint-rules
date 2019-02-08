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
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UMethod
import java.util.EnumSet

val ISSUE_WRONG_TEST_METHOD_NAME = Issue.create("WrongTestMethodName",
    "Flags test methods that start with test.",
    "The @Test annotation already states that this is a test hence the test prefix is not necessary.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(WrongTestMethodNameDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES), EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class WrongTestMethodNameDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext) = WrongTestMethodNameVisitor(context)

  class WrongTestMethodNameVisitor(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(node: UMethod) {
      context.evaluator.getAllAnnotations(node as UAnnotated, true)
          .mapNotNull { it.qualifiedName?.split(".")?.lastOrNull() }
          .filter { it == "Test" }
          .filter { node.name.startsWith("test", ignoreCase = true) }
          .forEach { _ ->
            val fix = LintFix.create()
                .name("Remove test prefix")
                .replace()
                .text(node.name)
                .with(node.name.replace("test", "", ignoreCase = true).decapitalize())
                .autoFix()
                .build()
            context.report(ISSUE_WRONG_TEST_METHOD_NAME, node, context.getNameLocation(node), "Test method starts with test.", fix)
          }
    }
  }
}

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
import org.jetbrains.uast.UMethod
import java.util.EnumSet

@JvmField val ISSUE_WRONG_TEST_METHOD_NAME = Issue.create("WrongTestMethodName", "A test method name should not start with test.",
    "A test method name should not start with test.", CORRECTNESS, 6, WARNING,
    Implementation(WrongTestMethodNameDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class WrongTestMethodNameDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext) = WrongTestMethodNameVisitor(context)

  class WrongTestMethodNameVisitor(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(method: UMethod) {
      context.evaluator.getAllAnnotations(method, true)
          .mapNotNull { it.qualifiedName?.split(".")?.lastOrNull() }
          .filter { it == "Test" }
          .filter { method.name.startsWith("test", ignoreCase = true) }
          .forEach { context.report(ISSUE_WRONG_TEST_METHOD_NAME, method, context.getNameLocation(method), "Test method starts with test.") }
    }
  }
}

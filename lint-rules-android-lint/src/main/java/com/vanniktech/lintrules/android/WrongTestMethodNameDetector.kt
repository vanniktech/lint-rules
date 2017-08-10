package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiMethod
import java.util.EnumSet

@JvmField val ISSUE_WRONG_TEST_METHOD_NAME = Issue.create("WrongTestMethodName", "A test method name should not start with test.",
    "A test method name should not start with test.", CORRECTNESS, 6, WARNING,
    Implementation(WrongTestMethodNameDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class WrongTestMethodNameDetector : Detector(), Detector.JavaPsiScanner {
  override fun getApplicablePsiTypes() = listOf(PsiMethod::class.java)

  override fun createPsiVisitor(context: JavaContext) = WrongTestMethodNameVisitor(context)

  class WrongTestMethodNameVisitor(private val context: JavaContext) : JavaElementVisitor() {
    override fun visitMethod(method: PsiMethod) {
      method.modifierList.annotations
          .map { it.qualifiedName?.split(".")?.lastOrNull() }
          .filterNotNull()
          .filter { it == "Test" }
          .filter { method.name.startsWith("test", ignoreCase = true) }
          .forEach {
            context.report(ISSUE_WRONG_TEST_METHOD_NAME, context.getLocation(method.nameIdentifier), "Test method starts with test.")
          }
    }
  }
}

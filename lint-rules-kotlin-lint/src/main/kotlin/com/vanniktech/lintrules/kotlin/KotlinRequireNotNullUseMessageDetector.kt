@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.kotlin

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.EnumSet

val ISSUE_KOTLIN_REQUIRE_NOT_NULL_USE_MESSAGE = Issue.create(
  "KotlinRequireNotNullUseMessage",
  "Marks usage of the requireNotNull method without lazy messages.",
  "The default generated message from requireNotNull often lacks context, hence it's best to provide a custom message.",
  CORRECTNESS,
  PRIORITY,
  WARNING,
  Implementation(KotlinRequireNotNullUseMessageDetector::class.java, EnumSet.of(JAVA_FILE)),
)

class KotlinRequireNotNullUseMessageDetector :
  Detector(),
  Detector.UastScanner {
  override fun getApplicableMethodNames() = listOf("requireNotNull")

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    if (node.valueArgumentCount == 1) {
      context.report(ISSUE_KOTLIN_REQUIRE_NOT_NULL_USE_MESSAGE, node, context.getNameLocation(node), "Provide a message")
    }
  }
}

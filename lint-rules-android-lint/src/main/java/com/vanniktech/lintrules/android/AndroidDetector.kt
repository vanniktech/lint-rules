package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.MESSAGES
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.Arrays.asList
import java.util.EnumSet

val ISSUE_RESOURCES_GET_DRAWABLE = Issue.create("ResourcesGetDrawableCall", "Marks usage of deprecated getDrawable() on Resources.",
    "Instead of getDrawable(), ContextCompat or the method with the Theme Overload should be used instead.",
    MESSAGES, PRIORITY, WARNING,
    Implementation(AndroidDetector::class.java, EnumSet.of(JAVA_FILE)))

val ISSUE_RESOURCES_GET_COLOR = Issue.create("ResourcesGetColorCall", "Marks usage of deprecated getColor() on Resources.",
    "Instead of getColor(), ContextCompat or the method with the Theme Overload should be used instead.",
    MESSAGES, PRIORITY, WARNING,
    Implementation(AndroidDetector::class.java, EnumSet.of(JAVA_FILE)))

val ISSUE_RESOURCES_GET_COLOR_STATE_LIST = Issue.create("ResourcesGetColorStateListCall", "Marks usage of deprecated getColorStateList() on Resources.",
    "Instead of getColorStateList(), ContextCompat or the method with the Theme Overload should be used instead.",
    MESSAGES, PRIORITY, WARNING,
    Implementation(AndroidDetector::class.java, EnumSet.of(JAVA_FILE)))

class AndroidDetector : Detector(), Detector.UastScanner {
  override fun getApplicableMethodNames() = asList("getColor", "getDrawable", "getColorStateList")

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val isInResources = context.evaluator.isMemberInClass(method, "android.content.res.Resources")
    val methodName = node.methodName

    if ("getDrawable" == methodName && isInResources) {
      context.report(ISSUE_RESOURCES_GET_DRAWABLE, node, context.getNameLocation(node), "Calling deprecated getDrawable.")
    }

    if ("getColor" == methodName && isInResources) {
      context.report(ISSUE_RESOURCES_GET_COLOR, node, context.getNameLocation(node), "Calling deprecated getColor.")
    }

    if ("getColorStateList" == methodName && isInResources) {
      context.report(ISSUE_RESOURCES_GET_COLOR_STATE_LIST, node, context.getNameLocation(node), "Calling deprecated getColorStateList.")
    }
  }
}

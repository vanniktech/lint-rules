package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.tryResolve
import org.jetbrains.uast.visitor.AbstractUastVisitor
import java.util.EnumSet

val ISSUE_DEFAULT_SCHEDULER = Issue.create("RxJava2DefaultScheduler",
    "Pass a scheduler instead of relying on the default Scheduler.",
    "Calling this method will rely on a default scheduler. This is not necessary the best default. Being explicit and taking the overload for passing one is preferred.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(RxJava2DefaultSchedulerDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class RxJava2DefaultSchedulerDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext) = RxJava2DefaultSchedulerHandler(context)

  inner class RxJava2DefaultSchedulerHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(node: UMethod) {
      node.accept(RxJava2DefaultSchedulerVisitor(context))
    }
  }

  inner class RxJava2DefaultSchedulerVisitor(private val context: JavaContext) : AbstractUastVisitor() {
    override fun visitCallExpression(node: UCallExpression): Boolean {
      val identifier = node.methodIdentifier

      identifier?.uastParent?.let {
        val method = it.tryResolve() as? PsiMethod
        val annotation = AnnotationUtil.findAnnotation(method, "io.reactivex.annotations.SchedulerSupport")

        if (annotation != null) {
          val value = AnnotationUtil.getStringAttributeValue(annotation, null)

          if (!("none" == value || "custom" == value)) {
            context.report(ISSUE_DEFAULT_SCHEDULER, context.getNameLocation(node), "${identifier.name}() is using its default scheduler.")
          }
        }
      }

      return false
    }
  }
}

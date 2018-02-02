package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import java.util.EnumSet

val ISSUE_RAW_SCHEDULER_CALL = Issue.create("RxJava2SchedulersFactoryCall",
    "Instead of calling the Schedulers factory methods directly inject the Schedulers.",
    "Injecting the Schedulers instead of accessing them via the factory methods has the benefit that unit testing is way easier. Instead of overriding them via the Plugin mechanism we can just pass a custom Scheduler.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(RxJava2SchedulersFactoryCallDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

private val SCHEDULERS_METHODS = listOf("io", "computation", "newThread", "single", "from")
private val ANDROID_SCHEDULERS_METHODS = listOf("mainThread")

class RxJava2SchedulersFactoryCallDetector : Detector(), UastScanner {
  override fun getApplicableMethodNames() = SCHEDULERS_METHODS + ANDROID_SCHEDULERS_METHODS

  override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val evaluator = context.evaluator

    val isInSchedulers = evaluator.isMemberInClass(method, "io.reactivex.schedulers.Schedulers")
    val isInAndroidSchedulers = evaluator.isMemberInClass(method, "io.reactivex.android.schedulers.AndroidSchedulers")

    val isSchedulersMatch = SCHEDULERS_METHODS.contains(node.methodName) && isInSchedulers
    val isAndroidSchedulersMatch = ANDROID_SCHEDULERS_METHODS.contains(node.methodName) && isInAndroidSchedulers

    val shouldIgnore = node.isCalledFromProvidesMethod(context)

    if ((isSchedulersMatch || isAndroidSchedulersMatch) && !shouldIgnore) {
      context.report(ISSUE_RAW_SCHEDULER_CALL, node, context.getNameLocation(node), "Inject this Scheduler instead of calling it directly.")
    }
  }

  private fun UElement.isCalledFromProvidesMethod(context: JavaContext): Boolean {
    if (this is UMethod) {
      return context.evaluator.getAllAnnotations(psi, false)
          .any { "dagger.Provides" == it.qualifiedName }
    }

    return uastParent?.isCalledFromProvidesMethod(context) ?: false
  }
}

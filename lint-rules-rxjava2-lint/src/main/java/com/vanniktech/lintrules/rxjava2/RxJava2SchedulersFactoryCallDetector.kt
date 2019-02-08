package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.getContainingUMethod
import java.util.EnumSet

val ISSUE_RAW_SCHEDULER_CALL = Issue.create("RxJava2SchedulersFactoryCall",
    "Instead of calling the Schedulers factory methods directly inject the Schedulers.",
    "Injecting the Schedulers instead of accessing them via the factory methods has the benefit that unit testing is way easier. Instead of overriding them via the Plugin mechanism we can just pass a custom Scheduler.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(RxJava2SchedulersFactoryCallDetector::class.java, EnumSet.of(JAVA_FILE)))

private val schedulersMethods = listOf("io", "computation", "newThread", "single", "from")
private val androidSchedulersMethods = listOf("mainThread")

class RxJava2SchedulersFactoryCallDetector : Detector(), UastScanner {
  override fun getApplicableMethodNames() = schedulersMethods + androidSchedulersMethods

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val evaluator = context.evaluator

    val isInSchedulers = evaluator.isMemberInClass(method, "io.reactivex.schedulers.Schedulers")
    val isInAndroidSchedulers = evaluator.isMemberInClass(method, "io.reactivex.android.schedulers.AndroidSchedulers")

    val isSchedulersMatch = schedulersMethods.contains(node.methodName) && isInSchedulers
    val isAndroidSchedulersMatch = androidSchedulersMethods.contains(node.methodName) && isInAndroidSchedulers

    val containingMethod = node.getContainingUMethod()
    val shouldIgnore = containingMethod != null && context.evaluator.getAllAnnotations(containingMethod as UAnnotated, false)
        .any { annotation -> listOf("dagger.Provides", "io.reactivex.annotations.SchedulerSupport").any { it == annotation.qualifiedName } }

    if ((isSchedulersMatch || isAndroidSchedulersMatch) && !shouldIgnore) {
      context.report(ISSUE_RAW_SCHEDULER_CALL, node, context.getNameLocation(node), "Inject this Scheduler instead of calling it directly.")
    }
  }
}

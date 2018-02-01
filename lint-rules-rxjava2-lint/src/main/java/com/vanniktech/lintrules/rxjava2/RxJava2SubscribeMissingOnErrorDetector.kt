package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.ERROR
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.EnumSet

@Suppress("Detekt.VariableMaxLength") val ISSUE_SUBSCRIBE_MISSING_ON_ERROR = Issue.create("RxJava2SubscribeMissingOnError",
    "Flags a version of the subscribe() method without an error Consumer.",
    "When calling the subscribe() method an error Consumer should always be used. Otherwise errors might be thrown and may crash the application or get forwarded to the Plugin Error handler.",
    CORRECTNESS, PRIORITY, ERROR,
    Implementation(RxJava2SubscribeMissingOnErrorDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class RxJava2SubscribeMissingOnErrorDetector : Detector(), Detector.UastScanner {
  override fun getApplicableMethodNames() = listOf("subscribe")

  override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val evaluator = context.evaluator

    val isInObservable = evaluator.isMemberInClass(method, "io.reactivex.Observable")
    val isInFlowable = evaluator.isMemberInClass(method, "io.reactivex.Flowable")
    val isInSingle = evaluator.isMemberInClass(method, "io.reactivex.Single")
    val isInCompletable = evaluator.isMemberInClass(method, "io.reactivex.Completable")
    val isInMaybe = evaluator.isMemberInClass(method, "io.reactivex.Maybe")

    val isReactiveType = listOf(isInObservable, isInFlowable, isInSingle, isInCompletable, isInMaybe).any { it }

    if (isReactiveType && node.valueArgumentCount < 2) {
      context.report(ISSUE_SUBSCRIBE_MISSING_ON_ERROR, node, context.getNameLocation(node), "Using a version of subscribe() without an error Consumer.")
    }
  }
}

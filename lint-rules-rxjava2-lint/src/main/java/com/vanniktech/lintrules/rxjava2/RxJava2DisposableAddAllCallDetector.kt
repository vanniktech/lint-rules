package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.EnumSet

@Suppress("Detekt.VariableMaxLength") val ISSUE_CALLING_COMPOSITE_DISPOSABLE_ADD_ALL = Issue.create("RxJava2DisposableAddAllCallDetector",
    "Marks usage of addAll() on CompositeDisposable.",
    "Instead of using addAll(), add() should be used separately for each Disposable.",
    CORRECTNESS, 5, WARNING,
    Implementation(RxJava2CallingDisposableAddAllDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class RxJava2CallingDisposableAddAllDetector : Detector(), Detector.UastScanner {
  override fun getApplicableMethodNames() = listOf("addAll")

  override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    if (context.evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable")) {
      context.report(ISSUE_CALLING_COMPOSITE_DISPOSABLE_ADD_ALL, node, context.getNameLocation(node), "Calling addAll instead of add separately.")
    }
  }
}

package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.Category.Companion.MESSAGES
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

@Suppress("Detekt.VariableMaxLength") val CALLING_COMPOSITE_DISPOSABLE_ADD_ALL = Issue.create("CallingCompositeDisposableAddAll",
    "Using addAll() instead of add() separately",
    "Instead of using addAll(), add() should be used separately.",
    MESSAGES, 5, WARNING,
    Implementation(RxJava2CallingDisposableAddAll::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class RxJava2CallingDisposableAddAll : Detector(), Detector.UastScanner {
  override fun getApplicableMethodNames() = listOf("addAll")

  override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    if (context.evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable")) {
      context.report(CALLING_COMPOSITE_DISPOSABLE_ADD_ALL, node, context.getNameLocation(node), "Using addAll() instead of add() separately")
    }
  }
}

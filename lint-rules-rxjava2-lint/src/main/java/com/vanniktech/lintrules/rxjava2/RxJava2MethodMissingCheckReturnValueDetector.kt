package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.MESSAGES
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiType
import org.jetbrains.uast.UMethod
import java.util.EnumSet

@Suppress("Detekt.VariableMaxLength") val ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE = Issue.create(
    "RxJava2MethodMissingCheckReturnValue",
    "Method is missing the @CheckReturnValue annotation.",
    "Methods returning RxJava Reactive Types should be annotated with the @CheckReturnValue annotation. Static analyze tools such as Lint or ErrorProne can detect when the return value of a method is not used. This is usually an indication of a bug. If this is done on purpose (e.g. fire & forget) it should be stated explicitly.",
    MESSAGES, PRIORITY, WARNING,
    Implementation(RxJava2MethodMissingCheckReturnValueDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class RxJava2MethodMissingCheckReturnValueDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext) = CheckReturnValueVisitor(context)

  class CheckReturnValueVisitor(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(method: UMethod) {
      val returnType = method.returnType

      if (returnType != null && isTypeThatRequiresAnnotation(returnType)) {
        context.evaluator.getAllAnnotations(method, true)
            .filter { "io.reactivex.annotations.CheckReturnValue" == it.qualifiedName }
            .forEach { return }

        val modifier = method.modifierList.children.joinToString(separator = " ") { it.text }

        val fix = fix()
            .replace()
            .name("Add @CheckReturnValue")
            .range(context.getLocation(method))
            .shortenNames()
            .text(modifier)
            .with("io.reactivex.annotations.CheckReturnValue " + modifier)
            .build()

        context.report(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE, method, context.getNameLocation(method), "Method should have @CheckReturnValue annotation.", fix)
      }
    }

    private fun isTypeThatRequiresAnnotation(psiType: PsiType): Boolean {
      val canonicalText = psiType.canonicalText
          .replace("<[\\w.]*>".toRegex(), "") // We need to remove the generics.

      return (canonicalText.matches("io\\.reactivex\\.[\\w]+".toRegex())
          || "io.reactivex.disposables.Disposable" == canonicalText
          || "io.reactivex.observers.TestObserver" == canonicalText
          || "io.reactivex.subscribers.TestSubscriber" == canonicalText)
    }
  }
}

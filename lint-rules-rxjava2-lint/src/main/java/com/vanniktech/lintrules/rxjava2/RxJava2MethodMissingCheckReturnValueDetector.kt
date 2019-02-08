package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.lang.jvm.JvmModifier
import com.intellij.psi.PsiType
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.kotlin.declarations.KotlinUMethod
import java.util.EnumSet

@Suppress("Detekt.VariableMaxLength") val ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE = Issue.create(
    "RxJava2MethodMissingCheckReturnValue",
    "Method is missing the @CheckReturnValue annotation.",
    "Methods returning RxJava Reactive Types should be annotated with the @CheckReturnValue annotation. Static analyze tools such as Lint or ErrorProne can detect when the return value of a method is not used. This is usually an indication of a bug. If this is done on purpose (e.g. fire & forget) it should be stated explicitly.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(RxJava2MethodMissingCheckReturnValueDetector::class.java, EnumSet.of(JAVA_FILE)))

class RxJava2MethodMissingCheckReturnValueDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext) = CheckReturnValueVisitor(context)

  class CheckReturnValueVisitor(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(node: UMethod) {
      val returnType = node.returnType
      val isPropertyFunction = node is KotlinUMethod && node.sourcePsi is KtProperty

      if (returnType != null && isTypeThatRequiresAnnotation(returnType) && !isPropertyFunction) {
        val hasAnnotatedMethod = context.evaluator.getAllAnnotations(node as UAnnotated, true)
          .any { "io.reactivex.annotations.CheckReturnValue" == it.qualifiedName }
        if (hasAnnotatedMethod) return

        val hasIgnoredModifier = ignoredModifiers().any { node.hasModifier(it) }
        if (hasIgnoredModifier) return

        val modifier = node.modifierList.children.joinToString(separator = " ") { it.text }

        val fix = LintFix.create()
            .replace()
            .name("Add @CheckReturnValue")
            .range(context.getLocation(node))
            .shortenNames()
            .reformat(true)
            .text(modifier)
            .with("@io.reactivex.annotations.CheckReturnValue $modifier")
            .autoFix()
            .build()

        context.report(ISSUE_METHOD_MISSING_CHECK_RETURN_VALUE, node, context.getNameLocation(node), "Method should have @CheckReturnValue annotation.", fix)
      }
    }

    private fun isTypeThatRequiresAnnotation(psiType: PsiType): Boolean {
      val canonicalText = psiType.canonicalText
          .replace("<[\\w.]*>".toRegex(), "") // We need to remove the generics.

      return canonicalText.matches("io\\.reactivex\\.[\\w]+".toRegex()) ||
          "io.reactivex.disposables.Disposable" == canonicalText ||
          "io.reactivex.observers.TestObserver" == canonicalText ||
          "io.reactivex.subscribers.TestSubscriber" == canonicalText
    }

    companion object {
      private const val IGNORE_MODIFIERS_PROP = "com.vanniktech.lintrules.rxjava2.RxJava2MethodMissingCheckReturnValueDetector.ignoreMethodAccessModifiers"

      private fun ignoredModifiers(): List<JvmModifier> {
        return System.getProperty(IGNORE_MODIFIERS_PROP)
            ?.split(",")
            ?.map { JvmModifier.valueOf(it.toUpperCaseAsciiOnly()) }
            .orEmpty()
      }
    }
  }
}

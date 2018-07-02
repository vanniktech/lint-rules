package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import java.util.EnumSet
import com.intellij.psi.PsiModifierListOwner
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

private const val IGNORE_ANNOTATION = "Ignore"

internal val ISSUE_IGNORE_WITHOUT_REASON = Issue.create(
  "IgnoreWithoutReason",
  "Test is ignored without given any reason.",
  "Ignoring a test without reason makes it difficult to figure out the problem later. Please define an explicit reason why it is ignored, and when it can be resolved.",
  Category.CORRECTNESS,
  PRIORITY,
  Severity.WARNING,
  Implementation(IgnoreWithoutReason::class.java, EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES))
)

class IgnoreWithoutReason : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UMethod::class.java, UClass::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler =
          IgnoreAnnotationVisitor(context)

    class IgnoreAnnotationVisitor(private val context: JavaContext) : UElementHandler() {

        override fun visitMethod(node: UMethod) {
            processAnnotations(node, node)
        }

        override fun visitClass(node: UClass) {
            processAnnotations(node, node)
        }

        private fun processAnnotations(element: UElement, modifierListOwner: PsiModifierListOwner) {
            val annotations = context.evaluator.getAllAnnotations(modifierListOwner, false)

            // Do the verification if only we have "Ignore" annotation
            annotations.firstOrNull { it.qualifiedName?.split(".")?.lastOrNull() == IGNORE_ANNOTATION }
                  ?.let {
                      if (it.findDeclaredAttributeValue("value")?.text.isNullOrBlank()) {
                          context.report(
                                ISSUE_IGNORE_WITHOUT_REASON,
                                element,
                                context.getNameLocation(element),
                                "Test is ignored without given any explanation.")
                      }
                  }
        }
    }
}

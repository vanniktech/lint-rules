package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.WARNING
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getValueIfStringLiteral
import java.util.EnumSet

val ISSUE_IGNORE_WITHOUT_REASON = Issue.create("IgnoreWithoutReason",
    "Test is ignored without given any reason.",
    "Ignoring a test without reason makes it difficult to figure out the problem later. Please define an explicit reason why it is ignored, and when it can be resolved.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(IgnoreWithoutReasonDetector::class.java, EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)))

val ANNOTATIONS = listOf("Ignore", "Disabled")

class IgnoreWithoutReasonDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UMethod::class.java, UClass::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler = IgnoreAnnotationVisitor(context)

  class IgnoreAnnotationVisitor(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(node: UMethod) = processAnnotations(node, node)

    override fun visitClass(node: UClass) = processAnnotations(node, node)

    private fun processAnnotations(element: UElement, modifierListOwner: UAnnotated) {
      val annotations = context.evaluator.getAllAnnotations(modifierListOwner, false)

      // Do the verification if only we have "Ignore" or "Disabled" annotation.
      annotations.firstOrNull { it.qualifiedName?.split(".")?.lastOrNull() in ANNOTATIONS }?.let {
        if (it.findDeclaredAttributeValue("value")?.getValueIfStringLiteral().isNullOrBlank()) {
          context.report(ISSUE_IGNORE_WITHOUT_REASON, element, context.getLocation(it), "Test is ignored without given any explanation.")
        }
      }
    }
  }
}

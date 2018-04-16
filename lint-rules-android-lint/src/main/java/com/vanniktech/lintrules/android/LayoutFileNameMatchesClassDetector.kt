package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.tryResolveNamed
import java.util.EnumSet

val ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS = Issue.create("LayoutFileNameMatchesClass",
    "Checks that the layout file matches the class name.",
    "Layout file names should always match the name of the class. FooActivity should have a layout file named activity_foo hence.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(LayoutFileNameMatchesClassDetector::class.java, EnumSet.of(JAVA_FILE)))

class LayoutFileNameMatchesClassDetector : Detector(), UastScanner {
  override fun getApplicableMethodNames() = listOf("setContentView")

  override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    super.visitMethod(context, node, method)

    val resourcePrefix = context.project.resourcePrefix()
    val firstParameter = node.valueArguments.getOrNull(0)

    val isNoLayoutReference = firstParameter?.asSourceString()?.startsWith("R.layout") == false

    val layoutFileName = firstParameter
        ?.tryResolveNamed()
        ?.name

    val className = node.getContainingUClass()
        ?.name

    if (isNoLayoutReference || layoutFileName == null || className == null) {
      return
    }

    val expectedLayoutFileName = className.toSnakeCase()
        .replace(resourcePrefix, "")
        .run {
          val array = split("_")
          resourcePrefix + array.last() + "_" + array.subList(0, array.size - 1).joinToString(separator = "_")
        }

    if (layoutFileName != expectedLayoutFileName) {
      val fix = fix()
          .replace()
          .text(layoutFileName)
          .with(expectedLayoutFileName)
          .build()

      context.report(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS, node, context.getLocation(node.valueArguments.first()), "Parameter should be named R.layout.$expectedLayoutFileName", fix)
    }
  }
}

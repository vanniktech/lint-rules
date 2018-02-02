package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiNamedElement
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UEnumConstant
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UVariable
import java.util.EnumSet

val ISSUE_NAMING_PATTERN = Issue.create("NamingPattern",
    "Names should be well named.",
    """Sometimes there is more than one reasonable way to convert an English phrase into camel case, such as when acronyms or unusual constructs like "IPv6" or "iOS" are present. XML HTTP request becomes XmlHttpRequest. XMLHTTPRequest would be incorrect.""",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(NamingPatternDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class NamingPatternDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf<Class<out UElement>>(UVariable::class.java, UMethod::class.java, UClass::class.java)

  override fun createUastHandler(context: JavaContext) = NamingPatternHandler(context)

  inner class NamingPatternHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitVariable(variable: UVariable) {
      val isConstant = variable.isFinal && variable.isStatic
      val isEnumConstant = variable is UEnumConstant

      if (!isConstant && !isEnumConstant) {
        process(variable, variable)
      }
    }

    override fun visitMethod(method: UMethod) {
      process(method, method)
    }

    override fun visitClass(clazz: UClass) {
      process(clazz, clazz)
    }

    private fun process(scope: UElement, declaration: PsiNamedElement) {
      if (declaration.name?.isDefinedCamelCase() == false) {
        context.report(ISSUE_NAMING_PATTERN, scope, context.getNameLocation(scope), "Not named in defined camel case.")
      }
    }
  }
}

private fun String.isDefinedCamelCase(): Boolean {
  val toCharArray = toCharArray()
  return toCharArray
      .mapIndexed { index, current -> current to toCharArray.getOrNull(index + 1) }
      .none { it.first.isUpperCase() && it.second?.isUpperCase() ?: false }
}

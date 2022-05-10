@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.isReceiver
import com.intellij.psi.PsiNamedElement
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UEnumConstant
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UParameter
import org.jetbrains.uast.UVariable
import java.util.EnumSet

val ISSUE_NAMING_PATTERN = Issue.create(
  "NamingPattern",
  "Names should be well named.",
  """Sometimes there is more than one reasonable way to convert an English phrase into camel case, such as when acronyms or unusual constructs like "IPv6" or "iOS" are present. XML HTTP request becomes XmlHttpRequest. XMLHTTPRequest would be incorrect.""",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(NamingPatternDetector::class.java, EnumSet.of(JAVA_FILE))
)

class NamingPatternDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf<Class<out UElement>>(UVariable::class.java, UMethod::class.java, UClass::class.java)

  override fun createUastHandler(context: JavaContext) = NamingPatternHandler(context)

  class NamingPatternHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitVariable(node: UVariable) {
      val isConstant = node.isFinal && node.isStatic
      val isEnumConstant = node is UEnumConstant

      if (!isConstant && !isEnumConstant) {
        process(node)
      }
    }

    override fun visitMethod(node: UMethod) = process(node)

    override fun visitClass(node: UClass) = process(node)

    private fun process(element: PsiNamedElement) {
      val name = element.name
      val isGeneratedKotlinMethod = element is UMethod && element.language is KotlinLanguage && element.sourcePsi is KtProperty
      val isGeneratedKotlinMethodAccessor = element is UMethod && element.language is KotlinLanguage && element.sourcePsi is KtPropertyAccessor
      val isKotlinReceiver = element is UParameter && element.isReceiver()

      if (!isGeneratedKotlinMethod &&
        !isGeneratedKotlinMethodAccessor &&
        !isKotlinReceiver &&
        name?.isDefinedCamelCase() == false &&
        EXCLUDES.none { name.contains(it) && !name.startsWith(it) }
      ) {
        context.report(ISSUE_NAMING_PATTERN, element, context.getNameLocation(element), "${element.name} is not named in defined camel case")
      }
    }
  }

  companion object {
    val EXCLUDES = listOf(
      "iOS",
      "I18N"
    )
  }
}

private fun String.isDefinedCamelCase(): Boolean {
  val toCharArray = toCharArray()
  return toCharArray
    .mapIndexed { index, current -> current to toCharArray.getOrNull(index + 1) }
    .none { it.first.isUpperCase() && it.second?.isUpperCase() ?: false }
}

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.JavaPsiScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierList
import com.intellij.psi.PsiVariable
import java.util.EnumSet

val ANNOTATION_ORDER = listOf(
    "Deprecated",
    "Override",
    "Test",
    "Ignore",
    // Annotations.
    "Documented",
    "Retention",
    // Dagger / JSR 305.
    "Component",
    "SubComponent",
    "Module",
    "Inject",
    "Provides",
    "Binds",
    "BindsInstance",
    "BindsOptionalOf",
    "IntoMap",
    "IntoSet",
    "Qualifier",
    "Singleton",
    "Reusable",
    "MapKey",
    // Nullability.
    "Nullable",
    "NonNull",
    // Checking the things.
    "CheckResult",
    "CheckReturnValue",
    // ButterKnife.
    "BindAnim",
    "BindArray",
    "BindBitmap",
    "BindBool",
    "BindColor",
    "BindDimen",
    "BindDrawable",
    "BindFloat",
    "BindFont",
    "BindInt",
    "BindString",
    "BindView",
    "BindViews",
    // Suppressing the suppressed.
    "Suppress",
    "SuppressLint",
    "SuppressWarnings",
    // Json.
    "Json",
    "JsonQualifier",
    // Retrofit 2.
    "GET",
    "POST",
    "PUSH",
    "PATCH",
    "DELETE",
    "Query",
    "Path",
    "Body",
    // Threads from support annotations.
    "MainThread",
    "UiThread",
    "WorkerThread",
    "BinderThread",
    "AnyThread",
    // Other things from support annotations.
    "Keep",
    "CallSuper",
    "RestrictTo",
    "TargetApi",
    "SdkConstant",
    "IntDef",
    "StringDef",
    "RequiresApi",
    "RequiresPermission",
    "RequiresPermission.Read",
    "RequiresPermission.Write",
    "VisibleForTesting",
    // AutoValue + extensions.
    "AutoValue",
    "ParcelAdapter",
    // Resources from support annotations.
    "AnimatorRes",
    "AnimRes",
    "AnyRes",
    "ArrayRes",
    "AttrRes",
    "BoolRes",
    "ColorRes",
    "DimenRes",
    "DrawableRes",
    "FontRes",
    "FractionRes",
    "IdRes",
    "IntegerRes",
    "InterpolatorRes",
    "LayoutRes",
    "MenuRes",
    "PluralsRes",
    "RawRes",
    "StringRes",
    "StyleableRes",
    "TransitionRes",
    "XmlRes",
    // Colors from support annotations.
    "ColorInt",
    "ColorLong",
    // Others from support annotations.
    "Dimension",
    "GuardedBy",
    "HalfFloat",
    "Px",
    // Ranges and sizes from support annotations.
    "IntRange",
    "FloatRange",
    "Size")

@JvmField val ISSUE_WRONG_ANNOTATION_ORDER = Issue.create("WrongAnnotationOrder",
    "Annotations should be applied within a specific order.",
    "Annotations should be applied within a specific order.", Category.CORRECTNESS, 6, WARNING,
    Implementation(AnnotationOrderDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class AnnotationOrderDetector : Detector(), JavaPsiScanner {
  override fun getApplicablePsiTypes(): List<Class<out PsiElement>> = listOf(PsiVariable::class.java, PsiMethod::class.java)

  override fun createPsiVisitor(context: JavaContext) = AnnotationOrderVisitor(context)

  class AnnotationOrderVisitor(private val context: JavaContext) : JavaElementVisitor() {
    override fun visitVariable(variable: PsiVariable) {
      variable.modifierList?.let { processAnnotations(variable.nameIdentifier, it) }
    }

    override fun visitMethod(method: PsiMethod) {
      processAnnotations(method.nameIdentifier, method.modifierList)
    }

    @Suppress("LabeledExpression") private fun processAnnotations(identifier: PsiIdentifier?, modifierList: PsiModifierList) {
      var size = 0

      val annotations = modifierList.annotations
          .map { it.qualifiedName?.split(".")?.lastOrNull() }
          .filterNotNull()

      val numberOfRecognizedAnnotations = annotations.count { ANNOTATION_ORDER.contains(it) }

      val isInCorrectOrder = ANNOTATION_ORDER.contains(annotations.firstOrNull()) && annotations
          .all {
            if (ANNOTATION_ORDER.contains(it)) {
              for (i in size until ANNOTATION_ORDER.size) {
                size++

                if (it == ANNOTATION_ORDER[i]) {
                  return@all true
                }
              }

              return@all false
            }

            return@all true
          }

      if (!isInCorrectOrder && numberOfRecognizedAnnotations > 0) {
        val correctOrder = ANNOTATION_ORDER
            .filter { annotations.contains(it) }
            .plus(annotations.filterNot { ANNOTATION_ORDER.contains(it) })
            .joinToString(separator = " ") { "@$it" }

        context.report(ISSUE_WRONG_ANNOTATION_ORDER, context.getLocation(identifier), "Annotations are in wrong order. Should be $correctOrder")
      }
    }
  }
}

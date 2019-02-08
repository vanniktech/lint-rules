package com.vanniktech.lintrules.android

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiType
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UVariable
import java.util.EnumSet

val ISSUE_ALERT_DIALOG_USAGE = Issue.create("AlertDialogUsage",
    "Use the support library AlertDialog instead of android.app.AlertDialog.",
    "Support library AlertDialog is much more powerful and plays better together with the new theming / styling than the AlertDialog built into the framework.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(AlertDialogUsageDetector::class.java, EnumSet.of(JAVA_FILE)))

private const val FQDN_ANDROID_ALERT_DIALOG = "android.app.AlertDialog"

class AlertDialogUsageDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf<Class<out UElement>>(UVariable::class.java, UClass::class.java)

  override fun createUastHandler(context: JavaContext) = AlertDialogUsageHandler(context)

  class AlertDialogUsageHandler(
    private val context: JavaContext
  ) : UElementHandler() {
    override fun visitVariable(node: UVariable) = process(node.type, node)

    override fun visitClass(node: UClass) = node.uastSuperTypes.forEach { process(it.type, it) }

    private fun process(type: PsiType, node: UElement) {
      if (context.evaluator.typeMatches(type, FQDN_ANDROID_ALERT_DIALOG)) {
        context.report(ISSUE_ALERT_DIALOG_USAGE, node, context.getLocation(node), "Should not be using android.app.AlertDialog.")
      }
    }
  }
}

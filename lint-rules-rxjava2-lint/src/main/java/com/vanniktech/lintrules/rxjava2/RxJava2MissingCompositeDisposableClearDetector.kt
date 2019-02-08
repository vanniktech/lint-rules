package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.ERROR
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.visitor.AbstractUastVisitor
import java.util.EnumSet

val ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR = Issue.create("RxJava2MissingCompositeDisposableClear",
    "Marks CompositeDisposables that are not being cleared.",
    "A class is using CompositeDisposable and not calling clear(). This can leave operations running and even cause memory leaks. It's best to always call clear() once you're done. e.g. in onDestroy() for Activitys.",
    CORRECTNESS, PRIORITY, ERROR,
    Implementation(RxJava2MissingCompositeDisposableClearDetector::class.java, EnumSet.of(JAVA_FILE)))

class RxJava2MissingCompositeDisposableClearDetector : Detector(), Detector.UastScanner {
  override fun getApplicableUastTypes() = listOf(UClass::class.java)

  override fun createUastHandler(context: JavaContext) = MissingCompositeDisposableClearVisitor(context)

  class MissingCompositeDisposableClearVisitor(
    private val context: JavaContext
  ) : UElementHandler() {
    override fun visitClass(node: UClass) {
      val compositeDisposables = node.fields
          .filter { "io.reactivex.disposables.CompositeDisposable" == it.type.canonicalText }
          .toMutableSet()

      node.accept(object : AbstractUastVisitor() {
        override fun visitCallExpression(node: UCallExpression): Boolean {
          return if ("clear" == node.methodName) {
            val iterator = compositeDisposables.iterator()

            while (iterator.hasNext()) {
              if (node.receiver?.asRenderString() == iterator.next().name) {
                iterator.remove()
              }
            }

            true
          } else {
            super.visitCallExpression(node)
          }
        }
      })

      compositeDisposables.forEach {
        context.report(ISSUE_MISSING_COMPOSITE_DISPOSABLE_CLEAR, it, context.getLocation(it), "clear() is not called.")
      }
    }
  }
}

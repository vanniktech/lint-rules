package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.UastLintUtils
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UReferenceExpression
import org.jetbrains.uast.getContainingUFile
import java.util.EnumSet

val ISSUE_SHOULD_USE_STATIC_IMPORT = Issue.create("ShouldUseStaticImport",
    "Flags declarations that should be statically imported.",
    "Certain declarations like TimeUnit.SECONDS should be statically imported to increase the readability.",
    CORRECTNESS, PRIORITY, WARNING,
    Implementation(ShouldUseStaticImportDetector::class.java, EnumSet.of(JAVA_FILE)))

@Suppress("Detekt.StringLiteralDuplication")
private val methodsToStaticallyImport = mapOf(
    // Arrays.
    "asList" to "java.util.Arrays",
    // Collections.
    "emptyEnumeration" to "java.util.Collections",
    "emptyIterator" to "java.util.Collections",
    "emptyList" to "java.util.Collections",
    "emptyListIterator" to "java.util.Collections",
    "emptyMap" to "java.util.Collections",
    "emptySet" to "java.util.Collections",
    "singleton" to "java.util.Collections",
    "singletonList" to "java.util.Collections",
    "singletonMap" to "java.util.Collections",
    "singletonIterator" to "java.util.Collections",
    "singletonSpliterator" to "java.util.Collections",
    "unmodifiableCollection" to "java.util.Collections",
    "unmodifiableSet" to "java.util.Collections",
    "unmodifiableSortedSet" to "java.util.Collections",
    "unmodifiableNavigableSet" to "java.util.Collections",
    "unmodifiableList" to "java.util.Collections",
    "unmodifiableMap" to "java.util.Collections",
    "unmodifiableSortedMap" to "java.util.Collections",
    "unmodifiableNavigableMap" to "java.util.Collections",
    // Mockito.
    "mock" to "org.mockito.Mockito.mock",
    "mockingDetails" to "org.mockito.Mockito.mockingDetails",
    "spy" to "org.mockito.Mockito.spy",
    "verify" to "org.mockito.Mockito.verify",
    "reset" to "org.mockito.Mockito.reset",
    "clearInvocations" to "org.mockito.Mockito.clearInvocations",
    "verifyNoMoreInteractions" to "org.mockito.Mockito.verifyNoMoreInteractions",
    "verifyZeroInteractions" to "org.mockito.Mockito.verifyZeroInteractions",
    "when" to "org.mockito.Mockito.when",
    "doThrow" to "org.mockito.Mockito.doThrow",
    "doCallRealMethod" to "org.mockito.Mockito.doCallRealMethod",
    "doAnswer" to "org.mockito.Mockito.doAnswer",
    "doNothing" to "org.mockito.Mockito.doNothing",
    "doReturn" to "org.mockito.Mockito.doReturn",
    "inOrder" to "org.mockito.Mockito.inOrder",
    "ignoreStubs" to "org.mockito.Mockito.ignoreStubs",
    "never" to "org.mockito.Mockito.never",
    "times" to "org.mockito.Mockito.times",
    "atLeastOnce" to "org.mockito.Mockito.atLeastOnce",
    "atLeast" to "org.mockito.Mockito.atLeast",
    "atMost" to "org.mockito.Mockito.atMost",
    "calls" to "org.mockito.Mockito.calls",
    "only" to "org.mockito.Mockito.only",
    "timeout" to "org.mockito.Mockito.timeout",
    "after" to "org.mockito.Mockito.after",
    "validateMockitoUsage" to "org.mockito.Mockito.validateMockitoUsage",
    "withSettings" to "org.mockito.Mockito.withSettings",
    "description" to "org.mockito.Mockito.description",
    "debug" to "org.mockito.Mockito.debug",
    "framework" to "org.mockito.Mockito.framework",
    "mockitoSession" to "org.mockito.Mockito.mockitoSession"
)

@Suppress("Detekt.StringLiteralDuplication")
private val referencesToStaticallyImport = mapOf(
    // TimeUnit.
    "NANOSECONDS" to "java.util.concurrent.TimeUnit",
    "MICROSECONDS" to "java.util.concurrent.TimeUnit",
    "MILLISECONDS" to "java.util.concurrent.TimeUnit",
    "SECONDS" to "java.util.concurrent.TimeUnit",
    "MINUTES" to "java.util.concurrent.TimeUnit",
    "HOURS" to "java.util.concurrent.TimeUnit",
    "DAYS" to "java.util.concurrent.TimeUnit",
    // Locales.
    "ENGLISH" to "java.util.Locale",
    "FRENCH" to "java.util.Locale",
    "GERMAN" to "java.util.Locale",
    "ITALIAN" to "java.util.Locale",
    "JAPANESE" to "java.util.Locale",
    "KOREAN" to "java.util.Locale",
    "CHINESE" to "java.util.Locale",
    "SIMPLIFIED_CHINESE" to "java.util.Locale",
    "TRADITIONAL_CHINESE" to "java.util.Locale",
    "FRANCE" to "java.util.Locale",
    "GERMANY" to "java.util.Locale",
    "ITALY" to "java.util.Locale",
    "JAPAN" to "java.util.Locale",
    "KOREA" to "java.util.Locale",
    "CHINA" to "java.util.Locale",
    "PRC" to "java.util.Locale",
    "TAIWAN" to "java.util.Locale",
    "UK" to "java.util.Locale",
    "US" to "java.util.Locale",
    "CANADA" to "java.util.Locale",
    "CANADA_FRENCH" to "java.util.Locale",
    "ROOT" to "java.util.Locale",
    // Android View.
    "VISIBLE" to "android.view.View",
    "GONE" to "android.view.View",
    "INVISIBLE" to "android.view.View",
    // Android Service.
    "START_CONTINUATION_MASK" to "android.app.Service",
    "START_STICKY_COMPATIBILITY" to "android.app.Service",
    "START_STICKY" to "android.app.Service",
    "START_NOT_STICKY" to "android.app.Service",
    "START_REDELIVER_INTENT" to "android.app.Service",
    "START_TASK_REMOVED_COMPLETE" to "android.app.Service",
    "START_FLAG_REDELIVERY" to "android.app.Service",
    "START_FLAG_RETRY" to "android.app.Service",
    // Android Versions.
    "INCREMENTAL" to "android.os.Build.VERSION",
    "RELEASE" to "android.os.Build.VERSION",
    "BASE_OS" to "android.os.Build.VERSION",
    "SECURITY_PATCH" to "android.os.Build.VERSION",
    "SDK" to "android.os.Build.VERSION",
    "SDK_INT" to "android.os.Build.VERSION",
    "PREVIEW_SDK_INT" to "android.os.Build.VERSION",
    "CODENAME" to "android.os.Build.VERSION",
    "RESOURCES_SDK_INT" to "android.os.Build.VERSION",
    // Android Build.
    "DISPLAY" to "android.os.Build",
    "PRODUCT" to "android.os.Build",
    "DEVICE" to "android.os.Build",
    "BOARD" to "android.os.Build",
    "CPU_ABI" to "android.os.Build",
    "CPU_ABI2" to "android.os.Build",
    "MANUFACTURER" to "android.os.Build",
    "BRAND" to "android.os.Build",
    "MODEL" to "android.os.Build",
    "BOOTLOADER" to "android.os.Build",
    "RADIO" to "android.os.Build",
    "HARDWARE" to "android.os.Build",
    "SERIAL" to "android.os.Build",
    // Android Version Codes.
    "CUR_DEVELOPMENT" to "android.os.Build.VERSION_CODES",
    "BASE" to "android.os.Build.VERSION_CODES",
    "BASE_1_1" to "android.os.Build.VERSION_CODES",
    "CUPCAKE" to "android.os.Build.VERSION_CODES",
    "DONUT" to "android.os.Build.VERSION_CODES",
    "ECLAIR" to "android.os.Build.VERSION_CODES",
    "ECLAIR_0_1" to "android.os.Build.VERSION_CODES",
    "ECLAIR_MR1" to "android.os.Build.VERSION_CODES",
    "FROYO" to "android.os.Build.VERSION_CODES",
    "GINGERBREAD" to "android.os.Build.VERSION_CODES",
    "GINGERBREAD_MR1" to "android.os.Build.VERSION_CODES",
    "HONEYCOMB" to "android.os.Build.VERSION_CODES",
    "HONEYCOMB_MR1" to "android.os.Build.VERSION_CODES",
    "HONEYCOMB_MR2" to "android.os.Build.VERSION_CODES",
    "ICE_CREAM_SANDWICH" to "android.os.Build.VERSION_CODES",
    "ICE_CREAM_SANDWICH_MR1" to "android.os.Build.VERSION_CODES",
    "JELLY_BEAN" to "android.os.Build.VERSION_CODES",
    "JELLY_BEAN_MR1" to "android.os.Build.VERSION_CODES",
    "JELLY_BEAN_MR2" to "android.os.Build.VERSION_CODES",
    "KITKAT" to "android.os.Build.VERSION_CODES",
    "KITKAT_WATCH" to "android.os.Build.VERSION_CODES",
    "LOLLIPOP" to "android.os.Build.VERSION_CODES",
    "LOLLIPOP_MR1" to "android.os.Build.VERSION_CODES",
    "VERSION_CODES.M" to "android.os.Build",
    "VERSION_CODES.N" to "android.os.Build",
    "N_MR1" to "android.os.Build.VERSION_CODES",
    "O" to "android.os.Build.VERSION_CODES",
    // Future proof Version Codes.
    "P" to "android.os.Build.VERSION_CODES",
    "Q" to "android.os.Build.VERSION_CODES",
    "R" to "android.os.Build.VERSION_CODES",
    "S" to "android.os.Build.VERSION_CODES",
    "T" to "android.os.Build.VERSION_CODES",
    "U" to "android.os.Build.VERSION_CODES",
    "V" to "android.os.Build.VERSION_CODES",
    "W" to "android.os.Build.VERSION_CODES",
    "X" to "android.os.Build.VERSION_CODES",
    "Y" to "android.os.Build.VERSION_CODES",
    "Z" to "android.os.Build.VERSION_CODES",
    // Retention Policy.
    "SOURCE" to "java.lang.annotation.RetentionPolicy",
    "CLASS" to "java.lang.annotation.RetentionPolicy",
    "RUNTIME" to "java.lang.annotation.RetentionPolicy",
    // Mockito.
    "STRICT_STUBS" to "org.mockito.quality.Strictness",
    "WARN" to "org.mockito.quality.Strictness",
    "LENIENT" to "org.mockito.quality.Strictness"
)

class ShouldUseStaticImportDetector : Detector(), Detector.UastScanner {
  override fun getApplicableMethodNames() = methodsToStaticallyImport.keys.toList()

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val methodName = node.methodName
    val className = methodsToStaticallyImport[methodName]

    if (methodName != null && className != null) {
      val isStaticallyImported = node.sourcePsi?.text?.startsWith(methodName) ?: false
      val matches = context.evaluator.isMemberInClass(method, className)

      if (!isStaticallyImported && matches) {
        context.report(ISSUE_SHOULD_USE_STATIC_IMPORT, node, context.getNameLocation(node), "Should statically import $methodName")
      }
    }
  }

  // For some reason visitReference may be called multiple times with the same parameters.
  private val processed = mutableSetOf<UReferenceExpression>()

  override fun getApplicableReferenceNames() = referencesToStaticallyImport.keys.toList()

  override fun visitReference(context: JavaContext, reference: UReferenceExpression, referenced: PsiElement) {
    if (processed.add(reference)) {
      val name = reference.asRenderString()
      val uFile = reference.getContainingUFile() ?: return

      for (uImportStatement in uFile.imports) {
        if (uImportStatement.asSourceString().contains(name)) {
          return // Static import and we don't need to look further.
        }
      }

      val isQualified = reference.uastParent is UQualifiedReferenceExpression
      val matches = UastLintUtils.getQualifiedName(referenced) == referencesToStaticallyImport[name] + "." + name

      if (matches && isQualified) {
        context.report(ISSUE_SHOULD_USE_STATIC_IMPORT, reference, context.getLocation(reference), "Should statically import $name")
      }
    }
  }

  override fun afterCheckFile(context: Context) {
    super.afterCheckFile(context)
    processed.clear()
  }
}

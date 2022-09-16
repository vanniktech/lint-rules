@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope.GRADLE_FILE
import com.android.tools.lint.detector.api.Severity.WARNING
import java.util.EnumSet

val ISSUE_JCENTER = Issue.create(
  "JCenter",
  "Marks usage of the jcenter() repository.",
  "JCenter has gotten less and less reliable and it's best to avoid if possible. This check will flag usages of jcenter() in your gradle files.",
  CORRECTNESS, PRIORITY, WARNING,
  Implementation(JcenterDetector::class.java, EnumSet.of(GRADLE_FILE)),
)

class JcenterDetector : Detector(), Detector.GradleScanner {
  override fun checkMethodCall(
    context: GradleContext,
    statement: String,
    parent: String?,
    parentParent: String?,
    namedArguments: Map<String, String>,
    unnamedArguments: List<String>,
    cookie: Any,
  ) {
    if (statement == "jcenter") {
      val fix = fix()
        .replace()
        .text(statement)
        .with("mavenCentral")
        .name("Replace with mavenCentral()")
        .autoFix()
        .build()
      context.report(ISSUE_JCENTER, cookie, context.getLocation(cookie), "Don't use `jcenter()`", fix)
    }
  }
}

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
  Implementation(JcenterDetector::class.java, EnumSet.of(GRADLE_FILE))
)

class JcenterDetector : Detector(), Detector.GradleScanner {
  override fun checkDslPropertyAssignment(context: GradleContext, property: String, value: String, parent: String, parentParent: String?, valueCookie: Any, statementCookie: Any) {
    super.checkDslPropertyAssignment(context, property, value, parent, parentParent, valueCookie, statementCookie)

    if (property == "jcenter") {
      val fix = fix()
        .replace()
        .text(property)
        .with("mavenCentral")
        .name("Replace with mavenCentral()")
        .autoFix()
        .build()
      context.report(ISSUE_JCENTER, statementCookie, context.getLocation(statementCookie), "Don't use jcenter().", fix)
    }
  }
}

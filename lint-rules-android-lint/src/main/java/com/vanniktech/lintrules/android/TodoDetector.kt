package com.vanniktech.lintrules.android

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope.GRADLE_FILE
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.MANIFEST
import com.android.tools.lint.detector.api.Scope.PROGUARD_FILE
import com.android.tools.lint.detector.api.Scope.RESOURCE_FILE
import com.android.tools.lint.detector.api.Severity.FATAL
import com.android.tools.lint.detector.api.XmlContext
import org.jetbrains.uast.UClass
import org.w3c.dom.Document
import java.util.EnumSet
import java.util.regex.Pattern

private const val COMMENT = "TODO:"
private val pattern = Pattern.compile("[\\t]*$COMMENT.*")

val ISSUE_TODO = Issue.create("Todo",
    "Marks todos in any given file.",
    "Marks todo in any given file since they should be resolved.",
    CORRECTNESS, PRIORITY, FATAL,
    Implementation(TodoDetector::class.java, EnumSet.of(JAVA_FILE, GRADLE_FILE, PROGUARD_FILE, MANIFEST, RESOURCE_FILE), EnumSet.of(JAVA_FILE, GRADLE_FILE, PROGUARD_FILE, MANIFEST, RESOURCE_FILE)))

class TodoDetector : Detector(), Detector.UastScanner, Detector.GradleScanner, Detector.OtherFileScanner, Detector.XmlScanner {
  override fun getApplicableUastTypes() = listOf(UClass::class.java)

  override fun visitDocument(context: XmlContext, document: Document) {
    // Needs to be overridden but we we'll do the work in afterCheckFile.
  }

  override fun afterCheckFile(context: Context) {
    val source = context.getContents().toString()
    val matcher = pattern.matcher(source)

    while (matcher.find()) {
      val start = matcher.start()
      val end = matcher.end()

      val location = Location.create(context.file, source, start, end)
      context.report(ISSUE_TODO, location, "Contains todo.")
    }
  }
}

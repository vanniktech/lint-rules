@file:Suppress("UnstableApiUsage") // We know that Lint API's aren't final.

package com.vanniktech.lintrules.kotlin

import com.android.tools.lint.client.api.LintClient
import com.android.tools.lint.detector.api.TextFormat.RAW
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class IssueRegistryTest {
  @Before fun setUp() {
    LintClient.clientName = "Test"
  }

  @After fun tearDown() {
    LintClient.resetClientName()
  }

  @Test fun everyBriefDescriptionIsASentence() {
    IssueRegistry().issues
      .map { it.getBriefDescription(RAW) }
      .forEach { assertTrue("$it is not a sentence", it.first().isUpperCase() && it.last() == '.' && it == it.trim()) }
  }

  @Test fun everyExplanationConsistsOfSentences() {
    IssueRegistry().issues
      .map { it.getExplanation(RAW) }
      .forEach { assertTrue("$it is not a sentence", it.first().isUpperCase() && it.last() == '.' && it == it.trim()) }
  }

  @Test fun idsStartsWithKotlin() {
    IssueRegistry().issues
      .map { it.id }
      .forEach { assertTrue("$it is not starting with Kotlin", it.startsWith("Kotlin")) }
  }

  @Test fun idsDoNotHaveDetector() {
    IssueRegistry().issues
      .map { it.id }
      .forEach { assertTrue("$it is containing Detector", !it.contains("Detector")) }
  }

  @Test fun readmeContent() {
    val output = IssueRegistry().issues
      .sortedBy { it.id }
      .joinToString(separator = "\n") { "- **${it.id}** - ${it.getExplanation(RAW)}" }

    requireNotNull(
      File(requireNotNull(IssueRegistryTest::class.java.classLoader).getResource(".").file)
        .parentFile
        ?.parentFile
        ?.parentFile
        ?.parentFile
        ?.parentFile
        ?.resolve("lint-rules-kotlin.md"),
    )
      .writeText(output)
  }
}

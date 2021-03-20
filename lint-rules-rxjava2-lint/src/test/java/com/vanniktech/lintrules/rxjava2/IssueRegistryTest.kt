package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.TextFormat.RAW
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IssueRegistryTest {
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

  @Test fun idsStartsWithRxJava2() {
    IssueRegistry().issues
      .map { it.id }
      .forEach { assertTrue("$it is not starting with RxJava2", it.startsWith("RxJava2")) }
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

    assertEquals(
      """
        - **RxJava2DefaultScheduler** - Calling this method will rely on a default scheduler. This is not necessary the best default. Being explicit and taking the overload for passing one is preferred.
        - **RxJava2DisposableAddAllCall** - Instead of using addAll(), add() should be used separately for each Disposable.
        - **RxJava2DisposableDisposeCall** - Instead of using dispose(), clear() should be used. Calling clear will result in a CompositeDisposable that can be used further to add more Disposables. When using dispose() this is not the case.
        - **RxJava2MethodMissingCheckReturnValue** - Methods returning RxJava Reactive Types should be annotated with the @CheckReturnValue annotation. Static analyze tools such as Lint or ErrorProne can detect when the return value of a method is not used. This is usually an indication of a bug. If this is done on purpose (e.g. fire & forget) it should be stated explicitly.
        - **RxJava2MissingCompositeDisposableClear** - A class is using CompositeDisposable and not calling clear(). This can leave operations running and even cause memory leaks. It's best to always call clear() once you're done. e.g. in onDestroy() for Activitys.
        - **RxJava2SchedulersFactoryCall** - Injecting the Schedulers instead of accessing them via the factory methods has the benefit that unit testing is way easier. Instead of overriding them via the Plugin mechanism we can just pass a custom Scheduler.
        - **RxJava2SubscribeMissingOnError** - When calling the subscribe() method an error Consumer should always be used. Otherwise errors might be thrown and may crash the application or get forwarded to the Plugin Error handler.
      """.trimIndent(),
      output
    )
  }
}

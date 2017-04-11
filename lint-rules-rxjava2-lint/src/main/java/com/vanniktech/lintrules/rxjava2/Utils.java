package com.vanniktech.lintrules.rxjava2;

import com.intellij.psi.PsiType;

final class Utils {
  static boolean isRxJava2TypeThatRequiresCheckReturnValueAnnotation(final PsiType psiType) {
    final String canonicalText = psiType.getCanonicalText();
    return canonicalText.matches("io\\.reactivex\\.[\\w]+")
        || "io.reactivex.disposables.Disposable".equals(canonicalText)
        || "io.reactivex.observers.TestObserver".equals(canonicalText)
        || "io.reactivex.subscribers.TestSubscriber".equals(canonicalText);
  }

  private Utils() {
    throw new AssertionError("No instances.");
  }
}

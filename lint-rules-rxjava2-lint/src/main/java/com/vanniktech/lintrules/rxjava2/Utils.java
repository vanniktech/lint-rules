package com.vanniktech.lintrules.rxjava2;

import com.intellij.psi.PsiType;

final class Utils {
  static boolean isRxJava2ReactiveType(final PsiType psiType) {
    return "io.reactivex.Observable".equals(psiType.getCanonicalText())
        || "io.reactivex.Flowable".equals(psiType.getCanonicalText())
        || "io.reactivex.Single".equals(psiType.getCanonicalText())
        || "io.reactivex.Completable".equals(psiType.getCanonicalText())
        || "io.reactivex.Maybe".equals(psiType.getCanonicalText());
  }

  private Utils() {
    throw new AssertionError("No instances.");
  }
}

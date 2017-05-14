package com.vanniktech.lintrules.android;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.CORRECTNESS;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static com.intellij.psi.PsiModifier.ABSTRACT;
import static com.intellij.psi.PsiModifier.FINAL;

public class NonFinalClassDetector extends Detector implements Detector.JavaPsiScanner {
  static final Issue ISSUE_NON_FINAL_CLASS = Issue.create("NonFinalClass", "Class should be marked as final.",
      "Class should be marked as final.", CORRECTNESS, 8, WARNING,
      new Implementation(NonFinalClassDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  @Override public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
    return Collections.<Class<? extends PsiElement>>singletonList(PsiClass.class);
  }

  @Override public JavaElementVisitor createPsiVisitor(@NonNull final JavaContext context) {
    return new NonFinalClassDetectorVisitor(context);
  }

  static class NonFinalClassDetectorVisitor extends JavaElementVisitor {
    private final JavaContext context;

    NonFinalClassDetectorVisitor(final JavaContext context) {
      this.context = context;
    }

    @Override public void visitClass(final PsiClass psiClass) {
      final PsiModifierList modifierList = psiClass.getModifierList();

      if (modifierList != null) {
        final boolean isFinal = modifierList.hasExplicitModifier(FINAL);
        final boolean isAbstract = modifierList.hasExplicitModifier(ABSTRACT);

        if (!isFinal && !isAbstract) {
          context.report(ISSUE_NON_FINAL_CLASS, context.getLocation(psiClass.getFirstChild()), "Class is not marked as final.");
        }
      }
    }
  }
}

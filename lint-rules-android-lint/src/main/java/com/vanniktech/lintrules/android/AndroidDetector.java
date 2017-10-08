package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import java.util.EnumSet;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class AndroidDetector extends Detector implements Detector.JavaPsiScanner {
  @Override public List<String> getApplicableMethodNames() {
    return asList("getColor", "getDrawable", "getColorStateList");
  }

  @Override public void visitMethod(final JavaContext context, final JavaElementVisitor visitor,
      final PsiMethodCallExpression call, final PsiMethod method) {
    final PsiReferenceExpression methodExpression = call.getMethodExpression();
    final String fullyQualifiedMethodName = methodExpression.getQualifiedName();

    handleResourcesCalls(context, call, methodExpression, fullyQualifiedMethodName);
  }

  private void handleResourcesCalls(final JavaContext context, final PsiMethodCallExpression call,
      final PsiReferenceExpression methodExpression, final String fullyQualifiedMethodName) {
    final boolean isResourcesGetDrawableSuppressed = context.getDriver().isSuppressed(context, ISSUE_RESOURCES_GET_DRAWABLE, methodExpression);
    if ("android.content.res.Resources.getDrawable".equals(fullyQualifiedMethodName) && !isResourcesGetDrawableSuppressed) {
      context.report(ISSUE_RESOURCES_GET_DRAWABLE, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using deprecated getDrawable()");
    }

    final boolean isResourcesGetColorSuppressed = context.getDriver().isSuppressed(context, ISSUE_RESOURCES_GET_COLOR, methodExpression);
    if ("android.content.res.Resources.getColor".equals(fullyQualifiedMethodName) && !isResourcesGetColorSuppressed) {
      context.report(ISSUE_RESOURCES_GET_COLOR, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using deprecated getColor()");
    }

    final boolean isResourcesGetColorStateListSuppressed = context.getDriver().isSuppressed(context, ISSUE_RESOURCES_GET_COLOR_STATE_LIST, methodExpression);
    if ("android.content.res.Resources.getColorStateList".equals(fullyQualifiedMethodName) && !isResourcesGetColorStateListSuppressed) {
      context.report(ISSUE_RESOURCES_GET_COLOR_STATE_LIST, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using deprecated getColorStateList()");
    }
  }

  static Issue[] getIssues() {
    return new Issue[] {
      ISSUE_RESOURCES_GET_DRAWABLE, ISSUE_RESOURCES_GET_COLOR, ISSUE_RESOURCES_GET_COLOR_STATE_LIST
    };
  }

  static final Issue ISSUE_RESOURCES_GET_DRAWABLE =
      Issue.create("ResourcesGetDrawable", "Using getDrawable(), which is deprecated.",
          "Instead of getDrawable(), ContextCompat or the method with the Theme Overload should be used instead.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  static final Issue ISSUE_RESOURCES_GET_COLOR =
      Issue.create("ResourcesGetColor", "Using getColor(), which is deprecated.",
          "Instead of getColor(), ContextCompat or the method with the Theme Overload should be used instead.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));

  static final Issue ISSUE_RESOURCES_GET_COLOR_STATE_LIST =
      Issue.create("ResourcesGetColorStateList", "Using getColorStateList(), which is deprecated.",
          "Instead of getColorStateList(), ContextCompat or the method with the Theme Overload should be used instead.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, EnumSet.of(JAVA_FILE, TEST_SOURCES)));
}

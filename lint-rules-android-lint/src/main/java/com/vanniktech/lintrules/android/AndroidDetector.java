package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import java.util.Arrays;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public final class AndroidDetector extends Detector implements Detector.JavaPsiScanner {
  @Override public List<String> getApplicableMethodNames() {
    return Arrays.asList("findViewById", "getColor", "getDrawable", "getColorStateList");
  }

  @Override public void visitMethod(final JavaContext context, final JavaElementVisitor visitor,
      final PsiMethodCallExpression call, final PsiMethod method) {
    final PsiReferenceExpression methodExpression = call.getMethodExpression();
    final String fullyQualifiedMethodName = methodExpression.getQualifiedName();

    handleFindViewById(context, call, methodExpression, fullyQualifiedMethodName);
    handleResourcesCalls(context, call, methodExpression, fullyQualifiedMethodName);
  }

  private void handleFindViewById(final JavaContext context, final PsiMethodCallExpression call,
      final PsiReferenceExpression methodExpression, final String fullyQualifiedMethodName) {
    final boolean isWindowFindViewByIdSuppressed = context.getDriver().isSuppressed(context, ISSUE_WINDOW_FIND_VIEW_BY_ID, methodExpression);
    if ("android.view.Window.findViewById".equals(fullyQualifiedMethodName) && !isWindowFindViewByIdSuppressed) {
      context.report(ISSUE_WINDOW_FIND_VIEW_BY_ID, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using findViewById instead of ButterKnife");
    }

    final boolean isViewFindViewByIdSuppressed = context.getDriver().isSuppressed(context, ISSUE_VIEW_FIND_VIEW_BY_ID, methodExpression);
    if ("android.view.View.findViewById".equals(fullyQualifiedMethodName) && !isViewFindViewByIdSuppressed) {
      context.report(ISSUE_VIEW_FIND_VIEW_BY_ID, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using findViewById instead of ButterKnife");
    }

    final boolean isDialogFindViewByIdSuppressed = context.getDriver().isSuppressed(context, ISSUE_DIALOG_FIND_VIEW_BY_ID, methodExpression);
    if ("android.app.Dialog.findViewById".equals(fullyQualifiedMethodName) && !isDialogFindViewByIdSuppressed) {
      context.report(ISSUE_DIALOG_FIND_VIEW_BY_ID, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using findViewById instead of ButterKnife");
    }

    final boolean isActivityFindViewByIdSuppressed = context.getDriver().isSuppressed(context, ISSUE_ACTIVITY_FIND_VIEW_BY_ID, methodExpression);
    if ("android.app.Activity.findViewById".equals(fullyQualifiedMethodName) && !isActivityFindViewByIdSuppressed) {
      context.report(ISSUE_ACTIVITY_FIND_VIEW_BY_ID, call, context.getLocation(methodExpression.getReferenceNameElement()),
          "Using findViewById instead of ButterKnife");
    }
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
        ISSUE_WINDOW_FIND_VIEW_BY_ID, ISSUE_VIEW_FIND_VIEW_BY_ID, ISSUE_DIALOG_FIND_VIEW_BY_ID, ISSUE_ACTIVITY_FIND_VIEW_BY_ID,
        ISSUE_RESOURCES_GET_DRAWABLE, ISSUE_RESOURCES_GET_COLOR, ISSUE_RESOURCES_GET_COLOR_STATE_LIST
    };
  }

  static final Issue ISSUE_WINDOW_FIND_VIEW_BY_ID =
      Issue.create("WindowFindViewById", "Using findViewById() instead of ButterKnife.",
          "Instead of using findViewById ButterKnife should be used.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));

  static final Issue ISSUE_VIEW_FIND_VIEW_BY_ID =
      Issue.create("ViewFindViewById", "Using findViewById() instead of ButterKnife.",
          "Instead of using findViewById ButterKnife should be used.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));

  static final Issue ISSUE_DIALOG_FIND_VIEW_BY_ID =
      Issue.create("DialogFindViewById", "Using findViewById() instead of ButterKnife.",
          "Instead of using findViewById ButterKnife should be used.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));

  static final Issue ISSUE_ACTIVITY_FIND_VIEW_BY_ID =
      Issue.create("ActivityFindViewById", "Using findViewById() instead of ButterKnife.",
          "Instead of using findViewById ButterKnife should be used.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));

  static final Issue ISSUE_RESOURCES_GET_DRAWABLE =
      Issue.create("ResourcesGetDrawable", "Using getDrawable(), which is deprecated.",
          "Instead of getDrawable(), ContextCompat or the method with the Theme Overload should be used instead.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));

  static final Issue ISSUE_RESOURCES_GET_COLOR =
      Issue.create("ResourcesGetColor", "Using getColor(), which is deprecated.",
          "Instead of getColor(), ContextCompat or the method with the Theme Overload should be used instead.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));

  static final Issue ISSUE_RESOURCES_GET_COLOR_STATE_LIST =
      Issue.create("ResourcesGetColorStateList", "Using getColorStateList(), which is deprecated.",
          "Instead of getColorStateList(), ContextCompat or the method with the Theme Overload should be used instead.",
              MESSAGES, 5, WARNING,
          new Implementation(AndroidDetector.class, JAVA_FILE_SCOPE));
}

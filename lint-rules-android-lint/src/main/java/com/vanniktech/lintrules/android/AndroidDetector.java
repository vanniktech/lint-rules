package com.vanniktech.lintrules.android;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.intellij.psi.PsiMethod;
import java.util.EnumSet;
import java.util.List;
import org.jetbrains.uast.UCallExpression;

import static com.android.tools.lint.detector.api.Category.MESSAGES;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE;
import static com.android.tools.lint.detector.api.Scope.TEST_SOURCES;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static java.util.Arrays.asList;

public final class AndroidDetector extends Detector implements Detector.UastScanner {
  @Override public List<String> getApplicableMethodNames() {
    return asList("getColor", "getDrawable", "getColorStateList");
  }

  @Override public void visitMethod(final JavaContext context, final UCallExpression node, final PsiMethod method) {
    final boolean isInResources = context.getEvaluator().isMemberInClass(method, "android.content.res.Resources");
    final String methodName = node.getMethodName();

    if ("getDrawable".equals(methodName) && isInResources) {
      context.report(ISSUE_RESOURCES_GET_DRAWABLE, node, context.getNameLocation(node), "Using deprecated getDrawable()");
    }

    if ("getColor".equals(methodName) && isInResources) {
      context.report(ISSUE_RESOURCES_GET_COLOR, node, context.getNameLocation(node), "Using deprecated getColor()");
    }

    if ("getColorStateList".equals(methodName) && isInResources) {
      context.report(ISSUE_RESOURCES_GET_COLOR_STATE_LIST, node, context.getNameLocation(node), "Using deprecated getColorStateList()");
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

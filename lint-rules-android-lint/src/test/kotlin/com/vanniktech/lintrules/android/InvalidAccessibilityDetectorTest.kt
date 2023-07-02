@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class InvalidAccessibilityDetectorTest {
  @Test fun contentDescriptionNull() {
    lint()
      .files(
        xml(
          "res/layout/ids.xml",
          """
          <ImageView
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:contentDescription="@null"/>
          """,
        ).indented(),
      )
      .issues(ISSUE_INVALID_ACCESSIBILITY)
      .run()
      .expect(
        """
        res/layout/ids.xml:5: Warning: Either set a proper accessibility text or use importantForAccessibility [InvalidAccessibility]
            android:contentDescription="@null"/>
                                        ~~~~~
        0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
        Autofix for res/layout/ids.xml line 5: Change:
        @@ -5 +5
        -     android:contentDescription="@null" />
        +     android:importantForAccessibility="no" />
        """.trimMargin(),
      )
  }
}

@file:Suppress("UnstableApiUsage") // We know that Lint APIs aren't final.

package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class WrongGlobalIconColorDetectorTest {
  @Test fun ignoresPlainDrawable() {
    lint()
      .files(
        xml(
          "res/drawable/drawable.xml",
          """
          <shape
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:shape="rectangle">
            <solid android:color="#1aeebf"/>
            <size android:height="4dp"/>
          </shape>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expectClean()
  }

  @Test fun ignoresIcLauncherForeground() {
    lint()
      .files(
        xml(
          "res/drawable/ic_launcher_foreground.xml",
          """
          <shape
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:shape="rectangle">
            <solid android:color="#1aeebf"/>
            <size android:height="4dp"/>
          </shape>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expectClean()
  }

  @Test fun ignoresIcLauncherBackground() {
    lint()
      .files(
        xml(
          "res/drawable/ic_launcher_background.xml",
          """
          <shape
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:shape="rectangle">
            <solid android:color="#1aeebf"/>
            <size android:height="4dp"/>
          </shape>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expectClean()
  }

  @Test fun ignoresIcInName() {
    lint()
      .files(
        xml(
          "res/drawable/img_country_democratic_republic_of_congo.xml",
          """
          <shape
              xmlns:android="http://schemas.android.com/apk/res/android"
              android:shape="rectangle">
            <solid android:color="#1aeebf"/>
            <size android:height="4dp"/>
          </shape>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expectClean()
  }

  @Test fun flagsIcDrawable() {
    lint()
      .files(
        xml(
          "res/drawable/ic_drawable.xml",
          """
          <vector xmlns:android="http://schemas.android.com/apk/res/android"
              android:height="24dp"
              android:viewportHeight="24"
              android:viewportWidth="24"
              android:width="24dp">
            <path
                android:fillColor="#FF0000"
                android:fillType="evenOdd"
                android:pathData="M18.364,5.636C18.7545,6.0266 18.7545,6.6597 18.364,7.0503L13.4135,11.9993L18.364,16.9497C18.7545,17.3403 18.7545,17.9734 18.364,18.364C17.9734,18.7545 17.3403,18.7545 16.9497,18.364L11.9993,13.4135L7.0503,18.364C6.6597,18.7545 6.0266,18.7545 5.636,18.364C5.2455,17.9734 5.2455,17.3403 5.636,16.9497L10.5858,11.9986L5.636,7.0503C5.2455,6.6597 5.2455,6.0266 5.636,5.636C6.0266,5.2455 6.6597,5.2455 7.0503,5.636L12,10.5844L16.9497,5.636C17.3403,5.2455 17.9734,5.2455 18.364,5.636Z" />
          </vector>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expect(
        """
          |res/drawable/ic_drawable.xml:7: Warning: Should use global tint color [WrongGlobalIconColor]
          |      android:fillColor="#FF0000"
          |                         ~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
          |Fix for res/drawable/ic_drawable.xml line 7: Use appropriate color:
          |@@ -7 +7
          |-       android:fillColor="#FF0000"
          |+       android:fillColor="#62FF00"
        """.trimMargin(),
      )
  }

  @Test fun flagsAndroidColorReference() {
    lint()
      .files(
        xml(
          "res/drawable/ic_drawable.xml",
          """
          <vector xmlns:android="http://schemas.android.com/apk/res/android"
              android:height="24dp"
              android:viewportHeight="24"
              android:viewportWidth="24"
              android:width="24dp">
            <path
                android:fillColor="@android:color/white"
                android:fillType="evenOdd"
                android:pathData="M18.364,5.636C18.7545,6.0266 18.7545,6.6597 18.364,7.0503L13.4135,11.9993L18.364,16.9497C18.7545,17.3403 18.7545,17.9734 18.364,18.364C17.9734,18.7545 17.3403,18.7545 16.9497,18.364L11.9993,13.4135L7.0503,18.364C6.6597,18.7545 6.0266,18.7545 5.636,18.364C5.2455,17.9734 5.2455,17.3403 5.636,16.9497L10.5858,11.9986L5.636,7.0503C5.2455,6.6597 5.2455,6.0266 5.636,5.636C6.0266,5.2455 6.6597,5.2455 7.0503,5.636L12,10.5844L16.9497,5.636C17.3403,5.2455 17.9734,5.2455 18.364,5.636Z" />
          </vector>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expect(
        """
          |res/drawable/ic_drawable.xml:7: Warning: Should use global tint color [WrongGlobalIconColor]
          |      android:fillColor="@android:color/white"
          |                         ~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
          |Fix for res/drawable/ic_drawable.xml line 7: Use appropriate color:
          |@@ -7 +7
          |-       android:fillColor="@android:color/white"
          |+       android:fillColor="#62FF00"
        """.trimMargin(),
      )
  }

  @Test fun flagsAttrReference() {
    lint()
      .files(
        xml(
          "res/drawable/ic_drawable.xml",
          """
          <vector xmlns:android="http://schemas.android.com/apk/res/android"
              android:height="24dp"
              android:viewportHeight="24"
              android:viewportWidth="24"
              android:width="24dp">
            <path
                android:fillColor="?attr/colorSecondary"
                android:fillType="evenOdd"
                android:pathData="M18.364,5.636C18.7545,6.0266 18.7545,6.6597 18.364,7.0503L13.4135,11.9993L18.364,16.9497C18.7545,17.3403 18.7545,17.9734 18.364,18.364C17.9734,18.7545 17.3403,18.7545 16.9497,18.364L11.9993,13.4135L7.0503,18.364C6.6597,18.7545 6.0266,18.7545 5.636,18.364C5.2455,17.9734 5.2455,17.3403 5.636,16.9497L10.5858,11.9986L5.636,7.0503C5.2455,6.6597 5.2455,6.0266 5.636,5.636C6.0266,5.2455 6.6597,5.2455 7.0503,5.636L12,10.5844L16.9497,5.636C17.3403,5.2455 17.9734,5.2455 18.364,5.636Z" />
          </vector>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expect(
        """
          |res/drawable/ic_drawable.xml:7: Warning: Should use global tint color [WrongGlobalIconColor]
          |      android:fillColor="?attr/colorSecondary"
          |                         ~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings
        """.trimMargin(),
      )
      .expectFixDiffs(
        """
          |Fix for res/drawable/ic_drawable.xml line 7: Use appropriate color:
          |@@ -7 +7
          |-       android:fillColor="?attr/colorSecondary"
          |+       android:fillColor="#62FF00"
        """.trimMargin(),
      )
  }

  @Test fun ignore00000000Color() {
    lint()
      .files(
        xml(
          "res/drawable/ic_drawable.xml",
          """
          <vector xmlns:android="http://schemas.android.com/apk/res/android"
              android:height="24dp"
              android:viewportHeight="24"
              android:viewportWidth="24"
              android:width="24dp">
            <path
                android:fillColor="#62FF00"
                android:fillType="evenOdd"
                android:pathData="M18.364,5.636C18.7545,6.0266 18.7545,6.6597 18.364,7.0503L13.4135,11.9993L18.364,16.9497C18.7545,17.3403 18.7545,17.9734 18.364,18.364C17.9734,18.7545 17.3403,18.7545 16.9497,18.364L11.9993,13.4135L7.0503,18.364C6.6597,18.7545 6.0266,18.7545 5.636,18.364C5.2455,17.9734 5.2455,17.3403 5.636,16.9497L10.5858,11.9986L5.636,7.0503C5.2455,6.6597 5.2455,6.0266 5.636,5.636C6.0266,5.2455 6.6597,5.2455 7.0503,5.636L12,10.5844L16.9497,5.636C17.3403,5.2455 17.9734,5.2455 18.364,5.636Z"
                android:strokeColor="#00000000"
                android:strokeWidth="1" />
          </vector>
          """,
        ).indented(),
      )
      .issues(ISSUE_WRONG_GLOBAL_ICON_COLOR)
      .run()
      .expectClean()
  }
}

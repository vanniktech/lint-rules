package com.vanniktech.lintrules.android

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class LayoutFileNameMatchesClassDetectorTest {
  private val r = java("""
      package foo;

      public final class R {
        public static final class layout {
          public static final int activity_bar = 1;
          public static final int activity_foo = 2;
          public static final int activity_game_times = 3;
          public static final int unit_test_activity_foo = 3;
          public static final int foo2bar_activity_rider = 4;
          public static final int foo2bar_activity = 5;
        }
      }""").indented()

  private val activity = java("""
      package foo;

      public abstract class Activity {
        public void setContentView(int viewId) { }
      }""").indented()

  @Test fun nonRUsage() {
    lint()
        .files(r, activity, java("""
          package foo;

          class FooActivity extends Activity {
            void foo(int id) {
              setContentView(id);
            }
          }""").indented())
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expectClean()
  }

  @Test fun fooActivityUsesActivityFoo() {
    lint()
        .files(r, activity, java("""
          package foo;

          class FooActivity extends Activity {
            void foo() {
              setContentView(R.layout.activity_foo);
            }
          }""").indented())
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expectClean()
  }

  @Test fun foo2BarRiderActivityUsesActivitySomethingWithResourcePrefix() {
    lint()
        .files(r, activity, kt("""
          package foo

          class Foo2BarRiderActivity : Activity() {
            fun foo() {
              setContentView(R.layout.foo2bar_activity_rider)
            }
          }""").indented(), resourcePrefix("foo2bar_"))
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expectClean()
  }

  @Test fun foo2BarActivityUsesActivityWithResourcePrefix() {
    lint()
        .files(r, activity, kt("""
          package foo

          class Foo2BarActivity : Activity() {
            fun foo() {
              setContentView(R.layout.foo2bar_activity)
            }
          }""").indented(), resourcePrefix("foo2bar_"))
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expectClean()
  }

  @Test fun fooActivityUsesActivityFooWithResourcePrefix() {
    lint()
        .files(r, activity, java("""
          package foo;

          class FooActivity extends Activity {
            void foo() {
              setContentView(R.layout.unit_test_activity_foo);
            }
          }""").indented(), resourcePrefix("unit_test_"))
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expectClean()
  }

  @Test fun gameTimesActivityUsesActivityGameTimes() {
    lint()
        .files(r, activity, kt("""
          package foo

          class GameTimesActivity : Activity() {
            fun foo() {
              setContentView(R.layout.activity_game_times)
            }
          }""").indented())
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expectClean()
  }

  @Test fun fooActivityUsesActivityBarWithResourcePrefix() {
    lint()
        .files(r, activity, java("""
            package foo;

            class FooActivity extends Activity {
              void foo() {
                setContentView(R.layout.activity_bar);
              }
            }""").indented(), resourcePrefix("unit_test_"))
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expect("""
            |src/main/java/foo/FooActivity.java:5: Warning: Parameter should be named R.layout.unit_test_activity_foo [LayoutFileNameMatchesClass]
            |    setContentView(R.layout.activity_bar);
            |                   ~~~~~~~~~~~~~~~~~~~~~
            |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
            |Fix for src/main/java/foo/FooActivity.java line 4: Replace with unit_test_activity_foo:
            |@@ -5 +5
            |-     setContentView(R.layout.activity_bar);
            |+     setContentView(R.layout.unit_test_activity_foo);
            |""".trimMargin())
  }

  @Test fun gameTimesActivityUsesActivityBar() {
    lint()
        .files(r, activity, java("""
          package foo;

          class GameTimesActivity extends Activity {
            void foo() {
              setContentView(R.layout.activity_bar);
            }
          }""").indented())
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expect("""
          |src/foo/GameTimesActivity.java:5: Warning: Parameter should be named R.layout.activity_game_times [LayoutFileNameMatchesClass]
          |    setContentView(R.layout.activity_bar);
          |                   ~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/GameTimesActivity.java line 4: Replace with activity_game_times:
          |@@ -5 +5
          |-     setContentView(R.layout.activity_bar);
          |+     setContentView(R.layout.activity_game_times);
          |""".trimMargin())
  }

  @Test fun themesActivityUsesActivityBarInKotlin() {
    lint()
        .files(r, activity, kt("""
          package foo

          class ThemesActivity : Activity() {
            fun foo() {
              setContentView(R.layout.activity_bar)
            }
          }""").indented())
        .issues(ISSUE_LAYOUT_FILE_NAME_MATCHES_CLASS)
        .run()
        .expect("""
          |src/foo/ThemesActivity.kt:5: Warning: Parameter should be named R.layout.activity_themes [LayoutFileNameMatchesClass]
          |    setContentView(R.layout.activity_bar)
          |                   ~~~~~~~~~~~~~~~~~~~~~
          |0 errors, 1 warnings""".trimMargin())
        .expectFixDiffs("""
          |Fix for src/foo/ThemesActivity.kt line 4: Replace with activity_themes:
          |@@ -5 +5
          |-     setContentView(R.layout.activity_bar)
          |+     setContentView(R.layout.activity_themes)
          |""".trimMargin())
  }
}

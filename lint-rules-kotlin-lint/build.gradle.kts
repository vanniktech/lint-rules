import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm")
  id("com.vanniktech.maven.publish")
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

dependencies {
  compileOnly(libs.lint.api)
}

dependencies {
  compileOnly(libs.lint.api)
}

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.lint.core)
  testImplementation(libs.lint.tests)
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions {
    // Lint forces Kotlin (regardless of what version the project uses), so this
    // forces a matching language level for now. Similar to `targetCompatibility` for Java.
    apiVersion.set(KOTLIN_1_8)
    languageVersion.set(KOTLIN_1_8)
  }
}

tasks.withType(Jar::class.java).configureEach {
  manifest.attributes["Lint-Registry-v2"] = "com.vanniktech.lintrules.kotlin.IssueRegistry"
}

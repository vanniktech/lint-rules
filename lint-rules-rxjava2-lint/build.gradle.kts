plugins {
  id("org.jetbrains.kotlin.jvm")
  id("com.vanniktech.maven.publish")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  compileOnly(libs.lint.api)
}

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.lint.core)
  testImplementation(libs.lint.tests)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
  kotlinOptions.jvmTarget = "1.8"

  // Lint still requires 1.4 (regardless of what version the project uses), so this forces a lower
  // language level for now. Similar to `targetCompatibility` for Java.
  kotlinOptions.apiVersion = "1.4"
  kotlinOptions.languageVersion = "1.4"
}

tasks.withType(Jar::class.java).configureEach {
  manifest.attributes["Lint-Registry-v2"] = "com.vanniktech.lintrules.rxjava2.IssueRegistry"
}
plugins {
  id("com.android.library")
  id("com.vanniktech.maven.publish")
}

android {
  namespace = "com.vanniktech.lintrulesandroid"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies {
  lintPublish(project(":lint-rules-android-lint"))
}

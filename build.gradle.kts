buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  dependencies {
    classpath(libs.plugin.androidgradleplugin)
    classpath(libs.plugin.dokka)
    classpath(libs.plugin.kotlin)
    classpath(libs.plugin.publish)
  }
}

plugins {
  alias(libs.plugins.codequalitytools)
}

codeQualityTools {
  checkstyle {
    enabled = false // Kotlin only.
  }
  pmd {
    enabled = false // Kotlin only.
  }
  ktlint {
    toolVersion = libs.versions.ktlint.get()
  }
  detekt {
    enabled = false // Don"t want.
  }
  cpd {
    enabled = false // Kotlin only.
  }
  lint {
    lintConfig = rootProject.file("lint.xml")
    checkAllWarnings = true
  }
  kotlin {
    allWarningsAsErrors = false
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

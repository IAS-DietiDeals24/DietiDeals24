buildscript {
    dependencies {
        classpath(libs.gradle)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.google.firebase.perf) apply false
    alias(libs.plugins.sonarqube)
}

sonar {
    properties {
        property("sonar.host.url", "http://localhost:55513")
        property("sonar.projectKey", "dietideals24-frontendandroid")
        property("sonar.projectName", "dietideals24-frontendandroid")
    }
}
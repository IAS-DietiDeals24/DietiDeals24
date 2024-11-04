plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.perf)
}

android {
    namespace = "com.iasdietideals24.dietideals24"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iasdietideals24.dietideals24"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    buildToolsVersion = "34.0.0"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.material)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)
    implementation(libs.retrofit)
    implementation(libs.converter.jackson)
    implementation(libs.facebook.login)
    implementation(libs.koin)
    implementation(libs.coil)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.lifecycle.common.jvm)
    implementation(libs.commons.text)
    implementation(libs.paging.runtime.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.amplify)
    implementation(libs.aws.auth.cognito)
    implementation(libs.jackson.datatype.jsr310)
    coreLibraryDesugaring(libs.desugar)
}
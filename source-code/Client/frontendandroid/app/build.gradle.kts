plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.crashlytics)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    testImplementation(libs.junit.jupiter.api)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}
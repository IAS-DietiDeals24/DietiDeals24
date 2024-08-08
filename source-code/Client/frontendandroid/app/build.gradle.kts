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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.crashlytics)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.googleid)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.facebook.login)

    testImplementation(libs.junit.jupiter.api)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}
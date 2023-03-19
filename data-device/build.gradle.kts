@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.arli.sunnyday.data.device"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":data-location"))

    implementation(libs.kotlin.coroutines.playServices)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.playServices.location)
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.arli.sunnyday.data.db.room"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":data-db"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)

    testImplementation(libs.androidx.room.testing)

    androidTestImplementation(libs.bundles.test.instrumentedTests)
    androidTestImplementation(libs.test.turbine)
}

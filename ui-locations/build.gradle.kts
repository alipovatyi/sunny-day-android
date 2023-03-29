@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.arli.sunnyday.ui.locations"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":data-location"))
    implementation(project(":data-weather"))
    implementation(project(":domain"))
    implementation(project(":ui-common"))

    implementation(libs.androidx.core)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.accompanist.permissions)

    testImplementation(libs.bundles.test.unitTests)
}

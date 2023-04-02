@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.arli.sunnyday.ui.navigation"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":data-model"))
    implementation(project(":ui-common"))
    implementation(project(":ui-details"))
    implementation(project(":ui-locations"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigationCompose)
}

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
    implementation(project(":ui-common"))
    implementation(project(":ui-details"))
    implementation(project(":ui-locations"))
    implementation(project(":ui-search"))
    implementation(project(":ui-settings"))

    implementation(libs.androidx.core)
}

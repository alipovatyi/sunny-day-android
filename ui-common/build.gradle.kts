@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.arli.sunnyday.ui.common"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":resources"))

    implementation(libs.androidx.core)
    implementation(libs.material)
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.androidx.compose)
    debugApi(libs.androidx.compose.uiTooling)
}

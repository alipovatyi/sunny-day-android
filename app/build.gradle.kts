@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.secrets)
}

android {
    namespace = "dev.arli.sunnyday"

    defaultConfig {
        applicationId = "dev.arli.sunnyday"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    setFlavorDimensions(listOf("env"))

    productFlavors {
        create("staging") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-staging"

            buildConfigField("String", "API_URL", "\"https://api.open-meteo.com/v1/\"")
            buildConfigField("String", "DATA_SOURCE_URL", "\"https://open-meteo.com/\"")
        }

        create("production") {
            dimension = "env"

            buildConfigField("String", "API_URL", "\"https://api.open-meteo.com/v1/\"")
            buildConfigField("String", "DATA_SOURCE_URL", "\"https://open-meteo.com/\"")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    secrets {
        propertiesFileName = "secrets.properties"
        defaultPropertiesFileName = "secrets.defaults.properties"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":data-api"))
    implementation(project(":data-api-retrofit"))
    implementation(project(":data-common"))
    implementation(project(":data-config"))
    implementation(project(":data-db"))
    implementation(project(":data-db-room"))
    implementation(project(":data-device"))
    implementation(project(":data-location"))
    implementation(project(":data-model"))
    implementation(project(":data-prefs"))
    implementation(project(":data-prefs-datastore"))
    implementation(project(":data-settings"))
    implementation(project(":data-weather"))
    implementation(project(":domain"))
    implementation(project(":resources"))
    implementation(project(":ui-common"))
    implementation(project(":ui-details"))
    implementation(project(":ui-locations"))
    implementation(project(":ui-navigation"))
    implementation(project(":ui-search"))
    implementation(project(":ui-settings"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.google.places)

    coreLibraryDesugaring(libs.desugar)

    testImplementation(libs.bundles.test.unitTests)
}

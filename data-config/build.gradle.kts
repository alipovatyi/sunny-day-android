plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.bundles.test.unitTests)
}

plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":data-location"))
    implementation(project(":data-weather"))
    api(project(":data-model"))

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.bundles.test.unitTests)
}

plugins {
    id("kotlin")
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":data-api"))
    implementation(project(":data-config"))
    implementation(project(":data-model"))

    implementation(libs.kotlin.serialization.json)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinSerialization)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.bundles.test.unitTests)
    testImplementation(libs.test.mockWebServer)
}

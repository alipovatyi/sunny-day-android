plugins {
    id("kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(libs.dagger)

    testImplementation(libs.bundles.test.unitTests)
}
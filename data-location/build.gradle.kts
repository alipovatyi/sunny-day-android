plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":data-db"))
    implementation(project(":data-model"))

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.bundles.test.unitTests)
}

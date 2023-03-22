plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":data-db"))
    implementation(project(":domain"))

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.bundles.test.unitTests)
}

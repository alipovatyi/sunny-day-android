plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}

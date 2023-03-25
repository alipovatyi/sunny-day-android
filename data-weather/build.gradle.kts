plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":data-api"))
    implementation(project(":data-common"))
    implementation(project(":data-config"))
    implementation(project(":data-db"))
    implementation(project(":data-model"))

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.bundles.test.unitTests)
}

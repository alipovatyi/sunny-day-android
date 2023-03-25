plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":data-api"))
    implementation(project(":data-common"))
    implementation(project(":data-db"))
    implementation(project(":data-model"))

    testImplementation(libs.bundles.test.unitTests)
}

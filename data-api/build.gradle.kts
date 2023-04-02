plugins {
    id("kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":data-common"))
    implementation(project(":data-model"))

    implementation(libs.kotlin.serialization.json)
}

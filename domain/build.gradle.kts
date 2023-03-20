plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":data-settings"))
    implementation(project(":data-weather"))

    api(platform(libs.arrow.bom))
    api(libs.arrow.core)
}

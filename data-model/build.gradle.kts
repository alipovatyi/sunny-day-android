plugins {
    id("kotlin")
}

dependencies {
    api(platform(libs.arrow.bom))
    api(libs.arrow.core)
}

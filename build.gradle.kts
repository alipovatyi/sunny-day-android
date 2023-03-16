import dev.arli.gradle.applyPlugin

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply true
}

allprojects {
    applyPlugin(rootProject.libs.plugins.detekt)

    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        autoCorrect = true

        source(layout.projectDirectory.asFileTree.matching { include("**/*.kts") })
    }
}

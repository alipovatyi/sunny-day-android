import com.android.build.gradle.BaseExtension
import dev.arli.gradle.applyPlugin
import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
    }
}
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
        detektPlugins(rootProject.libs.detekt.compose)
    }

    tasks.withType<Detekt>().configureEach {
        autoCorrect = true

        source(layout.projectDirectory.asFileTree.matching { include("**/*.kts") })
    }

    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
            }
        }
    }

    pluginManager.withPlugin("com.android.application") {
        configureAndroidProject()
    }

    pluginManager.withPlugin("com.android.library") {
        configureAndroidProject()
    }
}

fun Project.configureAndroidProject() {
    extensions.configure<BaseExtension> {
        namespace = "dev.arli.sunnyday"

        compileSdkVersion(libs.versions.compileSdk.get().toInt())

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
        }
    }
}

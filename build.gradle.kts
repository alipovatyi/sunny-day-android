import com.android.build.gradle.BaseExtension
import dev.arli.gradle.applyPlugin
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

    tasks.withType<Detekt>().configureEach {
        autoCorrect = true

        source(layout.projectDirectory.asFileTree.matching { include("**/*.kts") })
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
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
        compileSdkVersion(libs.versions.compileSdk.get().toInt())

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

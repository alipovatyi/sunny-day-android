package dev.arli.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependency

fun Project.applyPlugin(notation: Provider<PluginDependency>) {
    apply(plugin = notation.get().pluginId)
}

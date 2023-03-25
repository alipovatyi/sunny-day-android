pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Sunny Day"

include(":app")
include(":data-api")
include(":data-api-retrofit")
include(":data-common")
include(":data-config")
include(":data-db")
include(":data-db-room")
include(":data-device")
include(":data-location")
include(":data-prefs")
include(":data-prefs-datastore")
include(":data-settings")
include(":data-weather")
include(":domain")
include(":data-model")
include(":resources")
include(":ui-common")
include(":ui-details")
include(":ui-locations")
include(":ui-navigation")
include(":ui-search")
include(":ui-settings")

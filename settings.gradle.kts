pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Zarina"
include(":app")

include(":feature:home:ui:api")
include(":feature:home:ui:impl")
include(":feature:home:data")
include(":feature:home:domain")

include(":core:domain")
include(":core:navigation")
include(":core:feature")
include(":core:usecase")
include(":core:network")
include(":core:datastore")
include(":core:sharedpreferences")
include(":core:database")
include(":core:permission")
include(":core:coroutines-util")
include(":core:ui-kit")
include(":core:text")

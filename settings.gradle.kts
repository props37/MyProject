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

include(":feature:onboarding:ui:api")
include(":feature:onboarding:ui:impl")

include(":feature:home:ui:api")
include(":feature:home:ui:impl")
include(":feature:home:data")
include(":feature:home:domain")

include(":feature:catalog:ui:api")
include(":feature:catalog:ui:impl")

include(":feature:wishlist:ui:api")
include(":feature:wishlist:ui:impl")

include(":data:content")
include(":data:auth")
include(":data:category")
include(":data:wishlist")
include(":data:location")
include(":data:onboarding")
include(":data:user")
include(":data:geography")

include(":core:domain")
include(":core:navigation")
include(":core:navigation-util")
include(":core:feature")
include(":core:usecase")
include(":core:network")
include(":core:datastore")
include(":core:sharedpreferences")
include(":core:database")
include(":core:permission")
include(":core:coroutines-util")
include(":core:ui-kit")
include(":core:ui-compose")
include(":core:ui-common")
include(":core:ui-model")
include(":core:text")
include(":core:media")
include(":core:media-compose")
include(":core:platform")
include(":core:build-util")
include(":core:resource")
include(":core:paging")
include(":core:googleplayservices")
include(":core:di-feature")

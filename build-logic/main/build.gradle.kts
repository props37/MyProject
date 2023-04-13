plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "zarina.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
    }
}

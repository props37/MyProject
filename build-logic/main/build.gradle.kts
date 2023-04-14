plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "zarina.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("composeMetrics") {
            id = "zarina.compose.metrics"
            implementationClass = "ComposeMetricsPlugin"
        }
    }
}

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.checkDependencyUpdates)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }

    explicitApi()
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
}

android {
    namespace = "ru.livetyping.zarina.core.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlin.serialization.json)

    api(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.json)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
}

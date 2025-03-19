plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
}

android {
    namespace = "ru.livetyping.zarina.data.mindbox"
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
    implementation(projects.core.network)
    implementation(projects.core.buildUtil)

    implementation(libs.kotlin.serialization.json)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
}

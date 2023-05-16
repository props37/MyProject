// Workaround for https://github.com/gradle/gradle/issues/22797; remove after Gradle 8.1 migration
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("zarina.android.application")
    id("zarina.compose.metrics")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.play.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.gitVersioning)
}

androidGitVersion {
    codeFormat = "MNNNPP"
    format = "%tag%%-branch%%-count%"
}

android {
    namespace = "ru.zarina.zarina"
    compileSdk = 33

    val tagVersionCode = androidGitVersion.code()
    val tagVersionName = androidGitVersion.name()
    println("tagVersionCode = $tagVersionCode, tagVersionName = $tagVersionName")

    defaultConfig {
        applicationId = "ru.zarina.zarina"
        minSdk = 24
        targetSdk = 33
        versionCode = tagVersionCode
        versionName = tagVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        all {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }
    kapt {
        correctErrorTypes = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.core)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.accompanist.systemUi)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.compose)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.coil.compose)
    implementation(libs.timber)
    implementation(libs.google.play.services.location)
    implementation(libs.google.play.services.maps.core)
    implementation(libs.google.play.services.maps.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)
    implementation(libs.mindbox)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)

    debugImplementation(libs.compose.ui.tooling.core)
    debugImplementation(libs.compose.ui.test.manifest)
}

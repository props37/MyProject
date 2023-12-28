plugins {
    id("zarina.android.application")
    id("zarina.compose.metrics")

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googlePlayServices)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)

    alias(libs.plugins.androidGitVersion)
    alias(libs.plugins.checkDependencyUpdates)
}

androidGitVersion {
    codeFormat = "MNNNPP"
    format = "%tag%--%branch%--%commit%"
}

kapt {
    correctErrorTypes = true
}

android {
    val appId = "ru.zarina.zarina"

    namespace = appId
    compileSdk = 34

    val generatedVersionCode = androidGitVersion.code()
    val generatedVersionName = androidGitVersion.name()
    println("Version code: $generatedVersionCode")
    println("Version name: $generatedVersionName")

    defaultConfig {
        applicationId = appId
        minSdk = 24
        targetSdk = 34
        versionCode = generatedVersionCode
        versionName = generatedVersionName

        resourceConfigurations.add("ru")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        all {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.jetpack.compose.compiler.get()
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.jetpack.core)
    implementation(libs.jetpack.activity.compose)
    implementation(libs.jetpack.lifecycle.runtime.compose)
    implementation(libs.jetpack.lifecycle.viewModel.compose)
    implementation(libs.jetpack.navigation.compose)
    implementation(libs.jetpack.dataStore.preferences)
    implementation(libs.jetpack.room)
    ksp(libs.jetpack.room.compiler)
    implementation(libs.jetpack.splashScreen)
    implementation(libs.jetpack.media3.exoplayer)
    implementation(libs.jetpack.media3.ui)
    implementation(libs.jetpack.paging.compose)
    implementation(libs.jetpack.security.cripto)

    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.ui)
    implementation(libs.jetpack.compose.material)
    implementation(libs.jetpack.compose.material3)
    implementation(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.immutableCollections)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.json)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    implementation(libs.koin.annotations)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    ksp(libs.koin.compiler)

    implementation(libs.accompanist.systemUi)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigationMaterial)
    implementation(libs.accompanist.webview)

    implementation(libs.coil.compose)
    implementation(libs.composeShimmer)

    implementation(libs.timber)

    implementation(libs.googlePlayServices.location)
    implementation(libs.googlePlayServices.maps)
    implementation(libs.googlePlayServices.maps.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)

    implementation(libs.mindbox)

    debugImplementation(libs.leakCanary)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
    androidTestImplementation(platform(libs.jetpack.compose.bom))
    androidTestImplementation(libs.jetpack.compose.junit4)

    lintChecks(libs.lint.composeChecks)
}

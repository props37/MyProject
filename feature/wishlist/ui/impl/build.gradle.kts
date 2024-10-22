plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "ru.livetyping.zarina.feature.wishlist.ui.impl"
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

    buildFeatures {
        compose = true
    }

    lint {
        disable += listOf("UsingMaterialAndMaterial3Libraries")
    }
}

kotlin {
    explicitApi()
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("config/compose/stability_config.txt")
}

dependencies {
    implementation(projects.feature.wishlist.ui.api)
    implementation(projects.core.domain)
    implementation(projects.core.uiKit)
    implementation(projects.core.coroutinesUtil)
    implementation(projects.core.navigationUtil)

    implementation(libs.jetpack.lifecycle.runtime.compose)
    implementation(libs.jetpack.lifecycle.viewModel.compose)
    implementation(libs.jetpack.navigation.compose)
    implementation(libs.jetpack.paging.compose)

    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.ui)
    implementation(libs.jetpack.compose.material)
    implementation(libs.jetpack.compose.material.navigation)
    implementation(libs.jetpack.compose.material3)
    implementation(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    implementation(libs.coil.compose)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    implementation(libs.kotlin.immutableCollections)

    implementation(libs.timber)

    lintChecks(libs.lint.composeChecks)

    testImplementation(libs.junit)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
}
